package francis.epicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import francis.epicapp.R;

/**
 * Created by Francis Boulet-Roule on 30-Mar-17.
 */

public class StreamFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stream, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final WebView maWebView = (WebView) view.findViewById(R.id.streamWebView);
        final ProgressBar bar = (ProgressBar) view.findViewById(R.id.progBar);

        maWebView.getSettings().setJavaScriptEnabled(true);
        maWebView.loadUrl("https://player.twitch.tv/?channel=epicjoystick&autoplay=true");

        maWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                Log.d("webView progress", "" + progress);

                bar.setProgress(progress);
                if (progress >= 100){

                    bar.setVisibility(View.GONE);

                    maWebView.setVisibility(View.VISIBLE);
                    maWebView.performClick();
                }
            }
        });


        maWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (((AppCompatActivity) getActivity()).getSupportActionBar().isShowing()) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                    } else {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    }
                }
                return false;
            }
        });

    }
}
