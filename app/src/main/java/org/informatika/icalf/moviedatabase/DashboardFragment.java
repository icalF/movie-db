package org.informatika.icalf.moviedatabase;


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
import android.widget.GridView;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  private MovieAdapter movieAdapter;
  private CursorLoader cursor;

  public DashboardFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_dashboard, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    GridView gv = (GridView) getActivity().findViewById(R.id.movie_thumbnails);
    movieAdapter = new MovieAdapter(getActivity(), null, 0);
    if (gv != null) {
      gv.setAdapter(movieAdapter);
    }

    movieAdapter.notifyDataSetChanged();
    getActivity().getSupportLoaderManager().initLoader(0, null, this);

    FetchMovieTask weatherTask = new FetchMovieTask(getActivity());
    weatherTask.execute();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
    cursor = new CursorLoader(getActivity(),
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
