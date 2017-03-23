package francis.epicapp.fragments.playlists;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import francis.epicapp.R;
import francis.epicapp.services.DataBase;
import francis.epicapp.youtubeElements.Playlist;
import francis.epicapp.youtubeElements.YoutubeFetcher;


/**
 * A RENOMMER QUAND FINI
 * page d'affichage de toutes les playlist choisi par les streamers. Peut-etre trouver un meilleur moyen de display toutes les playlist du chanel
 */
public class SecondFragment extends Fragment {
    protected static ArrayList<Playlist> allPlaylists = new ArrayList<>();
    protected static ArrayList<Playlist> selectedPlaylists = new ArrayList<>();

    public static TabLayout tabLayout;
    ViewPager pager;

    public static String PLAYLIST_TO_FETCH = "selected";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedPlaylists = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        // Inflate the layout for this fragment

        //Va voir ce que les streamer on demander comme playlist
        DataBase.getSelectedPlaylistsId(true);

        //Va loader toutes les playlist du channel
        PlaylistFetcher task = new PlaylistFetcher();
        task.execute();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = (TabLayout) getView().findViewById(R.id.tabsPlaylists);
        pager = (ViewPager) getView().findViewById(R.id.pager);
    }


    public static void showTabs() {
        tabLayout.removeAllTabs();

        for (int i = 0; i < DataBase.selectedPlayListsIds.size(); i++) {
            for (int j = 0; j < allPlaylists.size(); j++) {
                if (allPlaylists.get(j).getPlaylistId().equals(DataBase.selectedPlayListsIds.get(i))) {
                    tabLayout.addTab(tabLayout.newTab().setText(allPlaylists.get(j).getPlaylistTitle()));
                    selectedPlaylists.add(allPlaylists.get(j));
                    break;
                }
            }
        }
        tabLayout.setVisibility(View.VISIBLE);
    }




    public class PlaylistFetcher extends AsyncTask<Object, Object, ArrayList> {

        @Override
        protected ArrayList<Playlist> doInBackground(Object... option) {

            try {
                allPlaylists = YoutubeFetcher.getChannelPlaylists();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return allPlaylists;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);


            if (PLAYLIST_TO_FETCH.equals("all")) {
                tabLayout.removeAllTabs();
                for (int i = 0; i < allPlaylists.size(); i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(allPlaylists.get(i).getPlaylistTitle()));
                    selectedPlaylists.add(allPlaylists.get(i));
                }


                tabLayout.setVisibility(View.VISIBLE);
            }

//            pager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
//                @Override
//                public Fragment getItem(int position) {
//                    SinglePlaylistFragment frag = new SinglePlaylistFragment();
//
//                    Bundle args = new Bundle();
//                    try {
//                        args.putByteArray("playlist", object2Bytes(allPlaylists.get(position)));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    frag.setArguments(args);
//
//                    return frag;
//                }
//
//                @Override
//                public int getCount() {
//                    return 3;
//                }
//            });

            tabLayout.setupWithViewPager(pager);

        }
    }
    static public byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }
}
