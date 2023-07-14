package client;


import client.controller.ClientController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ClientController clientController = new ClientController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
