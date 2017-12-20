package ch.loway.oss.ari4java.sandbox;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.clients.RabbitDirectIntegrationTest;
import ch.loway.oss.ari4java.tools.*;
import ch.loway.oss.ari4java.tools.amqp.RabbitMQClient;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import org.junit.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMQHttpTest {
    private static final String RABBITMQ_HOST = "localhost"; // rabbitmq running on localhost can be used for AMQP integration testing
    private static final String VIRTUAL_HOST = "/";
    private static final String QUEUE_NAME = "test_queue";
    private static final String EXCHANGE_TOPIC = "test_topic";

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void initializationTest() throws IOException, TimeoutException {
        ARI ari = new ARI();
        NettyHttpClient hc = new NettyHttpClient();
        RabbitMQClient mqClient = new RabbitMQClient();
        mqClient.setQueueNameName(QUEUE_NAME);
        mqClient.setExchangeName(EXCHANGE_TOPIC);
        mqClient.setConnection(RABBITMQ_HOST, VIRTUAL_HOST);
        ari.setHttpClient(mqClient);
    }
}
