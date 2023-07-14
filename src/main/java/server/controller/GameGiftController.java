package server.controller;

import server.model.*;

import java.util.ArrayList;

public class GameGiftController {
    private Game game;
    public final static int WIDTH = 1000;
    public final static int HEIGHT = 800;
    private final int R = 13;

    public GameGiftController(Game game) {
        this.game = game;
    }
    public void handleGifts(){
        checkGiftExpireTime(game.getPlayer1());
        checkGiftExpireTime(game.getPlayer2());
        unlockGift(game.getPlayer1());
        unlockGift(game.getPlayer2());
    }
    public void createGift(){
//        game.getPlayer1().getGift() == null && game.getPlayer2().getGift() == null &&
        if (game.getActivatedGift() == null){
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
    private void unlockGift(Player player){
        if (game.getActivatedGift() == null) {
            return;
        }
        SnakeHead snakeHead = player.getSnake().getSnakeHead();
        Gift gift = game.getActivatedGift();
        if (Point.distance(snakeHead.getX(),snakeHead.getY(),gift.getX(),gift.getY()) <= R+SnakeHead.getR()) {
            if (player.getGift() != null){
                disActiveGift(player, player.getGift());
            }
            activeGift(player,gift);
        }
    }
    private void checkGiftExpireTime(Player player) {
        if (player.getGift() == null) {
            return;
        }
        if (System.currentTimeMillis() - player.getGift().getActiveTime() > 5000) {
            disActiveGift(player, player.getGift());
            player.setGift(null);
        }
    }
    private void activeGift(Player player,Gift gift){
        game.setActivatedGift(null);
        Player otherPlayer = game.getPlayer1();
        if (otherPlayer.equals(player)) {
            otherPlayer = game.getPlayer2();
        }
        switch (gift.getClass().getSimpleName()){
            case "Freeze":
                otherPlayer.getSnake().setPureV(otherPlayer.getSnake().getPureV()/2);
                gift.setActiveTime(System.currentTimeMillis());
                player.setGift(gift);
                break;
            case "Boost":
                player.setGift(gift);
                otherPlayer.getSnake().setPureV(otherPlayer.getSnake().getPureV() * 2);
                gift.setActiveTime(System.currentTimeMillis());
                break;
            case "Confuse":
                otherPlayer.setGij(true);
                gift.setActiveTime(System.currentTimeMillis());
                player.setGift(gift);
                break;
            case "Clear":
                game.getPlayer1().getSnake().getSnakeBody().setSnakeBodyPartArray(new ArrayList<>());
                game.getPlayer2().getSnake().getSnakeBody().setSnakeBodyPartArray(new ArrayList<>());
                break;
        }
    }
    private void disActiveGift(Player player,Gift gift){
        player.setGift(null);
        Player otherPlayer = game.getPlayer1();
        if (otherPlayer.equals(player)) {
            otherPlayer = game.getPlayer2();
        }
        switch (gift.getClass().getSimpleName()){
            case "Freeze":
                otherPlayer.getSnake().setPureV(otherPlayer.getSnake().getPureV()*2);

                break;
            case "Boost":
                otherPlayer.getSnake().setPureV(otherPlayer.getSnake().getPureV() / 2);
                break;
            case "Confuse":
                otherPlayer.setGij(false);
                break;
        }
    }

}
