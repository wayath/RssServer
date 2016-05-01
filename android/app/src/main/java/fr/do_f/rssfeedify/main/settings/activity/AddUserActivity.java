package fr.do_f.rssfeedify.main.settings.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import fr.do_f.rssfeedify.api.json.login.RegisterResponse;
import fr.do_f.rssfeedify.view.RevealBackgroundView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserActivity extends AppCompatActivity
        implements RevealBackgroundView.onStateChangeListener {


    private static final Interpolator   DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    private static final String         ARG_LOC = "startingLocation";
    private static final String         TAG = "AddUserActivity";

    @Bind(R.id.vRevealBackground)
    RevealBackgroundView        vRevealBackground;

    @Bind(R.id.content)
    LinearLayout                content;

    @Bind(R.id.loading_spinner)
    ProgressBar                 loading;

    @Bind(R.id.adduser_name)
    EditText                    username;

    @Bind(R.id.adduser_password)
    EditText                    password;

    private View                view;

    public static void newActivity(int[] startingLocation, Activity activity) {
        Intent i = new Intent(activity, AddUserActivity.class);
        i.putExtra(ARG_LOC, startingLocation);
        activity.startActivityForResult(i, Utils.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_settings_activity_adduser);
        ButterKnife.bind(this);
        view = findViewById(android.R.id.content);
        init();
        setupRevealBackground();
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
            //init();
        } else {
            content.animate().translationY(0).setDuration(500).setInterpolator(DECELERATE_INTERPOLATOR);
        }
    }

    @OnClick(R.id.adduser_submit)
    public void onSubmit(View v) {
        if (username.getText().length() == 0
                || password.getText().length() == 0)
        {
            Snackbar.make(view, "blabla", Snackbar.LENGTH_SHORT).show();
            return ;
        }
        hideContent();
        RegisterResponse.RegisterPost p = new RegisterResponse.RegisterPost(username.getText().toString(),
                password.getText().toString());

        Call<RegisterResponse> call = RestClient.get().register(p);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.body() != null) {
                    String msg = "";
                    if (response.body().getMessage().equals("success")) {
                        msg = getResources().getString(R.string.login_register_success);
                        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
                        setResult(RESULT_OK, null);
                        finish();
                    } else {
                        if (response.body().getMessage().equals("bad_params")) //bad_params
                            msg = getResources().getString(R.string.login_register_error_bad_params);
                        else
                            msg = getResources().getString(R.string.login_register_error_already_exists);
                        showContent();
                        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(view, "response.body == null", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Snackbar.make(view, "onFailure", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void showContent()
    {
        loading.animate()
                .alpha(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        content.animate().alpha(1).setDuration(Utils.ANIM_DURATION);
                    }
                });
    }

    public void hideContent()
    {
        content.animate()
                .alpha(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        loading.animate().alpha(1).setDuration(Utils.ANIM_DURATION);
                    }
                });
    }
}
