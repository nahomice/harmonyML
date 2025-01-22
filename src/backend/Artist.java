package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Artist {
    private String name;
    private String biography;
    private final List<Song> songs;
    private final List<Album> albums;
    private final List<String> genres;
    private final List<String> socialMediaLinks;
    private final List<Artist> collaborators;
    MusicLibraryManager musicLibraryManager=new MusicLibraryManager();
    // Constructor
    public Artist(String name, String biography) {
        this.name = name;
        this.biography = biography;
        this.songs = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.socialMediaLinks = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }

    public Artist(String artistName) {
        this(artistName, "");
    }

    // Add song to artist
    public void addSong(Song song) {
        songs.add(song);
    }

    // Add album to artist
    public void addAlbum(Album album) {
        albums.add(album);
    }

    // Add genre to artist
    public void addGenre(String genre) {
        if (!genres.contains(genre)) {
            genres.add(genre);
        }
    }

    // Add social media link
    public void addSocialMediaLink(String link) {
        socialMediaLinks.add(link);
    }

    // Add collaborator
    public void addCollaborator(Artist collaborator) {
        collaborators.add(collaborator);
    }

    // Save artist to file
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Artist:" + name + ";" + biography);
            writer.newLine();

            for (Song song : songs) {
                writer.write("Song:" + song.getTitle() + ";" + song.getArtist() + ";" + song.getGenre());
                writer.newLine();
            }

            for (Album album : albums) {
                writer.write("Album:" + album.getTitle());
                writer.newLine();
            }

            for (String genre : genres) {
                writer.write("Genre:" + genre);
                writer.newLine();
            }



            for (Artist collaborator : collaborators) {
                writer.write("Collaborator:" + collaborator.getName());
                writer.newLine();
            }

            for (String link : socialMediaLinks) {
                writer.write("SocialMedia:" + link);
                writer.newLine();
            }

            writer.write("---");
            writer.newLine();
        }
    }

    // Load artists from file
    public static List<Artist> loadFromFile(String filePath) throws IOException {
        List<Artist> artists = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Artist currentArtist = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Artist:")) {
                    String[] parts = line.split(";", 2);
                    currentArtist = new Artist(parts[0].replace("Artist:", "").trim(), parts[1].trim());
                    artists.add(currentArtist);
                } else if (line.startsWith("Song:") && currentArtist != null) {
                    String[] parts = line.split(";", 3);
                    String title = parts[0].replace("Song:", "").trim();
                    String artist = parts[1].trim();
                    String genre = parts[2].trim();
                    currentArtist.addSong(new Song(title, artist, genre, null, null));
                } else if (line.startsWith("Album:") && currentArtist != null) {
                    String albumTitle = line.replace("Album:", "").trim();
                    currentArtist.addAlbum(new Album(albumTitle));
                } else if (line.startsWith("Genre:") && currentArtist != null) {
                    String genre = line.replace("Genre:", "").trim();
                    currentArtist.addGenre(genre);
                } else if (line.startsWith("Award:") && currentArtist != null) {


                } else if (line.startsWith("Collaborator:") && currentArtist != null) {
                    String collaboratorName = line.replace("Collaborator:", "").trim();
                    currentArtist.addCollaborator(new Artist(collaboratorName));
                } else if (line.startsWith("SocialMedia:") && currentArtist != null) {
                    String link = line.replace("SocialMedia:", "").trim();
                    currentArtist.addSocialMediaLink(link);
                }
            }
        }
        return artists;
    }

    // Custom method to display artist details
    public String getArtistDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Artist: ").append(name).append("\n");
        details.append("Biography: ").append(biography).append("\n");
        details.append("Genres: ").append(String.join(", ", genres)).append("\n");
        details.append("Albums: ");
        for (Album album : albums) {
            details.append(album.getTitle()).append(" ");
        }
        details.append("\nAwards: ");

        details.append("\nCollaborators: ");
        for (Artist collaborator : collaborators) {
            details.append(collaborator.getName()).append(" ");
        }
        details.append("\nSocial Media: ").append(String.join(", ", socialMediaLinks));
        return details.toString();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Song> getSongs() {
        return List.copyOf(songs);
    }

    public List<Album> getAlbums() {
        return List.copyOf(albums);
    }

    public List<String> getGenres() {
        return List.copyOf(genres);
    }

    public List<String> getSocialMediaLinks() {
        return List.copyOf(socialMediaLinks);
    }


    public List<Artist> getCollaborators() {
        return List.copyOf(collaborators);
    }
}
