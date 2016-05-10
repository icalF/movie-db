package org.informatika.icalf.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by icalF on 5/5/2016.
 */
public class MainActivity extends ActionBarActivity implements DashboardFragment.Callback {

  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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
