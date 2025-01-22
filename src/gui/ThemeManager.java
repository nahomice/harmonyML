package gui;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private boolean isDarkTheme = false;

    public void toggleTheme() {
        isDarkTheme = !isDarkTheme;
    }

    public void applyTheme(JFrame frame) {
        Color bgColor = isDarkTheme ? Color.DARK_GRAY : Color.WHITE;
        Color fgColor = isDarkTheme ? Color.WHITE : Color.BLACK;

        SwingUtilities.invokeLater(() -> {
            frame.getContentPane().setBackground(bgColor);
            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JPanel) {
                    component.setBackground(bgColor);
                    for (Component child : ((JPanel) component).getComponents()) {
                        if (child instanceof JLabel || child instanceof JButton || child instanceof JTextField) {
                            child.setForeground(fgColor);
                            if (child instanceof JButton) {
                                child.setBackground(bgColor);
                            }
                        }
                    }
                }
            }
        });
    }
}
