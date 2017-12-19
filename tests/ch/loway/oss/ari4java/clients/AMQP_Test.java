package ch.loway.oss.ari4java.clients;

import ch.loway.oss.ari4java.ARIProxyManager;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * Test AMQP functionality as integration test against localhost rabbitmq server
 *
 */
public class AMQP_Test {
    private static final String RABBITMQ_HOST = "localhost";
    private static final String VIRTUAL_HOST = "/";
    private static final String EXCHANGE_TOPIC = "test_topic";
    private Connection connection;
    private ARIProxyManager ariProxyManager;

    @Before
    public void setUp() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST); // rabbitmq running on localhost can be used for AMQP integration testing
        factory.setVirtualHost(VIRTUAL_HOST);
        connection = factory.newConnection();
        ariProxyManager = new ARIProxyManager(connection, EXCHANGE_TOPIC);
    }

    @After
    public void tearDown() throws IOException {
        connection.close();
    }

    @Test
    public void sendAndRecieveMessage() throws IOException, InterruptedException {
        String message = "Hello World";
        ariProxyManager.send(message);
        String receivedMessage = ariProxyManager.receive();
        assertEquals(message, receivedMessage);
    }
}

