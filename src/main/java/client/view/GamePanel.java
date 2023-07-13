package client.view;

import client.ClientController;
import client.controller.GamePainter;
import client.models.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class GamePanel extends CurveCustomPanel {
    private Game game;
    private GamePainter gamePainter;
    public GamePanel(ClientController clientController) {
        super(clientController);
        setLayout(null);
        gamePainter = new GamePainter();

    }
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        gamePainter.paint(game,graphics2D);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    public void setKeyListener(KeyListener keyListener) {
        if (getKeyListeners().length == 0) {
            addKeyListener(keyListener);
        }
    }
}
