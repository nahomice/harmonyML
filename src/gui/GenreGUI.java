package gui;

import backend.Genre;
import backend.Song;
import backend.Artist;
import backend.Album;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenreGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private List<Genre> genres;
    private final String genreFilePath = "C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\Genre.txt";

    // Constructor
    public GenreGUI(Genre genre) {
        try {
            this.genres = Genre.loadFromFile(genreFilePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading genres: " + e.getMessage());
            this.genres = new ArrayList<>();
        }
        initialize();
    }

    // Initialize the GUI
    private void initialize() {
        frame = new JFrame("🎵 Genre Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Add sections
        addGenresSection();
        addAddGenreForm();
        addActionButtons();

        // Wrap main panel in a scrollable pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);

        // Set a modern look and feel
        setLookAndFeel();

        frame.setVisible(true);
    }

    // Add genres section
    private void addGenresSection() {
        mainPanel.removeAll(); // Clear existing content

        JLabel header = new JLabel("📂 Available Genres");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(header);

        for (Genre genre : genres) {
            JPanel genrePanel = new JPanel(new BorderLayout());
            genrePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(genre.getName()),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            genrePanel.setBackground(Color.WHITE);

            // Genre description and stats
            JPanel infoPanel = new JPanel(new GridLayout(0, 1));
            infoPanel.setOpaque(false);
            infoPanel.add(new JLabel("<html><b>Description:</b> " + genre.getDescription() + "</html>"));
            infoPanel.add(new JLabel("<html><b>Number of Songs:</b> " + genre.getSongCount() + "</html>"));
            infoPanel.add(new JLabel("<html><b>Average Rating:</b> " + genre.getAverageRating() + "</html>"));

            // Content panels for songs, artists, and albums
            JPanel contentPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            contentPanel.setOpaque(false);
            contentPanel.add(createListPanel("🎵 Songs", genre.getSongs().stream().map(Song::getTitle).toArray(String[]::new)));
            contentPanel.add(createListPanel("🎤 Artists", genre.getArtists().stream().map(Artist::getName).toArray(String[]::new)));
            contentPanel.add(createListPanel("💿 Albums", genre.getAlbums().stream().map(Album::getTitle).toArray(String[]::new)));

            genrePanel.add(infoPanel, BorderLayout.NORTH);
            genrePanel.add(contentPanel, BorderLayout.CENTER);
            mainPanel.add(genrePanel);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Helper to create a list panel
    private JPanel createListPanel(String title, String[] items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setOpaque(false);
        for (String item : items) {
            JLabel label = new JLabel(item);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(label);
        }
        return panel;
    }

    // Add new genre form
    private void addAddGenreForm() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("➕ Add New Genre"));
        formPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Genre:");
        JTextField nameField = new JTextField();
        styleTextField(nameField);

        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        styleTextField(descriptionField);

        JLabel song1Label = new JLabel("Song:");
        JTextField song1Field = new JTextField();
        styleTextField(song1Field);

        JLabel song2Label = new JLabel("Song:");
        JTextField song2Field = new JTextField();
        styleTextField(song2Field);

        JLabel artistLabel = new JLabel("Artist:");
        JTextField artistField = new JTextField();
        styleTextField(artistField);

        JButton saveButton = new JButton("Save Genre");
        styleButton(saveButton);

        JButton addMusicButton = new JButton("Add Music");
        styleButton(addMusicButton);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);
        formPanel.add(song1Label);
        formPanel.add(song1Field);
        formPanel.add(song2Label);
        formPanel.add(song2Field);
        formPanel.add(artistLabel);
        formPanel.add(artistField);
        formPanel.add(addMusicButton);  // Add the button to the form
        formPanel.add(saveButton);

        addMusicButton.addActionListener(e -> {
            // Open file chooser to select a music file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a Music File");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Audio Files", "mp3", "wav", "flac", "aac")); // Filter for audio files

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();  // Get the path of the selected file

                // Optionally, you can add this file path to a text field or handle it
                JOptionPane.showMessageDialog(frame, "File Selected: " + filePath);

                // You can now store the file path in the song fields (song1Field and song2Field)
                song1Field.setText(filePath); // Set the file path in the song1 field for simplicity
            }
        });

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String song1 = song1Field.getText().trim();
            String song2 = song2Field.getText().trim();
            String artist = artistField.getText().trim();

            if (!name.isEmpty() && !description.isEmpty() && !song1.isEmpty() && !song2.isEmpty() && !artist.isEmpty()) {
                Genre newGenre = new Genre(name, description);

                // Create Song objects with title, artist, genre, and file path (using file path from the field)
                Song newSong1 = new Song(song1, artist, name, song1);  // Passing the file path here
                Song newSong2 = new Song(song2, artist, name, song2);  // Passing the file path here

                // Add songs and artist to the genre
                newGenre.addSong(newSong1);
                newGenre.addSong(newSong2);
                newGenre.addArtist(new Artist(artist)); // Add artist to the genre

                genres.add(newGenre);
                saveGenresToFile();
                JOptionPane.showMessageDialog(frame, "Genre saved successfully!");

                nameField.setText("");
                descriptionField.setText("");
                song1Field.setText("");
                song2Field.setText("");
                artistField.setText("");
                addGenresSection(); // Refresh the genres section
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(formPanel);
    }


    private void saveGenresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(genreFilePath, false))) {
            for (Genre genre : genres) {
                writer.write("Genre:" + genre.getName() + ";" + genre.getDescription());
                writer.newLine();

                // Write songs, artists, and albums
                for (Song song : genre.getSongs()) {
                    writer.write("Song:" + song.getTitle() + ";" + genre.getName() + ";" + genre.getName());
                    writer.newLine();
                }
                for (Artist artist : genre.getArtists()) {
                    writer.write("Artist:" + artist.getName());
                    writer.newLine();
                }
                for (Album album : genre.getAlbums()) {
                    writer.write("Album:" + album.getTitle());
                    writer.newLine();
                }
                writer.write("---");
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving genres: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        textField.setPreferredSize(new Dimension(200, 30));
    }


    // Add action buttons (Song, Artist, Album)
    private void addActionButtons() {
        JPanel actionButtonPanel = new JPanel(new FlowLayout());
        actionButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        actionButtonPanel.setOpaque(false);
        JButton addAlbumButton = new JButton("💿 Add Album");
        styleButton(addAlbumButton);
        mainPanel.add(actionButtonPanel);
    }
    public static List<Genre> loadFromFile(String filePath) throws IOException {
        List<Genre> genres = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Genre currentGenre = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Genre:")) {
                    String[] parts = line.split(";", 2); // Split at first semicolon
                    if (parts.length == 2) {  // Ensure there are exactly two parts (name and description)
                        String name = parts[0].replace("Genre:", "").trim();
                        String description = parts[1].trim();
                        currentGenre = new Genre(name, description);
                        genres.add(currentGenre);
                    } else {
                        // Handle the case where the genre line is improperly formatted
                        System.out.println("Skipping malformed genre line: " + line);
                    }
                } else if (line.startsWith("Song:") && currentGenre != null) {
                    String[] parts = line.split(";", 2); // Split song info
                    if (parts.length == 2) {  // Ensure there are exactly two parts (song title and artist)
                        String title = parts[0].replace("Song:", "").trim();
                        String artist = parts[1].trim();
                        currentGenre.addSong(new Song(title, artist));
                    } else {
                        // Handle malformed song line
                        System.out.println("Skipping malformed song line: " + line);
                    }
                } else if (line.startsWith("Artist:") && currentGenre != null) {
                    String artistName = line.replace("Artist:", "").trim();
                    currentGenre.addArtist(new Artist(artistName));
                }
            }
        }
        return genres;
    }


    // Apply consistent styling to buttons
    private void styleButton(JButton button) {
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    // Set modern look and feel
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main method to test the GUI
    public static void main(String[] args) {
        Genre genre = new Genre();
        new GenreGUI(genre);
    }
}