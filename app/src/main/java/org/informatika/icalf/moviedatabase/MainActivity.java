package org.informatika.icalf.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by icalF on 5/5/2016.
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container, new DashboardFragment())
              .commit();
    }
  }
}
