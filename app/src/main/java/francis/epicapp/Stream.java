package francis.epicapp;

/**
 * Created by Francis on 06-Apr-17.
 */

public class Stream {
    protected String jour;
    protected String heure;
    protected String titre;
    protected String streamer = "";
    protected String description;

    public Stream(String jour, String heure, String titre, String streamer, String description) {
        this.jour = jour;
        this.heure = heure;
        this.titre = titre;
        this.streamer = streamer;
        this.description = description;
    }

    public Stream(String jour, String heure, String titre, String description) {
        this.jour = jour;
        this.heure = heure;
        this.titre = titre;
        this.description = description;
    }

    public String getJour() {
        return jour;
    }

    public String getHeure() {
        return heure;
    }

    public String getStreamer() {
        return streamer;
    }

    public String getDescription() {
        return description;
    }

    public String getTitre() {
        return titre;
    }
}
