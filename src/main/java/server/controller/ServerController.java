package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.controller.datacontroller.UDPRequest;
import server.model.Client;
import server.model.Game;
import server.model.TCPServer;
import server.model.UDPServer;

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
            String jsonRequest = receivePacket.toString();
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
                    return;//response room is full
                }
                for (int i = 0; i < clients.size(); i++){
                    if (clients.get(i).getUserName().equals(udpRequest.getUserName())) {
                        return;// username taken
                    }
                }
                Client client = new Client(receivePacket.getAddress(), receivePacket.getPort(), udpRequest.getUserName());
                ClientController clientController = new ClientController(tcpServer, udpServer, client);
                client.setClientController(clientController);
                clientController.control();
                clients.add(client);
                break;
            case 1 :
                for (int i = 0 ; i < clients.size() ; i++) {
                    if (clients.get(i).getUserName().equals(udpRequest.getUserName())){
                        clients.get(i).getClientController().handleUDPRequest(udpRequest);
                        break;
                    }
                }
                break;
        }

    }
}
