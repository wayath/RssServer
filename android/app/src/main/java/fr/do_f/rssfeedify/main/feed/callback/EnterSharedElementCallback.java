package fr.do_f.rssfeedify.main.feed.callback;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import fr.do_f.rssfeedify.R;

/**
 * Created by do_f on 08/04/16.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class EnterSharedElementCallback extends SharedElementCallback {

    private static final String TAG = "EnterSharedElementCall";

    private final float mTitleStartTextSize;
    private final float mTitleEndTextSize;

    private final float mDateStartTextSize;
    private final float mDateEndTextSize;

    public EnterSharedElementCallback(Context context) {
        Resources res = context.getResources();
        mTitleStartTextSize = res.getDimensionPixelSize(R.dimen.title_small_text_size);
        mTitleEndTextSize = res.getDimensionPixelSize(R.dimen.title_large_text_size);

        mDateStartTextSize = res.getDimensionPixelSize(R.dimen.date_small_text_size);
        mDateEndTextSize = res.getDimensionPixelSize(R.dimen.date_large_text_size);
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        Log.i(TAG, "=== onSharedElementStart(List<String>, List<View>, List<View>)");

        //TextView    date = (TextView) sharedElements.get(0);
        TextView    title = (TextView) sharedElements.get(2);
        Log.i(TAG, "title : "+title.getText().toString());

        //date.setTextSize(mDateStartTextSize);
        title.setTextSize(mTitleStartTextSize);

    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        Log.i(TAG, "=== onSharedElementEnd(List<String>, List<View>, List<View>)");

        TextView date = (TextView) sharedElements.get(2);

        // Record the TextView's old width/height.
        int oldWidth = date.getMeasuredWidth();
        int oldHeight = date.getMeasuredHeight();

        // Setup the TextView's end values.
        date.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleEndTextSize);

        // Re-measure the TextView (since the text size has changed).
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        date.measure(widthSpec, heightSpec);

        // Record the TextView's new width/height.
        int newWidth = date.getMeasuredWidth();
        int newHeight = date.getMeasuredHeight();

        // Layout the TextView in the center of its container, accounting for its new width/height.
        int widthDiff = newWidth - oldWidth;
        int heightDiff = newHeight - oldHeight;
        date.layout(date.getLeft() - widthDiff / 2, date.getTop() - heightDiff / 2,
                date.getRight() + widthDiff / 2, date.getBottom() + heightDiff / 2);
    }

}
