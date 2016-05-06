package org.informatika.icalf.moviedatabase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/6/2016.
 */
public class MovieInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  private int id;
  private TrailerAdapter trailerAdapter;
  private CursorLoader cursorLoader;

  static final int COL_TITLE = 4;
  static final int COL_YEAR = 3;
  static final int COL_OVERVIEW = 2;
  static final int COL_POSTER = 1;
  static final int COL_VOTE = 5;
  static final int COL_RUTIME = 7;

  public MovieInfoFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getActivity().getIntent();
    id = intent.getIntExtra("film", 0);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_movie_info, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    getLoaderManager().initLoader(0, null, this);
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    cursorLoader = new CursorLoader(getActivity(),
            MovieContract.MovieEntry.buildMovieTrailers(this.id),
            null,
            null,
            null,
            null);
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    Cursor cursor = getActivity().getContentResolver().query(
            MovieContract.MovieEntry.buildMovieUri(id),
            null,
            MovieContract.MovieEntry._ID,
            new String[] {String.valueOf(id)},
            null
    );
    if (cursor != null) {
      cursor.moveToFirst();
    }

    TextView title = (TextView) getActivity().findViewById(R.id.movie_title);
    TextView overview = (TextView) getActivity().findViewById(R.id.overview);
    TextView vote = (TextView) getActivity().findViewById(R.id.vote);
    TextView year = (TextView) getActivity().findViewById(R.id.release_year);
    ImageView poster = (ImageView) getActivity().findViewById(R.id.movie_poster);
    TextView runtime = (TextView) getActivity().findViewById(R.id.runtime);

    Picasso.with(getActivity()).load(MovieContract.getPosterURL(cursor.getString(COL_POSTER))).into(poster);
    if (title != null) {
      title.setText(cursor.getString(COL_TITLE));
    }
    if (overview != null) {
      overview.setText(cursor.getString(COL_OVERVIEW));
    }
    if (vote != null) {
      vote.setText(Float.toString(cursor.getFloat(COL_VOTE)) + " / 10.0");
    }
    if (year != null) {
      year.setText(MovieContract.getYear(cursor.getString(COL_YEAR)));
    }
    if (runtime != null) {
      runtime.setText(Integer.toString(cursor.getInt(COL_RUTIME)) + " min");
    }

    cursor.close();
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) { }
}
