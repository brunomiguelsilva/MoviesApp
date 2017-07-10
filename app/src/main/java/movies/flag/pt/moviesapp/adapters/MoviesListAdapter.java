package movies.flag.pt.moviesapp.adapters;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.http.entities.Movie;
import movies.flag.pt.moviesapp.ui.fragments.MoviesScreenFragmentDetail;
import movies.flag.pt.moviesapp.ui.screens.MainScreen;

/**
 * Created by Silva on 21/06/2017.
 */

public class MoviesListAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MoviesListAdapter.class.getSimpleName();

    public MoviesListAdapter(Context context, List<Movie> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.d(TAG, String.format("Position = %d convertView = %s", position,
                convertView == null ? "null" : convertView.toString()));

        final Movie movie = getItem(position);
        final View rootView;
        ViewHolder holder;

        if(convertView == null){

            rootView = LayoutInflater.from(getContext()).inflate(R.layout.movies_screen_fragment_item, null);
            holder = new ViewHolder();
            holder.rootView = rootView;
            holder.nameTextView = (TextView) rootView.findViewById(R.id.movies_screen_fragment_item_name);
            holder.rowImageView = (ImageView) rootView.findViewById(R.id.movies_screen_fragment_item_image);
            rootView.setTag(holder);
        }
        else{
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }

        holder.nameTextView.setText(String.valueOf(movie.getTitle()));

        int screenWidth = holder.rootView.getResources().getDisplayMetrics().widthPixels;
        Picasso.with(holder.rowImageView.getContext())
                .load(movie.getPosterUrl(screenWidth))
                .placeholder(R.drawable.ic_local_movies_white_36dp)
                .error(R.drawable.ic_local_movies_white_36dp)
                .into(holder.rowImageView);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainScreen mainScreen = (MainScreen) getContext();
                Fragment fragment;
                fragment = MoviesScreenFragmentDetail.newInstance(movie);
                mainScreen.replaceFragment(fragment);
                Toast.makeText(v.getContext(), R.string.toast_clicked, Toast.LENGTH_SHORT).show();
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainScreen mainScreen = (MainScreen) getContext();
                mainScreen.navigateToDetailImageFullScreen(movie.getPosterPath());
                Toast.makeText(v.getContext(), R.string.toast_long_clicked, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return rootView;
    }

    private class ViewHolder{
        public View rootView;
        public ImageView rowImageView;
        public TextView nameTextView;
    }
}