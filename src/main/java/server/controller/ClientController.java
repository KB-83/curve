package server.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.controller.datacontroller.TCPRequest;
import server.controller.datacontroller.TCPResponse;
import server.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ClientController extends Thread{
    private Client client;
    private BufferedReader reader;
    private ObjectMapper objectMapper;
    private ServerController serverController;
    private boolean isInGame;

    public ClientController(ObjectMapper objectMapper,BufferedReader reader, Client client,ServerController serverController) {
        this.serverController = serverController;
        this.reader = reader;
        this.client = client;
        this.objectMapper = objectMapper;
        isInGame = true;
    }

    @Override
    public void run() {
        while (isInGame) {
            receiveTCPConnection();
        }
    }
    public void control() {

    }
    private void receiveTCPConnection() {
        try {
            String json = reader.readLine();
            TCPRequest tcpRequest = objectMapper.readValue(json,TCPRequest.class);
            handleTCPRequest(tcpRequest);
        } catch (IllegalArgumentException e){
            this.stop();
        }
        catch (IOException e) {
            isInGame = false;
            System.out.println("receiving exception line 48 server.controller.ClientController");
        }
    }
    private void handleTCPRequest(TCPRequest tcpRequest) {
        switch (tcpRequest.getType()) {
            case 0:
                break;
            case 1:
                snakeMoveRequest(tcpRequest.getMassage());
                break;
            case 2:
                serverController.removeClient(tcpRequest.getMassage());
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
    public void endGame(String winner,String looser) {
        Client looserClient = findLooser(looser);
        looserClient.setInWaitingRoom(true);
        client.setInWaitingRoom(true);
        serverController.sendClients(serverController.getUdpServer().getUdpServerSocket(),looserClient.getInetAddress(),
                looserClient.getPort(),looser);
        serverController.sendClients(serverController.getUdpServer().getUdpServerSocket(),client.getInetAddress(),
                client.getPort(),winner);
        TCPResponse tcpResponse = new TCPResponse(1,winner);
        sendTCPData(tcpResponse);
    }
    private Client findLooser(String looser) {
        ArrayList<Client> clients = serverController.getClients();
        for (Client client : clients) {
            if (client.getUserName().equals(looser)) {
                return client;
            }
        }
        return null;
    }
}
