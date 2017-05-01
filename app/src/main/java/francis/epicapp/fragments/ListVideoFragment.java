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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
            ImageButton searchBtn = (ImageButton) getActivity().findViewById(R.id.searchBtn);
            searchBtn.setVisibility(View.VISIBLE);
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
            private int nbAvantReload = 1;
            private int previousTotalItemCount;
            private boolean loading = false;

            private ArrayList<Video> nextvideos;

            public EndlessScrollListener(int visibleThreshold) {
                this.previousTotalItemCount = list.getAdapter().getCount();
                this.nbAvantReload = visibleThreshold;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                //si il y a moins d'items qu'avant, l'état est inconsistant, on lance un chargement
                //pour avoir des données consistantes
                Log.d("ChannelOnScroll", ""+previousTotalItemCount+", "+totalItemCount+", "+loading);
                if (totalItemCount < previousTotalItemCount) {
                    this.previousTotalItemCount = totalItemCount;
                    if (totalItemCount == 0) { this.loading = true; }
                }

                //si on charge mais que le nombre d'items a augmenté, on a fini le asynctask
                //remarquez si on donnait une référence à cet objet au asynctask,
                //il pourrait nous signaler a la place
                if (loading && (totalItemCount > previousTotalItemCount)) {
                    Log.d("ChannelOnScroll", "Hit ");
                    loading = false;
                    nextvideos = null;
                    previousTotalItemCount = totalItemCount;
                }

                //si on ne charge pas et que nextpagetoken != null, alors on regarde si il faut charger
                if (!loading && YoutubeFetcher.channelNextPage != null &&(firstVisibleItem + visibleItemCount + nbAvantReload) >= totalItemCount ) {
                    loading = onLoadMore(totalItemCount);
                }
            }

            // lance un chargeur
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
