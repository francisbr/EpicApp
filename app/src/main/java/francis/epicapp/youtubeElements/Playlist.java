package francis.epicapp.youtubeElements;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import francis.epicapp.R;
import francis.epicapp.fragments.ListVideoFragment;

/**
 * Created by Francis on 13-Feb-17.
 */

public class Playlist implements Serializable {

    protected String playlistId;
    protected String playlistTitle;
    protected String thumbnail;
    protected Date publishedAt;
    protected String description;

    private Object[] res;

    public Playlist(String id, String title, String description, String thumbnail, String publishedAt) {
        this.playlistTitle = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.playlistId = id;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        String toFormat = publishedAt.substring(0, 10) + " " + publishedAt.substring(11, 19);

        try {

            this.publishedAt = simpleDateFormat.parse(toFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.res = new Object[] {null, new ArrayList<> ()};
        getVideos();
    }

    public Playlist(String id, String title) {
        this.playlistTitle = title;
        this.playlistId = id;

        this.res = new Object[] {null, new ArrayList<> ()};
        getVideos();
    }

    protected void getVideos() {
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
        return (ArrayList<Video>)res[1];
    }

    public String getNextPageToken() {
        return (String)res[0];
    }

    public class VideosFetcher extends AsyncTask<Object[], Integer, Object[]> {
        @Override
        protected Object[] doInBackground(Object[]... list) {
            String playlistNextPage = null;
            try {
                 YoutubeFetcher.getPlaylistVideos(playlistId, res);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }


        @Override
        protected void onPostExecute(Object[] res) {

            Log.d("" + playlistTitle, "videoDownloaded (" + ((ArrayList<Video>) res[1]).size() + ")");
        }
    }

}
