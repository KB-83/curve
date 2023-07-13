package server.controller;

import server.model.Game;
import server.model.Snake;
import server.model.SnakeBodyPart;
import server.model.SnakeHead;

public class GameSnakeBodyController {
    Game game;


    public GameSnakeBodyController(Game game) {
        this.game = game;
    }
    public void addBody(){
        Snake snake = game.getPlayer1().getSnake();
        SnakeBodyPart snakeBodyPart = snake.getLastSnakeBodyPart();
        SnakeBodyPart snakeBodyPart1 = null;
        if (snakeBodyPart != null){
            if (distance(snakeBodyPart.getX(),snakeBodyPart.getY(),snake.getSnakeHead().getX(),snake.getSnakeHead().
                    getY()) > 3*SnakeBodyPart.getR()+ SnakeHead.getR()) {
                snakeBodyPart1 = new SnakeBodyPart();
                Point point = middlePoint(snakeBodyPart.getX(),snakeBodyPart.getY(),snake.getSnakeHead().getX(),snake.getSnakeHead().getY());
                snakeBodyPart1.setX(point.x);
                snakeBodyPart1.setY(point.y);
            }
        }
        else {

            snakeBodyPart1 = new SnakeBodyPart();
            snakeBodyPart1.setX(snake.getSnakeHead().getX());
            snakeBodyPart1.setY(snake.getSnakeHead().getX()+SnakeBodyPart.getR()+ SnakeHead.getR());
        }
        if (snakeBodyPart1 != null) {
            game.getPlayer1().getSnake().getSnakeBody().getSnakeBodyPartArray().add(snakeBodyPart1);
            snake.setLastSnakeBodyPart(snakeBodyPart1);
        }


    }
    private int distance(int x, int y, int x1, int y1){
        return (int) Math.sqrt(Math.pow(x-x1,2)+Math.pow(y-y1,2));
    }
    private Point middlePoint(int x, int y, int x1, int y1){
        return new Point((x+x1)/2,(y+y1)/2);
    }
}
class Point{
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x;
    int y;
}
