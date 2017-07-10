package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.http.entities.MoviesResponse;

/**
 * Created by Silva on 07/06/2017.
 */

public abstract class SearchMoviesAsyncTask extends ExecuteRequestAsyncTask<MoviesResponse> {

    private static final String PATH = "/search/movie";
    private static final String LANGUAGE_KEY = "language";
    private static final String QUERY_VALUE = "query";
    private static final String PAGE_NUMBER = "page";
    private String search;
    private int pageNumber;

    public SearchMoviesAsyncTask(Context context, String search, int pageNumber) {
        super(context);
        this.search = search;
        this.pageNumber = pageNumber;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected void addQueryParams(StringBuilder sb) {
        addQueryParam(sb, LANGUAGE_KEY, context.getString(R.string.language));
        addQueryParam(sb, QUERY_VALUE, search);
        addQueryParam(sb, PAGE_NUMBER, String.valueOf(pageNumber));
    }

    @Override
    protected Class<MoviesResponse> getResponseEntityClass() {
        return MoviesResponse.class;
    }
}
