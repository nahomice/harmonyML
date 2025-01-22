package gui;

import backend.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;

public class SongForm extends JFrame {

    private JTextField titleField;
    private JTextField artistField;
    private JTextField genreField;
    private JTextField fileField;
    private JTextField albumCoverField;
    private JButton browseSongButton;
    private JButton browseAlbumCoverButton;
    private JButton submitButton;
    private JButton clearButton;

    public SongForm() {
        setTitle("Add New Song");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Add Song Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Title Field
        addLabelAndField("Title:", titleField = new JTextField(20), mainPanel, gbc, 1);
        addLabelAndField("Artist:", artistField = new JTextField(20), mainPanel, gbc, 2);
        addLabelAndField("Genre:", genreField = new JTextField(20), mainPanel, gbc, 3);
        addLabelAndField("Song File Path:", fileField = new JTextField(20), mainPanel, gbc, 4);
        addLabelAndField("Album Cover Path:", albumCoverField = new JTextField(20), mainPanel, gbc, 5);

        // Browse Song Button
        browseSongButton = new roundedButton("Browse Song");
        browseSongButton.setFont(new Font("Arial", Font.BOLD, 14));
        browseSongButton.setBackground(new Color(100, 149, 237));
        browseSongButton.setForeground(Color.WHITE);
        browseSongButton.setToolTipText("Select a song file");
        gbc.gridx = 2;
        gbc.gridy = 4;
        mainPanel.add(browseSongButton, gbc);

        // Browse Album Cover Button
        browseAlbumCoverButton = new roundedButton("Browse Cover");
        browseAlbumCoverButton.setFont(new Font("Arial", Font.BOLD, 14));
        browseAlbumCoverButton.setBackground(new Color(100, 149, 237));
        browseAlbumCoverButton.setForeground(Color.WHITE);
        browseAlbumCoverButton.setToolTipText("Select an album cover image");
        gbc.gridx = 2;
        gbc.gridy = 5;
        mainPanel.add(browseAlbumCoverButton, gbc);

        // Submit Button
        submitButton = new roundedButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setToolTipText("Save song details");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(submitButton, gbc);

        // Clear Button
        clearButton = new roundedButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setToolTipText("Clear all fields");
        gbc.gridy = 7;
        mainPanel.add(clearButton, gbc);

        // Add Listeners
        browseSongButton.addActionListener(e -> browseFile(fileField));
        browseAlbumCoverButton.addActionListener(e -> browseFile(albumCoverField));
        submitButton.addActionListener(e -> handleSubmit());
        clearButton.addActionListener(e -> clearFields());

        add(mainPanel);
    }

    private void addLabelAndField(String labelText, JTextField field, JPanel panel, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void browseFile(JTextField targetField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            targetField.setText(selectedFile.getPath());
        }
    }

    private void handleSubmit() {
        String title = titleField.getText().trim();
        String artist = artistField.getText().trim();
        String genre = genreField.getText().trim();
        String filePath = fileField.getText().trim();
        String albumCoverPath = albumCoverField.getText().trim();

        if (title.isEmpty() || artist.isEmpty() || genre.isEmpty() || filePath.isEmpty() || albumCoverPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File songFile = new File(filePath);
            File albumCover = new File(albumCoverPath);

            Song song = new Song(title, artist, genre, songFile, albumCover);
            song.saveToFile();

            JOptionPane.showMessageDialog(this, "Song information saved successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving song information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        titleField.setText("");
        artistField.setText("");
        genreField.setText("");
        fileField.setText("");
        albumCoverField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SongForm form = new SongForm();
            form.setVisible(true);
        });
    }
}

// RoundedButton Class
class roundedButton extends JButton {
    public roundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        g2.dispose();
    }
}
