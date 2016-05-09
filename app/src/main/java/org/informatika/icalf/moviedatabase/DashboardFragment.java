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
import android.widget.AdapterView;
import android.widget.GridView;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/6/2016.
 */
public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  static final int COL_ID = 0;

  private MovieAdapter movieAdapter;
  private CursorLoader cursor;

  public interface Callback {
    void onItemSelected(Integer movId);
  }

  public DashboardFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

    GridView gv = (GridView) view.findViewById(R.id.movie_thumbnails);
    movieAdapter = new MovieAdapter(getActivity(), null, 0);
    movieAdapter.notifyDataSetChanged();

    if (gv != null) {
      gv.setAdapter(movieAdapter);
      gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
          Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
          if (cursor != null) {
            ((Callback) getActivity()).onItemSelected(cursor.getInt(COL_ID));
          }
        }
      });
    }

    FetchMovieTask weatherTask = new FetchMovieTask(getActivity());
    weatherTask.execute();

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    getLoaderManager().initLoader(0, null, this);
    super.onActivityCreated(savedInstanceState);
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
