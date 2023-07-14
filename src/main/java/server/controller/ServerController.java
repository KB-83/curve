package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.controller.datacontroller.TCPResponse;
import server.controller.datacontroller.UDPRequest;
import server.controller.datacontroller.UDPResponse;
import server.model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerController {
    private UDPServer udpServer;
    private TCPServer tcpServer;
    private ArrayList<Client> clients;

    private Thread udpThread;
    private Thread tcpThread;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final int PORT = 9001;
    public ServerController() throws IOException {
        clients = new ArrayList<>();
        tcpServer = new TCPServer(new ServerSocket(PORT));
        udpServer = new UDPServer(new DatagramSocket(PORT));
        run();
    }
    public void run(){
        udpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    receiveAndHandleUDPData();
                }
            }
        });
        tcpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    receiveTCPSocket();
                }
            }
        });
        udpThread.start();
        tcpThread.start();
    }
    private void receiveAndHandleUDPData(){
        try {
            byte[] receiveData = new byte[60000];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpServer.getUdpServerSocket().receive(receivePacket);
            String jsonRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());
            UDPRequest udpRequest = objectMapper.readValue(jsonRequest, UDPRequest.class);
            handleUDPRequest(udpRequest, receivePacket);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleUDPRequest(UDPRequest udpRequest,DatagramPacket receivePacket) {
        int requestNum = udpRequest.getRequestNum();
        switch (requestNum){
            case 0:
                if (clients.size() == 20) {
                    UDPResponse udpResponse = new UDPResponse(0,"FULL");
                    sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,receivePacket.getAddress(),receivePacket.getPort());
                    return;
                }
                for (int i = 0; i < clients.size(); i++){
                    if (clients.get(i).getUserName().equals(udpRequest.getUserName())) {
                        UDPResponse udpResponse = new UDPResponse(0,"TAKEN");
                        sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,receivePacket.getAddress(),receivePacket.getPort());
                        return;
                    }
                }
                Client client = new Client(receivePacket.getAddress(), receivePacket.getPort(), udpRequest.getUserName());
                clients.add(client);
                UDPResponse udpResponse = new UDPResponse(0,"TRUE");
                sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,receivePacket.getAddress(),receivePacket.getPort());
                for (int i = 0;i < clients.size(); i++) {
                    sendClients(udpServer.getUdpServerSocket(),clients.get(i).getInetAddress(),clients.get(i).getPort(),clients.get(i).getUserName());
                }
                break;
            case 1 :
//                sinchronize//{
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).getUserName().equals(udpRequest.getUserName())) {
                        sendGameRequest(clients.get(i),udpRequest);
                        break;
                    }
                }
                //}
                break;
            case 21 :
                gameRequestAnswer(udpRequest,true);
                break;
            case 22 :
                gameRequestAnswer(udpRequest,false);
                break;

        }

    }
    private void sendGameRequest(Client client,UDPRequest udpRequest){
        Client opponent = null;
        client.setWaitingForAnswer(true);
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUserName().equals(udpRequest.getOpponentName())) {
                opponent = clients.get(i);
                break;
            }
        }
        if (opponent == null){
            return;
        }
        UDPResponse udpResponse = new UDPResponse(3,udpRequest.getUserName());
        sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,opponent.getInetAddress(),opponent.getPort());
    }
    private void gameRequestAnswer(UDPRequest udpRequest,boolean isYes) {
        Client client1 = null;
        Client client2 = null ;
        for (Client client : clients){
            if (client.getUserName().equals(udpRequest.getOpponentName())){
                client1 = client;
            }
            else if (client.getUserName().equals(udpRequest.getUserName())) {
                client2 = client;
            }
        }
        if (isYes){
            startGame(client1,client2);
        }
        else {
            client1.setWaitingForAnswer(false);
        }
    }

    private void receiveTCPSocket() {
        Socket socket;
        try {
            socket = tcpServer.getTcpServerSocket().accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String username = bufferedReader.readLine();
            for (Client client : clients) {
                if (client.getUserName().equals(username)) {
                    client.setSocket(socket);
                    ClientController clientController = new ClientController(tcpServer,objectMapper,bufferedReader,client);
                    client.setClientController(clientController);
                    clientController.start();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void sendGameState(Client client1, Client client2, Game game) {

        try {
            String json = objectMapper.writeValueAsString(game);
            TCPResponse tcpResponse = new TCPResponse(0,json);
            client1.getClientController().sendTCPData(tcpResponse);
            client2.getClientController().sendTCPData(tcpResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendUdpMessage(DatagramSocket serverSocket, UDPResponse udpResponse,
                                InetAddress address, int port) {
        try {
            byte[] sendData = objectMapper.writeValueAsString(udpResponse).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendClients(DatagramSocket serverSocket,InetAddress address, int port,String userName) {
        ArrayList<String> clientsName = new ArrayList<>();
        for (int i = 0 ; i < clients.size(); i++) {
            if (!clients.get(i).getUserName().equals(userName) && clients.get(i).isInWaitingRoom()) {
                clientsName.add(clients.get(i).getUserName());
            }
        }
        try {
            UDPResponse udpResponse = new UDPResponse(1,objectMapper.writeValueAsString(clientsName));
            sendUdpMessage(serverSocket,udpResponse,address,port);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private synchronized void startGame(Client client1,Client client2) {
        Player player1 = new Player(client1.getUserName());
        player1.getSnake().setAngle(90);
        player1.getSnake().getSnakeHead().setX(400);
        player1.getSnake().getSnakeHead().setY(450);
        Player player2 = new Player(client2.getUserName());
        player2.getSnake().setAngle(-90);
        player2.getSnake().getSnakeHead().setX(500);
        player2.getSnake().getSnakeHead().setY(450);

        client1.setPlayer(player1);
        client2.setPlayer(player2);

        Game game = new Game(player1,player2);
        GameController gameController = new GameController(game,this,60,client1,client2);
        UDPResponse udpResponse = null;
        try {
            udpResponse = new UDPResponse(2,objectMapper.writeValueAsString(game));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < clients.size(); i++){
            if (clients.get(i).getUserName().equals(client1.getUserName())){
                clients.get(i).setInWaitingRoom(false);
                sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,clients.get(i).
                        getInetAddress(),clients.get(i).getPort());
            }
            else if (clients.get(i).getUserName().equals(client2.getUserName())) {
                clients.get(i).setInWaitingRoom(false);
                sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,clients.get(i).
                        getInetAddress(),clients.get(i).getPort());
            }
        }
        for (Client client :clients){
            sendClients(udpServer.getUdpServerSocket(),client.getInetAddress(),client.getPort(),client.getUserName());
        }
        gameController.startGame();
        //send game started data
        // start GameController and sen it to both clients
    }
}
