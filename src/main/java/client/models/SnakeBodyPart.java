package client.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SnakeBodyPart {
    private int x,y;
    @JsonIgnore
    private static final int R = 7;
    public SnakeBodyPart() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public static int getR(){
        return R;
    }
}
