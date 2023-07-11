package server.controller;
import server.model.Client;
import server.model.Game;
import server.util.Loop;

public class GameController implements Runnable{
    //todo:improve  its null just used in game loop
    private ServerController serverController;
    private Thread gameThread;
    private int FPS;
    private boolean running;
    private int tryFps;
    private boolean isPaused;
    private Client client1;
    private Client client2;
    private Game game;
    private Loop loop;
    private void startGame(){
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
                serverController.sendGameState(client1,client2,game);


                lastTime = System.nanoTime();
            }
            if (System.nanoTime()-startfPS >= 1000000000){
                startfPS = System.nanoTime();
                tryFps = 0;
            }
        }
    }
    private void updateGame() {
        client1.getPlayer().getSnake().setX((int) (client1.getPlayer().getSnake().getX() + client1.getPlayer().getSnake().getvX()));
        client1.getPlayer().getSnake().setY((int) (client1.getPlayer().getSnake().getY() + client1.getPlayer().getSnake().getvY()));
        client2.getPlayer().getSnake().setX((int) (client1.getPlayer().getSnake().getX() + client1.getPlayer().getSnake().getvX()));
        client2.getPlayer().getSnake().setY((int) (client1.getPlayer().getSnake().getY() + client1.getPlayer().getSnake().getvY()));
    }
}
