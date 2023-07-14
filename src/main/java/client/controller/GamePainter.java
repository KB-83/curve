package client.controller;


import client.model.*;

import java.awt.*;

public class GamePainter {
    public void paint(Game game, Graphics2D g2) {
        if (game != null) {
            drawSnake(game.getPlayer1().getSnake(),g2,Color.YELLOW);
            drawSnake(game.getPlayer2().getSnake(),g2,Color.BLUE);
            drawGift(game,g2);
        }
    }
    private void drawSnake(Snake snake, Graphics2D g2,Color color) {
        g2.setColor(color);
        g2.drawOval(snake.getSnakeHead().getX()- SnakeHead.getR(),snake.getSnakeHead().getY()- SnakeHead.getR(),
                2*SnakeHead.getR(),2*SnakeHead.getR());
        if (snake.getSnakeBody() == null) {
            return;
        }

        if (snake.getSnakeBody().getSnakeBodyPartArray() != null) {
            for (SnakeBodyPart snakeBodyPart : snake.getSnakeBody().getSnakeBodyPartArray()) {
                Color bodyColor = new Color(color.getRGB());  // Create a new Color object with the same RGB values
                g2.setColor(bodyColor);
                g2.fillOval(snakeBodyPart.getX()-SnakeBodyPart.getR(),snakeBodyPart.getY()-SnakeBodyPart.getR()
                        ,2*SnakeBodyPart.getR(),2*SnakeBodyPart.getR());
            }
        }
    }
    private void drawGift(Game game, Graphics2D g2) {
        if (game.getActivatedGift() != null) {
            Gift gift = game.getActivatedGift();
            g2.setColor(game.getActivatedGift().getColor());
            g2.fillOval(gift.getX() - Gift.R, gift.getY() - Gift.R, 2 *Gift.R,2 *Gift.R);
        }
    }

}
