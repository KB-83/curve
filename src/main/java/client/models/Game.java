package client.models;


public class Game {
    private Player player1;
    private Player player2;
    private Gift activatedGift;

    public Game() {
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
}
