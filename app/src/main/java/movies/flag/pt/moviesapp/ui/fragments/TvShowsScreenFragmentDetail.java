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
import movies.flag.pt.moviesapp.http.entities.TvShow;
import movies.flag.pt.moviesapp.ui.screens.DetailImageFullScreen;

import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.BASE_IMG_URL;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.POSTER_SIZE_W500;

/**
 * Created by Silva on 21/06/2017.
 */

public class TvShowsScreenFragmentDetail extends BaseScreenFragment {

    //region: Fields Declaration
    private static final String TV_SHOW_EXTRA = "TvShowsScreenFragmentDetail:TvShowExtra";
    private TvShow tvShow;
    private TextView textView;
    private ImageView tvShowDetailBackdrop;
    private ImageView tvShowDetailPoster;
    private ImageView tvShowStar;
    private TextView tvShowDetailRating;
    private TextView tvShowGenres;
    private TextView tvShowDetailReleaseLabel;
    private TextView tvShowDetailRelease;
    private TextView tvShowDetailSynopsis;
    private FloatingActionButton mFavoriteFab;
    //endregion

    public static TvShowsScreenFragmentDetail newInstance(TvShow tvShow) {
        Bundle args = new Bundle();
        args.putParcelable(TV_SHOW_EXTRA, tvShow);
        TvShowsScreenFragmentDetail fragment = new TvShowsScreenFragmentDetail();
        fragment.setArguments(args);
        return fragment;
    }

    //region: Override Methods.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null) {
            tvShow = args.getParcelable(TV_SHOW_EXTRA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tv_shows_screen_fragment_detail, container, false);

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

        tvShowDetailBackdrop = (ImageView) view.findViewById(R.id.tv_shows_screen_fragment_detail_backdrop);
        Picasso.with(tvShowDetailBackdrop.getContext())
                .load(tvShow.getBackdropUrl(screenWidth))
                .placeholder(R.drawable.ic_local_movies_white_36dp)
                .error(R.drawable.ic_local_movies_white_36dp)
                .into(tvShowDetailBackdrop);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.tv_shows_screen_fragment_detail_collapsing_toolbar);
        collapsingToolbar.setTitle(tvShow.getName());

        tvShowDetailPoster = (ImageView) view.findViewById(R.id.tv_shows_screen_fragment_detail_poster);
        Picasso.with(tvShowDetailPoster.getContext())
                .load(tvShow.getPosterUrl(screenWidth))
                .placeholder(R.drawable.ic_local_movies_white_36dp)
                .error(R.drawable.ic_local_movies_white_36dp)
                .into(tvShowDetailPoster);

        textView = (TextView)view.findViewById(R.id.tv_shows_screen_fragment_detail_title_tv);
        textView.setText(tvShow.getName());

        tvShowDetailRating = (TextView)view.findViewById(R.id.tv_shows_screen_fragment_detail_rating);
        tvShowDetailRating.setText(String.valueOf(new DecimalFormat("#.#").format(tvShow.getVoteAverage()) + " /10"));

        //getAdult

        //getVoteCount

        //getVideo

        tvShowGenres = (TextView)view.findViewById(R.id.movie_genres);
        tvShowGenres.setText(TextUtils.join(", ", tvShow.getTvShowGenres()));

        tvShowDetailReleaseLabel = (TextView)view.findViewById(R.id.tv_shows_screen_fragment_detail_release_label_tv);
        tvShowDetailReleaseLabel.setText(getString(R.string.label_release_date));

        tvShowDetailRelease = (TextView)view.findViewById(R.id.movie_detail_release);
        tvShowDetailRelease.setText(tvShow.getFirstAirDate());

        tvShowDetailSynopsis = (TextView)view.findViewById(R.id.tv_shows_screen_fragment_detail_synopsis);
        tvShowDetailSynopsis.setText(tvShow.getOverview());

        mFavoriteFab = (FloatingActionButton) view.findViewById(R.id.tv_shows_screen_fragment_detail_favorite);
    }

    private void addListeners() {
        tvShowDetailPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                String posterPath = BASE_IMG_URL + POSTER_SIZE_W500 + tvShow.getPosterPath();
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
            sb.append("Titulo: " + textView.getText() + "\n");
            sb.append("Género: " + tvShowGenres.getText() + "\n");
            //sb.append(url.toString());
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_action_subject_prefix) + tvShow.getName());
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.title_share_action)));
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.message_no_trailer) + tvShow.getName(), Toast.LENGTH_LONG).show();
        }
    }
    //endregion
}
