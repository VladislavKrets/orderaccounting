package online.omnia.order;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by lollipop on 06.10.2017.
 */
public class OfferHandlingThread extends Thread{
    private Socket socket;
    public OfferHandlingThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            String url = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("HTTP/1.1")) {
                    url = line.split(" ")[1];
                    break;
                }
            }
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("HTTP/1.1 200 OK\\r\\n\\r\\n");
            writer.flush();
            socket.close();

            if (url == null) return;
            LandingHandler landingHandler = new LandingHandler();
            OrderEntity orderEntity = landingHandler.orderCreator(landingHandler.getLandingParameters(url));
            System.out.println(orderEntity);
            FileWorkingUtils.writeLog(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), url);
            MySQLDaoImpl.getInstance().addOrderEntity(orderEntity);
            landingHandler = null;
            orderEntity = null;
            line = null;
            url = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoParametersException ignore) {

        }
    }
}
