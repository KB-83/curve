package client.controller;

import client.model.Game;
import client.util.Loop;
import client.view.GamePanel;

public class GameController {
    private Game game;
    private GamePanel gamePanel;
    private Loop loop;

    public GameController(Game game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
        loop = new Loop(60,game,gamePanel);
    }
    public void startGame() {
        loop.start();
    }
}
