package backend;

import java.util.List;
import java.util.ArrayList;

public class Album {
    private  String title;
    private  String releaseDate;
    private  List<Song> songs;

    public Album(String title, String releaseDate) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.songs = new ArrayList<>();
    }

    public Album(String albumTitle, String title, String releaseDate, List<Song> songs) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.songs = songs;
    }

    public Album(String albumTitle) {
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void loadAlbumCover(Song songHi, String s) {
    }
}







