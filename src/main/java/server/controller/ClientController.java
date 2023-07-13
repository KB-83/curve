package server.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import server.Main;
import server.controller.datacontroller.TCPRequest;
import server.controller.datacontroller.UDPRequest;
import server.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;

public class ClientController extends Thread{
    private UDPServer udpServer;
    private TCPServer tcpServer;
    private Client client;
    private BufferedReader reader;
    private ObjectMapper objectMapper;

    public ClientController(TCPServer tcpServer, ObjectMapper objectMapper,BufferedReader reader, Client client) {
        this.tcpServer = tcpServer;
        this.reader = reader;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        while (true) {
            receiveTCPConnection();
        }
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
//    public void handleTCPRequest(UDPRequest udpRequest) {
//        if (udpRequest.getRequestNum() == 1){
////            start the game
//            Player player1 = new Player(udpRequest.getUserName());
//            Player player2 = new Player(udpRequest.getUserName());
//            Game game = new Game(player1,player2);
//            // start GameController and sen it to both clients
//        }
//    }
    private void receiveTCPConnection() {
        try {
            String json = reader.readLine();
            TCPRequest tcpRequest = objectMapper.readValue(json,TCPRequest.class);
            handleTCPRequest(tcpRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleTCPRequest(TCPRequest tcpRequest) {
        switch (tcpRequest.getType()) {
            case 0:
                break;
            case 1:
                System.out.println(tcpRequest.getMassage());
                snakeMoveRequest(tcpRequest.getMassage());
                break;
        }
    }
    private void snakeMoveRequest(String direction){
        int angle = 10;
        Snake snake = client.getPlayer().getSnake();
        switch (direction) {
            case "RIGHT":
                snake.setAngle(snake.getAngle() + angle);
                break;
            case "LEFT":
                snake.setAngle(snake.getAngle() - angle);
                break;
        }
    }
}
