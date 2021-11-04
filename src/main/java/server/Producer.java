package server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {
    private final static String QUEUE_NAME = "skierQueue";
    private ObjectPool<Channel> pool;

    public Producer(ObjectPool<Channel> pool) throws IOException, TimeoutException {
        this.pool = pool;
    }

    public void produce(String message) throws Exception {
        Channel channel = null;
        try {
            channel = pool.borrowObject();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != channel) {
                    pool.returnObject(channel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
