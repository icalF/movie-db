package org.informatika.icalf.moviedatabase.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by icalF on 5/5/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  static final String DATABASE_NAME = "tmdb.db";

  public MovieDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

  public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
    super(context, name, factory, version, errorHandler);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
          MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +

          // the ID of the location entry associated with this weather data
          MovieContract.MovieEntry.COLUMMN_POSTER_URL + " TEXT NOT NULL, " +
          MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +

          MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
          MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
          MovieContract.MovieEntry.COLUMN_VOTE + " REAL NOT NULL, " +
          MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
          MovieContract.MovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL)";

    db.execSQL(SQL_CREATE_MOVIE_TABLE);

    final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
          MovieContract.TrailerEntry._ID + " TEXT PRIMARY KEY, " +
          MovieContract.TrailerEntry.COLUMMN_MOV_ID + " INTEGER NOT NULL, " +

          // Set up the location column as a foreign key to movie table.
          "FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMMN_MOV_ID + ") REFERENCES " +
          MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") )";

    db.execSQL(SQL_CREATE_TRAILER_TABLE);

    final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
            MovieContract.ReviewEntry._ID + " TEXT PRIMARY KEY , " +

            MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
            MovieContract.ReviewEntry.COLUMMN_MOV_ID + " INTEGER NOT NULL, " +

            // Set up the location column as a foreign key to movie table.
            "FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMMN_MOV_ID + ") REFERENCES " +
            MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") )";

    db.execSQL(SQL_CREATE_REVIEW_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
    onCreate(db);
  }
}
