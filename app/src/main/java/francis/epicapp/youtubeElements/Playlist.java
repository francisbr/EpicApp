package francis.epicapp.youtubeElements;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Francis on 13-Feb-17.
 */

public class Playlist implements Serializable {

    protected ArrayList<Video> videoList = new ArrayList<>();

    protected String playlistId;
    protected String playlistTitle;
    protected String thumbnail;
    protected Date publishedAt;
    protected String description;




    public Playlist(String id, String title, String description, String thumbnail, String publishedAt) {
        this.playlistTitle = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.playlistId = id;


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        String toFormat = publishedAt.substring(0,10) + " " + publishedAt.substring(11,19);

        try {

            this.publishedAt = simpleDateFormat.parse(toFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        getVideos();
    }

    public Playlist(String id, String title) {
        this.playlistTitle = title;
        this.playlistId = id;
        getVideos();
    }

    protected void getVideos(){
        VideosFetcher fetcher = new VideosFetcher();
        fetcher.execute();


    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public ArrayList<Video> getVideoList() {
        return videoList;
    }

    public class VideosFetcher extends AsyncTask<ArrayList<Video>, Integer, ArrayList> {

        @Override
        protected ArrayList<Video> doInBackground(ArrayList<Video>... list) {
            try {
                videoList = YoutubeFetcher.getPlaylistVideos(playlistId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return videoList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }


        @Override
        protected void onPostExecute(ArrayList videos) {

            Log.d("" + playlistTitle, "videoDownloaded (" + videoList.size() + ")");
        }
    }
}
