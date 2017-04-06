package francis.epicapp.fragments;


import android.os.Bundle;
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

import java.util.ArrayList;

import francis.epicapp.R;
import francis.epicapp.Stream;


/**
 * rien fait encore
 */
public class HoraireFragment extends Fragment {

    ListView listView;
    ArrayList<Stream> semaine = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horaire, container, false);

        semaine.add(new Stream("Lundi", "19h00", "Epic Talkshow", "Epic joystick et 1UPHGG", "Un podcast explosif avec Epic Joystick et 1UPHGG!\n\nDiscussion sur les dernières nouvelles concernant les jeux vidéo et la technologies."));
        semaine.add(new Stream("Mardi", "19h00", "Les mardis Mat", "Mat", "Que ça soit des nouveautés, des jeux mobiles ou les titres gratuits du mois, Mat vous attend!"));
        semaine.add(new Stream("Mercredi", "19h00", "Mercredi avec Alex", "Alex", "Une rotation de jeux avec Alex!\n\nEn ce moment, Alex tente de compléter The Last Guardian, Titanfall 2, HITMAN, et la série Mega Man."));
        semaine.add(new Stream("Jeudi", "19h00", "Les jeudis subs", "Une panoplie de jeux choisis par les abonnés!\n\nSi vous êtes un abonné payant, visitez la section Subscribers sur www.epicjoysitck.com pour voter pour les jeux!"));

        listView = (ListView) view.findViewById(R.id.maListeView);

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

                LinearLayout hideLayout = (LinearLayout) convertView.findViewById(R.id.toHide);
                hideLayout.setVisibility(View.GONE);

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


                return convertView;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.toHide);
                ImageView icon = (ImageView) view.findViewById(R.id.expandIcon);

                switch (hideLayout.getVisibility()) {
                    case View.GONE:
                        icon.setRotation(icon.getRotation() + 90);
                        hideLayout.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        icon.setRotation(icon.getRotation() - 90);
                        hideLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
