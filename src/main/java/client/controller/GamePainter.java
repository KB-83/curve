package client.controller;


import client.models.Game;
import client.models.Gift;
import client.models.Snake;
import client.models.SnakeBodyPart;

import java.awt.*;

public class GamePainter {
    public void paint(Game game, Graphics2D g2) {
        if (game != null) {
            drawSnake(game.getPlayer1().getSnake(),g2);
            drawSnake(game.getPlayer2().getSnake(),g2);
            drawGift(game,g2);
        }
    }
    private void drawSnake(Snake snake, Graphics2D g2) {
        if (snake.getSnakeBody().getSnakeBodyPartArray() != null) {
            for (SnakeBodyPart snakeBodyPart : snake.getSnakeBody().getSnakeBodyPartArray()) {
                g2.setColor(Color.YELLOW);
                g2.translate(snakeBodyPart.getX(), snakeBodyPart.getY());
                g2.rotate(snakeBodyPart.getArc());
            }
        }
    }
    private void drawGift(Game game, Graphics2D g2) {
        if (game.getActivatedGift() != null) {
            Gift gift = game.getActivatedGift();
            g2.drawOval(gift.getX(), gift.getY(), 40,40);
        }
    }

}
