package server.controller;

import server.model.*;

import java.util.ArrayList;

public class GameDeathController {
    private Game game;
    public final static int WIDTH = 1000;
    public final static int HEIGHT = 800;

    public GameDeathController(Game game) {
        this.game = game;
    }
    public void checkPlayersDeath(){
        Snake snake1 = game.getPlayer1().getSnake();
        Snake snake2 = game.getPlayer2().getSnake();
        if (!checkBounds(snake1.getSnakeHead())){
            //game is over
            System.out.println("player one loosed");
        }
        if (!checkBounds(snake2.getSnakeHead())) {
            // game is over
            System.out.println("player two loosed");
        }
        checkHit(snake1,snake2);

    }
    private boolean checkBounds(SnakeHead snakeHead){
        if (snakeHead.getX() < 0 || snakeHead.getX() > WIDTH ||
                snakeHead.getX() < 0 || snakeHead.getY() > HEIGHT) {
            return false;
        }
        return true;
    }
    private void checkHit(Snake snake1, Snake snake2) {

        SnakeHead snakeHead1 = snake1.getSnakeHead();
        SnakeHead snakeHead2 = snake2.getSnakeHead();
        ArrayList<SnakeBodyPart> snakeBodyParts1 = snake1.getSnakeBody().getSnakeBodyPartArray();
        ArrayList<SnakeBodyPart> snakeBodyParts2 = snake2.getSnakeBody().getSnakeBodyPartArray();
        if (snakeBodyParts1.size() != 0) {
            for (SnakeBodyPart snakeBodyPart : snakeBodyParts1) {
                if (Point.distance(snakeHead1.getX(),snakeHead1.getY(),snakeBodyPart.getX(),
                        snakeBodyPart.getY()) < 2 * SnakeBodyPart.getR()) {
                    //player one loosed
                    System.out.println("player one loosed");
                }
                if (Point.distance(snakeHead2.getX(),snakeHead2.getY(),snakeBodyPart.getX(),
                        snakeBodyPart.getY()) < 2 * SnakeBodyPart.getR()) {
                    //player two loosed
                    System.out.println("player two loosed");
                }
            }
        }
        if (snakeBodyParts2.size() != 0) {
            for (SnakeBodyPart snakeBodyPart : snakeBodyParts2) {
                if (Point.distance(snakeHead1.getX(),snakeHead1.getY(),snakeBodyPart.getX(),
                        snakeBodyPart.getY()) < 2 * SnakeBodyPart.getR()) {
                    //player one loosed
                    System.out.println("player one loosed");
                }
                if (Point.distance(snakeHead2.getX(),snakeHead2.getY(),snakeBodyPart.getX(),
                        snakeBodyPart.getY()) < 2 * SnakeBodyPart.getR()) {
                    //player two loosed
                    System.out.println("player two loosed");
                }
            }
        }
    }
}
