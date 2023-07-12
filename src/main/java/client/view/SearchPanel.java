package client.view;

import client.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchPanel extends CurveCustomPanel{
    private CardPanel cardPanel;
    private JComboBox<String> themeComboBox;
    private JButton start;
    public SearchPanel(CardPanel cardPanel, ClientController clientController) {
        super(clientController);
        this.cardPanel = cardPanel;
        setLayout(null);
        setupUI();
    }

    private void setupUI() {
        int startWidth = 100;
        int startHeight = 50;
        start = new JButton("GO");
        start.setBounds((WIDTH - startWidth - 30)/2 ,(3*HEIGHT)/4,startWidth,startHeight);
        start.setForeground(Color.CYAN);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClientController().startRequest(themeComboBox.getItemAt(themeComboBox.getSelectedIndex()));
            }
        });
        JLabel themeLabel = new JLabel("Choose a theme:");
        themeLabel.setBounds(200,300,200,50);
        themeLabel.setForeground(Color.CYAN);
        add(themeLabel);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String selectedTheme = (String) themeComboBox.getSelectedItem();
                    JOptionPane.showMessageDialog(null, "Selected theme: " + selectedTheme);
                }
            });
            add(selectButton);
            add(start);
        }
        public void setPlayers(ArrayList<String> players) {
        if (themeComboBox != null) {
            remove(themeComboBox);
        }
            themeComboBox = new JComboBox<>(players.toArray(new String[0]));
            themeComboBox.setBounds(500,300,200,50);
            themeComboBox.setForeground(Color.CYAN);
            themeComboBox.setBackground(Color.black);
            add(themeComboBox);
            revalidate();
            repaint();
        }
    }

