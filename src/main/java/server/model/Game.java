package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Game {
    private Player player1;
    private Player player2;
    private Gift activatedGift;
    @JsonIgnore
//    private client.model.Gift playerOneGift;
//    @JsonIgnore
//    private client.model.Gift playerTwoGift;


    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

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

//    public client.model.Gift getPlayerOneGift() {
//        return playerOneGift;
//    }
//
//    public void setPlayerOneGift(client.model.Gift playerOneGift) {
//        this.playerOneGift = playerOneGift;
//    }
//
//    public client.model.Gift getPlayerTwoGift() {
//        return playerTwoGift;
//    }
//
//    public void setPlayerTwoGift(client.model.Gift playerTwoGift) {
//        this.playerTwoGift = playerTwoGift;
//    }
}
