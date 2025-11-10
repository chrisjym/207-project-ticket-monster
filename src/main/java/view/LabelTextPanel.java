package view;

import javax.swing.*;
import java.awt.*;

/**
 * A panel containing a label and a text field.
 */
class LabelTextPanel extends JPanel {
    LabelTextPanel(JLabel label, JTextField textField) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        label.setFont(new Font("SegoeUI", Font.BOLD, 14));
        textField.setFont(new Font("SegoeUI", Font.PLAIN, 14));

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension fieldSize = new Dimension(250, 40); // width 250px, height 30px
        textField.setMaximumSize(fieldSize);
        textField.setPreferredSize(fieldSize);
        textField.setMinimumSize(fieldSize);

        add(label);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(textField);
    }
}
