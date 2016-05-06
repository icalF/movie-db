package org.informatika.icalf.moviedatabase.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by icalF on 5/5/2016.
 */
public class MovieContract {
  public static final String CONTENT_AUTHORITY = "org.informatika.icalf.moviedatabase";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_TRAILER = "trailer";
  public static final String PATH_REVIEW = "review";
  public static final String PATH_MOVIE = "movie";

  public static String getPosterURL(String path) {
    return "http://image.tmdb.org/t/p/w185" + path;
  }

  public static long getIdFromUri(Uri uri) {
    return Long.parseLong(uri.getPathSegments().get(1));
  }

  public static String getYear(String s) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = new GregorianCalendar();
    try {
      calendar.setTime(df.parse(s));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return Integer.toString(calendar.get(Calendar.YEAR));
  }

  public static String getMethodFromUri(Uri uri) {
    return uri.getPathSegments().get(1);
  }

  public static String getExtraFromUri(Uri uri) {
    return uri.getPathSegments().get(2);
  }

  public enum MovieMethod {
    popular,
    top_rated
  }
  public enum MovieExtra {
    reviews,
    trailers
  }

  public static final class ReviewEntry implements BaseColumns {
    public static final String TABLE_NAME = "review";

    // Columns
    public static final String COLUMMN_MOV_ID = "id_movie";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_CONTENT = "content";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

    public static Uri buildReviewUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }

  public static final class TrailerEntry implements BaseColumns {
    public static final String TABLE_NAME = "trailer";

    // Columns
    public static final String COLUMMN_MOV_ID = "id_movie";
    public static final String COLUMN_URL = "url";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

    public static Uri buildTrailerUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }

  public static final class MovieEntry implements BaseColumns {
    public static final String TABLE_NAME = "movie";

    // Columns
    public static final String COLUMMN_POSTER_URL = "poster_url";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "released_date";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_VOTE = "vote";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_RUNTIME = "runtime";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    public static Uri buildMovieUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static Uri buildMoviePopulars() {
      return CONTENT_URI.buildUpon().appendPath(MovieMethod.values()[0].toString()).build();
    }

    public static Uri buildMovieTopRated() {
      return CONTENT_URI.buildUpon().appendPath(MovieMethod.values()[1].toString()).build();
    }

    public static Uri buildMovieReviews(long id) {
      return CONTENT_URI.buildUpon()
              .appendPath(Long.toString(id))
              .appendPath(MovieExtra.values()[0].toString())
              .build();
    }

    public static Uri buildMovieTrailers(long id) {
      return CONTENT_URI.buildUpon()
              .appendPath(Long.toString(id))
              .appendPath(MovieExtra.values()[1].toString())
              .build();
    }
  }
}
