package backend;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class AudioPlayer {
    private Player player;
    public long getPlaybackPosition() {
        long mediaPlayer = 0;
        return mediaPlayer != 0 ? mediaPlayer / 1000 : 0;
    }

    public void playSong(File file) {

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//

    public void stopSong() {
        if (player != null) {
            player.close();
        }

    }

    public boolean isPlaying() {
        return false;
    }
}
