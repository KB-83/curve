package client.view;

import javax.swing.*;
import java.awt.*;

public class CurveCustomFrame extends JFrame {
//    GraphicManager gM;
    public CardPanel cardPanel;
    public CurveCustomFrame() {

//        this.gM = gM;
        this.cardPanel = new CardPanel();
        this.setFocusable(false);
        this.add(cardPanel);
        this.pack();// check
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.revalidate();
        this.setVisible(true);
    }
}
