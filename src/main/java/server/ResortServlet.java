package server;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import model.Resort;
import model.Season;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@WebServlet(name = "server.ResortServlet")
public class ResortServlet extends HttpServlet {
    private GenericObjectPool<Channel> channelPool;

    @Override
    public void init() throws ServletException {
        this.channelPool = new GenericObjectPool<>(new ChannelFactory());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            Season season = new Gson().fromJson(json, Season.class);
            Resort resort = new Resort(season.getYear(), Integer.valueOf(urlParts[1]));

            if (season.getYear() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: " + "invalid request body");
            } else {
                response.setStatus(HttpServletResponse.SC_CREATED);
                // do any sophisticated processing with urlParts which contains all the url params
                response.getWriter().write("Post " + urlPath + ' ' + resort.toString());
                try {
                    ResortProducer resortProducer = new ResortProducer(this.channelPool);
                    // send lift id and skier id to the message queue
                    // skierId, resortId, seasonId, dayId, time, liftId
                    String message = String.format("%d,%d", resort.getYear(), resort.getResortId());
                    resortProducer.produce(message);
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
            response.getWriter().write("get a list of ski resorts in the database");
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

    private boolean isGetUrlValid(String[] urlParts) {
        return urlParts.length == 3 && urlParts[2].equals("seasons");
    }

    private boolean isPostUrlValid(String[] urlParts) {
        return urlParts.length == 3 && urlParts[2].equals("seasons");
    }
}
