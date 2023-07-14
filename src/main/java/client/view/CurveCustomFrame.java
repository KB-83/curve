package client.view;

import client.controller.ClientController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CurveCustomFrame extends JFrame {
    public CardPanel cardPanel;
    private ClientController clientController;
    private RequestFrame requestFrame;
    public CurveCustomFrame(ClientController clientController) {
        this.clientController = clientController;
        requestFrame = new RequestFrame(clientController);
        this.cardPanel = new CardPanel(clientController);
        this.setFocusable(false);
        this.add(cardPanel);
        this.pack();// check
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.revalidate();
        this.setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientController.exitRequest();
            }
        });
    }

    public RequestFrame getRequestFrame() {
        return requestFrame;
    }
}
