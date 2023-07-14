package client.listener;

import client.controller.ClientController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovementListener implements KeyListener {
    private ClientController clientController;

    public MovementListener(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            clientController.movementRequest("RIGHT");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            clientController.movementRequest("LEFT");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
