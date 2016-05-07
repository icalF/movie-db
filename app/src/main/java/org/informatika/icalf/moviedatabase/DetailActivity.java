package org.informatika.icalf.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by icalF on 5/5/2016.
 */
public class DetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container1, new MovieInfoFragment())
              .add(R.id.container2, new MovieTrailersFragment())
              .add(R.id.container3, new MovieReviewsFragment())
              .commit();
    }
  }
}
