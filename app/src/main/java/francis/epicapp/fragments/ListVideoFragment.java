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
import java.util.Date;

import francis.epicapp.R;
import francis.epicapp.youtubeElements.Video;
import francis.epicapp.youtubeElements.YoutubeFetcher;


/**
 * List toutes les videos du channel. Fetch les video lorsque l;on load la page
 */
public class ListVideoFragment extends Fragment {

    public static VideosFetcher fetcher;

    ListView list;



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

                    TextView dateText = (TextView) convertView.findViewById(R.id.dateText);


                    title.setText(currentVid.getTitle());
                    //description.setText(videos[position].description);
                    dateText.setText(calculateTimeDifference(currentVid.getPublishedAt()));

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

    private String calculateTimeDifference(Date publishedAt) {
        String formattedDifference = "Just now";

        Date now = new Date();
        long different = now.getTime() - publishedAt.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        long elapsedWeeks = elapsedDays / 7;
        long elapsedMonths = elapsedWeeks / 4;
        long elapsedYears = elapsedMonths / 12;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;



        if ( elapsedYears > 1 )
            formattedDifference = "" + elapsedYears + " years ago";
        else if ( elapsedYears == 1 )
            formattedDifference = "" + elapsedYears + " year ago";
        else {
            if ( elapsedMonths > 1 )
                formattedDifference = "" + elapsedMonths + " months ago";
            else if ( elapsedMonths == 1 )
                formattedDifference = "" + elapsedMonths + " month ago";
            else {
                if ( elapsedWeeks > 1 )
                    formattedDifference = "" + elapsedWeeks + " weeks ago";
                else if ( elapsedWeeks == 1 )
                    formattedDifference = "" + elapsedWeeks + " week ago";
                else {
                    if ( elapsedDays > 1 )
                        formattedDifference = "" + elapsedDays + " days ago";
                    else if ( elapsedDays == 1 )
                        formattedDifference = "" + elapsedDays + " day ago";
                    else {
                        if ( elapsedHours > 1 )
                            formattedDifference = "" + elapsedHours + " hours ago";
                        else if ( elapsedHours == 1 )
                            formattedDifference = "" + elapsedHours + " hour ago";
                        else {
                            if ( elapsedMinutes > 2 )
                                formattedDifference = "" + elapsedMinutes + " minutes ago";
                        }
                    }
                }
            }
        }


        return formattedDifference;
    }

}
