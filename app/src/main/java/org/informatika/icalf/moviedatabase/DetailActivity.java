package org.informatika.icalf.moviedatabase;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by icalF on 5/5/2016.
 */
public class DetailActivity extends AppCompatActivity {

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    getSharedPreferences(getString(R.string.sharedpref), Context.MODE_PRIVATE)
            .edit()
            .putInt("mode", id);

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    if (savedInstanceState == null) {
      Bundle arguments = new Bundle();
      arguments.putInt(MovieInfoFragment.MOVIE_ID, getIntent().getIntExtra(MovieInfoFragment.MOVIE_ID, 0));

      MovieInfoFragment fragment = new MovieInfoFragment();
      fragment.setArguments(arguments);

      getSupportFragmentManager().beginTransaction()
              .add(R.id.movie_detail_container, fragment)
              .commit();
    }
  }
}
