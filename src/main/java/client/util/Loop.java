package client.util;


import client.model.Game;
import client.view.GamePanel;

public class Loop implements Runnable{
    private Thread gameThread;
    private int FPS;
    private boolean running;
    private boolean isPaused;
    private GamePanel gamePanel;
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
        while (running && gamePanel.getGame() != null && gamePanel.getGame().isRunnig() ){
            // sorry but it is the best i can design fo pause mechanisem :(
//            while (gameState.isPaused()){}
            currentTime = System.nanoTime();
            delta = (currentTime - lastTime) / drawInterval ;
            if(delta >= 1){
                tryFps++;
                gamePanel.repaint();
                lastTime = System.nanoTime();
            }
            if (System.nanoTime()-startfPS >= 1000000000){
                startfPS = System.nanoTime();
                tryFps = 0;
            }
        }
    }
}
