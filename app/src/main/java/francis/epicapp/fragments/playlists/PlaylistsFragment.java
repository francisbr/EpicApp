package francis.epicapp.fragments.playlists;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import francis.epicapp.InternetStatusListener;
import francis.epicapp.R;
import francis.epicapp.youtubeElements.Playlist;


/**
 * A RENOMMER QUAND FINI
 * page d'affichage de toutes les playlist choisi par les streamers. Peut-etre trouver un meilleur moyen de display toutes les playlist du chanel
 */
public class PlaylistsFragment extends Fragment {
    public TabLayout tabLayout;
    ViewPager pager;

    ArrayList<Playlist> playlists;

    ProgressBar bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        playlists = new ArrayList<>();
        playlists.add(new Playlist("PL8ymlYxc4B4R1uujHyhVZhZPm8ooHUTE7", "EpicTalkShow"));
        playlists.add(new Playlist("PL8ymlYxc4B4ROiGUKr4Vmt9TFSBf7h2ZJ", "EpicJoystick: Critique"));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = (TabLayout) getView().findViewById(R.id.tabsPlaylists);
        pager = (ViewPager) getView().findViewById(R.id.pager);

        bar = (ProgressBar) view.findViewById(R.id.playlistProgressBar);


        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (InternetStatusListener.isOnline(getContext(), (LinearLayout) getView().findViewById(R.id.layoutPlaylist))) {
            WaitingForVideos task = new WaitingForVideos();
            task.execute();
        } else
            bar.setVisibility(View.GONE);
    }

    public class WaitingForVideos extends AsyncTask<Object, Object, ArrayList> {

        @Override
        protected ArrayList<Playlist> doInBackground(Object... option) {

            for (int i = 0; i < playlists.size(); i++) {
                do {
                } while (playlists.get(i).getVideoList().isEmpty());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    SinglePlaylistFragment frag = new SinglePlaylistFragment();
                    Bundle bundle = new Bundle();

                    bundle.putByteArray("playlist", toByteArray(playlists.get(position)));
                    frag.setArguments(bundle);
                    return frag;
                }

                @Override
                public int getCount() {
                    return playlists.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {

                    return playlists.get(position).getPlaylistTitle();
                }
            });

            bar.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
        }
    }


    private byte[] toByteArray(Playlist playlist) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] myBytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(playlist);
            out.flush();
            myBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return myBytes;
    }


}
