package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Song {
    private String title;
    private String artist;
    private String genre;
    private File file;
    private File albumCover;
    public Song(String title, String artist, String genre, File file, File albumCover) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.file = file;
        this.albumCover = albumCover;


    }

    public Song() {

    }

    public Song(String song2, Artist artist, String genreName) {
    }

    public Song(String song1) {
    }

    public Song(String title, String artist, String genreName, double v) {
    }

    public Song(String title, String artist) {
    }

    public Song(String song2, String artist, String name, String song21) {
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

        public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public File getFile() {
        return file;
    }

    public File getAlbumCover() {
        return albumCover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setAlbumCover(File albumCover) {
        this.albumCover = albumCover;
    }

    public void saveToFile() {
        try {
            File songFile = new File("Song.txt");
            if (!songFile.exists()) {
                songFile.createNewFile();
            }
            // Append data to the file
            FileWriter writer = new FileWriter(songFile, true);
            writer.write("Title: " + title + "\n");
            writer.write("Artist: " + artist + "\n");
            writer.write("Genre: " + genre + "\n");
            writer.write("File Path: " + file.getPath() + "\n");
            writer.write("Album Cover Path: " + albumCover.getPath() + "\n");
            writer.write("\n====================\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Song> loadFromFile(String s) {
        List<Song> songs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Nahom W\\Desktop\\Harmony OOP  Final version\\Song.txt"))) {
            String line;
            String title = null, artist = null, genre = null;
            File file = null, albumCover = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Title:")) {
                    title = line.substring(6).trim();
                } else if (line.startsWith("Artist:")) {
                    artist = line.substring(7).trim();
                } else if (line.startsWith("Genre:")) {
                    genre = line.substring(6).trim();
                } else if (line.startsWith("File Path:")) {
                    file = new File(line.substring(10).trim());
                } else if (line.startsWith("Album Cover Path:")) {
                    albumCover = new File(line.substring(17).trim());
                } else if (line.isEmpty()) {
                    // Create a Song object when a blank line is encountered
                    if (title != null && artist != null && genre != null && file != null && albumCover != null) {
                        songs.add(new Song(title, artist, genre, file, albumCover));
                    }
                    // Reset variables for the next song
                    title = artist = genre = null;
                    file = albumCover = null;
                }
            }

            // Add the last song if the file doesn't end with a blank line
            if (title != null && artist != null && genre != null && file != null && albumCover != null) {
                songs.add(new Song(title, artist, genre, file, albumCover));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public int getDuration() {
        return 0;
    }


    public double getRating() {
        return 0;
    }
}
