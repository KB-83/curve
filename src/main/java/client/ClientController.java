package client;

import client.models.Game;
import client.view.CurveCustomFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ClientController {

    private Socket clientTCPSocket;
    private Thread udpThread;
    private Thread tcpThread;
    private DatagramSocket clientUDPSocket;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9000;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String userName ;
    private CurveCustomFrame curveCustomFrame;
    public ClientController() throws IOException {
        clientTCPSocket = new Socket("localhost",SERVER_PORT);
        clientUDPSocket = new DatagramSocket();
        curveCustomFrame = new CurveCustomFrame(this);
        run();
    }
    public void run(){
        udpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String response = receiveUDPData();
                    handleUDPData(response);
                }
            }
        });
        tcpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String response = receiveTCPData();
                    handleTCPData(response);
                }
            }
        });
        udpThread.start();
        tcpThread.start();
    }
    public void joinRequest(String userName){
        UDPRequest joinRequest = new UDPRequest(userName,0,null);
            sendUDPData(joinRequest);
    }
    private void joinResponse(String response){
        if (response.equals("TRUE")) {
            setUserName(curveCustomFrame.cardPanel.getLoginPanel().getUsernameField().getText());
            curveCustomFrame.cardPanel.getCardLayout().show(curveCustomFrame.cardPanel, "SEARCH");
            return;
        }
        System.out.println(response);
    }
    public void startRequest(String opponentName){
        UDPRequest startRequest = new UDPRequest(userName,1,opponentName);
        sendUDPData(startRequest);
    }
    private String receiveUDPData(){
        byte[] receiveData = new byte[60000];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientUDPSocket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        return response;
    }
    private void sendUDPData(Request request){
        try {
            byte[] sendData = objectMapper.writeValueAsString(request).getBytes();
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            clientUDPSocket.send(sendPacket);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //todo : make s response
    private void handleUDPData(String s){
        UDPResponse udpResponse;
        try {
            udpResponse = objectMapper.readValue(s,UDPResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        switch (udpResponse.getType()) {
            case 0:
                joinResponse(udpResponse.getMassage());
                break;
            case 1:
                try {
                    curveCustomFrame.cardPanel.getSearchPanel().setPlayers(objectMapper.readValue
                            (udpResponse.getMassage(), new TypeReference<ArrayList<String>>() {}));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                Game game;
                try {
                    game = objectMapper.readValue(udpResponse.getMassage(),Game.class);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                startGame(game);
                break;
        }
    }
    private void startGame(Game game){
        curveCustomFrame.cardPanel.getCardLayout().show(curveCustomFrame.cardPanel, "GAME");
    }
    private String receiveTCPData(){return "";}
    private void sendTCPData(Request request){}
    private void handleTCPData(String s){}

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
