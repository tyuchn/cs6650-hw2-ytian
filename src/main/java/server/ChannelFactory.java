package server;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private Connection connection;

    public ChannelFactory() {
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("35.171.160.230");
//        factory.setPort(5672);
//        factory.setUsername("guest1");
//        factory.setPassword("guest1");
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel create() throws Exception {
        return this.connection.createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

}
