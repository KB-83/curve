package teSt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public void start() {
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to the server. Please enter your name:");
            System.out.println("15");
            String name = reader.readLine();
            System.out.println("17");
            String message = "NAME:" + name;
            byte[] sendData = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            clientSocket.send(sendPacket);
            System.out.println("23");
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(response);

            while (true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(response);

                if (response.equals("Starting the game...")) {
                    break;
                }
            }

            // Game logic goes here

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient();
        client.start();
    }
}


