package francis.epicapp.services;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import francis.epicapp.fragments.playlists.SecondFragment;

/**
 * Created by Francis on 13-Feb-17.
 */

public abstract class DataBase {
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef;


    public static ArrayList<String> selectedPlayListsIds = new ArrayList<>();
    public static String data = "";

    static int i = 1;

    /**
     *  lis les playlist id rentrer par les streamer sur Firebase
     * @param reset TOUJOUR TRUE (false seulement dans la recursion
     */
    public static void getSelectedPlaylistsId(boolean reset) {
        if (reset) {
            i = 1;
            selectedPlayListsIds.clear();
        }


        myRef = database.getReference("playlist" + i);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                data = dataSnapshot.getValue(String.class);


                try {
                    if (!data.equals(null)) {

                        selectedPlayListsIds.add(data);
                        i++;
                        getSelectedPlaylistsId(false);
                    }
                } catch (NullPointerException e){

                    if (selectedPlayListsIds.size() <=0)
                        SecondFragment.PLAYLIST_TO_FETCH = "all";
                    else {
                        SecondFragment.PLAYLIST_TO_FETCH = "selected";
                    }
                    SecondFragment.showTabs();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DATABASE", "Failed to read value.", error.toException());
            }
        });



    }
}
