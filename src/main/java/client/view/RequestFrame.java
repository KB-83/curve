package client.view;


import client.controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RequestFrame extends JFrame{
    private JButton yes;
    private JButton no;
    private ClientController clientController;
    private JLabel opponentName;

    public RequestFrame(ClientController clientController) {
        opponentName = new JLabel();
        opponentName.setBounds(0,10,200,50);
        this.clientController = clientController;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientController.sendJoinRequestAnswer("NO");
            }
        });
        setResizable(false);

        setTitle("join request");
        setSize(200,300);
        setLayout(null);

        yes = new JButton("yes");
        no = new JButton("no");
        yes.setBounds(0,100,100,50);
        no.setBounds(100,100,100,50);

        yes.addActionListener(e -> {
            clientController.sendJoinRequestAnswer("YES");
            setVisible(false);
        });

        no.addActionListener(e -> {
            clientController.sendJoinRequestAnswer("NO");
            setVisible(false);
        });
        add(no);
        add(yes);
        setLocationRelativeTo(this);
        add(opponentName);

    }


    public void setItVisible(String opponentName){
        this.opponentName.setText("do you want to play with:"+opponentName);
        repaint();
        revalidate();
        setVisible(true);
        setLocationRelativeTo(this);
    }
}
