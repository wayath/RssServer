package fr.do_f.rssfeedify.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by do_f on 07/04/16.
 */
public class RevealBackgroundView extends View {

    public static final int    STATE_NOT_STARTED = 0;
    public static final int    STATE_START = 1;
    public static final int    STATE_FINISHED = 2;

    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private static final int    FILL_TIME = 500;

    private Paint fillPaint;

    ObjectAnimator  revealAnimator;

    private int     startLocationX;
    private int     startLocationY;
    private int     currentRadius;

    private int     state;

    private onStateChangeListener onStateChangeListener;

    public RevealBackgroundView(Context context) {
        super(context);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);
    }

    public void setColor(int color){
        fillPaint.setColor(color);
    }

    public void startFromLocation(int[] startingLocation) {
        changeState(STATE_START);
        startLocationX = startingLocation[0];
        startLocationY = startingLocation[1];
        revealAnimator = ObjectAnimator.ofInt(this, "currentRadius", 0, getWidth() + getHeight()).setDuration(FILL_TIME);
        revealAnimator.setInterpolator(INTERPOLATOR);
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }
        });
        revealAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (state == STATE_FINISHED) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        } else {
            canvas.drawCircle(startLocationX, startLocationY, currentRadius, fillPaint);
        }
    }

    public void setCurrentRadius(int radius) {
        this.currentRadius = radius;
        invalidate();
    }

    public void changeState(int state) {
        if (this.state == state)
            return ;

        this.state = state;
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(state);
    }

    public void setOnStateChangeListener(onStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public static interface onStateChangeListener {
        void onStateChange(int state);
    }
}
