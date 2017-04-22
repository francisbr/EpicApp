package francis.epicapp.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.ArrayList;

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
        loadOnlineData();

        semaine.add(new Stream("Lundi", "19h00", "Epic Talkshow", "Epic joystick,1UPHGG", "Un podcast explosif avec Epic Joystick et 1UPHGG!\n\nDiscussion sur les dernières nouvelles concernant les jeux vidéo et la technologies."));
        semaine.add(new Stream("Mardi", "19h00", "Les mardis Mat", "Mat", "Que ça soit des nouveautés, des jeux mobiles ou les titres gratuits du mois, Mat vous attend!"));
        semaine.add(new Stream("Mercredi", "19h00", "Mercredi avec Alex", "Alex", "Une rotation de jeux avec Alex!\n\nEn ce moment, Alex tente de compléter The Last Guardian, Titanfall 2, HITMAN, et la série Mega Man."));
        semaine.add(new Stream("Jeudi", "19h00", "Les jeudis subs", "", "Une panoplie de jeux choisis par les abonnés!\n\nSi vous êtes un abonné payant, visitez la section Subscribers sur www.epicjoysitck.com pour voter pour les jeux!"));

        listView = (ListView) view.findViewById(R.id.maListeView);

        Log.d("semaineSize", "" + semaine.size());

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

    private void loadOnlineData() {

        mDatabase = database.getReference("Horaire");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                semaine.clear();
                int i = 0;
                while (dataSnapshot.child(Integer.toString(i)).getValue() != null) {
                    addStream(dataSnapshot.child(Integer.toString(i)));
                    Log.d("loop", "" + i);

                    i++;
                }
                afficheListeSemaine();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void addStream(DataSnapshot streamData) {
        Log.d("children", streamData.toString());

        Log.d("Titre", "" + streamData.child("Titre").getValue());
        Log.d("Heure", "" + streamData.child("Heure").getValue());
        Log.d("Jour", "" + streamData.child("Jour").getValue());
        Log.d("Description", "" + streamData.child("Description").getValue());
        Log.d("Streamers", "" + streamData.child("Streamers").getValue());
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
}

