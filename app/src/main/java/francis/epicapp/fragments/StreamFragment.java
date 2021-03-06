package francis.epicapp.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import francis.epicapp.InternetStatusListener;
import francis.epicapp.R;

/**
 * Created by Francis Boulet-Roule on 30-Mar-17.
 */

public class StreamFragment extends Fragment {

    WebView maWebView;
    ProgressBar bar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stream, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        maWebView = (WebView) view.findViewById(R.id.streamWebView);
        bar = (ProgressBar) view.findViewById(R.id.progBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (InternetStatusListener.isOnline(getContext())) {
            loadStream();
        } else {
            bar.setVisibility(View.GONE);
        }

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //code for portrait mode
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } else {
            //code for landscape mode
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //code for portrait mode
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } else {
            //code for landscape mode
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

    }

    private void loadStream() {
        maWebView.getSettings().setJavaScriptEnabled(true);
        maWebView.loadUrl("https://player.twitch.tv/?channel=epicjoystick&autoplay=true");

        maWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                bar.setProgress(progress);
                if (progress >= 100) {

                    bar.setVisibility(View.GONE);

                    maWebView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        maWebView.destroy();
        maWebView = null;
        super.onDestroy();
    }
}
