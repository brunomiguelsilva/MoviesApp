package movies.flag.pt.moviesapp.ui.screens;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import movies.flag.pt.moviesapp.R;

public class LocationScreen extends FragmentActivity implements OnMapReadyCallback {

    //region: Fields Declaration
    private static final int CALL_PHONE_PERMISSION_ID = 1;
    private GoogleMap mMap;
    private TextView phone;
    protected FinishScreenReceiver finishScreenReceiver;
    //endregion

    //region: Override Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_screen);

        finishScreenReceiver = new FinishScreenReceiver() ;
        LocalBroadcastManager.getInstance(this).registerReceiver(finishScreenReceiver, new IntentFilter(BaseScreen.Constants.FINISH_SCREEN_ACTION));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_screen_map);
        mapFragment.getMapAsync(this);

        findViews();
        addListeners();
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishScreenReceiver);
        super.onDestroy();
    }
    //endregion

    //region: Private Methods.
    private void findViews() {
        phone = (TextView) findViewById(R.id.location_screen_phone);
    }

    private void addListeners() {
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(LocationScreen.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LocationScreen.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PHONE_PERMISSION_ID);

                    return;
                }
                startCallIntent();
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void startCallIntent () {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
        startActivity(intent);
    }

    private class FinishScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context , Intent intent) {
            finish();
        }
    }
    //endregion

    //region: Public Methods.
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Flag and move the camera
        LatLng flagLocation = new LatLng(38.7331441, -9.1468265);

        //Activate Traffic Layer
        mMap.setTrafficEnabled(true);
        //Add pin to Flag location
        Marker flagMarker = mMap.addMarker(new MarkerOptions().position(flagLocation).title("Flag"));
        flagMarker.showInfoWindow();

        //Defines camera position
        CameraPosition position = new CameraPosition.Builder().target(flagLocation).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CALL_PHONE_PERMISSION_ID){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCallIntent();
            } else {
                finish();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }
    }
    //endregion
}