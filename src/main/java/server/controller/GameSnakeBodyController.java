package server.controller;

import server.model.*;

public class GameSnakeBodyController {
    Game game;


    public GameSnakeBodyController(Game game) {
        this.game = game;
    }
    public void addBody(Snake snake){
        SnakeBodyPart snakeBodyPart = snake.getLastSnakeBodyPart();
        SnakeBodyPart snakeBodyPart1 = null;
        if (snakeBodyPart != null){
            if (Point.distance(snakeBodyPart.getX(),snakeBodyPart.getY(),snake.getSnakeHead().getX(),snake.getSnakeHead().
                    getY()) > 3*SnakeBodyPart.getR()+ SnakeHead.getR()) {
                snakeBodyPart1 = new SnakeBodyPart();
                Point point = Point.middlePoint(snakeBodyPart.getX(),snakeBodyPart.getY(),snake.getSnakeHead().getX(),snake.getSnakeHead().getY());
                snakeBodyPart1.setX(point.getX());
                snakeBodyPart1.setY(point.getY());
            }
        }
        else {
            snakeBodyPart1 = new SnakeBodyPart();
            snakeBodyPart1.setX(snake.getSnakeHead().getX());
            if (snake.getAngle()>0 && snake.getAngle() <180) {
                snakeBodyPart1.setY(snake.getSnakeHead().getY() - SnakeBodyPart.getR() - SnakeHead.getR());
            }
            else {
                snakeBodyPart1.setY(snake.getSnakeHead().getY() + SnakeBodyPart.getR() + SnakeHead.getR());
            }
        }
        if (snakeBodyPart1 != null) {
            snake.getSnakeBody().getSnakeBodyPartArray().add(snakeBodyPart1);
            snake.setLastSnakeBodyPart(snakeBodyPart1);
        }


    }
}

