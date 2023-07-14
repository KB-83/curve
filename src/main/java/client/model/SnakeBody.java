package client.model;

import java.util.ArrayList;

public class SnakeBody {
    private ArrayList<SnakeBodyPart> snakeBodyPartArray;

    public SnakeBody() {
    }

    public ArrayList<SnakeBodyPart> getSnakeBodyPartArray() {
        return snakeBodyPartArray;
    }

    public void setSnakeBodyPartArray(ArrayList<SnakeBodyPart> snakeBodyPartArray) {
        this.snakeBodyPartArray = snakeBodyPartArray;
    }
}
