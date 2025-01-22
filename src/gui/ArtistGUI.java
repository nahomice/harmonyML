package gui;

import backend.Artist;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class ArtistGUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextArea biographyField;
    private JTextField genreField;
    private JTextField socialMediaField;
    private JTextField collaboratorField;
    private JButton saveButton;
    private JButton toggleThemeButton;
    private boolean isNightTheme = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ArtistGUI window = new ArtistGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ArtistGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("🎤 Artist Management");
        frame.setBounds(100, 100, 600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel headerLabel = new JLabel("🎤 Manage Artist Information");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(headerLabel, gbc);

        JLabel nameLabel = new JLabel("Artist Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(nameField, gbc);

        JLabel biographyLabel = new JLabel("Biography:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(biographyLabel, gbc);

        biographyField = new JTextArea(5, 20);
        biographyField.setLineWrap(true);
        biographyField.setWrapStyleWord(true);
        JScrollPane biographyScrollPane = new JScrollPane(biographyField);
        gbc.gridx = 1;
        contentPanel.add(biographyScrollPane, gbc);

        JLabel genreLabel = new JLabel("Genres (comma separated):");
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(genreLabel, gbc);

        genreField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(genreField, gbc);

        JLabel socialMediaLabel = new JLabel("Social Media Links (comma separated):");
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(socialMediaLabel, gbc);

        socialMediaField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(socialMediaField, gbc);

        JLabel collaboratorLabel = new JLabel("Collaborators (comma separated):");
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(collaboratorLabel, gbc);

        collaboratorField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(collaboratorField, gbc);

        saveButton = new JButton("💾 Save Artist");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(50, 150, 250));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveArtist());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        contentPanel.add(saveButton, gbc);

        toggleThemeButton = new JButton("🌙 Toggle Theme");
        toggleThemeButton.setFont(new Font("Arial", Font.BOLD, 14));
        toggleThemeButton.setBackground(new Color(100, 100, 100));
        toggleThemeButton.setForeground(Color.WHITE);
        toggleThemeButton.setFocusPainted(false);
        toggleThemeButton.addActionListener(e -> toggleTheme(contentPanel));
        gbc.gridy = 7;
        contentPanel.add(toggleThemeButton, gbc);

        frame.add(contentPanel, BorderLayout.CENTER);

        applyDayTheme(contentPanel);
    }

    private void saveArtist() {
        String name = nameField.getText().trim();
        String biography = biographyField.getText().trim();
        String[] genres = genreField.getText().trim().split(",");
        String[] socialMediaLinks = socialMediaField.getText().trim().split(",");
        String[] collaborators = collaboratorField.getText().trim().split(",");

        if (!name.isEmpty() && !biography.isEmpty()) {
            Artist artist = new Artist(name, biography);

            for (String genre : genres) {
                artist.addGenre(genre.trim());
            }

            for (String link : socialMediaLinks) {
                artist.addSocialMediaLink(link.trim());
            }

            for (String collaborator : collaborators) {
                artist.addCollaborator(new Artist(collaborator.trim()));
            }

            try {
                artist.saveToFile("Artist.txt");
                JOptionPane.showMessageDialog(frame, "Artist saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving artist: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Name and biography cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        biographyField.setText("");
        genreField.setText("");
        socialMediaField.setText("");
        collaboratorField.setText("");
    }

    private void toggleTheme(JPanel contentPanel) {
        if (isNightTheme) {
            applyDayTheme(contentPanel);
        } else {
            applyNightTheme(contentPanel);
        }
        isNightTheme = !isNightTheme;
    }

    private void applyDayTheme(JPanel panel) {
        panel.setBackground(Color.WHITE);
        updateComponentColors(panel, Color.BLACK, Color.WHITE);
    }

    private void applyNightTheme(JPanel panel) {
        panel.setBackground(Color.DARK_GRAY);
        updateComponentColors(panel, Color.WHITE, Color.GRAY);
    }

    private void updateComponentColors(Container container, Color foreground, Color background) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel || component instanceof JButton) {
                component.setForeground(foreground);
            } else if (component instanceof JTextField || component instanceof JTextArea) {
                component.setForeground(foreground);
                component.setBackground(background);
            }
            if (component instanceof Container) {
                updateComponentColors((Container) component, foreground, background);
            }
        }
    }
}
