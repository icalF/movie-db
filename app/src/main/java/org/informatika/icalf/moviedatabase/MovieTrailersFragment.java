package org.informatika.icalf.moviedatabase;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/6/2016.
 */
public class MovieTrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  private CursorLoader cursorLoader;
  private int id;

  static final int COL_URL = 0;
  private TrailerAdapter trailerAdapter;

  public MovieTrailersFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_movie_trailers, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    ListView lv = (ListView) getActivity().findViewById(R.id.trailer);
    trailerAdapter = new TrailerAdapter(getActivity(), null, 0);
    if (lv != null) {
      lv.setAdapter(trailerAdapter);
    }

    trailerAdapter.notifyDataSetChanged();
    getActivity().getSupportLoaderManager().initLoader(1, null, this);
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getActivity().getIntent();
    id = intent.getIntExtra("film", 0);
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
    trailerAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    trailerAdapter.swapCursor(null);
  }
}
