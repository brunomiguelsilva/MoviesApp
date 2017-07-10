package movies.flag.pt.moviesapp.http.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.BACKDROP_SIZE_W300;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.BACKDROP_SIZE_W780;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.BASE_IMG_URL;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.POSTER_SIZE_W185;
import static movies.flag.pt.moviesapp.constants.TheMovieDbApi.POSTER_SIZE_W342;

/**
 * Created by Silva on 26/06/2017.
 */

public class TvShow implements Parcelable {
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPosterUrl() {
        return BASE_IMG_URL + POSTER_SIZE_W185 + posterPath;
    }

    public String getPosterUrl(int screenWidth) {
        if (screenWidth >= 1024) {
            return BASE_IMG_URL + POSTER_SIZE_W342 + posterPath;
        }
        return BASE_IMG_URL + POSTER_SIZE_W185 + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public List<String> getTvShowGenres() {
        List<String> genres = new ArrayList<>();
        List<Integer> ids = getGenreIds();
        for (int i=0; i<ids.size(); i++) {
            Genre genre = Genre.getById(ids.get(i));
            if (genre != null) {
                genres.add(genre.getTitle());
            }
        }
        return genres;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getBackdropUrl() {
        return BASE_IMG_URL + BACKDROP_SIZE_W300 + backdropPath;
    }

    public String getBackdropUrl(int screenWidth) {
        if (screenWidth >= 1024) {
            return BASE_IMG_URL + BACKDROP_SIZE_W780 + backdropPath;
        }
        return BASE_IMG_URL + BACKDROP_SIZE_W300 + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalName);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.posterPath);
        dest.writeString(this.firstAirDate);
        dest.writeValue(this.popularity);
        dest.writeList(this.genreIds);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.backdropPath);
        dest.writeString(this.overview);
        dest.writeStringList(this.originCountry);
    }

    public TvShow() {
    }

    protected TvShow(Parcel in) {
        this.originalName = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.firstAirDate = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.originalLanguage = in.readString();
        this.backdropPath = in.readString();
        this.overview = in.readString();
        this.originCountry = in.createStringArrayList();
    }

    public static final Parcelable.Creator<TvShow> CREATOR = new Parcelable.Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
