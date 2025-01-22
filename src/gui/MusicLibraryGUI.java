package gui;

import backend.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class MusicLibraryGUI {
    private List<Artist> loadArtistsFromFile() {
        List<Artist> artists = new ArrayList<>();
        String filePath = "C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\Artist.txt";
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "Artist.txt file not found at the specified path: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            return artists;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String name = null, biography = null, genre = null, socialMedia = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove extra spaces

                // Parse fields
                if (line.startsWith("Artist:")) {
                    String[] parts = line.substring(7).split(";");
                    name = parts[0].trim();
                    biography = (parts.length > 1) ? parts[1].trim() : "";
                } else if (line.startsWith("Genre:")) {
                    genre = line.substring(6).trim();
                } else if (line.startsWith("SocialMedia:")) {
                    socialMedia = line.substring(12).trim();
                } else if (line.startsWith("---")) {
                    // At the end of a section, create an Artist object if valid
                    if (name != null && biography != null) {
                        Artist artist = new Artist(name, biography);
                        if (genre != null) artist.addGenre(genre);
                        if (socialMedia != null) artist.addSocialMediaLink(socialMedia);
                        artists.add(artist);
                    }
                    // Reset fields for the next entry
                    name = biography = genre = socialMedia = null;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load artists: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return artists;
    }

    Artist artist = null;
    //    private AudioPlayer currentAudioPlayer = new AudioPlayer(); // Tracks the current audio player
    private final LoginManager loginManager;
    private final MusicLibraryManager musicLibraryManager;
    private JFrame frame;

    public MusicLibraryGUI() {

        // Initialize LoginManager and register a test user
        loginManager = new LoginManager();

        // Initialize MusicLibraryManager
        Song song = new Song();
        song.loadFromFile("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\Song.txt");
        musicLibraryManager = new MusicLibraryManager();

        // Initialize the GUI
        initialize();
    }

    void initialize() {
        // Create the main frame
        JFrame frame = new JFrame("Melody Nexus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add theme toggle to the frame
        addThemeToggle(frame);


        // Main panel with CardLayout
        JPanel mainPanel = new JPanel(new CardLayout());
        frame.add(mainPanel);

        // Login Panel
        JPanel loginPanel = createLoginPanel(mainPanel, frame);
        mainPanel.add(loginPanel, "LoginPanel");

        // Library Panel
        JPanel libraryPanel = createLibraryPanel();
        mainPanel.add(libraryPanel, "LibraryPanel");

        frame.setVisible(true);
    }

    private void addThemeToggle(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");

        JToggleButton toggleThemeButton = new JToggleButton("🌞 Day Mode");
        toggleThemeButton.setFocusPainted(false);
        toggleThemeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        toggleThemeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toggleThemeButton.setBackground(Color.DARK_GRAY);
        toggleThemeButton.setForeground(Color.WHITE);
        toggleThemeButton.setOpaque(true);

        // Action listener for the toggle button
        toggleThemeButton.addActionListener(e -> {
            if (toggleThemeButton.isSelected()) {
                toggleThemeButton.setText("🌙 Night Mode");
                switchToNightTheme(frame);
            } else {
                toggleThemeButton.setText("🌞 Day Mode");
                switchToDayTheme(frame);
            }
        });

        viewMenu.add(toggleThemeButton); // Add toggle button to the menu
        menuBar.add(viewMenu);
        frame.setJMenuBar(menuBar);
    }


    private JPanel createLoginPanel(JPanel mainPanel, JFrame frame) {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Melody Nexus: Music Collection Library");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(189, 154, 130));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (loginManager.authenticate(username, password)) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "LibraryPanel");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(100, 180, 100));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (loginManager.isUserRegistered(username)) {
                JOptionPane.showMessageDialog(frame, "User already exists. Please try a different username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = new User(username, password);
            loginManager.registerUser(newUser);
            saveUserToFile(newUser);

            JOptionPane.showMessageDialog(frame, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        return loginPanel;
    }

    private void saveUserToFile(User user) {
        File file = new File("users.txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(user.getUsername() + "," + user.getPassword() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving user data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLibraryPanel() {
        // Create the main library panel with BorderLayout
        JPanel libraryPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Load the background image
                    ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\src\\resources\\DALL·E 2024-12-28 22.51.12 - A serene and artistic background design for a music library, featuring soft, flowing waves of various shades of blue, green, and purple, with abstract.jpeg");
                    Image backgroundImage = backgroundIcon.getImage();

                    if (backgroundImage != null) {
                        // Scale the image to fit the panel's size
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        System.out.println("Failed to load image.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Title for the music library
        JLabel libraryTitle = new JLabel("Music Library", SwingConstants.CENTER);
        libraryTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        libraryPanel.add(libraryTitle, BorderLayout.NORTH);

        // Search area
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Verdana", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 30));

        JButton searchByGenreButton = new JButton("Search by Genre");
        searchByGenreButton.setBackground(new Color(34, 139, 34));
        searchByGenreButton.setForeground(Color.WHITE);
        searchByGenreButton.setFocusPainted(false);

        JButton searchByArtistButton = new JButton("Search by Artist");
        searchByArtistButton.setBackground(new Color(70, 130, 180));
        searchByArtistButton.setForeground(Color.WHITE);
        searchByArtistButton.setFocusPainted(false);
        //manageartist
        // Add "Manage Artists" button
        JButton manageArtistsButton = new JButton("Manage Artists");
        manageArtistsButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        manageArtistsButton.setForeground(Color.WHITE);
        manageArtistsButton.addActionListener(e -> new ArtistGUI()); // Open the ArtistGUI

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(manageArtistsButton);
//
        // Add "Manage Artists" button


        //manage genre
        // Add Manage Genres button
        JButton manageGenresButton = new JButton("Manage Genres");
        manageGenresButton.setBackground(new Color(128, 0, 128));
        manageGenresButton.setForeground(Color.WHITE);

        // Add action to open GenreGUI
        manageGenresButton.addActionListener(e -> {
            Genre genre = new Genre("Default Genre", "Description of the default genre"); // Placeholder genre
            new GenreGUI(genre);
        });
        //panel for genremanager
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(searchByGenreButton);
        buttonPanel.add(searchByArtistButton);
        buttonPanel.add(manageGenresButton);
        JButton recentPlaysButton = new JButton();
//        buttonPanel.add(recentPlaysButton);
        JButton createPlaylistButton = new JButton();
//        buttonPanel.add(createPlaylistButton);
        JButton viewPlaylistsButton = new JButton();
//        buttonPanel.add(viewPlaylistsButton);


        JPanel buttonPanel1 = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(searchByGenreButton);
        buttonPanel.add(searchByArtistButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        // Panel to hold search results
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel to show selected song details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Add search functionality
        searchByGenreButton.addActionListener(e -> {
            String genreName = searchField.getText().trim();

            if (genreName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a genre to search.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Load genres from the Genre.txt file
                List<Genre> genres = Genre.loadFromFile("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\Genre.txt");

                // Find the matching genre
                Genre matchingGenre = null;
                for (Genre genre : genres) {
                    if (genre.getName().equalsIgnoreCase(genreName)) {
                        matchingGenre = genre;
                        break;
                    }
                }

                if (matchingGenre != null) {
                    // Get songs from the genre
                    List<Song> genreSongs = matchingGenre.getSongs();

                    // Display the songs in the result panel
                    displaySearchResults(genreSongs, "Genre", genreName, resultPanel, detailsPanel);
                } else {
                    // No genre found
                    JOptionPane.showMessageDialog(frame, "No genre found with the name: " + genreName, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load genres.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        searchByArtistButton.addActionListener(e -> {
            String artistName = searchField.getText().trim();
            List<Song> songs = Song.loadFromFile("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\Song.txt").stream().filter(song -> song.getArtist().equalsIgnoreCase(artistName)).collect(Collectors.toList());

            resultPanel.removeAll();

            if (songs.isEmpty()) {
                resultPanel.add(new JLabel("No songs found for artist: " + artistName));
            } else {
                for (Song song : songs) {
                    JPanel songPanel = new JPanel(new BorderLayout());
                    songPanel.setBorder(BorderFactory.createTitledBorder(song.getTitle()));

                    // Album cover image
                    JLabel albumCoverLabel = new JLabel();
                    albumCoverLabel.setIcon(new ImageIcon(song.getAlbumCover().getPath()));
                    albumCoverLabel.setHorizontalAlignment(JLabel.CENTER);
                    albumCoverLabel.setPreferredSize(new Dimension(150, 150)); // Adjust size as needed

                    // Song info and buttons
                    JPanel songInfoPanel = new JPanel();
                    songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
                    songInfoPanel.add(new JLabel("Title: " + song.getTitle()));
                    songInfoPanel.add(new JLabel("Artist: " + song.getArtist()));
                    songInfoPanel.add(new JLabel("Genre: " + song.getGenre()));

                    JButton detailsButton = new JButton("Details");
                    detailsButton.addActionListener(ev -> showSongDetails(song, detailsPanel));
                    songInfoPanel.add(detailsButton);

                    songPanel.add(albumCoverLabel, BorderLayout.WEST);
                    songPanel.add(songInfoPanel, BorderLayout.CENTER);
                    resultPanel.add(songPanel);
                }
            }

            resultPanel.revalidate();
            resultPanel.repaint();
        });


        recentPlaysButton = new JButton("Recent Plays");
        recentPlaysButton.setBackground(new Color(70, 130, 180));
        recentPlaysButton.setForeground(Color.WHITE);

        // Recent Plays button functionality
        recentPlaysButton.addActionListener(e -> {
            List<Song> recentSongs = musicLibraryManager.getRecentPlays();
            displaySearchResults(recentSongs, "recent plays", "", resultPanel, detailsPanel);
        });

        buttonPanel.add(recentPlaysButton);

        // Create scrollable area for the results
        JScrollPane resultScrollPane = new JScrollPane(resultPanel);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        libraryPanel.add(searchPanel, BorderLayout.NORTH);
        libraryPanel.add(resultScrollPane, BorderLayout.CENTER);
        libraryPanel.add(detailsPanel, BorderLayout.SOUTH);

        // Playlist buttons
        createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.setBackground(new Color(255, 165, 0));
        createPlaylistButton.setForeground(Color.WHITE);

        viewPlaylistsButton = new JButton("View Playlists");
        viewPlaylistsButton.setBackground(new Color(34, 139, 34));
        viewPlaylistsButton.setForeground(Color.WHITE);

        // Create Playlist functionality
        createPlaylistButton.addActionListener(e -> {
            String playlistName = JOptionPane.showInputDialog(frame, "Enter Playlist Name:");
            if (playlistName != null && !playlistName.trim().isEmpty()) {
                musicLibraryManager.createPlaylist(playlistName);

                // Save the playlist name to playlist.txt
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("playlist.txt", true))) {
                    writer.write(playlistName);
                    writer.newLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to save playlist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // View Playlists functionality
        viewPlaylistsButton.addActionListener(e -> {
            // Load playlists from playlist.txt
            Set<String> playlistNames = new HashSet<>();
            String playlistFilePath = "C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\playlist.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(playlistFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    playlistNames.add(line.trim());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load playlists.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit early if loading playlists fails
            }

            if (playlistNames.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No playlists available.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit if no playlists exist
            }

            // Prompt the user to select a playlist
            String selectedPlaylist = (String) JOptionPane.showInputDialog(frame, "Select a Playlist:", "Playlists", JOptionPane.PLAIN_MESSAGE, null, playlistNames.toArray(), null);

            if (selectedPlaylist != null && !selectedPlaylist.isEmpty()) {
                // Retrieve the songs from the selected playlist
                List<Song> playlistSongs = musicLibraryManager.getPlaylist(selectedPlaylist);

                if (playlistSongs.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "The selected playlist is empty.", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Display the playlist songs
                    displaySearchResults(playlistSongs, "playlist", selectedPlaylist, resultPanel, detailsPanel);
                }
            }
        });


        buttonPanel.add(createPlaylistButton);
        buttonPanel.add(viewPlaylistsButton);

        libraryPanel.repaint();

        return libraryPanel;
    }

    // Helper method to load playlist from file
    private Playlist loadPlaylist (String playlistName) throws IOException{
        try {
            // Assuming playlists are saved in individual files, named after the playlist
            return Playlist.loadFromFile("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\playlist.txt");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load playlist.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    //now we will do
// Helper method to display the search results with "Genre's" title
// Helper method to display the search results with "Genre's" title
// Helper method to display the search results with "Genre's" title aligned left and songs listed side by side
    private void switchToDayTheme(JFrame frame) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            updateUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToNightTheme(JFrame frame) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("control", new Color(45, 45, 45));
            UIManager.put("text", Color.WHITE);
            UIManager.put("nimbusLightBackground", new Color(60, 63, 65));
            UIManager.put("info", new Color(30, 30, 30));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusBlueGrey", new Color(45, 45, 45));
            UIManager.put("nimbusSelectionBackground", new Color(75, 110, 175));
            updateUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(JFrame frame) {
        SwingUtilities.updateComponentTreeUI(frame);
        frame.repaint();
    }

    private void displaySearchResults(List<Song> songs, String searchType, String searchValue, JPanel resultPanel, JPanel detailsPanel) {
        // Clear the previous results
        resultPanel.removeAll();
        detailsPanel.removeAll();

        if (songs.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No songs found for " + searchType + ": " + searchValue);
            resultPanel.add(noResultsLabel);
        } else {
            // Add "Genre's" title aligned to the left
            JLabel genreTitleLabel = new JLabel("Genre's");
            genreTitleLabel.setFont(new Font("Verdana", Font.BOLD, 22));
            genreTitleLabel.setForeground(new Color(255, 99, 71)); // Tomato color for the title
            resultPanel.add(genreTitleLabel);

            // Determine the grid dimensions based on the number of songs
            int columns = 4; // Number of columns for the grid
            int rows = (int) Math.ceil((double) songs.size() / columns);

            // Set GridLayout for resultPanel
            resultPanel.setLayout(new GridLayout(rows, columns, 15, 15)); // Gaps of 15px between rows and columns

            // Add each song with album cover
            for (Song song : songs) {
                // Load album cover image
                File albumCoverFile = song.getAlbumCover();
                ImageIcon albumCoverIcon = new ImageIcon();

                if (albumCoverFile != null && albumCoverFile.exists()) {
                    Image image = new ImageIcon(albumCoverFile.getAbsolutePath()).getImage();
                    albumCoverIcon = new ImageIcon(image.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                } else {
                    // Placeholder for missing images
                    albumCoverIcon = new ImageIcon(new ImageIcon("path_to_placeholder_image.jpg").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                }

                // Panel for each song item
                JPanel songPanel = new JPanel();
                songPanel.setLayout(new BorderLayout());
                songPanel.setBackground(Color.BLACK); // Make background black
                songPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Border to indicate clickability

                // Album cover image
                JLabel imageLabel = new JLabel(albumCoverIcon);
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                songPanel.add(imageLabel, BorderLayout.CENTER);

                // Title label
                JLabel titleLabel = new JLabel(song.getTitle());
                titleLabel.setHorizontalAlignment(JLabel.CENTER);
                titleLabel.setForeground(Color.BLACK); // Set title text to black
                titleLabel.setBackground(Color.WHITE); // Optional: Add a white background for contrast
                titleLabel.setOpaque(true); // Ensure background is visible
                songPanel.add(titleLabel, BorderLayout.SOUTH);

                // Add MouseListener to make the song panel clickable
                songPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        // Display song details in the details panel when clicked
                        showSongDetails(song, detailsPanel);

                        // Add the "Add to Playlist" button after a song is selected
                        JButton addToPlaylistButton = new JButton("Add to Playlist");
                        addToPlaylistButton.setBackground(new Color(255, 69, 0));
                        addToPlaylistButton.setForeground(Color.WHITE);

                        // Add action listener for the "Add to Playlist" button
// Add to Playlist Button
                        addToPlaylistButton.addActionListener(e -> {
                            // Create a list of available playlists (adjusted to handle Object)
                            Collection<Object> playlistNames = musicLibraryManager.getPlaylistNames();

                            if (playlistNames.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "No playlists available. Create a new playlist first.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Show a dialog to choose an existing playlist
                            String selectedPlaylist = (String) JOptionPane.showInputDialog(frame, "Select a Playlist:", "Add to Playlist", JOptionPane.PLAIN_MESSAGE, null, playlistNames.toArray(), null);

                            // Check if selectedPlaylist is valid and not empty
                            if (selectedPlaylist != null && !selectedPlaylist.trim().isEmpty()) {
                                // Add the song to the selected playlist
                                musicLibraryManager.addSongToPlaylist(selectedPlaylist, song);
                                JOptionPane.showMessageDialog(frame, "Song added to " + selectedPlaylist, "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(frame, "No playlist selected or invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });


// View Playlist Button

                        JButton viewPlaylistButton = new JButton() {
                        };
                        viewPlaylistButton.addActionListener(e -> {
                            // Path to the directory containing playlists
                            String playlistDirectoryPath = "C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\playlists";

                            File playlistDirectory = new File(playlistDirectoryPath);

                            // Check if the directory exists and is accessible
                            if (!playlistDirectory.exists() || !playlistDirectory.isDirectory()) {
                                JOptionPane.showMessageDialog(detailsPanel, "The playlist directory does not exist or is not accessible.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Retrieve the list of playlist names (directories or files within the directory)
                            String[] playlistNames = playlistDirectory.list((dir, name) -> {
                                File file = new File(dir, name);
                                // Optional: Filter to only include directories or files with specific extensions
                                return file.isDirectory(); // Use `file.isFile()` for files only
                            });

                            if (playlistNames == null || playlistNames.length == 0) {
                                JOptionPane.showMessageDialog(detailsPanel, "No playlists available in the directory.", "Information", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }

                            // Prompt the user to select a playlist
                            String selectedPlaylist = (String) JOptionPane.showInputDialog(detailsPanel, "Select a Playlist:", "View Playlist", JOptionPane.PLAIN_MESSAGE, null, playlistNames, null);

                            if (selectedPlaylist != null) {
                                // Path to the selected playlist
                                String selectedPlaylistPath = playlistDirectoryPath + File.separator + selectedPlaylist;

                                File playlistFile = new File(selectedPlaylistPath);

                                if (playlistFile.isDirectory()) {
                                    // Handle playlist stored as a directory (e.g., list files within the directory)
                                    File[] songFiles = playlistFile.listFiles();
                                    List<Song> playlistSongs = new ArrayList<>();
                                    if (songFiles != null) {
                                        for (File songFile : songFiles) {
                                            // Assuming you have a method to parse songs from files
                                            Song song = parseSongFromFile(songFile);
                                            if (song != null) {
                                                playlistSongs.add(song);
                                            }
                                        }
                                    }
                                    // Display the songs in the playlist
                                    displaySearchResults(playlistSongs, "playlist", selectedPlaylist, new JPanel(), detailsPanel);
                                } else {
                                    JOptionPane.showMessageDialog(detailsPanel, "The selected playlist is not a valid directory.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });


                        // Add the "Add to Playlist" button to the details panel
                        detailsPanel.add(addToPlaylistButton);
                        detailsPanel.revalidate();
                        detailsPanel.repaint();
                    }
                });

                // Add the song panel to the result panel
                resultPanel.add(songPanel);
            }
        }

        // Refresh the panel to show the new results
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private Song parseSongFromFile(File songFile) {
        return null;
    }

    // Method to display song details and buttons
    private AudioPlayer currentAudioPlayer = null;

    private void showSongDetails(Song song, JPanel detailsPanel) {
        detailsPanel.removeAll();

// Stop the currently playing song before playing a new one
        if (currentAudioPlayer != null) {
            currentAudioPlayer.stopSong();
        }

        currentAudioPlayer = new AudioPlayer();

// Display album cover
        JLabel albumCoverLabel = new JLabel();
        if (song.getAlbumCover() != null && song.getAlbumCover().exists()) {
            albumCoverLabel.setIcon(new ImageIcon(song.getAlbumCover().getPath()));
        } else {
            albumCoverLabel.setIcon(new ImageIcon("path_to_placeholder_image.jpg")); // Add a placeholder image path
        }
        albumCoverLabel.setHorizontalAlignment(JLabel.CENTER);
        albumCoverLabel.setPreferredSize(new Dimension(200, 200)); // Adjust size as needed

// Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Title: " + song.getTitle());
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        JLabel artistLabel = new JLabel("Artist: " + song.getArtist());
        artistLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
        JLabel genreLabel = new JLabel("Genre: " + song.getGenre());
        genreLabel.setFont(new Font("Georgia", Font.PLAIN, 14));

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createHorizontalStrut(10)); // Spacer
        infoPanel.add(artistLabel);
        infoPanel.add(Box.createHorizontalStrut(10)); // Spacer
        infoPanel.add(genreLabel);

// Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton playButton = new JButton("Play");
        playButton.setBackground(new Color(34, 139, 34));
        playButton.setForeground(Color.WHITE);

        JButton stopButton = new JButton("Stop");
        stopButton.setBackground(new Color(130, 92, 98));
        stopButton.setForeground(Color.WHITE);
        stopButton.setEnabled(false);

        JButton favoriteButton = new JButton("Favorite");
        favoriteButton.setBackground(new Color(255, 223, 0));
        favoriteButton.setForeground(Color.BLACK);

        JButton addToPlaylistButton = new JButton("Add to Playlist");
        addToPlaylistButton.setBackground(new Color(100, 149, 237));
        addToPlaylistButton.setForeground(Color.WHITE);

// Add buttons to the panel
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Spacer
        buttonPanel.add(stopButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Spacer
        buttonPanel.add(favoriteButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Spacer
        buttonPanel.add(addToPlaylistButton);

// Play button functionality
        playButton.addActionListener(e -> {
            currentAudioPlayer.playSong(song.getFile());
            musicLibraryManager.addToRecentPlays(song);
            stopButton.setEnabled(true);
            playButton.setEnabled(false);
        });

// Stop button functionality
        stopButton.addActionListener(e -> {
            currentAudioPlayer.stopSong();
            stopButton.setEnabled(false);
            playButton.setEnabled(true);
        });

// Favorite button functionality
        favoriteButton.addActionListener(e -> {
            if (musicLibraryManager.getFavoriteSongs().contains(song)) {
                musicLibraryManager.removeFromFavorites(song);
                favoriteButton.setText("Favorite");
            } else {
                musicLibraryManager.addToFavorites(song);
                favoriteButton.setText("Unfavorite");
            }
        });

// Add to Playlist functionality
//        JButton addToPlaylistButton = new JButton("Add to Playlist");
//        JButton addToPlaylistButton = new JButton("Add to Playlist");
        addToPlaylistButton.addActionListener(e -> {
            // Get the path to the playlist directory
            File playlistDirectory = new File("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\src\\playlists\\");

            // Get all files in the directory that end with ".txt" (playlist files)
            File[] playlistFiles = playlistDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

            // If no playlist files are found
            if (playlistFiles == null || playlistFiles.length == 0) {
                JOptionPane.showMessageDialog(frame, "No playlists available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create an array to hold the playlist names
            List<String> playlistNames = new ArrayList<>();
            for (File playlistFile : playlistFiles) {
                playlistNames.add(playlistFile.getName().replace(".txt", "")); // Remove the ".txt" extension
            }

            // Show available playlists in a dialog box
            String selectedPlaylist = (String) JOptionPane.showInputDialog(frame, "Select Playlist to Add Song:", "Add Song to Playlist", JOptionPane.PLAIN_MESSAGE, null, playlistNames.toArray(), null);

            // If the user selects a playlist
            if (selectedPlaylist != null && !selectedPlaylist.isEmpty()) {
                // Assuming you have a method to add the song to the playlist
                musicLibraryManager.addSongToPlaylist(selectedPlaylist, song); // Ensure this method exists
                JOptionPane.showMessageDialog(frame, "Song added to " + selectedPlaylist);
            }
        });


// Progress Bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMaximum(song.getDuration()); // Assuming Song class has getDuration
        progressBar.setStringPainted(true);

        Timer timer = new Timer(1000, e -> {
            if (currentAudioPlayer != null && currentAudioPlayer.isPlaying()) {
                progressBar.setValue((int) currentAudioPlayer.getPlaybackPosition());
                progressBar.setString(formatTime(currentAudioPlayer.getPlaybackPosition()) + " / " + formatTime(song.getDuration()));
            }
        });

        playButton.addActionListener(e -> timer.start());
        stopButton.addActionListener(e -> {
            timer.stop();
            progressBar.setValue(0);
        });

// Add components to details panel
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.add(albumCoverLabel);
        detailsPanel.add(infoPanel);
        detailsPanel.add(buttonPanel);
        detailsPanel.add(progressBar);

        detailsPanel.revalidate();
        detailsPanel.repaint();

    }


    // Helper method to show playlist dialog
    private void showPlaylistDialog() {
        // Open a dialog to select the playlist
        String[] playlistNames = musicLibraryManager.getPlaylistNames().toArray(new String[0]);
        String selectedPlaylist = (String) JOptionPane.showInputDialog(null, "Select Playlist", "Choose Playlist", JOptionPane.PLAIN_MESSAGE, null, playlistNames, playlistNames.length > 0 ? playlistNames[0] : null);

        if (selectedPlaylist == null || selectedPlaylist.isEmpty()) {
            return; // Exit if no playlist is selected
        }

        // Retrieve the selected playlist's songs
        List<Song> playlist = musicLibraryManager.getPlaylistSongs(selectedPlaylist);

        // Create the playlist dialog
        JDialog playlistDialog = new JDialog((Frame) null, "Playlist: " + selectedPlaylist, true);
        playlistDialog.setSize(400, 300);
        playlistDialog.setLocationRelativeTo(null);

        // Create the list model and populate it with the playlist's songs
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Song song : playlist) {
            model.addElement(song.getTitle() + " - " + song.getArtist());
        }

        // Display the list in a scroll pane
        JList<String> playlistList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(playlistList);
        playlistDialog.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> playlistDialog.dispose());
        playlistDialog.add(closeButton, BorderLayout.SOUTH);

        playlistDialog.setVisible(true);
    }

    private void showPlaylist() {
    }


    private void getClass(Object currentSong) {
    }

    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicLibraryGUI::new);
    }
}