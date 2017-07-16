package movies.flag.pt.moviesapp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.http.entities.Movie;
import movies.flag.pt.moviesapp.ui.screens.DetailImageFullScreen;

import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.BASE_IMG_URL;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.POSTER_SIZE_W500;

/**
 * Created by Silva on 21/06/2017.
 */

public class MoviesScreenFragmentDetail extends BaseScreenFragment {

    //region: Fields Declaration
    private static final String MOVIE_EXTRA = "MoviesScreenFragmentDetail:MovieExtra";
    private Movie movie;
    private TextView textView;
    private ImageView movieDetailBackdrop;
    private ImageView movieDetailPoster;
    private ImageView movieStar;
    private TextView movieDetailRating;
    private TextView movieGenres;
    private TextView movieDetailReleaseLabel;
    private TextView movieDetailRelease;
    private TextView movieDetailSynopsis;
    private FloatingActionButton mFavoriteFab;
    //endregion

    public static MoviesScreenFragmentDetail newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_EXTRA, movie);
        MoviesScreenFragmentDetail fragment = new MoviesScreenFragmentDetail();
        fragment.setArguments(args);
        return fragment;
    }

    //region: Override Methods.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null) {
            movie = args.getParcelable(MOVIE_EXTRA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_screen_fragment_detail, container, false);

        findViews(view);
        addListeners();

        return view;
    }

    @Override
    protected void refreshData() {
    }
    //endregion

    //region: Private Methods.
    private void findViews(View view) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        movieDetailBackdrop = (ImageView) view.findViewById(R.id.movies_screen_fragment_detail_backdrop);
        Picasso.with(movieDetailBackdrop.getContext())
                .load(movie.getBackdropUrl(screenWidth))
                .placeholder(R.drawable.ic_local_movies_white_36dp)
                .error(R.drawable.ic_local_movies_white_36dp)
                .into(movieDetailBackdrop);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.movies_screen_fragment_detail_collapsing_toolbar);
        collapsingToolbar.setTitle(movie.getTitle());

        movieDetailPoster = (ImageView) view.findViewById(R.id.movies_screen_fragment_detail_poster);
        Picasso.with(movieDetailPoster.getContext())
                .load(movie.getPosterUrl(screenWidth))
                .placeholder(R.drawable.ic_local_movies_white_36dp)
                .error(R.drawable.ic_local_movies_white_36dp)
                .into(movieDetailPoster);

        textView = (TextView)view.findViewById(R.id.movies_screen_fragment_detail_title_tv);
        textView.setText(movie.getTitle());

        movieDetailRating = (TextView)view.findViewById(R.id.movies_screen_fragment_detail_rating);
        movieDetailRating.setText(String.valueOf(new DecimalFormat("#.#").format(movie.getVoteAverage()) + " /10"));

        //getAdult

        //getVoteCount

        //getVideo

        movieGenres = (TextView)view.findViewById(R.id.movies_screen_fragment_detail_genres);
        movieGenres.setText(TextUtils.join(", ", movie.getMovieGenres()));

        movieDetailReleaseLabel = (TextView)view.findViewById(R.id.movies_screen_fragment_detail_release_label_tv);
        movieDetailReleaseLabel.setText(getString(R.string.label_release_date));

        movieDetailRelease = (TextView)view.findViewById(R.id.movies_screen_fragment_detail_release);
        movieDetailRelease.setText(movie.getReleaseDate());

        movieDetailSynopsis = (TextView)view.findViewById(R.id.movies_screen_fragment_detail_synopsis);
        movieDetailSynopsis.setText(movie.getOverview());

        mFavoriteFab = (FloatingActionButton) view.findViewById(R.id.movies_screen_fragment_detail_favorite);
    }

    private void addListeners() {
        movieDetailPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                String posterPath = BASE_IMG_URL + POSTER_SIZE_W500 + movie.getPosterPath();
                extras.putString(getResources().getString(R.string.cover_image), posterPath);
                Intent intent = new Intent(getActivity(), DetailImageFullScreen.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        mFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideoUrl();
            }
        });
    }

    private void shareVideoUrl(){
        Uri url = Uri.parse("www.youtube.com");
        if (url != null) {
            Intent shareIntent = new Intent();
            shareIntent.setType("text/plain");
            shareIntent.setAction(Intent.ACTION_SEND);
            StringBuilder sb = new StringBuilder();
            sb.append(getResources().getString(R.string.share_tittle) + textView.getText() + "\n");
            sb.append(getResources().getString(R.string.share_genre) + movieGenres.getText() + "\n");
            //sb.append(url.toString());
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_action_subject_prefix) + movie.getTitle());
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.title_share_action)));
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.message_no_trailer) + movie.getTitle(), Toast.LENGTH_LONG).show();
        }
    }
    //endregion
}
