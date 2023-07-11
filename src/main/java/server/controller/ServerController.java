package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.model.Client;
import server.model.Game;
import teSt.serVer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    private Client[] clients;
    private ServerSocket serverSocket;
    private ObjectMapper objectMapper = new ObjectMapper();
    public final static boolean LIFE_IS_HARD = true;
    public ServerController() throws IOException {
        serverSocket = new ServerSocket(9000);
        while (LIFE_IS_HARD) {
            Socket socket = serverSocket.accept();
            socket.getInetAddress();
//            UserController userController = new UserController(socket);
        }
    }
    public void sendGameState(Client client1, Client client2, Game game) {

        try {
            String json = objectMapper.writeValueAsString(game);
            byte[] jsonBytes = json.getBytes();
            OutputStream outputStream1 = client1.getSocket().getOutputStream();
            OutputStream outputStream2 = client2.getSocket().getOutputStream();

            outputStream1.write(jsonBytes);
            outputStream1.flush();
            outputStream1.close();

            outputStream2.write(jsonBytes);
            outputStream2.flush();
            outputStream2.close();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
