package francis.epicapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import francis.epicapp.InternetStatusListener;
import francis.epicapp.R;
import francis.epicapp.Stream;


/**
 * rien fait encore
 */
public class HoraireFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase;

    ListView listView;
    ArrayList<Stream> semaine = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horaire, container, false);



        listView = (ListView) view.findViewById(R.id.maListeView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.toHide);
                ImageView icon = (ImageView) view.findViewById(R.id.expandIcon);

                switch (hideLayout.getVisibility()) {
                    case View.GONE:
                        icon.setRotation(0);
                        hideLayout.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        icon.setRotation(-90);
                        hideLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (InternetStatusListener.isOnline(getContext())) {
            HoraireFecther task = new HoraireFecther();
            task.execute();
        } else {
            Snackbar.make(view, "No Internet, coudn't refresh :(", 4000).show();
            retreiveHoraire();
        }
    }

    private void loadOnlineData() {

        mDatabase = database.getReference("Horaire");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                semaine.clear();
                int i = 0;
                while (dataSnapshot.child(Integer.toString(i)).getValue() != null) {
                    addStream(dataSnapshot.child(Integer.toString(i)));
                    i++;
                }
                afficheListeSemaine();
                saveData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveData() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(semaine);
        prefsEditor.putString("Horaire", json);
        prefsEditor.commit();
    }

    private void retreiveHoraire() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("Horaire", "");

        Type type = new TypeToken<List<Stream>>() {
        }.getType();
        semaine = gson.fromJson(json, type);

        afficheListeSemaine();
    }

    private void addStream(DataSnapshot streamData) {
        ArrayList listStreamers = (ArrayList) streamData.child("Streamers").getValue();
        semaine.add(new Stream((String) streamData.child("Jour").getValue(), (String) streamData.child("Heure").getValue(), (String) streamData.child("Titre").getValue(), listStreamers, (String) streamData.child("Description").getValue()));
    }

    private void afficheListeSemaine() {
        listView = (ListView) getView().findViewById(R.id.maListeView);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return semaine.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_jour, parent, false);

                TextView jourText = (TextView) convertView.findViewById(R.id.jourStream);
                TextView heureText = (TextView) convertView.findViewById(R.id.heureStream);
                TextView descriptionText = (TextView) convertView.findViewById(R.id.descriptionStream);
                TextView titreText = (TextView) convertView.findViewById(R.id.titleStream);
                TextView streamerText = (TextView) convertView.findViewById(R.id.streamerStream);

                jourText.setText(semaine.get(position).getJour());
                heureText.setText(semaine.get(position).getHeure());
                descriptionText.setText(semaine.get(position).getDescription());
                titreText.setText(semaine.get(position).getTitre());
                if (semaine.get(position).getStreamer().isEmpty()) {
                    streamerText.setText("");
                } else {
                    streamerText.setText("avec " + semaine.get(position).getStreamer());
                }

                View mView = convertView;
                if (mView == null) {
                    LayoutInflater vi = (LayoutInflater) mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    mView = vi.inflate(R.layout.fragment_horaire, null);
                }

                listView.setBackgroundColor(Color.WHITE);
                TextView text = (TextView) mView.findViewById(R.id.jourStream);
                if (semaine.get(position) != null) {
                    text.setTextColor(Color.BLACK);
                    //.setText(semaine.get(position));
                    //text.setBackgroundColor(Color.BLUE);
                }

                TextView textHeure = (TextView) mView.findViewById(R.id.heureStream);
                if (semaine.get(position) != null) {
                    textHeure.setTextColor(Color.BLACK);
                    //.setText(semaine.get(position));
                    //textHeure.setBackgroundColor(Color.BLUE);
                }

                TextView textDes = (TextView) mView.findViewById(R.id.descriptionStream);
                if (semaine.get(position) != null) {
                    textDes.setTextColor(Color.BLACK);
                    //.setText(semaine.get(position));
                    //textDes.setBackgroundColor(Color.rgb(135,206,250));
                    textDes.setBackgroundColor(Color.LTGRAY);
                }

                TextView textTitle = (TextView) mView.findViewById(R.id.titleStream);
                if (semaine.get(position) != null) {
                    textTitle.setTextColor(Color.BLACK);
                    //.setText(semaine.get(position));
                    //textTitle.setBackgroundColor(Color.BLUE);
                }

                TextView textStream = (TextView) mView.findViewById(R.id.streamerStream);
                if (semaine.get(position) != null) {
                    textStream.setTextColor(Color.BLACK);
                    //.setText(semaine.get(position));
                    //textStream.setBackgroundColor(Color.BLUE);
                }
                return convertView;
            }
        });
    }

    public class HoraireFecther extends AsyncTask<Object, Object, ArrayList> {

        @Override
        protected ArrayList doInBackground(Object... objects) {
            loadOnlineData();
            return null;
        }
    }
}

