package org.informatika.icalf.moviedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Base64;

import org.informatika.icalf.moviedatabase.data.MovieContract;

import java.io.UnsupportedEncodingException;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbReviews;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by icalF on 5/5/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {
  private String key;
  private final String LANG = "en";
  private final Context mContext;

  public FetchMovieTask(Context context) {
    mContext = context;

    try {
      byte[] b64 = Base64.decode(BuildConfig.TMDB_API_KEY, Base64.DEFAULT);
      key = new String(b64, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected Void doInBackground(String... params) {
    TmdbMovies tmdbMovies = new TmdbApi(key).getMovies();
    for (int page = 1; page < 100; ++page) {
      MovieResultsPage movieResultsPage = tmdbMovies.getPopularMovies(LANG, page);
      List<MovieDb> list = movieResultsPage.getResults();

      updateMovies(list);
    }
    return null;
  }

  private void updateMovies(List<MovieDb> list) {
    for (MovieDb movie : list) {
      // Check whether such movie existed or not
      Cursor movieCursor = mContext.getContentResolver().query(
              MovieContract.MovieEntry.buildMovieUri(movie.getId()),
              null,
              null,
              null,
              null);

      ContentValues movieValues = new ContentValues();
      TmdbMovies tmdbMovies = new TmdbApi(key).getMovies();
      movie = tmdbMovies.getMovie(movie.getId(), LANG, TmdbMovies.MovieMethod.videos);

      movieValues.put(MovieContract.MovieEntry._ID, movie.getId());
      movieValues.put(MovieContract.MovieEntry.COLUMMN_POSTER_URL, movie.getPosterPath());
      movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
      movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
      movieValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, movie.getRuntime());
      movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
      movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE, movie.getVoteAverage());
      movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

      // If movie exist, update content
      if (movieCursor != null && movieCursor.moveToFirst()) {
        mContext.getContentResolver().update(
                MovieContract.MovieEntry.buildMovieUri(movie.getId()),
                movieValues,
                null,
                null
        );

        movieCursor.close();
      }
      else  // insert movie
      {
        mContext.getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI,
                movieValues
        );

        updateReviews(movie.getId());
        updateTrailers(movie.getId(), movie.getVideos());
      }

    }
  }

  private void updateTrailers(int movId, List<Video> videos) {
    if (videos != null) {
      for (Video video : videos) {
        // Check whether such trailer existed or not
        Cursor trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailerUri(video.getKey()),
                null,
                null,
                null,
                null);

        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MovieContract.TrailerEntry._ID, video.getKey());
        trailerValues.put(MovieContract.TrailerEntry.COLUMMN_MOV_ID, movId);

        // If trailer exist, update content
        if (trailerCursor != null && trailerCursor.moveToFirst()) {
          mContext.getContentResolver().update(
                  MovieContract.TrailerEntry.buildTrailerUri(video.getKey()),
                  trailerValues,
                  null,
                  null
          );

          trailerCursor.close();
        }
        else  // insert trailer
        {
          mContext.getContentResolver().insert(
                  MovieContract.TrailerEntry.CONTENT_URI,
                  trailerValues
          );
        }
      }
    }
  }

  private void updateReviews(int id) {
    TmdbReviews tmdbReviews = new TmdbApi(key).getReviews();
    TmdbReviews.ReviewResultsPage reviewResultsPage = tmdbReviews.getReviews(id, LANG, 1);
    List<Reviews> reviews = reviewResultsPage.getResults();

    if (reviews != null) {
      for (Reviews review : reviews) {
        // Check whether such review existed or not
        Cursor trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailerUri(review.getId()),
                null,
                null,
                null,
                null);

        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MovieContract.ReviewEntry._ID, review.getId());
        reviewValues.put(MovieContract.ReviewEntry.COLUMMN_MOV_ID, id);
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, review.getContent());

        // If review exist, update content
        if (trailerCursor != null && trailerCursor.moveToFirst()) {
          mContext.getContentResolver().update(
                  MovieContract.ReviewEntry.buildReviewUri(review.getId()),
                  reviewValues,
                  null,
                  null
          );

          trailerCursor.close();
        }
        else  // review movie
        {
          mContext.getContentResolver().insert(
                  MovieContract.ReviewEntry.CONTENT_URI,
                  reviewValues
          );
        }
      }
    }
  }
}