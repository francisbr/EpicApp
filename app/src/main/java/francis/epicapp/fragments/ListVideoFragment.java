package francis.epicapp.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import francis.epicapp.R;
import francis.epicapp.youtubeElements.Video;
import francis.epicapp.youtubeElements.YoutubeFetcher;


/**
 * List toutes les videos du channel. Fetch les video lorsque l;on load la page
 */
public class ListVideoFragment extends Fragment {

    public static VideosFetcher fetcher;

    ListView list;

    public ListVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        list = (ListView) container.findViewById(R.id.maListeView);


        fetcher = new VideosFetcher();

        fetcher.execute();
        return inflater.inflate(R.layout.fragment_list_video, container, false);
    }

    public class VideosFetcher extends AsyncTask<Object, Object, ArrayList> {

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

            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return videos.size();
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
                    Video currentVid = (Video) videos.get(position);

                    if (convertView == null)
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_youtube, parent, false);

                    TextView title = (TextView) convertView.findViewById(R.id.titleListe);
                    //TextView description = (TextView) convertView.findViewById(R.id.description);
                    ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail_view);

                    title.setText(currentVid.getTitle());
                    //description.setText(videos[position].description);


                    Picasso.with(getContext())
                            .load(currentVid.getThumbnail())
                            .into(image);

                    return convertView;
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Video currentVid = (Video) videos.get(position);
                    currentVid.playVid(getContext());
                }
            });

        }
    }

}
