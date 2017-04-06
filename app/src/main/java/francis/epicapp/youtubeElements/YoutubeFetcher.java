package francis.epicapp.youtubeElements;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import francis.epicapp.youtubeElements.urlParameters.Order;
import francis.epicapp.youtubeElements.urlParameters.Type;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Francis on 09-Feb-17.
 */

public abstract class YoutubeFetcher {
    //URL config
    public static final String API_KEY = "AIzaSyBxhms_1haI2liqQqiJ7t_iL6yyIj5ZRTA";
    public static final String CHANNEL_ID = "UCAbe0RcmKz_kVxCsZdSwXSQ";
    public static final String PART = "snippet";
    public static final int MAX_RESULT = 50;
    //-----------------------------------------


    private static OkHttpClient http;
    private static JSONObject json = null;

    private static JSONObject getJSON(String url) throws IOException, JSONException {

        Request request = new Request.Builder().url(url).build();

        if (http == null)
            http = new OkHttpClient();

        Response response = http.newCall(request).execute();

        json = new JSONObject(response.body().string());

        return json;
    }

    public static ArrayList<Video> getChannelVideos() throws IOException, JSONException {

        JSONObject json = getJSON(urlBuilderForChannel(Type.video, Order.date, null));

        JSONArray videos = json.getJSONArray("items");

        JSONObject snippet = null;
        ArrayList<Video> tabVideos = new ArrayList<>();


        for (int i = 0; i < videos.length(); i++) {
            snippet = videos.getJSONObject(i).getJSONObject("snippet");
            String title = snippet.getString("title");
            String publishedAt = snippet.getString("publishedAt");
            String description = snippet.getString("description");
            String thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");

            String id = videos.getJSONObject(i).getJSONObject("id").getString("videoId");

            Video vid = new Video(id, title, description, thumbnail, publishedAt);

            tabVideos.add(vid);
        }

        return tabVideos;
    }

    public static ArrayList<Video> getPlaylistVideos(String PlaylistId) throws IOException, JSONException {

        JSONObject json = getJSON(urlBuilderForPlaylist(PlaylistId, null));

        JSONArray videos = json.getJSONArray("items");

        JSONObject snippet = null;
        ArrayList<Video> tabVideos = new ArrayList<>();


        for (int i = 0; i < videos.length(); i++) {
            snippet = videos.getJSONObject(i).getJSONObject("snippet");
            String title = snippet.getString("title");
            String publishedAt = snippet.getString("publishedAt");
            String description = snippet.getString("description");
            String thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");

            String id = snippet.getJSONObject("resourceId").getString("videoId");
            Video vid = new Video(id, title, description, thumbnail, publishedAt);

            tabVideos.add(vid);
        }

        return tabVideos;
    }

    public static ArrayList<Playlist> getChannelPlaylists() throws IOException, JSONException {


        JSONObject json = getJSON(urlBuilderForChannel(Type.playlist, Order.videoCount, null));

        JSONArray videos = json.getJSONArray("items");

        JSONObject snippet = null;
        ArrayList<Playlist> listPlaylist = new ArrayList<>();


        for (int i = 0; i < videos.length(); i++) {
            snippet = videos.getJSONObject(i).getJSONObject("snippet");
            String title = snippet.getString("title");
            String publishedAt = snippet.getString("publishedAt");
            String description = snippet.getString("description");
            String thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");

            String id = videos.getJSONObject(i).getJSONObject("id").getString("playlistId");

            Playlist playlist = new Playlist(id, title, description, thumbnail, publishedAt);

            listPlaylist.add(playlist);
        }

        return listPlaylist;
    }

    protected static String urlBuilderForChannel(Type type, Order order, String pageToken) {
        if (pageToken == null) {
            Log.d("URL channel", "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&channelId=" + CHANNEL_ID + "&part=" + PART + "&type=" + type.toString() + "&order=" + order.toString() + "&maxResults=" + MAX_RESULT);
            return "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&channelId=" + CHANNEL_ID + "&part=" + PART + "&type=" + type.toString() + "&order=" + order.toString() + "&maxResults=" + MAX_RESULT;
        } else {
            Log.d("URL channel", "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&channelId=" + CHANNEL_ID + "&part=" + PART + "&type=" + type.toString() + "&order=" + order.toString() + "&maxResults=" + MAX_RESULT + "&pageToken=" + pageToken);
            return "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&channelId=" + CHANNEL_ID + "&part=" + PART + "&type=" + type.toString() + "&order=" + order.toString() + "&maxResults=" + MAX_RESULT + "&pageToken=" + pageToken;
        }
    }

    protected static String urlBuilderForPlaylist(String playlistId, String pageToken) {

        if (pageToken == null) {
            Log.d("URL playlist", "https://www.googleapis.com/youtube/v3/playlistItems?key=" + API_KEY + "&playlistId=" + playlistId + "&part=" + PART + "&maxResults=" + MAX_RESULT);
            return "https://www.googleapis.com/youtube/v3/playlistItems?key=" + API_KEY + "&playlistId=" + playlistId + "&part=" + PART + "&maxResults=" + MAX_RESULT;
        } else {
            Log.d("URL playlist", "https://www.googleapis.com/youtube/v3/playlistItems?key=" + API_KEY + "&playlistId=" + playlistId + "&part=" + PART + "&maxResults=" + MAX_RESULT + "&pageToken=" + pageToken);
            return "https://www.googleapis.com/youtube/v3/playlistItems?key=" + API_KEY + "&playlistId=" + playlistId + "&part=" + PART + "&maxResults=" + MAX_RESULT + "&pageToken=" + pageToken;
        }
    }
}
