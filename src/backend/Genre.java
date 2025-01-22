package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Genre {
    private String name;
    private String description;
    private List<Song> songs;
    private List<Artist> artists;
    private List<Album> albums;
    private int songCount;
    private double averageRating;

    // Constructor for initializing Genre with name and description
    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
        this.songs = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.songCount = 0;
        this.averageRating = 0.0;
    }

    // Default constructor for creating empty genre objects
    public Genre() {
        this.songs = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    // Add a song to the genre
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            songCount++;
            updateAverageRating(song.getRating()); // Update average rating when a new song is added
        }
    }

    // Add an artist to the genre
    public void addArtist(Artist artist) {
        if (!artists.contains(artist)) {
            artists.add(artist);
        }
    }

    // Add an album to the genre
    public void addAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
        }
    }

    // Update the average rating of the genre whenever a new song is added
    private void updateAverageRating(double rating) {
        double totalRating = averageRating * (songCount - 1);
        totalRating += rating;
        averageRating = totalRating / songCount;
    }

    // Save genre details (including songs, artists, albums) to a file
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Genre:" + name + ";" + description);
            writer.newLine();

            for (Song song : songs) {
                writer.write("Song:" + song.getTitle() + ";" + song.getArtist() + ";" + song.getGenre());
                writer.newLine();
            }

            for (Artist artist : artists) {
                writer.write("Artist:" + artist.getName());
                writer.newLine();
            }

            for (Album album : albums) {
                writer.write("Album:" + album.getTitle());
                writer.newLine();
            }
            writer.write("---");
            writer.newLine();
        }
    }

    // Load genres from a file and reconstruct the genre objects with their songs, artists, and albums
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

    // Get genre details as a formatted string
    public String getGenreDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Genre: ").append(name).append("\n");
        details.append("Description: ").append(description).append("\n");
        details.append("Artists: ");
        for (Artist artist : artists) {
            details.append(artist.getName()).append(" ");
        }
        details.append("\nAlbums: ");
        for (Album album : albums) {
            details.append(album.getTitle()).append(" ");
        }
        details.append("\nSongs: ");
        for (Song song : songs) {
            details.append(song.getTitle()).append(" ");
        }
        details.append("\nAverage Rating: ").append(averageRating).append("\n");
        return details.toString();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSongCount() {
        return songCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public List<Song> getSongs() {
        return List.copyOf(songs);
    }

    public List<Artist> getArtists() {
        return List.copyOf(artists);
    }

    public List<Album> getAlbums() {
        return List.copyOf(albums);
    }
    public void addAlbums(List<Album> albumsToAdd) {
        for (Album album : albumsToAdd) {
            addAlbum(album);
        }
    }
}
