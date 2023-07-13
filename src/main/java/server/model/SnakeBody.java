package server.model;
import java.util.ArrayList;

public class SnakeBody {
    private ArrayList<SnakeBodyPart> snakeBodyPartArray;

    public SnakeBody() {
        snakeBodyPartArray = new ArrayList<>();
    }

    public ArrayList<SnakeBodyPart> getSnakeBodyPartArray() {
        return snakeBodyPartArray;
    }

    public void setSnakeBodyPartArray(ArrayList<SnakeBodyPart> snakeBodyPartArray) {
        this.snakeBodyPartArray = snakeBodyPartArray;
    }
}
