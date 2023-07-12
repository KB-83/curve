package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.controller.datacontroller.UDPRequest;
import server.controller.datacontroller.UDPResponse;
import server.model.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;

public class ServerController {
    private UDPServer udpServer;
    private TCPServer tcpServer;
    private ArrayList<Client> clients;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final int PORT = 9000;
    public final static boolean LIFE_IS_HARD = true;
    public ServerController() throws IOException {
        clients = new ArrayList<>();
        tcpServer = new TCPServer(new ServerSocket(PORT));
        udpServer = new UDPServer(new DatagramSocket(PORT));
        while (LIFE_IS_HARD) {
            byte[] receiveData = new byte[60000];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpServer.getUdpServerSocket().receive(receivePacket);
            String jsonRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());
            UDPRequest udpRequest = objectMapper.readValue(jsonRequest,UDPRequest.class);
            controlUDPRequest(udpRequest,receivePacket);

        }
    }
    public void sendGameState(Client client1, Client client2, Game game) {

        try {
            String json = objectMapper.writeValueAsString(game);
            byte[] jsonBytes = json.getBytes();
            OutputStream outputStream1 = client1.getSocket().getOutputStream();
            OutputStream outputStream2 = client2.getSocket().getOutputStream();

            outputStream1.write(jsonBytes);
            outputStream1.flush();
            outputStream1.close();

            outputStream2.write(jsonBytes);
            outputStream2.flush();
            outputStream2.close();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void controlUDPRequest(UDPRequest udpRequest,DatagramPacket receivePacket) {
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
                        startGame(udpRequest);
                        break;
                    }
                }
            //}
                break;
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
    private synchronized void startGame(UDPRequest udpRequest) {
//        start the game
        Player player1 = new Player(udpRequest.getUserName());
        Player player2 = new Player(udpRequest.getOpponentName());
        Game game = new Game(player1,player2);
        GameController gameController = new GameController(game,this);
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
