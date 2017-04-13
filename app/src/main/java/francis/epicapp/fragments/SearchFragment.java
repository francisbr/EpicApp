package francis.epicapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import francis.epicapp.InternetStatusListener;
import francis.epicapp.R;
import francis.epicapp.services.TimeDifference;
import francis.epicapp.youtubeElements.Video;
import francis.epicapp.youtubeElements.YoutubeFetcher;

/**
 * Created by francois on 12/04/17.
 */

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    ListView list;
    ProgressBar bar;
    SearchView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (ListView) view.findViewById(R.id.maListeView);
        bar = (ProgressBar) view.findViewById(R.id.searchProgressBar);
        search = (SearchView) view.findViewById(R.id.searchView);
        search.setOnQueryTextListener(this);

        list.setVisibility(View.GONE);
        bar.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        search.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d("SEARCH", "onQueryTextSubmit");
        bar.setVisibility(View.VISIBLE);
        search.clearFocus();
        VideosFetcher fetcher = new VideosFetcher(s);
        fetcher.execute();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d("SEARCH", "onQueryTextChange");
        return false;
    }

    private class SearchBA extends BaseAdapter {
        private ArrayList<Video> data;

        public SearchBA(ArrayList<Video> in) {
            this.data = in;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Video currentVid = (Video) data.get(position);

            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_youtube, parent, false);

            TextView title = (TextView) convertView.findViewById(R.id.titleListe);
            //TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail_view);

            TextView dateText = (TextView) convertView.findViewById(R.id.dateText);


            title.setText(currentVid.getTitle());
            //description.setText(videos[position].description);
            dateText.setText(TimeDifference.calculateTimeDifference(currentVid.getPublishedAt()));

            Picasso.with(getContext())
                    .load(currentVid.getThumbnail())
                    .into(image);

            return convertView;
        }

        public void update (ArrayList<Video> in) {
            data = in;
            Log.d("ChannelBA", "Notifying");
            notifyDataSetChanged();
        }

        public ArrayList<Video> getVideos () {
            return data;
        }
    }

    public class VideosFetcher extends AsyncTask<Object, Object, ArrayList> {
        String query;

        public VideosFetcher (String query) {
            Log.d("SEARCH", "created fetcher");
            this.query = query;
        }

        @Override
        protected ArrayList<Video> doInBackground(Object... params) {
            ArrayList<Video> mesVid = new ArrayList<>();

            try {
                mesVid = YoutubeFetcher.getSearchVideos(query);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mesVid;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final ArrayList videos) {
            list = (ListView) getActivity().findViewById(R.id.maListeView);

            list.setAdapter(new SearchBA(videos));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Video currentVid = (Video) videos.get(position);
                    currentVid.playVid(getContext());
                }
            });

            list.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
        }
    }

}
