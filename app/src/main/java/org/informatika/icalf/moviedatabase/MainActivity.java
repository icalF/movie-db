package org.informatika.icalf.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by icalF on 5/5/2016.
 */
public class MainActivity extends ActionBarActivity implements DashboardFragment.Callback {

  private boolean mTwoPane;

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    getSharedPreferences(getString(R.string.sharedpref), Context.MODE_PRIVATE)
            .edit()
            .putInt("mode", id);

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.method, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //      if (savedInstanceState == null) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.movie_detail_container, new MovieInfoFragment())
//                .commit();
//      }
    mTwoPane = findViewById(R.id.movie_detail_container) != null;
  }

  @Override
  public void onItemSelected(Integer movId) {
    Log.d("onItemSelected: ", String.valueOf(mTwoPane));
    if (mTwoPane) {
      Bundle args = new Bundle();
      args.putInt(MovieInfoFragment.MOVIE_ID, movId);

      MovieInfoFragment fragment = new MovieInfoFragment();
      fragment.setArguments(args);

      getSupportFragmentManager().beginTransaction()
              .replace(R.id.movie_detail_container, fragment)
              .commit();
    } else {
      Intent intent = new Intent(this, DetailActivity.class)
              .putExtra(MovieInfoFragment.MOVIE_ID, movId);
      startActivity(intent);
    }
  }
}
