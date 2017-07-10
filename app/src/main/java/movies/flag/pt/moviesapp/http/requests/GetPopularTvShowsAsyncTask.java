package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.http.entities.TvShowsResponse;

/**
 * Created by Silva on 26/06/2017.
 */

public abstract class GetPopularTvShowsAsyncTask extends ExecuteRequestAsyncTask<TvShowsResponse> {

    private static final String PATH = "/tv/popular";
    private static final String LANGUAGE_KEY = "language";
    private static final String PAGE_NUMBER = "page";
    private int pageNumber;

    public GetPopularTvShowsAsyncTask(Context context, int pageNumber) {
        super(context);
        this.pageNumber = pageNumber;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected void addQueryParams(StringBuilder sb) {
        addQueryParam(sb, LANGUAGE_KEY, context.getString(R.string.language));
        addQueryParam(sb, PAGE_NUMBER, String.valueOf(pageNumber));
    }

    @Override
    protected Class<TvShowsResponse> getResponseEntityClass() {
        return TvShowsResponse.class;
    }
}