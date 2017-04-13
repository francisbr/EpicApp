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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import francis.epicapp.InternetStatusListener;
import francis.epicapp.R;
import francis.epicapp.youtubeElements.Video;
import francis.epicapp.youtubeElements.YoutubeFetcher;
import francis.epicapp.services.TimeDifference;

/**
 * List toutes les videos du channel. Fetch les video lorsque l;on load la page
 */
public class ListVideoFragment extends Fragment {

    public static VideosFetcher fetcher;

    ListView list;
    ProgressBar bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_list_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (ListView) view.findViewById(R.id.maListeView);
        bar = (ProgressBar) view.findViewById(R.id.listeVideoProgressBar);


        list.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (InternetStatusListener.isOnline(getContext())) {
            fetcher = new VideosFetcher();
            fetcher.execute();
        } else {
            bar.setVisibility(View.GONE);
        }
    }

    public class VideosFetcher extends AsyncTask<Object, Object, ArrayList> {

        private class LoadMore extends AsyncTask<Object, Object, Boolean> {
            ArrayList<Video> nextvideos;
            public LoadMore(ArrayList<Video> nextvideos) {
                this.nextvideos = nextvideos;
            }

            @Override
            protected Boolean doInBackground(Object... objects) {
                try {
                    YoutubeFetcher.getMoreChannelVideos(nextvideos);
                    Log.d("LoadMore", "Got videos");
                    return true;
                } catch (IOException | JSONException e) {
                    return false;
                }
            }

            protected void onPostExecute(Boolean val) {
                Log.d("LoadMore", "Updating");
                ((ChannelBA) list.getAdapter()).update(nextvideos);
                return;
            }
        }
        private class EndlessScrollListener implements AbsListView.OnScrollListener {
            // The minimum number of items to have below your current scroll position
            // before loading more.
            private int nbAvantReload = 1;
            // The current offset index of data you have loaded
            // The total number of items in the dataset after the last load
            private int previousTotalItemCount;
            // True if we are still waiting for the last set of data to load.
            private boolean loading = false;

            private ArrayList<Video> nextvideos;

            public EndlessScrollListener(int visibleThreshold) {
                this.previousTotalItemCount = list.getAdapter().getCount();
                this.nbAvantReload = visibleThreshold;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                Log.d("ChannelOnScroll", ""+previousTotalItemCount+", "+totalItemCount+", "+loading);
                if (totalItemCount < previousTotalItemCount) {
                    this.previousTotalItemCount = totalItemCount;
                    if (totalItemCount == 0) { this.loading = true; }
                }
                // If it's still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.

                if (loading && (totalItemCount > previousTotalItemCount)) {
                    Log.d("ChannelOnScroll", "Hit ");
                    loading = false;
                    nextvideos = null;
                    previousTotalItemCount = totalItemCount;
                }

                // If it isn't currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                if (!loading && YoutubeFetcher.channelNextPage != null &&(firstVisibleItem + visibleItemCount + nbAvantReload) >= totalItemCount ) {
                    loading = onLoadMore(totalItemCount);
                }
            }

            // Defines the process for actually loading more data based on page
            // Returns true if more data is being loaded; returns false if there is no more data to load.
            public boolean onLoadMore(int totalItemsCount) {
                try {
                    nextvideos = (ArrayList<Video>) (((ChannelBA) list.getAdapter()).getVideos()).clone();
                    new LoadMore(nextvideos).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Don't take any action on changed
            }
        }

        private class ChannelBA extends BaseAdapter {
                private ArrayList<Video> data;

                public ChannelBA(ArrayList<Video> in) {
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

        @Override
        protected ArrayList<Video> doInBackground(Object... params) {
            ArrayList<Video> mesVid = new ArrayList<>();

            try {
                mesVid = YoutubeFetcher.getChannelVideos();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

            list.setAdapter(new ChannelBA(videos));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Video currentVid = (Video) videos.get(position);
                    currentVid.playVid(getContext());

                }
            });

            list.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);

            list.setOnScrollListener(new EndlessScrollListener(5));
        }
    }
}
