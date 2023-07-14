package server.controller;
import server.model.Client;
import server.model.Game;

public class GameController implements Runnable{
    private ServerController serverController;
    private GameDeathController gameDeathController;
    private GameGiftController gameGiftController;
    private GameSnakeBodyController gameSnakeBodyController;
    private Thread gameThread;
    private int FPS;
    private boolean running;
    private int tryFps;
    private boolean isPaused;
    private Game game;
    private Client client1;
    private Client client2;
    int i = 0;

    public GameController(Game game,ServerController serverController,int FPS,Client client1,Client client2) {
        this.client1 = client1;
        this.client2 = client2;
        this.FPS = FPS;
        this.game = game;
        this.serverController = serverController;
        gameDeathController = new GameDeathController(game);
        gameGiftController = new GameGiftController(game);
        gameSnakeBodyController = new GameSnakeBodyController(game);

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
        gameSnakeBodyController.addBody();
        gameDeathController.checkPlayersDeath();
        gameGiftController.handleGifts();
        double v = 4;
        if (i == 4) {
            double vX = Math.cos(Math.toRadians(game.getPlayer1().getSnake().getAngle())) * v;
            double vY = Math.sin(Math.toRadians(game.getPlayer1().getSnake().getAngle())) * v;
            game.getPlayer1().getSnake().setvY(vY);
            game.getPlayer1().getSnake().setvX(vX);
            game.getPlayer2().getSnake().getSnakeHead().setY(100);
//        game.getPlayer1().getSnake().getSnakeHead().setX(30);
            game.getPlayer1().getSnake().getSnakeHead().setX((int) (game.getPlayer1().getSnake().getSnakeHead().getX() + game.getPlayer1().getSnake().getvX()));
            game.getPlayer1().getSnake().getSnakeHead().setY((int) (game.getPlayer1().getSnake().getSnakeHead().getY() + game.getPlayer1().getSnake().getvY()));
            game.getPlayer2().getSnake().getSnakeHead().setX((int) (game.getPlayer2().getSnake().getSnakeHead().getX() + game.getPlayer2().getSnake().getvX()));
            game.getPlayer2().getSnake().getSnakeHead().setY((int) (game.getPlayer2().getSnake().getSnakeHead().getY() + game.getPlayer2().getSnake().getvY()));
            i = 0;
        }
        i ++;
    }
}
