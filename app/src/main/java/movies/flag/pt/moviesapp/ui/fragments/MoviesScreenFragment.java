package movies.flag.pt.moviesapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.adapters.MoviesListAdapter;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.database.entities.MovieDbEntity;
import movies.flag.pt.moviesapp.helpers.NetworkHelper;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.http.entities.Movie;
import movies.flag.pt.moviesapp.http.entities.MoviesResponse;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingMoviesAsyncTask;
import movies.flag.pt.moviesapp.http.requests.SearchMoviesAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

/**
 * Created by Silva on 26/06/2017.
 */

public class MoviesScreenFragment extends BaseScreenFragment {

    //region: Fields Declaration
    private static final String KEY_MOVIES_LIST = "Movies_List";
    private static final String KEY_PAGE_SELECTION = "Page_Selection";
    private static final String KEY_LIST_SELECTION = "Page_Selection";
    private static final int FIRST_PAGE = 1;


    private RelativeLayout rootView;
    private ListView listView;
    private int currentFirstVisibleItem, currentVisibleItemCount,totalItem;
    private SwipeRefreshLayout swipeLayout;
    private RelativeLayout footer;
    private MoviesListAdapter adapter;
    private String searchString = "";
    private List<Movie> movies;

    int currentPage = FIRST_PAGE;
    int totalPages = FIRST_PAGE;
    //endregion

    public static MoviesScreenFragment newInstance() {
        MoviesScreenFragment fragment = new MoviesScreenFragment();
        Bundle args = new Bundle();
        //args.putSerializable(listView);
        fragment.setArguments(args);
        return fragment;
    }

    //region: Override Methods.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            searchString = args.getString("SEARCH");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_screen_fragment, container, false);

        findViews(view);
        addListeners(view);

