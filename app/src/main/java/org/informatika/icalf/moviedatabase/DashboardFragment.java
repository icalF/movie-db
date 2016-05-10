package org.informatika.icalf.moviedatabase;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
  static final int THUMBNAILS_LOADER = 0;

  private MovieAdapter movieAdapter;
  private CursorLoader cursor;

  public interface Callback {
    void onItemSelected(Integer movId);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  public DashboardFragment() {
    // Required empty public constructor
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    getActivity().getSharedPreferences(getString(R.string.sharedpref), Context.MODE_PRIVATE)
            .edit()
            .putInt("mode", id)
            .commit();
    getLoaderManager().restartLoader(THUMBNAILS_LOADER, null, this);

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.method, menu);
    super.onCreateOptionsMenu(menu, inflater);
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
    getLoaderManager().initLoader(THUMBNAILS_LOADER, null, this);
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    int mode = getContext()
            .getSharedPreferences(getString(R.string.sharedpref), Context.MODE_PRIVATE)
            .getInt("mode", 0);
    Uri reqUri = (mode == R.id.popular ?
            MovieContract.MovieEntry.buildMoviePopulars() :
            MovieContract.MovieEntry.buildMovieTopRated());

    cursor = new CursorLoader(getContext(),
            reqUri,
            null,
            null,
            null,
            null);
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
