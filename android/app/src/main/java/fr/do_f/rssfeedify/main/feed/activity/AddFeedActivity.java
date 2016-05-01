package fr.do_f.rssfeedify.main.feed.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.feeds.AddFeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.AddFeedResponse.*;
import fr.do_f.rssfeedify.api.json.feeds.worker.WorkerResponse;
import fr.do_f.rssfeedify.view.RevealBackgroundView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFeedActivity extends AppCompatActivity
        implements RevealBackgroundView.onStateChangeListener {

    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    private static final String     ARG_LOC = "startingLocation";
    private static final String     TAG = "AddFeedActivity";

    @Bind(R.id.content)
    LinearLayout            content;

    @Bind(R.id.loading_spinner)
    ProgressBar             loading;

    @Bind(R.id.vRevealBackground)
    RevealBackgroundView    vRevealBackground;

    @Bind(R.id.feed_add_name)
    EditText                name;

    @Bind(R.id.feed_add_url)
    EditText                url;

    private String          token;

    public static void newActivity(int[] startingLocation, Activity activity) {
        Intent i = new Intent(activity, AddFeedActivity.class);
        i.putExtra(ARG_LOC, startingLocation);
        activity.startActivityForResult(i, Utils.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_feed_activity_addfeed);
        ButterKnife.bind(this);
        loading.setVisibility(View.GONE);
        init();
        setupRevealBackground();
        SharedPreferences sp = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE);
        token = sp.getString(Utils.TOKEN, "null");
    }

    protected void init() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        content.setTranslationY(-size.y);
    }

    protected void setupRevealBackground() {
        vRevealBackground.setColor(getResources().getColor(R.color.ganjify));
        vRevealBackground.setOnStateChangeListener(this);
        final int[] startingLocation = getIntent().getIntArrayExtra(ARG_LOC);
        vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                vRevealBackground.startFromLocation(startingLocation);
                return true;
            }
        });
    }

    @Override
    public void onStateChange(int state) {
        if (state == RevealBackgroundView.STATE_FINISHED) {

        } else {
            content.animate().translationY(0).setDuration(500).setInterpolator(DECELERATE_INTERPOLATOR);
        }
    }

    @OnClick(R.id.feed_add_submit)
    public void onClick(View v) {
        if (name.getText().length() == 0
                && url.getText().length() == 0)
            return ;

        hideContent();
        AddFeedPost p = new AddFeedPost(name.getText().toString(), url.getText().toString());
        Call<AddFeedResponse> call = RestClient.get(token).addFeed(p);
        call.enqueue(new Callback<AddFeedResponse>() {
            @Override
            public void onResponse(Call<AddFeedResponse> call, Response<AddFeedResponse> response) {
                if (response.body() == null) {
                    showContent();
                } else {
                    updateWorker(response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<AddFeedResponse> call, Throwable t) {
                showContent();
            }
        });
    }

    public void updateWorker(int id) {
        Call<WorkerResponse> call = RestClient.get(token).worker(id);
        call.enqueue(new Callback<WorkerResponse>() {
            @Override
            public void onResponse(Call<WorkerResponse> call, Response<WorkerResponse> response) {
                if (response.body() == null) {
                    showContent();
                    Log.d(TAG, "updateWorker : "+response.code());
                } else {
                    setResult(RESULT_OK, null);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<WorkerResponse> call, Throwable t) {
                Log.d(TAG, "WORKER ON FAILURE + "+t.getMessage());
            }
        });
    }

    public void showContent()
    {
        content.animate()
                .alpha(1)
                .setDuration(Utils.ANIM_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        loading.animate().alpha(0).setDuration(Utils.ANIM_DURATION);
                        loading.setVisibility(View.GONE);
                    }
                });
    }

    public void hideContent()
    {
        content.animate()
                .alpha(0)
                .setDuration(Utils.ANIM_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        loading.setVisibility(View.VISIBLE);
                        loading.animate().alpha(1).setDuration(Utils.ANIM_DURATION);
                    }
                });
    }
}
