package client;

import client.view.CurveCustomFrame;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    Socket socket;
    public ClientController() throws IOException {
        Socket socket = new Socket("localhost",9000);
        Scanner scanner = new Scanner(socket.getInputStream());
    }
}
