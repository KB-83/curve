package client.model;


public class Player {
    private Snake snake;
    private Gift gift;
    private String name;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
