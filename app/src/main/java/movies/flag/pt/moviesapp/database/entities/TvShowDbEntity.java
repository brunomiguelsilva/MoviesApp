package movies.flag.pt.moviesapp.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Silva on 29/06/2017.
 */

public class TvShowDbEntity extends SugarRecord implements Parcelable {

    public static final String TABLE_NAME = "TV_SHOW_DB_ENTITY";
    public static final String TITLE_COLUMN_NAME = "title";
    public static final String OVERVIEW_COLUMN_NAME = "overview";
    public static final String RELEASE_DATE_COLUMN_NAME = "release_date";
    public static final String GENRE_IDS_COLUMN_NAME = "genre_ids";
    public static final String POPULARITY_COLUMN_NAME = "popularity";
    public static final String VOTE_AVERAGE_COLUMN_NAME = "vote_average";

    String title;
    String overview;
    String release_date;
    String genre_ids;
    Double popularity;
    Double vote_average;

    // Default constructor is necessary for SugarRecord
    public TvShowDbEntity(){
    }

    public TvShowDbEntity(String title,
                          String overview,
                          String release_date,
                          String genre_ids,
                          Double popularity,
                          Double vote_average
    ){
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(String genre_ids) {
        this.genre_ids = genre_ids;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.genre_ids);
        dest.writeValue(this.popularity);
        dest.writeValue(this.vote_average);
    }

    protected TvShowDbEntity(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.genre_ids = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<TvShowDbEntity> CREATOR = new Creator<TvShowDbEntity>() {
        @Override
        public TvShowDbEntity createFromParcel(Parcel source) {
            return new TvShowDbEntity(source);
        }

        @Override
        public TvShowDbEntity[] newArray(int size) {
            return new TvShowDbEntity[size];
        }
    };
}
