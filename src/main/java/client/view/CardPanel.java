package client.view;

import java.awt.*;
import java.util.ArrayList;

public class CardPanel extends CurveCustomPanel{
    private final CardLayout cardLayout = new CardLayout();
    private GamePanel gamePanel;
    private LoginPanel loginPanel;
    private SearchPanel searchPanel;

    public CardPanel() {
//        this.gM = gM;
//        this.frame = frame;
        // other panels going to be added here
        this.gamePanel = new GamePanel();
        this.loginPanel = new LoginPanel(this);
        //test
        ArrayList<String> players = new ArrayList();
        players.add("kb");
        players.add("jafar");
        players.add("akbar");
        this.searchPanel = new SearchPanel(players,this);
        // panel settings

        this.setLayout(cardLayout);
        this.setFocusable(false);

        //adding panels order is important
        this.add(loginPanel , "LOGIN");
        this.add(gamePanel, "GAME");
        this.add(searchPanel,"SEARCH");
        this.revalidate();
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }
}
