package client.view;

import client.controller.ClientController;

import javax.swing.*;
import java.awt.*;

public abstract class CurveCustomPanel extends JPanel {
    public final static int WIDTH = 1000;
    public final static int HEIGHT = 800;
    private ClientController clientController ;
    public CurveCustomPanel(ClientController clientController) {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.clientController = clientController;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
