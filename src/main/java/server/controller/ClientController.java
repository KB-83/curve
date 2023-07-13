package server.controller;


import server.controller.datacontroller.UDPRequest;
import server.model.*;

import java.io.IOException;
import java.net.DatagramPacket;

public class ClientController implements Runnable{
    private UDPServer udpServer;
    private TCPServer tcpServer;
    private Client client;

    public ClientController(TCPServer tcpServer,UDPServer udpServer,Client client) {
        this.tcpServer = tcpServer;
        this.udpServer = udpServer;
        this.client = client;
    }

    @Override
    public void run() {

    }
    public void control() {

    }
    private void waitForOpponent() {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            udpServer.getUdpServerSocket().receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleTCPRequest(UDPRequest udpRequest) {
        if (udpRequest.getRequestNum() == 1){
//            start the game
            Player player1 = new Player(udpRequest.getUserName());
            Player player2 = new Player(udpRequest.getUserName());
            Game game = new Game(player1,player2);
            // start GameController and sen it to both clients
        }
    }
}
