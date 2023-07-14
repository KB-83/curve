package server.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player {
    private Snake snake;
    private Gift gift;
    private String name;
    @JsonIgnore
    private boolean gij;

    public Player() {
        snake = new Snake();
    }

    public Player(String name) {
        this.name = name;
        snake = new Snake();
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

    public boolean isGij() {
        return gij;
    }

    public void setGij(boolean gij) {
        this.gij = gij;
    }
}
