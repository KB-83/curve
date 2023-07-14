package server.controller;

import server.model.*;

public class GameGiftController {
    private Game game;
    public final static int WIDTH = 1000;
    public final static int HEIGHT = 800;
    private final int R = 13;

    public GameGiftController(Game game) {
        this.game = game;
    }
    public void handleGifts(){}
    public void createGift(){
        if (game.getPlayerOneGift() == null && game.getPlayerTwoGift() == null && game.getActivatedGift() == null){
            Point location = giftLocationGenerator();
            int i = (int) (4 *Math.random());
            Gift gift = null;
            switch (i) {
                case 0 :
                    gift = new Boost();
                    break;
                case 1 :
                    gift = new Freeze();
                    break;
                case 2 :
                    gift = new Confuse();
                    break;
                case 3 :
                    gift = new Clear();
                    break;
            }
            gift.setX(location.getX());
            gift.setY(location.getY());
            game.setActivatedGift(gift);
        }
    }
    private Point giftLocationGenerator(){
        boolean isValid = false;
        Point point = null ;
        while (!isValid) {
            int x = (int) (WIDTH * Math.random());
            int y = (int) (HEIGHT * Math.random());
            if (checkPointValidity(x,y,game.getPlayer1().getSnake()) && checkPointValidity(x,y,game.getPlayer2().getSnake())){
                isValid = true;
                point = new Point(x,y);
            }

        }
        return point;
    }
    private boolean checkPointValidity(int x, int y,Snake snake) {
        boolean answer = true;
        if (snake.getSnakeBody().getSnakeBodyPartArray().size() == 0) {
            return answer;
        }
        for (SnakeBodyPart snakeBodyPart : snake.getSnakeBody().getSnakeBodyPartArray()) {
            if (Point.distance(x,y,snakeBodyPart.getX(),snakeBodyPart.getY()) <= SnakeBodyPart.getR() + R ) {
                return false;
            }
        }
        return answer;
    }
}
