package ch.loway.oss.ari4java.tools.amqp;

import ch.loway.oss.ari4java.tools.*;
import ch.loway.oss.ari4java.tools.HttpResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.VariableLinkedBlockingQueue;

/**
 * AMQP client implementation based on RabbitMQ
 *
 */
public class RabbitMQClient implements HttpClient {

    private String exchangeName;
    private String queueName;
    private static final String EXCHANGE_TYPE = "direct";
    private static final boolean EXCHANGE_DURABLE = true;
    private boolean AUTO_ACK = true;
    private boolean MANDATORY = true;
    private Connection connection;
    private Channel channel;

    public RabbitMQClient() {
    }

    // Synchronous HTTP action
    @Override
    public String httpActionSync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody,
                                 List<HttpResponse> errors) throws RestException {
        return null;
    }

    // Asynchronous HTTP action, response is passed to HttpResponseHandler
    @Override
    public void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody,
                                final List<HttpResponse> errors, final HttpResponseHandler responseHandler)
            throws RestException {
    }

    public void setExchangeName(String exchangeName){
        this.exchangeName = exchangeName;
    }

    public void setQueueNameName(String queueName){
        this.queueName = queueName;
    }

    public Connection setConnection(String rabbitMQHost, String virtualHost) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost); // rabbitmq running on localhost can be used for AMQP integration testing
        factory.setVirtualHost(virtualHost);
        this.connection = factory.newConnection();
        return connection;
    }

    private void doExchangeQueueSetup() throws IOException {
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, EXCHANGE_TYPE, EXCHANGE_DURABLE);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, queueName);
    }

    private void tearDown() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    public void send(String message) throws IOException {
        doExchangeQueueSetup();
        channel.basicPublish(exchangeName, queueName, MANDATORY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
    }

    public String receive() throws IOException, InterruptedException {
        doExchangeQueueSetup();
        VariableLinkedBlockingQueue<String> replyHandoff = new VariableLinkedBlockingQueue<>(1);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    replyHandoff.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        String tag = channel.basicConsume(queueName, AUTO_ACK, consumer);
        return replyHandoff.poll(4, TimeUnit.SECONDS);
    }
}
