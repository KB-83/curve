package client.models;


public class Player {
    private Snake snake;
    private Gift gift;

    public Player(Snake snake, Gift gift) {
        this.snake = snake;
        this.gift = gift;
    }

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }
}
