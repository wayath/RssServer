package fr.do_f.rssfeedify.main.settings.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.users.DeleteUserResponse;
import fr.do_f.rssfeedify.api.json.users.GetUserReponse;
import fr.do_f.rssfeedify.api.json.users.UpdateUserResponse;
import fr.do_f.rssfeedify.api.json.users.UsersReponse;
import fr.do_f.rssfeedify.main.settings.adapter.AdminAdapter;
import fr.do_f.rssfeedify.main.settings.callback.RecyclerViewSwipedCallback;
import fr.do_f.rssfeedify.view.RevealBackgroundView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity
        implements RevealBackgroundView.onStateChangeListener,
        AdminAdapter.onActivictyInteraction {


    private static final Interpolator   DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    private static final String         ARG_LOC = "startingLocation";
    private static final String         TAG = "AddFeedActivity";

    @Bind(R.id.rvFeed)
    RecyclerView            rvFeed;

    @Bind(R.id.vRevealBackground)
    RevealBackgroundView    vRevealBackground;

    @Bind(R.id.toolbar)
    Toolbar                 toolbar;

    @Bind(R.id.fab)
    FloatingActionButton    fab;

    private AdminAdapter    adapter;
    private String          token;
    private View            view;

    public static void newActivity(int[] startingLocation, Activity activity) {
        Intent i = new Intent(activity, AdminActivity.class);
        i.putExtra(ARG_LOC, startingLocation);
        activity.startActivityForResult(i, Utils.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_settings_activity_admin);
        ButterKnife.bind(this);
        view = findViewById(android.R.id.content);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ganjify)));

        token = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE)
                .getString(Utils.TOKEN, "null");

        setupFeed();
        setupRevealBackground();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE && resultCode == RESULT_OK) {
            refreshRecycler();
        } else if (requestCode == Utils.REQUEST_CODE && resultCode == Utils.RESULT_DELETE) {
            setResult(Utils.RESULT_DELETE, null);
            finish();
        }
    }

    protected void setupRevealBackground() {
        vRevealBackground.setColor(getResources().getColor(R.color.white_material));
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
            refreshRecycler();
        }
    }

    private void setupFeed() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvFeed.setHasFixedSize(true);
        rvFeed.setLayoutManager(lm);
        adapter = new AdminAdapter(this, view);
        adapter.setOnActivictyInteraction(this);
        rvFeed.setAdapter(adapter);

        ItemTouchHelper.Callback callback =
                new RecyclerViewSwipedCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvFeed);
    }

    private void refreshRecycler() {
        Call<UsersReponse> call = RestClient.get(token).getAllUser();
        call.enqueue(new Callback<UsersReponse>() {
            @Override
            public void onResponse(Call<UsersReponse> call, Response<UsersReponse> response) {
                if (response.body() != null) {
                    adapter.refreshAdapter(response.body().getUsers());
                } else {

                }
            }

            @Override
            public void onFailure(Call<UsersReponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onUserClick(UsersReponse.User user) {
        DetailsUserActivity.newActivity(user, true, this);
    }

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        int[] startingLocation = new int[2];
        fab.getLocationOnScreen(startingLocation);
        startingLocation[0] += fab.getWidth() / 2;
        AddUserActivity.newActivity(startingLocation, this);
        overridePendingTransition(0, 0);
    }
}
