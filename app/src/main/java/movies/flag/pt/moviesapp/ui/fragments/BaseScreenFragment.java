package movies.flag.pt.moviesapp.ui.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.helpers.NetworkHelper;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.ui.screens.MainScreen;
import movies.flag.pt.moviesapp.utils.DLog;

/**
 * Created by Silva on 21/06/2017.
 */

public abstract class BaseScreenFragment extends Fragment{

    //region: Fields Declaration
    public static final int FIVE_MINUTES = 1000 * 60 * 5;
    protected final String tag = getClass().getSimpleName();
    protected MainScreen mainScreen;
    protected NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;
    //endregion

    //region: Override Methods.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DLog.d(tag, "onCreate");

        //mainScreen = (MainScreen) getActivity();

        networkChangeBroadcastReceiver = new NetworkChangeBroadcastReceiver();
        getActivity().registerReceiver(networkChangeBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onResume() {
        super.onResume();
        DLog.d(tag, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        DLog.d(tag, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        DLog.d(tag, "onStop");
    }

    @Override
    public void onDestroy() {
        DLog.d(tag, "onDestroy");
        getActivity().unregisterReceiver(networkChangeBroadcastReceiver);
        super.onDestroy();
    }
    //endregion

    //region: Abstract Methods.
    protected abstract void refreshData();
    //endregion

    //region: Private Methods.
    public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkHelper.networkIsAvailable(context)) {
                DLog.d(tag, "wifi is available");
                String lastRefreshTimestamp = SharedPreferencesHelper.getStringPreference(PreferenceIds.LAST_REFRESH_TIMESTAMP);
                long currentTime = System.currentTimeMillis();

                try {
                    if (currentTime - Long.parseLong(lastRefreshTimestamp) > FIVE_MINUTES) {
                        DLog.d(tag, "Start Refresh Data");
                        refreshData();
                    }
                }
                catch (Exception ex)
                {

                }
            } else {
                DLog.d(tag, "wifi is not available");
            }
        }
    }
    //endregion
}
