package client.controller;

import client.controller.datacontroller.TCPRequest;
import client.controller.datacontroller.TCPResponse;
import client.controller.datacontroller.UDPRequest;
import client.controller.datacontroller.UDPResponse;
import client.listener.MovementListener;
import client.model.Game;
import client.view.CurveCustomFrame;
import client.view.GamePanel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientController {

    private Socket clientTCPSocket;
    private Thread udpThread;
    private Thread tcpThread;
    private DatagramSocket clientUDPSocket;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9001;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String userName ;
    private CurveCustomFrame curveCustomFrame;
    private BufferedReader tcpReader;
    private Game curruntGame;
    private PrintWriter tcpWriter;
    private String waitingForMyAnswer;

    public ClientController() throws IOException {
        clientTCPSocket = new Socket("localhost",SERVER_PORT);
        clientTCPSocket.setTcpNoDelay(true);
        tcpReader = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
        tcpWriter = new PrintWriter(clientTCPSocket.getOutputStream(),true);
        clientUDPSocket = new DatagramSocket();
        curveCustomFrame = new CurveCustomFrame(this);
        run();
    }
    public void run(){
        udpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    UDPResponse response = receiveUDPData();
                    handleUDPData(response);
                }
            }
        });
        tcpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    TCPResponse response = receiveTCPData();
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
                tcpWriter.println(userName);
            curveCustomFrame.cardPanel.getCardLayout().show(curveCustomFrame.cardPanel, "SEARCH");
            return;
        }
        System.out.println(response);
    }
    public void startRequest(String opponentName){
        UDPRequest startRequest = new UDPRequest(userName,1,opponentName);
        sendUDPData(startRequest);
    }
    private UDPResponse receiveUDPData(){
        byte[] receiveData = new byte[60000];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientUDPSocket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        UDPResponse udpResponse;
        try {
            udpResponse = objectMapper.readValue(response,UDPResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return udpResponse;
    }
    private void sendUDPData(UDPRequest request){
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

    private void handleUDPData(UDPResponse udpResponse){
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
            case 3 :
                    curveCustomFrame.getRequestFrame().setItVisible(udpResponse.getMassage());
                    waitingForMyAnswer = udpResponse.getMassage();
                break;
        }
    }
    private void startGame(Game game){
        curruntGame = game;
        curveCustomFrame.cardPanel.getCardLayout().show(curveCustomFrame.cardPanel, "GAME");
        GamePanel gamePanel = curveCustomFrame.cardPanel.getGamePanel();
        gamePanel.setKeyListener(new MovementListener(this));
        gamePanel.requestFocus();
        GameController gameController = new GameController(curruntGame,gamePanel);
        gameController.startGame();
    }
    private TCPResponse receiveTCPData(){
        String jsonResponse = null;
        TCPResponse tcpResponse = null;
        try {
            jsonResponse = tcpReader.readLine();
            tcpResponse = objectMapper.readValue(jsonResponse, TCPResponse.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tcpResponse;
    }
    public void movementRequest(String direction) {
        TCPRequest tcpRequest = new TCPRequest(1,direction);
        sendTCPData(tcpRequest);
    }
    public void exitRequest(){
        TCPRequest tcpRequest = new TCPRequest(2,userName);
        sendTCPData(tcpRequest);
    }
    private void sendTCPData(TCPRequest tcpRequest){
        try {
            String json = objectMapper.writeValueAsString(tcpRequest);
            tcpWriter.println(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleTCPData(TCPResponse tcpResponse){
        switch (tcpResponse.getMode()) {
            case 0 :
                try {
                    curruntGame = objectMapper.readValue(tcpResponse.getMassage(), Game.class);
                    curveCustomFrame.cardPanel.getGamePanel().setGame(curruntGame);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 1 :
                curruntGame.setRunnig(false);
                curveCustomFrame.cardPanel.getGamePanel().setGame(null);
                curveCustomFrame.cardPanel.getCardLayout().show(curveCustomFrame.cardPanel, "SEARCH");
                System.out.println("Winner is: "+tcpResponse.getMassage());
                break;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void sendJoinRequestAnswer(String answer) {
        int type = 22;
        if (answer.equals("YES")){
            type = 21;
        }
        if (!waitingForMyAnswer.equals("")) {
            UDPRequest udpRequest = new UDPRequest(userName, type, waitingForMyAnswer);
            sendUDPData(udpRequest);
        }
        else{
            System.out.println("oops yourOpponentHasGone");
        }

    }


}
