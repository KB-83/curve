package client;

import client.models.Game;
import client.models.GameController;
import client.view.CurveCustomFrame;
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
    private static final int SERVER_PORT = 9000;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String userName ;
    private CurveCustomFrame curveCustomFrame;
    private BufferedReader tcpReader;
    private Game curruntGame;
    private PrintWriter tcpWriter;

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
                    String response = receiveUDPData();
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
        curruntGame = game;
        curveCustomFrame.cardPanel.getCardLayout().show(curveCustomFrame.cardPanel, "GAME");
        GameController gameController = new GameController(curruntGame,curveCustomFrame.cardPanel.getGamePanel());
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
    private void sendTCPData(TCPRequest tcpRequest){
        try {
            String json = objectMapper.writeValueAsString(tcpRequest);
            tcpWriter.write(json);
            tcpWriter.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleTCPData(TCPResponse tcpResponse){
        switch (tcpResponse.getMode()) {
            case 0 :
                try {
                    curruntGame = objectMapper.readValue(tcpResponse.getMassage(), Game.class);
                    curveCustomFrame.cardPanel.getGamePanel().setGame(curruntGame);
//                    System.out.println(curruntGame+"191");
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
