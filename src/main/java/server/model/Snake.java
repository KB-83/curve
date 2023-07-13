package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Snake {
    private SnakeHead snakeHead;
    private SnakeBody snakeBody;
    @JsonIgnore
    private double vX,vY;
    @JsonIgnore
    private int angle;
    @JsonIgnore
    private SnakeBodyPart lastSnakeBodyPart;

    public Snake() {
        snakeHead = new SnakeHead();
        snakeBody = new SnakeBody();
    }

    public SnakeHead getSnakeHead() {
        return snakeHead;
    }

    public void setSnakeHead(SnakeHead snakeHead) {
        this.snakeHead = snakeHead;
    }

    public SnakeBody getSnakeBody() {
        return snakeBody;
    }

    public void setSnakeBody(SnakeBody snakeBody) {
        this.snakeBody = snakeBody;
    }

    public double getvX() {
        return vX;
    }

    public void setvX(double vX) {
        this.vX = vX;
    }

    public double getvY() {
        return vY;
    }

    public void setvY(double vY) {
        this.vY = vY;
    }
    public SnakeBodyPart getLastSnakeBodyPart() {
        return lastSnakeBodyPart;
    }

    public void setLastSnakeBodyPart(SnakeBodyPart lastSnakeBodyPart) {
        this.lastSnakeBodyPart = lastSnakeBodyPart;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
