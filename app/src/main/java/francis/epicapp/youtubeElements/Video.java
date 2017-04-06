package francis.epicapp.youtubeElements;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import francis.epicapp.activities.VideoPlayActivity;

/**
 * Created by Francis on 09-Feb-17.
 */

public class Video implements Serializable {
    protected String title;
    protected Date publishedAt;
    protected String description;
    protected String thumbnail;
    protected String id;

    public Video(String id, String title, String description, String thumbnail, String publishedAt) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.id = id;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        String toFormat = publishedAt.substring(0, 10) + " " + publishedAt.substring(11, 19);

        try {

            this.publishedAt = simpleDateFormat.parse(toFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    public Date getPublishedAt() {
        return publishedAt;
    }
}
