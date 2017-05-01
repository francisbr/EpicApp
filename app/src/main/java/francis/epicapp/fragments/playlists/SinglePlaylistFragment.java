package francis.epicapp.fragments.playlists;

import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import francis.epicapp.R;
import francis.epicapp.youtubeElements.Playlist;
import francis.epicapp.youtubeElements.Video;
import francis.epicapp.youtubeElements.YoutubeFetcher;

/**
 * Created by Francis on 02-Mar-17.
 */

public class SinglePlaylistFragment extends Fragment {
    Playlist playlist;
    Bundle args;
    ListView list;
    private String playlistNextPage;
    private String playlistId;

    private class LoadMore extends AsyncTask<Object, Object, Boolean> {
        Object[] res;
        String playlistId;
        public LoadMore(String playlistId, Object[] res) {
            this.res = res;
            this.playlistId = playlistId;
        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            try {
                YoutubeFetcher.getMorePlaylistsVideos(playlistId, res);
                return true;
            } catch (IOException | JSONException e) {
                return false;
            }
        }
    }
    private class EndlessScrollListener implements AbsListView.OnScrollListener {
        private int nbAvantReload = 1;
        private int previousTotalItemCount = 0;
        private boolean loading = true;
        //res contient des infos passées par l'asynctask, par exemple le nextpagetoken
        private Object[] res;

        public EndlessScrollListener(int visibleThreshold, Object[] res) {
            this.nbAvantReload = visibleThreshold;
            this.res = res;
        }


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {
            Log.d("PlaylistOnScroll", ""+previousTotalItemCount+", "+totalItemCount+", "+loading);
            //si il y a moins d'items qu'avant, l'état est inconsistant, on lance un chargement
            //pour avoir des données consistantes
            if (totalItemCount < previousTotalItemCount) {
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }

            //si on charge mais que le nombre d'items a augmenté, on a fini le asynctask
            //remarquez si on donnait une référence à cet objet au asynctask,
            //il pourrait nous signaler a la place
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            //si on ne charge pas et que nextpagetoken != null, alors on regarde si il faut charger
            if (!loading && res[0] != null && (firstVisibleItem + visibleItemCount + nbAvantReload) >= totalItemCount ) {
                Log.d("PlaylistOnScroll", "Hit load more nextpagetoken="+(String) res[0]+", "+firstVisibleItem+", "+visibleItemCount+", "+nbAvantReload+", "+totalItemCount);
                loading = onLoadMore(totalItemCount);
            }
        }

        // lance un chargeur
        public boolean onLoadMore(int totalItemsCount) {
            try {
                new LoadMore(playlistId,res).execute();
            } catch (Exception e) {
                return false;
            }
            ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
            return true;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_video, container, false);

        args = getArguments();

        try {
            playlist = (Playlist) bytes2Object(args.getByteArray("playlist"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        list = (ListView) view.findViewById(R.id.maListeView);
        playlistId = playlist.getPlaylistId();
        playlistNextPage = playlist.getNextPageToken();

        list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return playlist.getVideoList().size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Video currentVid = (Video) playlist.getVideoList().get(position);

                if (convertView == null)
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_youtube, parent, false);

                TextView title = (TextView) convertView.findViewById(R.id.titleListe);
                TextView dateText = (TextView) convertView.findViewById(R.id.dateText);
                ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail_view);

                title.setText(currentVid.getTitle());
                //description.setText(videos[position].description);
                dateText.setVisibility(View.GONE);

                Picasso.with(getContext())
                        .load(currentVid.getThumbnail())
                        .into(image);

                return convertView;
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video currentVid = (Video) playlist.getVideoList().get(position);
                currentVid.playVid(getContext());
            }
        });

        list.setOnScrollListener(new EndlessScrollListener(5, new Object[] {playlistNextPage,playlist.getVideoList()}));
        return view;
    }

    static public Object bytes2Object(byte raw[]) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        return o;
    }


}
