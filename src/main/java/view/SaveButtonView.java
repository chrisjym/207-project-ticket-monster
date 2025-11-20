package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class SaveButtonView extends JPanel {

    private final String viewName = "save";
    private JButton saveButton = null;


    public SaveButtonView() {

        Color base = new Color(59, 130, 246);

        saveButton = new JButton("Save Event");
        saveButton.setFont(new Font("SegoeUI", Font.BOLD, 14));
        saveButton.setForeground(new Color(59, 130, 246));
        saveButton.setBackground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createLineBorder(base, 3));
        saveButton.setPreferredSize(new Dimension(160, 45));
        addActionListeners();

        add(saveButton);


    }

    private void addActionListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello!");
            }
        });
    }

}
