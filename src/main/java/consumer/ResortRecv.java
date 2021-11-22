package consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dal.ResortDao;
import model.Resort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResortRecv {
    private final static String QUEUE_NAME = "resortQueue";
    private final static int NUMBER_THREAD = 4;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("35.171.160.230");
        factory.setPort(5672);
        factory.setUsername("guest1");
        factory.setPassword("guest1");
        //factory.setHost("localhost");
        final Connection connection = factory.newConnection();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final Channel channel = connection.createChannel();
                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    // max one message per receiver
                    channel.basicQos(1);

                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), "UTF-8");
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        System.out.println( "Callback thread ID = " + Thread.currentThread().getId() + " Received '" + message + "'");
                        String[] result = message.split(",");
                        int year = Integer.valueOf(result[0]);
                        int resortId = Integer.valueOf(result[1]);
                        // add record into db
                        ResortDao resortDao = new ResortDao();
                        resortDao.createResort(new Resort(year, resortId));
                    };
                    // process messages
                    channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
                } catch (IOException ex) {
                    Logger.getLogger(SkierRecv.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        for (int i = 0; i < NUMBER_THREAD; i++) {
            Thread recv = new Thread(runnable);
            recv.start();
        }
    }
}
