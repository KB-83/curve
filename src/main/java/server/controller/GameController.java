package server.controller;
import server.model.Client;
import server.model.Game;
import server.model.Snake;

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
        game.setGameController(this);
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

    public void kill(String winnerName,String looserName) {
        client1.getClientController().endGame(winnerName,looserName);
        client2.getClientController().endGame(winnerName,looserName);
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
        gameSnakeBodyController.addBody(game.getPlayer1().getSnake());
        gameSnakeBodyController.addBody(game.getPlayer2().getSnake());
        gameDeathController.checkPlayersDeath();
        gameGiftController.handleGifts();
        gameGiftController.createGift();
        setSnakeV(game.getPlayer1().getSnake());
        setSnakeV(game.getPlayer2().getSnake());
        if (i == 4) {
            game.getPlayer1().getSnake().getSnakeHead().setX((int) (game.getPlayer1().getSnake().getSnakeHead().getX() + game.getPlayer1().getSnake().getvX()));
            game.getPlayer1().getSnake().getSnakeHead().setY((int) (game.getPlayer1().getSnake().getSnakeHead().getY() + game.getPlayer1().getSnake().getvY()));
            game.getPlayer2().getSnake().getSnakeHead().setX((int) (game.getPlayer2().getSnake().getSnakeHead().getX() + game.getPlayer2().getSnake().getvX()));
            game.getPlayer2().getSnake().getSnakeHead().setY((int) (game.getPlayer2().getSnake().getSnakeHead().getY() + game.getPlayer2().getSnake().getvY()));
            i = 0;
        }
        i ++;
    }
    private void setSnakeV(Snake snake){
        int pureV = snake.getPureV();
        double vX = Math.cos(Math.toRadians(snake.getAngle())) * pureV;
        double vY = Math.sin(Math.toRadians(snake.getAngle())) * pureV;
        snake.setvY(vY);
        snake.setvX(vX);
    }
}
