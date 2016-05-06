package org.informatika.icalf.moviedatabase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/5/2016.
 */
public class DetailActivity extends AppCompatActivity {
  int id;

  static final int COL_TITLE = 4;
  static final int COL_YEAR = 3;
  static final int COL_OVERVIEW = 2;
  static final int COL_POSTER = 1;
  static final int COL_VOTE = 5;
  static final int COL_RUTIME = 7;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    Intent intent = getIntent();
    id = intent.getIntExtra("film", 0);
  }

  @Override
  protected void onStart() {
    super.onStart();

    Cursor cursor = getContentResolver().query(
            MovieContract.MovieEntry.buildMovieUri(id),
            null,
            MovieContract.MovieEntry._ID,
            new String[] {String.valueOf(id)},
            null
    );
    if (cursor != null) {
      cursor.moveToFirst();
    }

    TextView title = (TextView) findViewById(R.id.movie_title);
    TextView overview = (TextView) findViewById(R.id.overview);
    TextView vote = (TextView) findViewById(R.id.vote);
    TextView year = (TextView) findViewById(R.id.release_year);
    ImageView poster = (ImageView) findViewById(R.id.movie_poster);
    TextView runtime = (TextView) findViewById(R.id.runtime);

    Picasso.with(this).load(MovieContract.getPosterURL(cursor.getString(COL_POSTER))).into(poster);
    if (title != null) {
      title.setText(cursor.getString(COL_TITLE));
    }
    if (overview != null) {
      overview.setText(cursor.getString(COL_OVERVIEW));
    }
    if (vote != null) {
      vote.setText(Float.toString(cursor.getFloat(COL_VOTE)));
    }
    if (year != null) {
      year.setText(MovieContract.getYear(cursor.getString(COL_YEAR)));
    }
    if (runtime != null) {
      runtime.setText(Integer.toString(cursor.getInt(COL_RUTIME)));
    }

    cursor.close();
  }
}
