package client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.awt.*;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Freeze.class),
        @JsonSubTypes.Type(value = Confuse.class),
        @JsonSubTypes.Type(value = Clear.class),
        @JsonSubTypes.Type(value = Boost.class)
})
public abstract class Gift {
    private int x,y;
    public final static int R = 13;
    @JsonIgnore
    private Color color;

    public Gift() {
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
