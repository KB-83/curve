package server.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.controller.datacontroller.TCPRequest;
import server.controller.datacontroller.TCPResponse;
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
                snakeMoveRequest(tcpRequest.getMassage());
                break;
        }
    }
    private void snakeMoveRequest(String direction){
        int angle = 10;
        Snake snake = client.getPlayer().getSnake();
        if (client.getPlayer().isGij()){
            angle = -angle;
        }
        switch (direction) {
            case "RIGHT":
                snake.setAngle(snake.getAngle() + angle);
                break;
            case "LEFT":
                snake.setAngle(snake.getAngle() - angle);
                break;
        }
    }
    public void sendTCPData(TCPResponse tcpResponse) {
        try {
            String response = objectMapper.writeValueAsString(tcpResponse);
            client.getPrintWriter().println(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
