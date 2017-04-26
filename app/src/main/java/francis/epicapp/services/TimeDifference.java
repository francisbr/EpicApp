package francis.epicapp.services;

import java.util.Date;

/**
 * Created by francois on 12/04/17.
 */

public class TimeDifference {
    public static String calculateTimeDifference(Date publishedAt) {
        String formattedDifference = "Just now";

        Date now = new Date();
        long different = now.getTime() - publishedAt.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        long elapsedWeeks = elapsedDays / 7;
        long elapsedMonths = elapsedWeeks / 4;
        long elapsedYears = elapsedMonths / 12;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        if (elapsedYears > 1)
            formattedDifference = "Il y a " + elapsedYears + "ans";
        else if (elapsedYears == 1)
            formattedDifference = "Il y a " + elapsedYears + " an";
        else {
            if (elapsedMonths > 1)
                formattedDifference = "Il y a " + elapsedMonths + " mois";
            else if (elapsedMonths == 1)
                formattedDifference = "Il y a " + elapsedMonths + " mois";
            else {
                if (elapsedWeeks > 1)
                    formattedDifference = "Il y a " + elapsedWeeks + " semaines";
                else if (elapsedWeeks == 1)
                    formattedDifference = "Il y a " + elapsedWeeks + " semaine";
                else {
                    if (elapsedDays > 1)
                        formattedDifference = "Il y a " + elapsedDays + " jours";
                    else if (elapsedDays == 1)
                        formattedDifference = "Il y a " + elapsedDays + " jour";
                    else {
                        if (elapsedHours > 1)
                            formattedDifference = "Il y a " + elapsedHours + " heures";
                        else if (elapsedHours == 1)
                            formattedDifference = "Il y a " + elapsedHours + " heure";
                        else {
                            if (elapsedMinutes > 2)
                                formattedDifference = "Il y a " + elapsedMinutes + " minutes";
                        }
                    }
                }
            }
        }


        return formattedDifference;
    }
}
