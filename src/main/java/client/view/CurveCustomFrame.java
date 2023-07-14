package client.view;

import client.controller.ClientController;

import javax.swing.*;

public class CurveCustomFrame extends JFrame {
//    GraphicManager gM;
    public CardPanel cardPanel;
    private ClientController clientController;
    public CurveCustomFrame(ClientController clientController) {
        this.clientController = clientController;
//        this.gM = gM;
        this.cardPanel = new CardPanel(clientController);
        this.setFocusable(false);
        this.add(cardPanel);
        this.pack();// check
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.revalidate();
        this.setVisible(true);
    }
}
