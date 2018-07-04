package online.omnia.order;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by lollipop on 05.10.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, String> parameters = FileWorkingUtils.iniFileReader();
        int port = Integer.parseInt(parameters.get("port"));
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket;
        while (true) {
            socket = serverSocket.accept();
            new OfferHandlingThread(socket).start();
        }
    }
}
