package server.controller;
import server.model.Client;
import server.model.Game;
import server.util.Loop;

public class GameController implements Runnable{
    private ServerController serverController;
    private Thread gameThread;
    private int FPS;
    private boolean running;
    private int tryFps;
    private boolean isPaused;
    private Game game;
    private Loop loop;

    public GameController(Game game,ServerController serverController) {
        this.game = game;
        this.serverController = serverController;
    }

    public void startGame(){
        if(gameThread == null) {
            gameThread = new Thread(this);
        }
        running = true;
        gameThread.start();
    }

    public void kill() {
        gameThread.stop();
        running = false;
        gameThread = null;
    }
    public void pause() {
        isPaused = true;
    }
    public void resume() {
        isPaused = false;
    }

    public void run() {
        final long drawInterval = 1000000000/FPS;
        long lastTime = System.nanoTime();
        long startfPS = System.nanoTime();
        long delta = 0;
        long currentTime;
        while (running){
            // sorry but it is the best i can design fo pause mechanisem :(
            //todo : improve pause mechanisem
            while (isPaused){}
            currentTime = System.nanoTime();
            delta = (currentTime - lastTime) / drawInterval ;
            if(delta >= 1){
                tryFps++;


                updateGame();
//                serverController.sendGameState(client1,client2,game);


                lastTime = System.nanoTime();
            }
            if (System.nanoTime()-startfPS >= 1000000000){
                startfPS = System.nanoTime();
                tryFps = 0;
            }
        }
    }
    private void updateGame() {
        game.getPlayer1().getSnake().setX((int) (game.getPlayer1().getSnake().getX() + game.getPlayer1().getSnake().getvX()));
        game.getPlayer1().getSnake().setY((int) (game.getPlayer1().getSnake().getY() + game.getPlayer1().getSnake().getvY()));
        game.getPlayer2().getSnake().setX((int) (game.getPlayer2().getSnake().getX() + game.getPlayer2().getSnake().getvX()));
        game.getPlayer2().getSnake().setY((int) (game.getPlayer2().getSnake().getY() + game.getPlayer2().getSnake().getvY()));
    }
}
