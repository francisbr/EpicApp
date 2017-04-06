package francis.epicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Francis on 05-Apr-17.
 */

public class InternetStatusListener extends BroadcastReceiver {
    private static final String TAG="INTERNET_STATUS";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "network status changed");
//        if(InternetStatusListener.isOnline(context)){//check if the device has an Internet connection
//            //Start a service that will make your TCP Connection.
//
//        }
    }

    public static boolean isOnline(Context context, LinearLayout layout, Boolean... show) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            if (show.length > 0){
                if (show[0]){
                    Snackbar.make(layout, "No Internet Connection", 4000).show();
                } else{}
            } else {
                Snackbar.make(layout, "No Internet Connection", 4000).show();
            }

            return false;
        }
    }
}
