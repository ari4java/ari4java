package ch.loway.oss.ari4java.tools.amqp;

import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.tools.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class GoAriRabbitMQClient implements HttpClient {
    private Thread appEventThread;
    private Thread commandResponseThread;
    private Connection conn;
    public static final int COMMAND_TIMEOUT_MILLIS = 10000;
    private final String EMPTY_JSON_RESPONSE = "[]";
    private Map<String, Optional<String>> dialogIds = new HashMap<>();
    final MessageQueue q = new MessageQueue();
    private AriVersion ariVersion;
    AtomicReference<String> response = new AtomicReference<String>(EMPTY_JSON_RESPONSE);

    // Synchronous HTTP action
    @Override
    public String httpActionSync(String uri, String method, List<HttpParam> parametersQuery,
                                 List<HttpParam> parametersForm, List<HttpParam> parametersBody,
                                 List<HttpResponse> errors) throws RestException {
        String response = EMPTY_JSON_RESPONSE;
        response = sendCommandToGoProxy(uri, method, parametersBody.toString());
        return response;
    }

    // Asynchronous HTTP action, response is passed to HttpResponseHandler
    @Override
    public void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery,
                                List<HttpParam> parametersForm, List<HttpParam> parametersBody,
                                final List<HttpResponse> errors, final HttpResponseHandler responseHandler)
            throws RestException {
        Future<String> futureResponse = sendCommandToGoProxyAsync(uri, method, parametersBody.toString(),
                responseHandler);
        if (futureResponse.isDone()) {
            try {
                responseHandler.onSuccess(futureResponse.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public MessageQueue getEventMessageQueue (AriVersion ariVersion) {
        this.ariVersion = ariVersion;
        return q;
    }

    /**
     * Initialize in the context of go-ari-proxy usage via AMQP needs to consume a known "app" channel queue.
     * From that known app channel it determines a "dialog_id" which is used in the naming of three further channel queues
     * events_{dialog_id}
     * commands_{dialog_id}
     * responses_{dialog_id}
     *
     * An important point is that the go-ari-proxy waits for StasisStart from Asterisk to build all the channel queues.
     * This process is currently one-way and some application must be triggered on the Asterisk side to begin it.
     *
     * @param rabbitHost  rabbitMQ server hostname, server providing the message bus using RabbitMQ
     * @param rabbitPort  rabbitMQ server port, port on server providing the message bus using RabbitMQ, defaults to 5672
     * @param username    login username for rabbitmq server
     * @param password    login password for rabbitmq server
     * @param appNames    list of application names to listen for
     * @param virtualHost virtualHost on the rabbitMQ server to communicate on
     */
    public void initialize(String rabbitHost, int rabbitPort, String username, String password,
                           List<String> appNames, String virtualHost) {
        Runnable appEventThreadRunner = new Runnable() {
            @Override
            public void run() {
                try{
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setUsername(username);
                    factory.setPassword(password);
                    factory.setVirtualHost(virtualHost);
                    factory.setHost(rabbitHost);
                    factory.setPort(rabbitPort);
                    conn = factory.newConnection();
                    createAppEventQueues(appNames);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };
        appEventThread = new Thread(appEventThreadRunner);
        appEventThread.start();
    }

    private void createAppEventQueues(List<String> appList) {
        appList.stream().forEach(appName -> {
            try {
                // channel for finding out about the three other channels go-ari-proxy will use (3 per app)
                Channel channel = conn.createChannel();
                channel.exchangeDeclare("AppConsumer", "direct", true);
                channel.queueDeclare(appName, true, false, false, null);
                channel.queueBind(appName, "AppConsumer", "standard-key");
                channel.basicConsume(appName, true,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException {
                            String routingKey = envelope.getRoutingKey();
                            String contentType = properties.getContentType();
                            long deliveryTag = envelope.getDeliveryTag();
                            ObjectMapper mapper = new ObjectMapper();
                            Map jsonFields = mapper.readValue(body, HashMap.class);
                            try {
                                dialogIds.put(appName, Optional.of((String) jsonFields.get("dialog_id")));
                                //events queue
                                String eventQueueName = String.format("events_%s", dialogIds.get(appName).get());
                                channel.exchangeDeclare("AppEventConsumer", "direct", true);
                                channel.queueDeclare(eventQueueName, true, false,
                                        false, null);
                                channel.queueBind(eventQueueName,
                                        "AppEventConsumer",
                                        "standard-key");
                                channel.basicConsume(eventQueueName, true,
                                    new DefaultConsumer(channel) {
                                        @Override
                                        public void handleDelivery(String consumerTag,
                                                                   Envelope envelope,
                                                                   AMQP.BasicProperties properties,
                                                                   byte[] body)
                                                throws IOException {
                                            String routingKey = envelope.getRoutingKey();
                                            String contentType = properties.getContentType();
                                            long deliveryTag = envelope.getDeliveryTag();
                                            ObjectMapper mapper = new ObjectMapper();
                                            Map jsonFields = mapper.readValue(body, HashMap.class);
                                            try {
                                                q.queue((Message) handleResponse(String.format("%s",
                                                        jsonFields.get("ari_body")),
                                                        buildImplKlazzName(Message.class)));
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            if (jsonFields.get("type").equals("StasisEnd")) {
                                                // need to remove dialog_id,
                                                // the whole set of go-ari-proxy driven queues is done
                                                dialogIds.put(appName, Optional.empty());
                                                appEventThread.interrupt(); //TODO that thread needs to handle this
                                            }
                                        }
                                    });
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                //nothing to do?
                            }

                        }
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sending a command to go-ari-proxy implies:
     * - we already know the dialog_id;
     * - begin a response listener with proper exchange/queue/routing_key for response output of the command
     * - send the command.
     *
     * @param uri url to send to Asterisk via the go-ari-proxy
     * @param method method to send to Asterisk via the go-ari-proxy
     * @param body body to send to Asterisk via the go-ari-proxy
     */
    private String sendCommandToGoProxy(String uri, String method, String body) {
        response.set(EMPTY_JSON_RESPONSE);
        Runnable commandResponseThreadRunner = new Runnable() {
            @Override
            public void run() {
                //FIXME get first entry of dialogIds assumes sending on any random app's queue will be OK -- probably NOT true
                Optional<String> firstAppDialogId = Optional.empty();
                if (!dialogIds.isEmpty()) {
                     firstAppDialogId =  dialogIds.entrySet().iterator().next().getValue();
                }
                while (!firstAppDialogId.isPresent()) { //block til we've a pipe to talk on
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!dialogIds.isEmpty()) {
                        firstAppDialogId =  dialogIds.entrySet().iterator().next().getValue();
                    }
                }
                try {
                    Channel channel = conn.createChannel();
                    // responses queue
                    String responseQueueName = String.format("responses_%s", firstAppDialogId.get());
                    channel.exchangeDeclare("AppResponseConsumer", "direct", true);
                    channel.queueDeclare(responseQueueName, true, false, false, null);
                    channel.queueBind(responseQueueName, "AppResponseConsumer", "standard-key");
                    channel.basicConsume(responseQueueName, true,
                            new DefaultConsumer(channel) {
                                @Override
                                public void handleDelivery(String consumerTag,
                                                           Envelope envelope,
                                                           AMQP.BasicProperties properties,
                                                           byte[] body)
                                        throws IOException {
                                    String routingKey = envelope.getRoutingKey();
                                    String contentType = properties.getContentType();
                                    long deliveryTag = envelope.getDeliveryTag();
                                    response.set(responseDecoder(body));
                                }
                            });

                    // commands queue - talk to this queue with basicPublish() as needed
                    String commandQueueName = String.format("commands_%s", firstAppDialogId.get());
                    channel.exchangeDeclare("AppCommandProducer", "direct", true);
                    channel.queueDeclare(commandQueueName, true, false, false, null);
                    channel.queueBind(commandQueueName, "AppCommandProducer", "standard-key");
                    channel.basicPublish("AppCommandProducer", "standard-key",
                            new AMQP.BasicProperties.Builder()
                            .contentType("application/json")
                            .deliveryMode(2)
                            .priority(1)
                            .userId("guest")
                            .build(), commandEncoder(uri, method, body));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (response.get().equals(EMPTY_JSON_RESPONSE)) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //block, so call this with a timeout or it will block forever on http failure
                }
            }
        };
        commandResponseThread = new Thread(commandResponseThreadRunner);
        commandResponseThread.start();
        try {
            commandResponseThread.join(COMMAND_TIMEOUT_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response.get();
    }

    private Future<String> sendCommandToGoProxyAsync(String uri, String method, String body,
                                                     HttpResponseHandler httpResponseHandler) {
        CompletableFuture<String> f = CompletableFuture.supplyAsync(
                () -> sendCommandToGoProxy(uri, method, body));
        f.handle((x,y) -> null).join();
        return f;
    }

    /**
     *  Utility method that maps commands to json byte values for sending to the proxy's command queue
     * @param uri uri to be encoded for go-ari-proxy
     * @param method method to be encoded for go-ari-proxy
     * @param body body to be encoded for go-ari-proxy
     * @return bytes to be sent to proxy
     * @throws JsonProcessingException
     */
    byte[] commandEncoder (String uri, String method, String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("unique_id", "");
        node.put("url", uri);
        node.put("method", method);
        node.put("body", body);
        return mapper.writeValueAsBytes(node);
    }

    String responseDecoder (byte [] body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map jsonFields = mapper.readValue(body, HashMap.class);
//        System.out.println(String.format("unique_id: %s", jsonFields.get("unique_id")));
//        System.out.println(String.format("status_code: %s", jsonFields.get("status_code")));
//        System.out.println(String.format("response_body: %s", jsonFields.get("response_body")));
        return String.format("%s", jsonFields.get("response_body"));
    }

    Object handleResponse(String json, Class klazz) {
        Object result  = null;
        try {
            if (Void.class.equals(klazz)) {
                result = null;
            } else if (klazz != null) {
                result = BaseAriAction.deserializeEvent(json, klazz);
            }
        } catch (RestException e) {
            return e;
        }
        return result;
    }

    private Class buildImplKlazzName (Class klazz) throws ClassNotFoundException {
        String ariVersionString = ariVersion.name().toLowerCase();
        String pkgAndClassNameForGeneratedModels = klazz.getName().replace("generated",
                String.format("generated.%s.models", ariVersionString));
        String implKlazzName = String.format("%s_impl_%s", pkgAndClassNameForGeneratedModels,
                ariVersion.name().toLowerCase() );
        return Class.forName(implKlazzName);
    }
}