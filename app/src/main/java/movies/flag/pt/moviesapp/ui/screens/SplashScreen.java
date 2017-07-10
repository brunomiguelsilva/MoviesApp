package movies.flag.pt.moviesapp.ui.screens;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;

/**
 * Created by Silva on 08/06/2017.
 */

public class SplashScreen extends BaseScreen {

    //region: Fields Declaration
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_TIME = 3000;

    private ImageView splashImageView;
    private LinearLayout rootView;
    //endregion

    //region: Override Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        findViews();
        launchSplashView();

        //verifyFirstUse();
        //addActivityDelay(SPLASH_DISPLAY_TIME);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //endregion

    //region: Private Methods.
    private void findViews(){
        splashImageView = (ImageView) findViewById(R.id.splash_screen_splash_image);
        rootView = (LinearLayout) findViewById(R.id.splash_screen_root);
    }

    private void launchSplashView(){
        splashImageView.setScaleX(0.2f);
        splashImageView.setScaleY(0.2f);
        splashImageView.setAlpha(0f);
        splashImageView.animate().alpha(1f).scaleX(2f).scaleY(2f).rotation(360).setDuration(5000).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startActivity(new Intent(SplashScreen.this, MainScreen.class));
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
    }
    //endregion

    //region: Obsolete Methods. Educational purposes.
    private void verifyFirstUse() {
        boolean firstUse = false;
        if(SharedPreferencesHelper.getStringPreference(PreferenceIds.IS_FIRST_USE) == null){
            firstUse = true;
            SharedPreferencesHelper.savePreference(PreferenceIds.IS_FIRST_USE, "");
        }
        Snackbar.make(rootView, getString(R.string.first_time, firstUse),
                Snackbar.LENGTH_LONG).show();
    }

    private void addActivityDelay(int delay) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                startActivity(intent);
            }
        }, delay);
    }
    //endregion
}