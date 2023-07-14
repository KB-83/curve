package server.model;

import server.controller.ClientController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress inetAddress;
    private Socket socket;
    private int port;
    private ClientController clientController;
    private Player player;
    private String userName;
    private boolean isInWaitingRoom;
    private PrintWriter printWriter;
    private boolean isWaitingForAnswer;

    public Client(InetAddress inetAddress,int port,String userName) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.userName = userName;
        isInWaitingRoom = true;
        isWaitingForAnswer = false;
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
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public boolean isInWaitingRoom() {
        return isInWaitingRoom;
    }

    public void setInWaitingRoom(boolean inWaitingRoom) {
        isInWaitingRoom = inWaitingRoom;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public boolean isWaitingForAnswer() {
        return isWaitingForAnswer;
    }

    public void setWaitingForAnswer(boolean waitingForAnswer) {
        isWaitingForAnswer = waitingForAnswer;
    }
}
