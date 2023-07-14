package server.model;

import java.net.DatagramSocket;

public class UDPServer {
    private DatagramSocket udpServerSocket;

    public UDPServer(DatagramSocket udpServerSocket) {
        this.udpServerSocket = udpServerSocket;
    }

    public DatagramSocket getUdpServerSocket() {
        return udpServerSocket;
    }

    public void setUdpServerSocket(DatagramSocket udpServerSocket) {
        this.udpServerSocket = udpServerSocket;
    }
}
