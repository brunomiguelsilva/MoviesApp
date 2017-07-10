package movies.flag.pt.moviesapp;

import android.app.Application;

import com.orm.SugarContext;

import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.utils.DLog;

/**
 * Created by Silva on 20/06/2017.
 */

public class MoviesApplication extends Application {

    //region: Fields Declaration
    private static final String TAG = MoviesApplication.class.getSimpleName();
    private static MoviesApplication instance;
    private int numberOfResumedScreens;

    public static MoviesApplication getInstance(){
        return instance;
    }
    //endregion

    //region: Override Methods.
    @Override
    public void onCreate() {
        super.onCreate();
        DLog.d(TAG, "onCreate");
        instance = this;
        SharedPreferencesHelper.init(this);
        SugarContext.init(this);
    }
    //endregion

    //region: Public Methods.
    public void addOnResumeScreen(){
        numberOfResumedScreens++;
    }

    public void removeOnResumeScreen(){
        numberOfResumedScreens--;
    }

    public boolean applicationIsInBackground(){
        return numberOfResumedScreens != 0;
    }

    public int numberOfApplicationIsInBackground(){
        return numberOfResumedScreens;
    }
    //endregion
}