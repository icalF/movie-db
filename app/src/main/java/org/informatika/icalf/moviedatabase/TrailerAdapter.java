package org.informatika.icalf.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.informatika.icalf.moviedatabase.data.MovieContract;

/**
 * Created by icalF on 5/6/2016.
 */
public class TrailerAdapter extends CursorAdapter {
  static final int COL_URL = 0;

  public TrailerAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return new ImageView(context)
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
    final String key = cursor.getString(COL_URL);
    String thumbnailURL = MovieContract.getThumbnailURL(key);

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieContract.getVideoURL(key)));
        context.startActivity(intent);
      }
    });

    // Trigger the download of the URL asynchronously into the image view.
    Picasso.with(context) //
            .load(thumbnailURL) //
            .into((ImageView)view);
  }
}
