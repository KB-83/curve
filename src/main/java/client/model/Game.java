package client.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Game {
    private Player player1;
    private Player player2;
    private Gift activatedGift;
    @JsonIgnore
    private boolean isRunnig;

    public Game() {
        isRunnig = true;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Gift getActivatedGift() {
        return activatedGift;
    }

    public void setActivatedGift(Gift activatedGift) {
        this.activatedGift = activatedGift;
    }

    public boolean isRunnig() {
        return isRunnig;
    }

    public void setRunnig(boolean runnig) {
        isRunnig = runnig;
    }

}
