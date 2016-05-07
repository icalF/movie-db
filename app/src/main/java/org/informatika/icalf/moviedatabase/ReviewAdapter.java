package org.informatika.icalf.moviedatabase;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by icalF on 5/7/2016.
 */
public class ReviewAdapter extends CursorAdapter {
  private static final int COL_AUTHOR = 1;
  private static final int COL_CONTENT = 2;

  public ReviewAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.list_review, parent, false);
    return view;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    TextView author = (TextView) view.findViewById(R.id.author);
    TextView content = (TextView) view.findViewById(R.id.content);

    author.setText(cursor.getString(COL_AUTHOR));
    content.setText(cursor.getString(COL_CONTENT));
  }
}
