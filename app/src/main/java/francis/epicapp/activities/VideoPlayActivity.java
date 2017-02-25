package francis.epicapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import francis.epicapp.R;

/**
 * Activity qui display les videos,
 * recoie en parametre l'id le titre et la description pour initialiser lui meme la video et afficher les infos.
 */
public class VideoPlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    String videoId, title, description;
    YouTubePlayerView player;

    TextView viewTitle, viewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        videoId = getIntent().getExtras().getString("videoId");
        title = getIntent().getExtras().getString("videoTitle");
        description = getIntent().getExtras().getString("videoDescription");

        player = (YouTubePlayerView) findViewById(R.id.playerView);
        viewTitle = (TextView) findViewById(R.id.titleVideo);
        viewDescription = (TextView) findViewById(R.id.descriptionVideo);

        viewTitle.setText(title);
        viewDescription.setText(description);


        setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.initialize(getString(R.string.api_key), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (null == youTubePlayer) return;
        if (!wasRestored) {
//            youTubePlayer.cueVideo(videoId);
            youTubePlayer.loadVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();

        Log.d("result", youTubeInitializationResult.toString());
    }

}
