package server.model;

import java.net.ServerSocket;

public class TCPServer {
    private ServerSocket tcpServerSocket;

    public TCPServer(ServerSocket tcpServerSocket) {
        this.tcpServerSocket = tcpServerSocket;
    }

    public ServerSocket getTcpServerSocket() {
        return tcpServerSocket;
    }

    public void setTcpServerSocket(ServerSocket tcpServerSocket) {
        this.tcpServerSocket = tcpServerSocket;
    }

}
