package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import server.controller.GiftController;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Freeze.class),
        @JsonSubTypes.Type(value = Confuse.class),
        @JsonSubTypes.Type(value = Clear.class),
        @JsonSubTypes.Type(value = Boost.class)
})

public abstract class Gift {
    private int x,y;
    @JsonIgnore
    private GiftController giftController;

    public Gift() {
        giftController = new GiftController();
    }

    public GiftController getGiftController() {
        return giftController;
    }

    public void setGiftController(GiftController giftController) {
        this.giftController = giftController;
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
}
