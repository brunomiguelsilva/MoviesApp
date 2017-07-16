package movies.flag.pt.moviesapp.ui.screens;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.ui.fragments.MoviesScreenFragment;
import movies.flag.pt.moviesapp.ui.fragments.TvShowsScreenFragment;

import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.BASE_IMG_URL;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.POSTER_SIZE_W500;

public class MainScreen extends BaseScreen
        implements NavigationView.OnNavigationItemSelectedListener {

    //region: Fields Declaration
    private static final String KEY_SEARCH = "Search";
    private final int REQUEST_CODE = 1;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    //endregion

    //region: Override Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        findViews();

        Fragment fragment;
        fragment = new MoviesScreenFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SEARCH, "");
        fragment.setArguments(args);
        replaceFragment(fragment);

        setActionBarTitle(navigationView.getMenu().getItem(0));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String theResponse;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            if(data.hasExtra("response")){
                theResponse = data.getExtras().getString("response");
                Fragment fragment;
                fragment = new MoviesScreenFragment();
                Bundle args = new Bundle();
                args.putString(KEY_SEARCH, theResponse);
                fragment.setArguments(args);
                replaceFragment(fragment);
            }

        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
        /*
        else {
            if(snackbar == null) {
                snackbar = Snackbar
                        .make(drawer, getResources().getString(R.string.action_close_app), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.action_yes), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                super.onBackPressed();
                            }
                        });
            }

            if(!snackbar.isShown()) {
                snackbar.show();
            } else {
                super.onBackPressed();
            }
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        //MenuItem shareItem = menu.findItem(R.id.action_share);
        //shareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        //setIntent("This is example text");
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        clearBackStack();

        if (id == R.id.nav_movies) {
            Fragment fragment;
            fragment = new MoviesScreenFragment();
            replaceFragment(fragment);
        } else if (id == R.id.nav_tv_shows) {
            Fragment fragment;
            fragment = new TvShowsScreenFragment();
            replaceFragment(fragment);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchScreen.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, LocationScreen.class);
            startActivity(intent);
        //} else if (id == R.id.nav_settings) {
        //    Intent intent = new Intent(this, SettingsScreen.class);
        //    startActivity(intent);
        } else if (id == R.id.nav_close) {
            Intent intent = new Intent(movies.flag.pt.moviesapp.ui.screens.BaseScreen.Constants.FINISH_SCREEN_ACTION);
            LocalBroadcastManager.getInstance(MainScreen.this).sendBroadcast(intent);
        }

        //Set the action bar title
        setActionBarTitle(item);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    //endregion

    //region: Private Methods.
    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setActionBarTitle(MenuItem item) {
        String title;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (String)item.getTitle();
        toolbar.setSubtitle(title);
    }

    private void clearBackStack() {
        // in my case I get the support fragment manager, it should work with the native one too
        FragmentManager fragmentManager = getSupportFragmentManager();
        // this will clear the back stack and displays no animation on the screen
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        /*
        FragmentManager manager = getSupportFragmentManager();
        while (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        */
    }
    //endregion

    //region: Public Methods.
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, null);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment, null);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void navigateToDetailImageFullScreen(String posterPath) {
        Bundle extras = new Bundle();
        String completePosterPath = BASE_IMG_URL + POSTER_SIZE_W500 + posterPath;
        extras.putString(getResources().getString(R.string.cover_image), completePosterPath);
        Intent intent = new Intent(this, DetailImageFullScreen.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
    //endregion
}