        if(savedInstanceState != null)
        {
            DLog.d(tag, "Get Movies from savedInstanceState");
            movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES_LIST);
            adapter = new MoviesListAdapter(getActivity(), movies);
            listView.setAdapter(adapter);
            currentPage = savedInstanceState.getInt(KEY_PAGE_SELECTION);
            int selection = savedInstanceState.getInt(KEY_LIST_SELECTION);
            listView.setSelection(selection);
            adapter.notifyDataSetChanged();
            footer.setVisibility(View.GONE);

        }
        else {
            adapter = new MoviesListAdapter(getActivity(), movies);

            if (searchString.isEmpty()) {
                executeGetNowPlayingRequest(FIRST_PAGE);
            } else {
                executeSearchMoviesRequest(searchString);
            }

        }

        checkNetwork();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            ArrayList<Movie> arrayMovie = new ArrayList<>(movies);
            outState.putParcelableArrayList(KEY_MOVIES_LIST, arrayMovie);
            outState.putInt(KEY_PAGE_SELECTION, currentPage);
            outState.putInt(KEY_LIST_SELECTION, currentFirstVisibleItem + currentVisibleItemCount);
        }
        catch(Exception ex)
        {

        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void refreshData() {
        DLog.d(tag, "ASK FOR DATA");
        executeGetNowPlayingRequest(FIRST_PAGE);
    }
    //endregion

    //region: Private Methods.
    private void findViews(View view) {
        rootView = (RelativeLayout)view.findViewById(R.id.movies_screen_fragment_root_view);
        listView = (ListView)view.findViewById(R.id.movies_screen_fragment_list_view);
        footer = (RelativeLayout)view.findViewById(R.id.list_item_footer);
        footer.setVisibility(View.GONE);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.movies_screen_fragment_swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void addListeners(View view) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final int lastItem = currentFirstVisibleItem + currentVisibleItemCount;
                if (lastItem == totalItem && scrollState == SCROLL_STATE_IDLE){
                    loadNextPage(currentPage + 1);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                totalItem = totalItemCount;
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentVisibleItemCount = 0;
                executeGetNowPlayingRequest(FIRST_PAGE);
            }
        });
    }

    private void loadNextPage(int page) {
        DLog.d(tag, "Load page: " + page);
        executeGetNowPlayingRequest(page);
    }


    private void executeGetNowPlayingRequest(int page) {
        new GetNowPlayingMoviesAsyncTask(getActivity(), page){

            @Override
            protected void onResponseSuccess(MoviesResponse moviesResponse) {
                DLog.d("MoviesScreenFragment", "onResponseSuccess " + moviesResponse);
                swipeLayout.setRefreshing(false);

                long currentTime = System.currentTimeMillis();
                SharedPreferencesHelper.savePreference(PreferenceIds.LAST_REFRESH_TIMESTAMP, Long.toString(currentTime));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String dateAsString = dateFormat.format(new Date(currentTime));

                Snackbar.make(rootView, Long.toString(currentTime), Snackbar.LENGTH_LONG).show();

                movies = moviesResponse.getMovies();
                currentPage = moviesResponse.getPage();
                totalPages = moviesResponse.getTotalPages();

                if (moviesResponse.getPage() == FIRST_PAGE) {
                    adapter = new MoviesListAdapter(getActivity(), movies);
                    List<MovieDbEntity> books = MovieDbEntity.listAll(MovieDbEntity.class);
                    MovieDbEntity.deleteAll(MovieDbEntity.class);
                } else {
                    adapter.addAll(movies);
                }

                for (Movie movie : movies) {
                    MovieDbEntity movieDbEntity = new MovieDbEntity(movie.getTitle(),
                            movie.getOverview(),
                            movie.getReleaseDate(),
                            TextUtils.join(", ", movie.getMovieGenres()),
                            movie.getPopularity(),
                            movie.getVoteAverage()
                    );
                    movieDbEntity.save(); // Save to database
                }

                List<MovieDbEntity> books = MovieDbEntity.listAll(MovieDbEntity.class);
                DLog.d(tag, "Total de Registos: " + books.size());

                listView.setAdapter(adapter);
                DLog.d(tag, "currentFirstVisibleItem: " + String.valueOf(currentFirstVisibleItem));
                DLog.d(tag, "currentVisibleItemCount: " + String.valueOf(currentVisibleItemCount));
                DLog.d(tag, "Focus On: " + String.valueOf(currentFirstVisibleItem + currentVisibleItemCount));
                listView.setSelection(currentFirstVisibleItem + currentVisibleItemCount);
                adapter.notifyDataSetChanged();
                footer.setVisibility(View.GONE);
            }

            @Override
            protected void onNetworkError() {
                DLog.d(tag, "onNetworkError");
                swipeLayout.setRefreshing(false);
                footer.setVisibility(View.GONE);
                // Here i now that some error occur when processing the request,
                // possible my internet connection if turned off
            }


            protected void onPreExecute() {
                footer.setVisibility(View.VISIBLE);
            }

        }.execute();
    }

    private void executeSearchMoviesRequest(String searchString) {
        new SearchMoviesAsyncTask(getActivity(), searchString, 1){

            @Override
            protected void onResponseSuccess(MoviesResponse moviesResponse) {
                DLog.d(tag, "onResponseSuccess " + moviesResponse);
                swipeLayout.setRefreshing(false);

                long currentTime = System.currentTimeMillis();
                SharedPreferencesHelper.savePreference(PreferenceIds.LAST_REFRESH_TIMESTAMP, Long.toString(currentTime));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String dateAsString = dateFormat.format(new Date(currentTime));

                Snackbar.make(rootView, Long.toString(currentTime), Snackbar.LENGTH_LONG).show();

                movies = moviesResponse.getMovies();
                currentPage = moviesResponse.getPage();
                totalPages = moviesResponse.getTotalPages();

                if (moviesResponse.getPage() == FIRST_PAGE) {
                    adapter = new MoviesListAdapter(getActivity(), movies);
                    List<MovieDbEntity> books = MovieDbEntity.listAll(MovieDbEntity.class);
                    MovieDbEntity.deleteAll(MovieDbEntity.class);
                } else {
                    adapter.addAll(movies);
                }

                for (Movie movie : movies) {
                    MovieDbEntity movieDbEntity = new MovieDbEntity(movie.getTitle(),
                            movie.getOverview(),
                            movie.getReleaseDate(),
                            TextUtils.join(", ", movie.getMovieGenres()),
                            movie.getPopularity(),
                            movie.getVoteAverage()
                    );
                    movieDbEntity.save(); // Save to database
                }

                List<MovieDbEntity> books = MovieDbEntity.listAll(MovieDbEntity.class);
                DLog.d(tag, "Total de Registos: " + books.size());

                listView.setAdapter(adapter);
                DLog.d(tag, "currentFirstVisibleItem: " + String.valueOf(currentFirstVisibleItem));
                DLog.d(tag, "currentVisibleItemCount: " + String.valueOf(currentVisibleItemCount));
                DLog.d(tag, "Focus On: " + String.valueOf(currentFirstVisibleItem + currentVisibleItemCount));
                listView.setSelection(currentFirstVisibleItem + currentVisibleItemCount);
                adapter.notifyDataSetChanged();
                footer.setVisibility(View.GONE);
            }

            @Override
            protected void onNetworkError() {
                DLog.d(tag, "onNetworkError");
                swipeLayout.setRefreshing(false);
                footer.setVisibility(View.GONE);
                // Here i now that some error occur when processing the request,
                // possible my internet connection if turned off
            }

            protected void onPreExecute() {
                footer.setVisibility(View.VISIBLE);
            }

        }.execute();
    }

    private boolean checkNetwork() {
        if (!NetworkHelper.networkIsAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Network unavailable (check your connection)", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    //endregion

}