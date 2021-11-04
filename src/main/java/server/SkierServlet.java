
package server;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import model.LiftRide;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@javax.servlet.annotation.WebServlet(name = "server.SkierServlet")
public class SkierServlet extends javax.servlet.http.HttpServlet {
    private final static String QUEUE_NAME = "skierQueue";
    private GenericObjectPool<Channel> channelPool;


    @Override
    public void init() throws ServletException {
        this.channelPool = new GenericObjectPool<>(new ChannelFactory());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        String urlPath = request.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isPostUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Error: " + "invalid post url");
        } else {
            String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            // Verify request format
            //System.out.println(json);
            LiftRide liftRide = new Gson().fromJson(json, LiftRide.class);
            if (liftRide.getLiftID() == null || liftRide.getTime() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: " + "invalid request body");
            } else {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Post " + urlPath + ' ' + liftRide.toString());
                try {
                    Producer producer = new Producer(this.channelPool);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                // create a channel and use that to publish to RabbitMQ. Close it at end of the request.
                try {
                    Producer producer = new Producer(this.channelPool);
                    // send lift id and skier id to the message queue
                    String message = String.valueOf(liftRide.getLiftID()) + ',' + urlParts[7];
                    producer.produce(message);
                    System.out.println(" [x] Sent '" + message + "'");
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String urlPath = request.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isGetUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Error: " + "invalid get url");
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            response.getWriter().write("Get " + urlPath);
        }
    }


    private boolean isGetUrlValid(String[] urlPath) {
        // validate the request url path according to the API spec
        // urlPath  = "/1/seasons/2019/days/1/skiers/123"
        // urlParts = [, 1, seasons, 2019, days, 1, skiers, 123]
        if (urlPath == null) {
            return false;
        }
        if (urlPath.length == 8) {
            return isValidSkierLiftUrl(urlPath);
        }
        else if (urlPath.length == 3) {
            if (urlPath[1].equals("vertical")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isPostUrlValid(String[] urlPath) {
        if (urlPath == null) {
            return false;
        }
        if (urlPath.length == 8) {
            return isValidSkierLiftUrl(urlPath);
        } else {
            return false;
        }
    }

    private boolean isValidSkierLiftUrl(String[] urlPath) {
        if (urlPath[2].equals("seasons") && urlPath[4].equals("days") && urlPath[6].equals("skiers")) {
            return true;
        } else {
            return false;
        }
    }

}

