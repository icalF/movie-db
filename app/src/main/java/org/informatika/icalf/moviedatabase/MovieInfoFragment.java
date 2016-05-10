package org.informatika.icalf.moviedatabase;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/6/2016.
 */
public class MovieInfoFragment extends Fragment {

  public static final String MOVIE_ID = "ID";
  private static final int COL_URL = 0;
  private static final int COL_AUTHOR = 1;
  private static final int COL_CONTENT = 2;

  static final int COL_TITLE = 4;
  static final int COL_YEAR = 3;
  static final int COL_OVERVIEW = 2;
  static final int COL_POSTER = 1;
  static final int COL_VOTE = 5;
  static final int COL_RUTIME = 7;

  private int id = 0;

  public MovieInfoFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle arguments = getArguments();
    if (arguments != null) {
      id = arguments.getInt(MOVIE_ID);
    }

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
    ContentResolver contentResolver = getActivity().getContentResolver();

    Cursor cursor = contentResolver.query(
            MovieContract.MovieEntry.buildMovieUri(id),
            null,
            null,
            null,
            null,
            null
    );

    // Load info detail
    TextView title = (TextView) view.findViewById(R.id.movie_title);
    TextView overview = (TextView) view.findViewById(R.id.overview);
    TextView vote = (TextView) view.findViewById(R.id.vote);
    TextView year = (TextView) view.findViewById(R.id.release_year);
    ImageView poster = (ImageView) view.findViewById(R.id.movie_poster);
    TextView runtime = (TextView) view.findViewById(R.id.runtime);

    if (cursor.moveToFirst()) {
      Picasso.with(getContext()).load(MovieContract.getPosterURL(cursor.getString(COL_POSTER))).into(poster);
      if (title != null) {
        title.setText(cursor.getString(COL_TITLE));
      }

      if (overview != null) {
        overview.setText(cursor.getString(COL_OVERVIEW));
      } else {
        overview.setText(R.string.no_overview);
      }

      if (vote != null) {
        vote.setText(Float.toString(cursor.getFloat(COL_VOTE)) + " / 10.0");
      }

      if (year != null) {
        year.setText(MovieContract.getYear(cursor.getString(COL_YEAR)));
      }

      if (runtime != null) {
        runtime.setText(Integer.toString(cursor.getInt(COL_RUTIME)) + " min");
      }
    }

    // Load trailers
    LinearLayout lv = (LinearLayout) view.findViewById(R.id.trailer);
    cursor = contentResolver.query(
            MovieContract.MovieEntry.buildMovieTrailers(id),
            null,
            null,
            null,
            null
    );

    if (cursor != null && cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        ImageView imageView = new ImageView(getContext()) {
          @Override
          protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
          }
        };

        final String key = cursor.getString(COL_URL);
        String thumbnailURL = MovieContract.getThumbnailURL(key);

        imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieContract.getVideoURL(key)));
            getContext().startActivity(intent);
          }
        });

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(getContext()) //
                .load(thumbnailURL) //
                .into(imageView);

        lv.addView(imageView);
      }
    } else {
      TextView textView = new TextView(getContext());
      textView.setText(R.string.no_trailer);
      lv.addView(textView);
    }

    // Load reviews
    lv = (LinearLayout) view.findViewById(R.id.review);
    cursor = contentResolver.query(
            MovieContract.MovieEntry.buildMovieReviews(id),
            null,
            null,
            null,
            null
    );

    if (cursor != null && cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater(null).inflate(R.layout.list_review, null);
        TextView author = (TextView) linearLayout.findViewById(R.id.author);
        TextView content = (TextView) linearLayout.findViewById(R.id.content);

        author.setText(cursor.getString(COL_AUTHOR));
        content.setText(cursor.getString(COL_CONTENT));

        lv.addView(linearLayout);
      }
    } else {
      TextView textView = new TextView(getContext());
      textView.setText(R.string.no_review);
      lv.addView(textView);
    }

    cursor.close();

    return view;
  }

  public void addToFavorite(View view) {
    getActivity().getSharedPreferences(getString(R.string.sharedpref), Context.MODE_PRIVATE)
                .edit()
                .putBoolean(String.valueOf(id), true)
                .commit();
  }
}
