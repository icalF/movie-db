package org.informatika.icalf.moviedatabase;

import android.content.Context;
import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

/**
 * Created by user on 5/5/2016.
 */
public class ScrollListener implements AbsListView.OnScrollListener {
  private final Context context;

  public ScrollListener(MainActivity mainActivity) {
    this.context = mainActivity;
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    final Picasso picasso = Picasso.with(context);
    if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
      picasso.resumeTag(context);
    } else {
      picasso.pauseTag(context);
    }
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                       int totalItemCount) {
    // Do nothing.
  }
}
