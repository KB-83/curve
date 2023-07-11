package server.model;

import server.controller.ClientController;

import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress inetAddress;
    private Socket socket;
    private int port;
    private ClientController clientController;
    private Player player;
    private String userName;

    public Client(InetAddress inetAddress,int port,String userName) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.userName = userName;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
