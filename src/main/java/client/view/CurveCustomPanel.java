package client.view;

import javax.swing.*;
import java.awt.*;

public abstract class CurveCustomPanel extends JPanel {
    public final static int WIDTH = 1000;
    public final static int HEIGHT = 800;
    public CurveCustomPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }
}
