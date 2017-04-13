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
            formattedDifference = "" + elapsedYears + " years ago";
        else if (elapsedYears == 1)
            formattedDifference = "" + elapsedYears + " year ago";
        else {
            if (elapsedMonths > 1)
                formattedDifference = "" + elapsedMonths + " months ago";
            else if (elapsedMonths == 1)
                formattedDifference = "" + elapsedMonths + " month ago";
            else {
                if (elapsedWeeks > 1)
                    formattedDifference = "" + elapsedWeeks + " weeks ago";
                else if (elapsedWeeks == 1)
                    formattedDifference = "" + elapsedWeeks + " week ago";
                else {
                    if (elapsedDays > 1)
                        formattedDifference = "" + elapsedDays + " days ago";
                    else if (elapsedDays == 1)
                        formattedDifference = "" + elapsedDays + " day ago";
                    else {
                        if (elapsedHours > 1)
                            formattedDifference = "" + elapsedHours + " hours ago";
                        else if (elapsedHours == 1)
                            formattedDifference = "" + elapsedHours + " hour ago";
                        else {
                            if (elapsedMinutes > 2)
                                formattedDifference = "" + elapsedMinutes + " minutes ago";
                        }
                    }
                }
            }
        }


        return formattedDifference;
    }
}
