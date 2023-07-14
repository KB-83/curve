package client.view;

import client.controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends CurveCustomPanel{

    private CardPanel cardPanel;
    private JButton start;
    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    public LoginPanel(CardPanel cardPanel, ClientController clientController) {
        super(clientController);
        this.cardPanel = cardPanel;
        setLayout(null);
        setButtons();
    }
    private void setButtons() {
        int startWidth = 100;
        int startHeight = 50;
        start = new JButton("START");
        start.setBounds((WIDTH - startWidth - 30)/2 ,(3*HEIGHT)/4,startWidth,startHeight);
        start.setForeground(Color.CYAN);
        usernameLabel.setBounds((WIDTH - startWidth - 30)/2 ,(2*HEIGHT)/4,startWidth,startHeight);
        usernameLabel.setForeground(Color.CYAN);
        usernameField.setBounds((WIDTH - startWidth - 30)/2 ,(2*HEIGHT)/4+50,startWidth,startHeight);
        add(start);


        add(usernameLabel);
        add(usernameField);

        // Create password label and password field
        addAction();
    }
    private void addAction() {
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientController().joinRequest(usernameField.getText());
            }
        });
    }

    public CardPanel getCardPanel() {
        return cardPanel;
    }

    public JButton getStart() {
        return start;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }
}
