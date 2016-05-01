package fr.do_f.rssfeedify.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import fr.do_f.rssfeedify.R;

/**
 * Created by do_f on 21/04/16.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkReceiver";

    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 2;

    private onNetworkStateChanged onNetworkStateChanged;

    public interface onNetworkStateChanged {
        void onStateChange(int state);
    }

    public void setOnNetworkStateChanged(onNetworkStateChanged onNetworkStateChanged) {
        this.onNetworkStateChanged = onNetworkStateChanged;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Log.d(TAG, "onReceive");
        if (networkInfo != null) {
            if (onNetworkStateChanged != null) {
                Log.d(TAG, "NETWORK == ON");
                onNetworkStateChanged.onStateChange(STATE_ON);

            }
        } else {
            if (onNetworkStateChanged != null) {
                Log.d(TAG, "NETWORK == OFF");
                onNetworkStateChanged.onStateChange(STATE_OFF);
            }
        }
    }



    public int singleCheck(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null) {
            return STATE_ON;
        } else {
            return STATE_OFF;
        }
    }
}