package org.informatika.icalf.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/5/2016.
 */
public class MovieAdapter extends CursorAdapter {
  public MovieAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return new ImageView (context)
    {
      @Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
      }
    };
  }

  @Override
  public void bindView(View view, final Context context, final Cursor cursor) {
    int posterId = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMMN_POSTER_URL);
    String posterURL = MovieContract.getPosterURL(cursor.getString(posterId));
    final int position = cursor.getPosition();

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cursor.moveToPosition(position);
        int id = cursor.getColumnIndex(MovieContract.MovieEntry._ID);

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("film", cursor.getInt(id));
        context.startActivity(intent);
      }
    });

    // Trigger the download of the URL asynchronously into the image view.
    Picasso.with(context) //
            .load(posterURL) //
            .into((ImageView)view);
  }
}
