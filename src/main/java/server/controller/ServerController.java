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
    private final int PORT = 9000;
    public final static boolean LIFE_IS_HARD = true;
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
                    // har client bayad montazere request khodesh bashe
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
                //todo : haminja socket ham begir
                ClientController clientController = new ClientController(tcpServer, udpServer, client);
                client.setClientController(clientController);
                clientController.control();
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
                        startGame(clients.get(i),udpRequest);
                        break;
                    }
                }
                //}
                break;
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
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        String jsonResponse = reader.readLine();
    }
    private void sendTCPData(){}
    private void handleTCPData(TCPResponse tcpResponse){}
    public void sendGameState(Client client1, Client client2, Game game) {

        try {
            String json = objectMapper.writeValueAsString(game);
            TCPResponse tcpResponse = new TCPResponse(0,json);
            String response = objectMapper.writeValueAsString(tcpResponse);
            PrintWriter printWriter1 = new PrintWriter(client1.getSocket().getOutputStream(),true);
            PrintWriter printWriter2 = new PrintWriter(client2.getSocket().getOutputStream(),true);

            printWriter1.println(response);
            printWriter2.println(response);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            if (!clients.get(i).getUserName().equals(userName) && clients.get(i).isWaiting()) {
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
    private synchronized void startGame(Client client1,UDPRequest udpRequest) {
        Client client2 = null;
        for (Client client : clients){
            if (client.getUserName().equals(udpRequest.getOpponentName())) {
                client2 = client;
                break;
            }
        }
//        start the game
        Player player1 = new Player(udpRequest.getUserName());
        Player player2 = new Player(udpRequest.getOpponentName());
        Game game = new Game(player1,player2);
        GameController gameController = new GameController(game,this,60,client1,client2);
        UDPResponse udpResponse = null;
        try {
            udpResponse = new UDPResponse(2,objectMapper.writeValueAsString(game));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < clients.size(); i++){
            if (clients.get(i).getUserName().equals(udpRequest.getUserName())){
                clients.get(i).setWaiting(false);
                sendUdpMessage(udpServer.getUdpServerSocket(),udpResponse,clients.get(i).
                        getInetAddress(),clients.get(i).getPort());
            }
            else if (clients.get(i).getUserName().equals(udpRequest.getOpponentName())) {
                clients.get(i).setWaiting(false);
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
