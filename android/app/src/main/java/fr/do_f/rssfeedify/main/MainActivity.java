package fr.do_f.rssfeedify.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.feeds.MarkAllFeedAsReadResponse;
import fr.do_f.rssfeedify.api.json.login.LogoutResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;
import fr.do_f.rssfeedify.api.json.users.DeleteUserResponse;
import fr.do_f.rssfeedify.api.json.users.GetUserReponse;
import fr.do_f.rssfeedify.api.json.users.UsersReponse;
import fr.do_f.rssfeedify.broadcast.NetworkReceiver;
import fr.do_f.rssfeedify.login.LoginActivity;
import fr.do_f.rssfeedify.main.feed.fragment.FeedFragment;
import fr.do_f.rssfeedify.main.feed.activity.AddFeedActivity;
import fr.do_f.rssfeedify.main.menu.adapter.MenuAdapter;

import fr.do_f.rssfeedify.main.menu.view.ContextMenuRecyclerView;
import fr.do_f.rssfeedify.main.settings.activity.AdminActivity;
import fr.do_f.rssfeedify.main.settings.activity.DetailsUserActivity;
import fr.do_f.rssfeedify.main.settings.activity.SettingsActivity;
import fr.do_f.rssfeedify.main.settings.callback.RecyclerViewSwipedCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuAdapter.onItemClickListener,
        NetworkReceiver.onNetworkStateChanged, DrawerLayout.DrawerListener {

    private static final String     TAG = "MainActivity";

    @Bind(R.id.drawer_layout)
    DrawerLayout                drawer;

    @Bind(R.id.rvFeed)
    RecyclerView                feed;

    @Bind(R.id.fab)
    FloatingActionButton        fab;

    @Bind(R.id.menu_home)
    LinearLayout                home;

    @Bind(R.id.toolbar)
    Toolbar                     toolbar;

    private String              token;
    private List<Feed>          feedInfo;
    private NetworkReceiver     network;
    private MenuAdapter         adapter;
    private int                 networkState;
    private SharedPreferences   sp;
    private GetUserReponse      cUser;
    private View                view;

    private int                 cPosition;


    public static void newActivity(Activity activity)
    {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        view = findViewById(android.R.id.content);
        init();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.HOME, null), Utils.FEED_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE && resultCode == RESULT_OK) {
            refreshRecycler();
        } else if (requestCode == Utils.REQUEST_CODE && resultCode == Utils.RESULT_DELETE) {
            deleteUser();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (network == null)
            initNetwork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregisters BroadcastReceiver when app is destroyed.
        if (network != null) {
            this.unregisterReceiver(network);
        }
    }

    public void init() {
        initNetwork();
        initView();
        initFeed();
    }

    public void initView() {
        sp = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE);
        token = sp.getString(Utils.TOKEN, "null");
        Log.d(TAG, "token : "+token);
        if (token.equals("null"))
            logout();

        Log.d(TAG, "--- INIT ---");
        Log.d(TAG, "USERNAME : "+sp.getString(Utils.USERNAME, "null"));

        network.setOnNetworkStateChanged(this);
        networkState = network.singleCheck(this);

        setSupportActionBar(toolbar);
        getUserInfo();

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ganjify)));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(this);

        LinearLayout logout = (LinearLayout) drawer.findViewById(R.id.menu_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkState == NetworkReceiver.STATE_ON) {
                    logout();
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getUserInfo() {

        String cUsername = sp.getString(Utils.USERNAME, "null");
        TextView menuTitle = (TextView) drawer.findViewById(R.id.menu_username);
        menuTitle.setText(cUsername);

        Log.d(TAG, "network state == "+networkState);

        if (networkState == NetworkReceiver.STATE_ON) {
            Log.d(TAG, "network state == "+networkState);
            Call<GetUserReponse> call = RestClient.get(token).getUser(cUsername);
            call.enqueue(new Callback<GetUserReponse>() {
                @Override
                public void onResponse(Call<GetUserReponse> call, Response<GetUserReponse> response) {
                    if (response.body() != null) {
                        cUser = response.body();
                    } else {
                        Log.d(TAG, "getUserInfo body == null : "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<GetUserReponse> call, Throwable t) {
                    Log.d(TAG, "getUserInfo onFailure : "+t.getMessage());
                }
            });
        }
    }

    public void initNetwork() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        network = new NetworkReceiver();
        registerReceiver(network, filter);
    }

    // init Drawer Menu List
    public void initFeed() {
        setupFeed();
        if (networkState == NetworkReceiver.STATE_OFF)
        {
            Type listType = new TypeToken<List<Feed>>() {}.getType();
            feedInfo = Utils.read(this, Utils.FILE_MENU, listType);
            Log.d(TAG, "INIT MENU NETWORK OFF");
            if (feedInfo == null) {
                //Snackbar.make(getView(), "Error, can't retreive the feed", Snackbar.LENGTH_SHORT).show();
                return ;
            } else {
                adapter.refreshAdapter(feedInfo);
            }
        }
        else {
            refreshRecycler();
        }
    }

    public void setupFeed() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        feed.setLayoutManager(lm);
        feed.setHasFixedSize(true);
        adapter = new MenuAdapter(this, view);
        adapter.setOnItemClickListener(this);
        feed.setAdapter(adapter);
        registerForContextMenu(feed);

        ItemTouchHelper.Callback callback =
                new RecyclerViewSwipedCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(feed);
    }

    public void refreshRecycler() {
        Call<GetFeedResponse> call = RestClient.get(token).getFeed();
        call.enqueue(new Callback<GetFeedResponse>() {
            @Override
            public void onResponse(Call<GetFeedResponse> call, Response<GetFeedResponse> response) {
                if (response.body() != null) {
                    feedInfo = response.body().getFeed();
                    Utils.write(getApplicationContext(), feedInfo, Utils.FILE_MENU);
                    adapter.refreshAdapter(response.body().getFeed());
                }
                else {
                    Log.d(TAG, "refreshRecycler :: error 500");
                }
            }

            @Override
            public void onFailure(Call<GetFeedResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        int[] startingLocation = new int[2];
        fab.getLocationOnScreen(startingLocation);
        startingLocation[0] += fab.getWidth() / 2;
        AddFeedActivity.newActivity(startingLocation, this);
        overridePendingTransition(0, 0);
    }


    // On Drawer Menu Item Click
    @Override
    public void onItemClick(int position) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.FEEDBYID, feedInfo.get(position)), Utils.FEED_FRAGMENT)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void setCurrentPosition(int position) {
        cPosition = position;
    }

    @OnClick(R.id.menu_home)
    public void onClickHome() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.HOME, null), Utils.FEED_FRAGMENT)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    // do_f Interface on Network Change
    @Override
    public void onStateChange(int state) {
        Log.d(TAG, "onStateChange NETWORK == "+state);
        if (state == NetworkReceiver.STATE_ON) {
            //onRefresh();
            networkState = state;
        } else {
            networkState = state;
        }
        Fragment f = getFragmentManager().findFragmentByTag(Utils.FEED_FRAGMENT);
        if (f instanceof FeedFragment) {
            ((FeedFragment) f).onStateChange(state);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_drawer, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_drawer_delete:
                markAllArticlesAsRead(feedInfo.get(cPosition).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void markAllArticlesAsRead(int id) {
        Log.d(TAG, "ID : "+id);
        Call<MarkAllFeedAsReadResponse> call = RestClient.get(token).markAllFeedAsRead(id);
        call.enqueue(new Callback<MarkAllFeedAsReadResponse>() {
            @Override
            public void onResponse(Call<MarkAllFeedAsReadResponse> call, Response<MarkAllFeedAsReadResponse> response) {
                if (response.body() != null) {
                    refreshRecycler();
                    Snackbar.make(view, "All articles has been set to \"view\"", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "FAIL OL + "+response.code(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MarkAllFeedAsReadResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (networkState == NetworkReceiver.STATE_OFF)
            return false;

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                if (cUser.getType().equals("admin")) {
                    int[] startingLocation = new int[2];
                    toolbar.getLocationOnScreen(startingLocation);
                    startingLocation[0] += toolbar.getWidth() / 2;
                    AdminActivity.newActivity(startingLocation, this);
                    overridePendingTransition(0, 0);
                } else {
                    UsersReponse.User tmp = new UsersReponse.User(cUser.getUsername(), cUser.getType());
                    DetailsUserActivity.newActivity(tmp, false, this);
                }
                return true;
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteUser() {
        final Activity a = this;
        Call<DeleteUserResponse> call = RestClient.get(token).deleteUser(cUser.getUsername());
        call.enqueue(new Callback<DeleteUserResponse>() {
            @Override
            public void onResponse(Call<DeleteUserResponse> call, Response<DeleteUserResponse> response) {
                if (response.body() != null) {
                    sp.edit().putString(Utils.TOKEN, "null").apply();
                    sp.edit().putString(Utils.USERNAME, "null").apply();
                    LoginActivity.newActivity(a);
                    finish();
                } else {
                    Log.d(TAG, "response == null : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<DeleteUserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });
    }

    private void logout() {
        final Activity a = this;
        Call<LogoutResponse> call = RestClient.get(token).logout();
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.body() != null) {
                    sp.edit().putString(Utils.TOKEN, "null").apply();
                    sp.edit().putString(Utils.USERNAME, "null").apply();
                    LoginActivity.newActivity(a);
                    finish();
                } else {
                    Log.d(TAG, "logout() body == null "+response.code());
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });
    }

    // USELESS

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { return true; }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) { }

    @Override
    public void onDrawerOpened(View drawerView) {
        refreshRecycler();
    }

    @Override
    public void onDrawerClosed(View drawerView) { }

    @Override
    public void onDrawerStateChanged(int newState) { }
}
