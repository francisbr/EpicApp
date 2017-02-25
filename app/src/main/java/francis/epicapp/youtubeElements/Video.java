package francis.epicapp.youtubeElements;

import android.content.Context;
import android.content.Intent;

import francis.epicapp.activities.VideoPlayActivity;

/**
 * Created by Francis on 09-Feb-17.
 */

public class Video {
    protected String title;
    protected Date publishedAt;
    protected String description;
    protected String thumbnail;
    protected String id;

    public Video(String id, String title, String description, String thumbnail, String publishedAt) {
        this.title = title;
        this.description = description;
        this.publishedAt = new Date(publishedAt.substring(8,9), publishedAt.substring(5,6), publishedAt.substring(0,3), publishedAt.substring(11,12), publishedAt.substring(14,15));
        this.thumbnail = thumbnail;
        this.id = id;
    }

    public void playVid(Context current) {
        Intent intent = new Intent(current, VideoPlayActivity.class);

        intent.putExtra("videoId", "" + this.id);
        intent.putExtra("videoTitle", "" + this.title);
        intent.putExtra("videoDescription", "" + this.description);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        current.startActivity(intent);

    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
