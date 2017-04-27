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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
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

public class HoraireFragment extends Fragment {
    private class subListView extends ListView {
        public subListView(Context context) {
            super(context);
        }
        //de Dedaniya HirenKumar sur stackoverflow
        //http://stackoverflow.com/a/24629341
        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase;
    ScrollView s;

    ArrayList<Stream> semaine = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_horaire, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.s = (ScrollView) view.findViewById(R.id.scrollHoraire);

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

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

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
        ArrayList<Stream> lundi = new ArrayList<>();
        ArrayList<Stream> mardi = new ArrayList<>();
        ArrayList<Stream> mercredi = new ArrayList<>();
        ArrayList<Stream> jeudi = new ArrayList<>();
        ArrayList<Stream> vendredi = new ArrayList<>();
        ArrayList<Stream> samedi = new ArrayList<>();
        ArrayList<Stream> dimanche = new ArrayList<>();


        lundi.clear();
        mardi.clear();
        mercredi.clear();
        jeudi.clear();
        vendredi.clear();
        samedi.clear();
        dimanche.clear();
        for (int i = 0; i < semaine.size(); i++) {
            switch (semaine.get(i).getJour()) {
                case "Lundi":
                    lundi.add(semaine.get(i));
                    break;
                case "Mardi":
                    mardi.add(semaine.get(i));
                    break;
                case "Mercredi":
                    mercredi.add(semaine.get(i));
                    break;
                case "Jeudi":
                    jeudi.add(semaine.get(i));
                    break;
                case "Vendredi":
                    vendredi.add(semaine.get(i));
                    break;
                case "Samedi":
                    samedi.add(semaine.get(i));
                    break;
                case "Dimanche":
                    dimanche.add(semaine.get(i));
                    break;
            }
        }

        definingLists(lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
    }

    private void definingLists(ArrayList<Stream> lundi, ArrayList<Stream> mardi, ArrayList<Stream> mercredi, ArrayList<Stream> jeudi, ArrayList<Stream> vendredi, ArrayList<Stream> samedi, ArrayList<Stream> dimanche) {
//        ListView listViewLundi = (ListView) getView().findViewById(R.id.listLundi);
//        ListView listViewMardi = (ListView) getView().findViewById(R.id.listMardi);
//        ListView listViewMercredi = (ListView) getView().findViewById(R.id.listMercredi);
//        ListView listViewJeudi = (ListView) getView().findViewById(R.id.listJeudi);
//        ListView listViewVendredi = (ListView) getView().findViewById(R.id.listVendredi);
//        ListView listViewSamedi = (ListView) getView().findViewById(R.id.listSamedi);
//        ListView listViewDimanche = (ListView) getView().findViewById(R.id.listDimanche);

//        associateListToView(listViewLundi, lundi);
//        associateListToView(listViewMardi, mardi);
//        associateListToView(listViewMercredi, mercredi);
//        associateListToView(listViewJeudi, jeudi);
//        associateListToView(listViewVendredi, vendredi);
//        associateListToView(listViewSamedi, samedi);
//        associateListToView(listViewDimanche, dimanche);


        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.layoutHoraire);
        if (lundi.size() > 0) {
            addJourToView(linearLayout, "Lundi", lundi);
        }
        if (mardi.size() > 0) {
            addJourToView(linearLayout, "Mardi", mardi);
        }
        if (mercredi.size() > 0) {
            addJourToView(linearLayout, "Mercredi", mercredi);
        }
        if (jeudi.size() > 0) {
            addJourToView(linearLayout, "Jeudi", jeudi);
        }
        if (vendredi.size() > 0) {
            addJourToView(linearLayout, "Vendredi", vendredi);
        }
        if (samedi.size() > 0) {
            addJourToView(linearLayout, "Samedi", samedi);
        }
        if (dimanche.size() > 0) {
            addJourToView(linearLayout, "Dimanche", dimanche);
        }


    }

    private void addJourToView(LinearLayout layout, String jour, ArrayList<Stream> liste) {
        //Cree le textview
        TextView txtJour = new TextView(getContext());
        txtJour.setText(jour);
        txtJour.setPadding(25, 0, 0, 0);
        txtJour.setTextSize(30);
        txtJour.setTextColor(Color.rgb(255, 255, 255));
        txtJour.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        //Cree le listview
        ListView listViewLundi = new subListView(getContext());
        associateListToView(listViewLundi, liste);


        //Ajoute les elements au layout
        try {
            layout.addView(txtJour);
            layout.addView(listViewLundi);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void associateListToView(ListView listView, final ArrayList<Stream> liste) {
        Log.d("--------", "" + liste.toString());

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                Log.d("size", "" + liste.size());
                return liste.size();
            }

            @Override
            public Object getItem(int i) {
                return liste.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_streams, parent, false);


                Log.d("pos", "" + position);
                TextView heureText = (TextView) convertView.findViewById(R.id.txtHeure);
                TextView descriptionText = (TextView) convertView.findViewById(R.id.txtDescription);
                TextView titreText = (TextView) convertView.findViewById(R.id.txtTitre);

                heureText.setText(liste.get(position).getHeure());
                descriptionText.setText(liste.get(position).getDescription());
                titreText.setText(liste.get(position).getTitre());
                return convertView;
            }
        });

        Log.d("--------", "--------");
    }

    public class HoraireFecther extends AsyncTask<Object, Object, ArrayList> {

        @Override
        protected ArrayList doInBackground(Object... objects) {
            loadOnlineData();
            return null;
        }
    }
}
