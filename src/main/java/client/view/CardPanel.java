package client.view;

import client.ClientController;

import java.awt.*;
import java.util.ArrayList;

public class CardPanel extends CurveCustomPanel{
    private final CardLayout cardLayout = new CardLayout();
    private GamePanel gamePanel;
    private LoginPanel loginPanel;
    private SearchPanel searchPanel;

    public CardPanel(ClientController clientController) {
        super(clientController);
//        this.gM = gM;
//        this.frame = frame;
        // other panels going to be added here
        this.gamePanel = new GamePanel(clientController);
        this.loginPanel = new LoginPanel(this,clientController);

        this.searchPanel = new SearchPanel(this,clientController);
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

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }
}
