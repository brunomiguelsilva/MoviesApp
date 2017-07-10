package movies.flag.pt.moviesapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.adapters.TvShowsListAdapter;
import movies.flag.pt.moviesapp.database.entities.TvShowDbEntity;
import movies.flag.pt.moviesapp.http.entities.TvShow;
import movies.flag.pt.moviesapp.http.entities.TvShowsResponse;
import movies.flag.pt.moviesapp.http.requests.GetPopularTvShowsAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

/**
 * Created by Silva on 26/06/2017.
 */

public class TvShowsScreenFragment extends BaseScreenFragment {

    //region: Fields Declaration
    private ListView listView;
    private SwipeRefreshLayout swipeLayout;
    //endregion

    public static TvShowsScreenFragment newInstance() {
        Bundle args = new Bundle();
        TvShowsScreenFragment fragment = new TvShowsScreenFragment();
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

        executeRequest();

        return view;
    }

    @Override
    protected void refreshData() {
    }
    //endregion

    //region: Private Methods.
    private void findViews(View view){
        listView = (ListView)view.findViewById(R.id.tv_shows_screen_fragment_list_view);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.tv_shows_screen_fragment_swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void addListeners(View view) {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeRequest();
            }
        });
    }

    private void executeRequest() {
        new GetPopularTvShowsAsyncTask(getActivity(), 1){

            @Override
            protected void onResponseSuccess(TvShowsResponse tvShowsScreenFragment) {
                DLog.d("TvShowsScreenFragment", "onResponseSuccess " + tvShowsScreenFragment);
                swipeLayout.setRefreshing(false);
                List<TvShow> tvShows = tvShowsScreenFragment.getResults();

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

                TvShowsListAdapter adapter = new TvShowsListAdapter(getActivity(), tvShows);
                listView.setAdapter(adapter);
                // Here i can create the adapter :)
            }

            @Override
            protected void onNetworkError() {
                DLog.d("MoviesScreenFragment", "onNetworkError ");
                swipeLayout.setRefreshing(false);
                // Here i now that some error occur when processing the request,
                // possible my internet connection if turned off
            }
        }.execute();
    }
    //endregion
}
