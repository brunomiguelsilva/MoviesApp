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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.adapters.TvShowsListAdapter;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.database.entities.TvShowDbEntity;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.http.entities.TvShow;
import movies.flag.pt.moviesapp.http.entities.TvShowsResponse;
import movies.flag.pt.moviesapp.http.requests.GetPopularTvShowsAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

/**
 * Created by Silva on 26/06/2017.
 */

public class TvShowsScreenFragment extends BaseScreenFragment {

    //region: Fields Declaration
    private static final String KEY_LIST = "List";
    private static final String KEY_PAGE_SELECTION = "Page_Selection";
    private static final String KEY_LIST_SELECTION = "Page_Selection";
    private static final int FIRST_PAGE = 1;


    private RelativeLayout rootView;
    private ListView listView;
    private int currentFirstVisibleItem, currentVisibleItemCount,totalItem;
    private SwipeRefreshLayout swipeLayout;
    private RelativeLayout footer;
    private TvShowsListAdapter adapter;
    private List<TvShow> tvShows;

    int currentPage = FIRST_PAGE;
    int totalPages = FIRST_PAGE;
    //endregion

    public static TvShowsScreenFragment newInstance() {
        TvShowsScreenFragment fragment = new TvShowsScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //region: Override Methods.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tv_shows_screen_fragment, container, false);

        findViews(view);
        addListeners(view);

        if(savedInstanceState != null)
        {
            DLog.d(tag, "Get TvShows from savedInstanceState");
            tvShows = savedInstanceState.getParcelableArrayList(KEY_LIST);
            adapter = new TvShowsListAdapter(getActivity(), tvShows);
            listView.setAdapter(adapter);
            currentPage = savedInstanceState.getInt(KEY_PAGE_SELECTION);
            int selection = savedInstanceState.getInt(KEY_LIST_SELECTION);
            listView.setSelection(selection);
            adapter.notifyDataSetChanged();
            footer.setVisibility(View.GONE);

        }
        else {
            adapter = new TvShowsListAdapter(getActivity(), tvShows);
            executeGetPopularTvShowsRequest(FIRST_PAGE);
        }

        //checkNetwork();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            ArrayList<TvShow> arrayTvShow = new ArrayList<>(tvShows);
            outState.putParcelableArrayList(KEY_LIST, arrayTvShow);
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
        executeGetPopularTvShowsRequest(FIRST_PAGE);
    }
    //endregion

    //region: Private Methods.
    private void findViews(View view){
        rootView = (RelativeLayout)view.findViewById(R.id.tv_shows_screen_fragment_root_view);
        listView = (ListView)view.findViewById(R.id.tv_shows_screen_fragment_list_view);
        footer = (RelativeLayout)view.findViewById(R.id.tv_shows_screen_fragment_list_item_footer);
        footer.setVisibility(View.GONE);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.tv_shows_screen_fragment_swipe_container);
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
                executeGetPopularTvShowsRequest(FIRST_PAGE);
            }
        });
    }

    private void loadNextPage(int page) {
        DLog.d(tag, "Load page: " + page);
        executeGetPopularTvShowsRequest(page);
    }

    private void executeGetPopularTvShowsRequest(int page) {
        new GetPopularTvShowsAsyncTask(getActivity(), page){

            @Override
            protected void onResponseSuccess(TvShowsResponse tvShowsScreenResponse) {
                DLog.d("TvShowsScreenFragment", "onResponseSuccess " + tvShowsScreenResponse);
                swipeLayout.setRefreshing(false);

                long currentTime = System.currentTimeMillis();
                SharedPreferencesHelper.savePreference(PreferenceIds.LAST_REFRESH_TIMESTAMP, Long.toString(currentTime));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String dateAsString = dateFormat.format(new Date(currentTime));

                Snackbar.make(rootView, Long.toString(currentTime), Snackbar.LENGTH_LONG).show();

                tvShows = tvShowsScreenResponse.getResults();
                currentPage = tvShowsScreenResponse.getPage();
                totalPages = tvShowsScreenResponse.getTotalPages();

                if (tvShowsScreenResponse.getPage() == FIRST_PAGE) {
                    adapter = new TvShowsListAdapter(getActivity(), tvShows);
                    List<TvShowDbEntity> list = TvShowDbEntity.listAll(TvShowDbEntity.class);
                    TvShowDbEntity.deleteAll(TvShowDbEntity.class);
                } else {
                    adapter.addAll(tvShows);
                }

                for (TvShow tvShow : tvShows) {
                    TvShowDbEntity tvShowDbEntity = new TvShowDbEntity(tvShow.getName(),
                            tvShow.getOverview(),
                            tvShow.getFirstAirDate(),
                            TextUtils.join(", ", tvShow.getTvShowGenres()),
                            tvShow.getPopularity(),
                            tvShow.getVoteAverage()
                    );
                    tvShowDbEntity.save(); // Save to database
                }

                List<TvShowDbEntity> list = TvShowDbEntity.listAll(TvShowDbEntity.class);
                DLog.d(tag, "Total de Registos: " + list.size());

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
                DLog.d("TvShowsScreenFragment", "onNetworkError ");
                swipeLayout.setRefreshing(false);

                List<TvShowDbEntity> list = TvShowDbEntity.listAll(TvShowDbEntity.class);
                DLog.d(tag, "Total de Registos: " + list.size());

                tvShows = new ArrayList<>();

                for (TvShowDbEntity tvShowEntity : list) {
                    TvShow tvShow = new TvShow();
                    tvShow.setName(tvShowEntity.getTitle());
                    tvShow.setOverview(tvShowEntity.getOverview());
                    tvShow.setFirstAirDate(tvShowEntity.getRelease_date());
                    tvShow.setPopularity(tvShowEntity.getPopularity());
                    tvShow.setVoteAverage(tvShowEntity.getVote_average());
                    tvShows.add(tvShow);
                }
                adapter = new TvShowsListAdapter(getActivity(), tvShows);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                footer.setVisibility(View.GONE);
            }
        }.execute();
    }
    //endregion
}
