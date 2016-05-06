package org.informatika.icalf.moviedatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/5/2016.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private MovieAdapter movieAdapter;
  private CursorLoader cursor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
//    deleteDatabase("tmdb.db");
  }

  @Override
  protected void onStart() {
    super.onStart();
    GridView gv = (GridView) findViewById(R.id.movie_thumbnails);
    movieAdapter = new MovieAdapter(this, null, 0);
    if (gv != null) {
      gv.setAdapter(movieAdapter);
    }

    movieAdapter.notifyDataSetChanged();
    getSupportLoaderManager().initLoader(0, null, this);

    FetchMovieTask weatherTask = new FetchMovieTask(this);
    weatherTask.execute();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
    cursor = new CursorLoader(this,
            MovieContract.MovieEntry.buildMoviePopulars(),
            null,
            null,
            null,
            sortOrder);
    return cursor;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    movieAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    movieAdapter.swapCursor(null);
  }
}
