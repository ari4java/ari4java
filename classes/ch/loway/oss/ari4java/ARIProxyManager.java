package ch.loway.oss.ari4java;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.VariableLinkedBlockingQueue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Deals with go-ari-proxy application layer communications
 */
public class ARIProxyManager {
    private String exchangeName;
    private String queueName;
    private static final String EXCHANGE_TYPE = "direct";
    private static final boolean EXCHANGE_DURABLE = true;
    private boolean AUTO_ACK = true;
    private boolean MANDATORY = true;
    private Connection connection;
    private Channel channel;

    public ARIProxyManager(Connection connection, String queueName) {
        this.connection = connection;
        this.exchangeName = queueName + "-xchng";
        this.queueName = queueName;
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

