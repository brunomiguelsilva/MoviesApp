package movies.flag.pt.moviesapp.ui.screens;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import movies.flag.pt.moviesapp.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Silva on 27/06/2017.
 */

public class DetailImageFullScreen extends BaseScreen {

    //region: Fields Declaration
    private ImageView imageFullView;
    private PhotoViewAttacher photoViewAttacher;
    //endregion

    //region: Override Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_image_full_screen);

        findViews();

        Bundle extras = getIntent().getExtras();
        loadImage(extras.getString(getResources().getString(R.string.detail_image_full_screen_cover_image)));
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
    private void findViews() {
        imageFullView = (ImageView) findViewById(R.id.detail_image_full_screen_image_view);
        photoViewAttacher = new PhotoViewAttacher(imageFullView);
    }

    private void loadImage(String url) {
        Picasso.with(getApplicationContext()).load(url).into(imageFullView, new Callback() {
            @Override public void onSuccess() {
                photoViewAttacher.update();
            }

            @Override public void onError() {
                showToast();
            }
        });
    }

    private void showToast(){
        Toast.makeText(this, R.string.toast_error_loading_image, Toast.LENGTH_SHORT).show();
    }

    //endregion
}