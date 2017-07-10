package movies.flag.pt.moviesapp.ui.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import movies.flag.pt.moviesapp.MoviesApplication;
import movies.flag.pt.moviesapp.utils.DLog;

/**
 * Created by Silva on 06/06/2017.
 */

public abstract class BaseScreen extends AppCompatActivity {

    //region: Fields Declaration
    protected final String tag = getClass().getSimpleName();
    private final MoviesApplication moviesApplication = MoviesApplication.getInstance();
    protected FinishScreenReceiver finishScreenReceiver;
    //endregion

    //region: Override Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DLog.d(tag, "onCreate");
        finishScreenReceiver = new FinishScreenReceiver() ;
        LocalBroadcastManager.getInstance(this).registerReceiver(finishScreenReceiver, new IntentFilter(Constants.FINISH_SCREEN_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        DLog.d(tag, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DLog.d(tag, "onResume");
        moviesApplication.addOnResumeScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DLog.d(tag, "onPause");
        moviesApplication.removeOnResumeScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DLog.d(tag, "onStop");
    }

    @Override
    protected void onDestroy() {
        DLog.d(tag, "onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishScreenReceiver);
        super.onDestroy();
    }
    //endregion.

    //region: Private Methods.
    private class FinishScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context , Intent intent) {
            DLog.d(tag, String.valueOf(moviesApplication.numberOfApplicationIsInBackground()));
            if(moviesApplication.applicationIsInBackground()) {
                DLog.d(tag, "onReceive Screen will finish!");
                finish();
            }
        }
    }
    //endregion

    public static class Constants {
        public static final String FINISH_SCREEN_ACTION = "finish_screen_action";
    }
}
