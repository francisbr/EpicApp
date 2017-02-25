package francis.epicapp.youtubeElements;

/**
 * Pas mal useless, prob prendre une libraire deja fait pour calculer les distance de temp
 */
public class Date {
    protected int jour;
    protected int mois;
    protected int annee;
    protected int heure;
    protected int minute;

    public Date(String jour, String mois, String annee, String heure, String minute) {
        this.jour = Integer.parseInt(jour);
        this.mois = Integer.parseInt(mois);
        this.annee = Integer.parseInt(annee);
        this.heure = Integer.parseInt(heure);
        this.minute = Integer.parseInt(minute);
    }

    public int getJour() {
        return jour;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }

    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }
}
