package backend;

import java.io.*;
import java.util.*;

public class Playlist {
    private String name;
    private final List<Song> songs;

    // Constructor
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    // Add a song to the playlist
    public void addSong(Song song) {
        songs.add(song);
    }

    // Remove a song from the playlist
    public void removeSong(Song song) {
        songs.remove(song);
    }

    // Remove a song by title
    public boolean removeSongByTitle(String title) {
        return songs.removeIf(song -> song.getTitle().equalsIgnoreCase(title));
    }

    // Get all songs in the playlist
    public List<Song> getSongs() {
        return new ArrayList<>(songs); // Return a copy to prevent modification
    }

    // Get the name of the playlist
    public String getName() {
        return name;
    }

    // Set the name of the playlist
    public void setName(String name) {
        this.name = name;
    }

    // Get the total number of songs in the playlist
    public int getSongCount() {
        return songs.size();
    }

    // Check if the playlist contains a specific song
    public boolean containsSong(Song song) {
        return songs.contains(song);
    }

    // Get songs by a specific artist in the playlist
    public List<Song> getSongsByArtist(String artist) {
        List<Song> artistSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                artistSongs.add(song);
            }
        }
        return artistSongs;
    }

    // Get songs by genre in the playlist
    public List<Song> getSongsByGenre(String genre) {
        List<Song> genreSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                genreSongs.add(song);
            }
        }
        return genreSongs;
    }

    // Shuffle the playlist
    public void shuffle() {
        Collections.shuffle(songs);
    }

    // Get the duration of all songs in the playlist (if needed, this method can be added later)
    public int getTotalDuration() {
        int totalDuration = 0;
        // Duration logic would be added here if you had duration data
        return totalDuration;
    }

    // Display playlist details
    public String displayPlaylist() {
        StringBuilder details = new StringBuilder("Playlist: " + name + "\n");
        for (Song song : songs) {
            details.append("- Title: ").append(song.getTitle())
                    .append(", Artist: ").append(song.getArtist())
                    .append(", Genre: ").append(song.getGenre()).append("\n");
        }
        return details.toString();
    }

    // Save playlist to a file
// Save playlist to a file
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the playlist name
            writer.write("Playlist: " + name + "\n");

            // Write each song's details in the format:
            // Title; Artist; Genre; File path; Album cover path
            for (Song song : songs) {
                writer.write("Title: "+song.getTitle() + "\n"+
                        "Artist name"+ ""+ song.getArtist() + "\n"
                        + "Genre Type" +song.getGenre() + "\n"+
                        "\n" );
            }
        }
    }

    // Load playlist from a file
    public static Playlist loadFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String playlistName = reader.readLine().replace("Playlist: ", "").trim();
            Playlist playlist = new Playlist(playlistName);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String title = parts[0];
                    String artist = parts[1];
                    String genre = parts[2];
                    File songFile = new File(parts[3]);
                    File albumCover = new File(parts[4]);
                    playlist.addSong(new Song(title, artist, genre, songFile, albumCover));
                }
            }
            return playlist;
        }
    }
}
