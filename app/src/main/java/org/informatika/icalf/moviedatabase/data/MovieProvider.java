package org.informatika.icalf.moviedatabase.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by icalF on 5/5/2016.
 */
public class MovieProvider extends ContentProvider {
  private static final UriMatcher sUriMatcher = buildUriMatcher();

  private MovieDbHelper mOpenHelper;

  static final int MOVIE = 100;
  static final int MOVIE_ID = 101;

  public MovieProvider() { super(); }

  static final int MOVIE_REVIEW = 110;
  static final int MOVIE_TRAILER = 111;
  static final int POPULAR_MOVIE = 120;
  static final int TOP_RATED_MOVIE = 121;

  static final int TRAILER = 200;
  static final int TRAILER_ID = 201;

  static final int REVIEW = 300;
  static final int REVIEW_ID = 301;

  @Override
  public boolean onCreate() { mOpenHelper = new MovieDbHelper(getContext()); return true; }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);

    int returnCount = 0;
    switch (match) {
      case MOVIE:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

      case REVIEW:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

      case TRAILER:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              returnCount++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

      default:
        return super.bulkInsert(uri, values);
    }
  }

  private static UriMatcher buildUriMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    final String authority = MovieContract.CONTENT_AUTHORITY;

    matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);
    matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
    matcher.addURI(authority, MovieContract.PATH_MOVIE + "/popular", POPULAR_MOVIE);
    matcher.addURI(authority, MovieContract.PATH_MOVIE + "/top_rated", TOP_RATED_MOVIE);
    matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/reviews", MOVIE_REVIEW);
    matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/trailers", MOVIE_TRAILER);

    matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
    matcher.addURI(authority, MovieContract.PATH_TRAILER + "/#", TRAILER_ID);

    matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
    matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_ID);

    // 3) Return the new matcher!
    return matcher;
  }

  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor retCursor;
    switch (sUriMatcher.match(uri)) {
      // "/movie/popular"
      case POPULAR_MOVIE: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC"
        );
        break;
      }

      // "/movie/top_rated"
      case TOP_RATED_MOVIE: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_VOTE + " DESC"
        );
        break;
      }

      // "/movie/#/reviews"
      case MOVIE_REVIEW: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.ReviewEntry.TABLE_NAME,
                projection,
                MovieContract.ReviewEntry.COLUMMN_MOV_ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))},
                null,
                null,
                sortOrder
        );
        break;
      }

      // "/movie/#/trailers"
      case MOVIE_TRAILER: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.TrailerEntry.TABLE_NAME,
                projection,
                MovieContract.TrailerEntry.COLUMMN_MOV_ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))},
                null,
                null,
                sortOrder
        );
        break;
      }

      // "/movie/*"
      case MOVIE_ID: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                MovieContract.MovieEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))},
                null,
                null,
                sortOrder
        );
        break;
      }

      // "/review/*"
      case REVIEW: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.ReviewEntry.TABLE_NAME,
                projection,
                MovieContract.ReviewEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))},
                null,
                null,
                sortOrder
        );
        break;
      }

      // "/trailer/*"
      case TRAILER: {
        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.TrailerEntry.TABLE_NAME,
                projection,
                MovieContract.TrailerEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))},
                null,
                null,
                sortOrder
        );
        break;
      }

      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    retCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return retCursor;
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);

    switch (match) {
      case MOVIE_REVIEW:
      case REVIEW:
        return MovieContract.ReviewEntry.CONTENT_TYPE;

      case MOVIE_TRAILER:
      case TRAILER:
        return MovieContract.TrailerEntry.CONTENT_TYPE;

      case POPULAR_MOVIE:
      case TOP_RATED_MOVIE:
      case MOVIE:
      case MOVIE_ID:
        return MovieContract.MovieEntry.CONTENT_TYPE;

      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Nullable
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    Uri returnUri;

    switch (match) {
      case MOVIE: {
        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if (_id > 0)
          returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
        else
          throw new android.database.SQLException("Failed to insert row into " + uri);
        break;
      }
      case TRAILER: {
        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
        if (_id > 0)
          returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
        else
          throw new android.database.SQLException("Failed to insert row into " + uri);
        break;
      }
      case REVIEW: {
        long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
        if (_id > 0)
          returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
        else
          throw new android.database.SQLException("Failed to insert row into " + uri);
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
//    db.close();
    return returnUri;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

    final int match = sUriMatcher.match(uri);
    int rowsDeleted;

    if (null == selection) selection = "1";
    switch (match) {
      case MOVIE_ID: {
        rowsDeleted = db.delete(
                MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))});
        break;
      }
      case TRAILER_ID: {
        rowsDeleted = db.delete(
                MovieContract.TrailerEntry.TABLE_NAME,
                MovieContract.TrailerEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))});
        break;
      }
      case REVIEW_ID: {
        rowsDeleted = db.delete(
                MovieContract.ReviewEntry.TABLE_NAME,
                MovieContract.ReviewEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))});
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (rowsDeleted != 0)
      getContext().getContentResolver().notifyChange(uri, null);
//    db.close();

    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

    final int match = sUriMatcher.match(uri);
    int rowsUpdated = 0;

    switch (match) {
      case MOVIE_ID: {
        rowsUpdated = db.update(
                MovieContract.MovieEntry.TABLE_NAME,
                values,
                MovieContract.MovieEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))});
        break;
      }
      case TRAILER_ID: {
        rowsUpdated = db.update(
                MovieContract.TrailerEntry.TABLE_NAME,
                values,
                MovieContract.TrailerEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))});
        break;
      }
      case REVIEW_ID: {
        rowsUpdated = db.update(
                MovieContract.ReviewEntry.TABLE_NAME,
                values,
                MovieContract.ReviewEntry._ID + " = ? ",
                new String[]{Long.toString(MovieContract.getIdFromUri(uri))});
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (rowsUpdated != 0)
      getContext().getContentResolver().notifyChange(uri, null);
//    db.close();

    return rowsUpdated;
  }
}