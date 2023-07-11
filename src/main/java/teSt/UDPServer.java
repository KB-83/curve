package teSt;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 2;
    private List<Player> players;
    private List<Player> waitingPlayers;

    public UDPServer() {
        players = new ArrayList<>();
        waitingPlayers = new ArrayList<>();
    }

    public void start() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received message from client: " + message);

                Player player = getPlayerByAddress(clientAddress, clientPort);
                if (player == null) {
                    player = new Player(clientAddress, clientPort);
                    players.add(player);
                }

                processClientMessage(player, message, serverSocket, clientAddress, clientPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player getPlayerByAddress(InetAddress address, int port) {
        for (Player player : players) {
            if (player.getAddress().equals(address) && player.getPort() == port) {
                return player;
            }
        }
        return null;
    }

    private void processClientMessage(Player player, String message, DatagramSocket serverSocket,
                                      InetAddress clientAddress, int clientPort) throws IOException {
        if (message.startsWith("NAME:")) {
            String name = message.substring(5);
            player.setName(name);

            sendUdpMessage(serverSocket, "Welcome to the game!", clientAddress, clientPort);

            waitingPlayers.add(player);
            sendWaitingPlayersList(serverSocket, clientAddress, clientPort);

            if (waitingPlayers.size() == MAX_PLAYERS) {
                startGame(serverSocket);
            }
        } else if (message.equals("START")) {
            sendUdpMessage(serverSocket, "Starting the game...", clientAddress, clientPort);
            sendUdpMessage(serverSocket, "Let the game begin!", clientAddress, clientPort);
        }
    }

    private void startGame(DatagramSocket serverSocket) throws IOException {
        for (Player player : waitingPlayers) {
            sendUdpMessage(serverSocket, "Starting the game...", player.getAddress(), player.getPort());
            sendUdpMessage(serverSocket, "Let the game begin!", player.getAddress(), player.getPort());
        }

        waitingPlayers.clear();
    }

    private void sendUdpMessage(DatagramSocket serverSocket, String message,
                                InetAddress address, int port) throws IOException {
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        serverSocket.send(sendPacket);
    }

    private void sendWaitingPlayersList(DatagramSocket serverSocket, InetAddress address, int port) throws IOException {
        StringBuilder message = new StringBuilder("Players waiting to play:");
        for (Player player : waitingPlayers) {
            message.append("\n- ").append(player.getName());
        }

        sendUdpMessage(serverSocket, message.toString(), address, port);
    }

    private class Player {
        private InetAddress address;
        private int port;
        private String name;

        public Player(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer();
        server.start();
    }
}


