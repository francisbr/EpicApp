package francis.epicapp;

import java.util.ArrayList;

/**
 * Created by Francis on 06-Apr-17.
 */

public class Stream {
    protected String jour;
    protected String heure;
    protected String titre;
    protected ArrayList<String> streamers = new ArrayList<String>();
    protected String description;

    public Stream(String jour, String heure, String titre, String streamers, String description) {
        this.jour = jour;
        this.heure = heure;
        this.titre = titre;
        if (!streamers.equals("")) {
            String tab[] = streamers.split(";");
            for (int i = 0; i < tab.length; i++)
                this.streamers.add(tab[i]);
        }
        this.description = description;
    }

    public Stream(String jour, String heure, String titre, ArrayList<String> streamers, String description) {
        this.jour = jour;
        this.heure = heure;
        this.titre = titre;
        this.streamers = streamers;
        this.description = description;
    }

    public String getJour() {
        return jour;
    }

    public String getHeure() {
        return heure;
    }

    public ArrayList<String> getStreamer() {
        return streamers;
    }

    public String getDescription() {
        return description;
    }

    public String getTitre() {
        return titre;
    }

    @Override
    public String toString() {
        return "" + titre + "@" + heure;
    }
}
