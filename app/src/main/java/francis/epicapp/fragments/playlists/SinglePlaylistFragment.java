package francis.epicapp.fragments.playlists;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import francis.epicapp.R;
import francis.epicapp.youtubeElements.Playlist;
import francis.epicapp.youtubeElements.Video;

/**
 * Created by Francis on 02-Mar-17.
 */

public class SinglePlaylistFragment extends Fragment {
    Playlist playlist;
    Bundle args;
    ListView list;

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
        }


        list = (ListView) view.findViewById(R.id.maListeView);

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
                Video currentVid = (Video) playlist.getVideoList().get(position);
                currentVid.playVid(getContext());
            }
        });
        return view;
    }

    static public Object bytes2Object( byte raw[] ) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
    }


}
