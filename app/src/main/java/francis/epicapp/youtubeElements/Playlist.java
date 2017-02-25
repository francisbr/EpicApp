package francis.epicapp.youtubeElements;

import java.util.ArrayList;

/**
 * Created by Francis on 13-Feb-17.
 */

public class Playlist {

    protected ArrayList<Video> videoList;

    protected String playlistId;
    protected String playlistTitle;
    protected String thumbnail;
    protected Date publishedAt;
    protected String description;

    protected ArrayList<String> pagesList;


    public Playlist(String id, String title, String description, String thumbnail, String publishedAt) {
        this.playlistTitle = title;
        this.description = description;
        this.publishedAt = new Date(publishedAt.substring(8,9), publishedAt.substring(5,6), publishedAt.substring(0,3), publishedAt.substring(11,12), publishedAt.substring(14,15));
        this.thumbnail = thumbnail;
        this.playlistId = id;
    }

    protected void getVideos(){

    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }
}
