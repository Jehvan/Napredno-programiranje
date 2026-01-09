import javax.print.attribute.HashAttributeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde;

enum SongStatus {
    PLAY,
    PAUSE,
    FWD,
    REW
}

abstract class SongState implements ISongState {
    Song song;

}

class Song {
    private String title;
    private String artist;
    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }
    @Override
    public String toString(){
        return "Song{title=" + this.title + ", artist=" + this.artist + "}";
    }
}

class MP3Player{
    private List<Song> songList = new ArrayList<>();
    private int currentSong = 0;

    private ISongState playingState;
    private ISongState pausedState;
    private ISongState stoppedState;

    private ISongState state;

    public MP3Player(List<Song> songs) {
        this.songList = songs;

        playingState = new PlayingState(this);
        pausedState = new PausedState(this);
        stoppedState = new StoppedState(this);

        state = stoppedState;
    }

    // button delegates
    public void pressPlay() { state.pressPlay(); }
    public void pressStop() { state.pressStop(); }
    public void pressFWD() { state.pressFWD(); }
    public void pressREW() { state.pressREW(); }

    // helpers
    public void setState(ISongState state) { this.state = state; }
    public ISongState getPlayingState() { return playingState; }
    public ISongState getPausedState() { return pausedState; }
    public ISongState getStoppedState() { return stoppedState; }

    public void nextSong() {
        currentSong = (currentSong + 1) % songList.size();
    }

    public void prevSong() {
        currentSong = (currentSong - 1 + songList.size()) % songList.size();
    }

    public void reset() {
        currentSong = 0;
    }

    public int getCurrentSongIndex() {
        return currentSong;
    }

    public void printCurrentSong(){
        System.out.println(this.songList.get(currentSong).toString());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("MP3Player{currentSong = ").append(this.currentSong)
                .append(", songList = [");
        for (Song s : songList){
            if (songList.get(songList.size() - 1) == s){
                sb.append(s.toString());
            } else sb.append(s.toString()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}

interface ISongState {
    void pressPlay();
    void pressStop();
    void pressFWD();
    void pressREW();
}

class PlayingState implements ISongState {

    private MP3Player player;

    public PlayingState(MP3Player player) {
        this.player = player;
    }

    @Override
    public void pressPlay() {
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.println("Song " + player.getCurrentSongIndex() + " is paused");
        player.setState(player.getPausedState());
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        player.nextSong();
        player.setState(player.getPausedState());
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        player.prevSong();
        player.setState(player.getPausedState());
    }
}

class PausedState implements ISongState {

    private MP3Player player;

    public PausedState(MP3Player player) {
        this.player = player;
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + player.getCurrentSongIndex() + " is playing");
        player.setState(player.getPlayingState());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are stopped");
        player.reset();
        player.setState(player.getStoppedState());
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        player.nextSong();
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        player.prevSong();
    }
}

class StoppedState implements ISongState {

    private MP3Player player;

    public StoppedState(MP3Player player) {
        this.player = player;
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + player.getCurrentSongIndex() + " is playing");
        player.setState(player.getPlayingState());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are already stopped");
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        player.nextSong();
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        player.prevSong();
    }
}
