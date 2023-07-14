package client.util;


import client.model.Game;
import client.view.GamePanel;

public class Loop implements Runnable{
    //    private GameState gameState;// to update
//    private GamePanel gamePanel;// to repaint
    private Thread gameThread;
    private int FPS;
    private boolean running;
    private boolean isPaused;
    private GamePanel gamePanel;
    //    this int is to test app rendering
    private int tryFps;

    public Loop(int FPS, Game game, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gamePanel.setGame(game);
        this.FPS = FPS;
    }

    public void start(){
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
//            while (gameState.isPaused()){}
            currentTime = System.nanoTime();
            delta = (currentTime - lastTime) / drawInterval ;
            if(delta >= 1){
                tryFps++;
                gamePanel.repaint();
                //todo : game would be update by tcp network connection
                lastTime = System.nanoTime();
            }
            if (System.nanoTime()-startfPS >= 1000000000){
                startfPS = System.nanoTime();
                tryFps = 0;
            }
        }
    }
}
