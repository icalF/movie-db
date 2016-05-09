package org.informatika.icalf.moviedatabase;

import android.content.Context;
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
  static final int COL_ID = 0;
  static final int COL_POSTER = 1;

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
    final int id = cursor.getInt(COL_ID);
    String posterURL = MovieContract.getPosterURL(cursor.getString(COL_POSTER));

    // Trigger the download of the URL asynchronously into the image view.
    Picasso.with(context) //
            .load(posterURL) //
            .into((ImageView)view);
  }
}
