package fr.do_f.rssfeedify.login;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.util.Log;

import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.login.fragment.LoginFragment;
import fr.do_f.rssfeedify.login.fragment.MenuFragment;
import fr.do_f.rssfeedify.login.fragment.RegisterFragment;
import fr.do_f.rssfeedify.main.MainActivity;
import fr.do_f.rssfeedify.main.menu.adapter.MenuAdapter;

public class LoginActivity extends AppCompatActivity
        implements MenuFragment.OnFragmentInteractionListener {


    private static final String     TAG = "LoginActivity";
    private FragmentManager fm;

    public static void newActivity(Activity activity)
    {
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE);

        if (!sp.getString(Utils.TOKEN, "null").equals("null"))
        {
            MainActivity.newActivity(this);
            finish();
        }

        Log.d(TAG, "TAMERE");
        fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, MenuFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 1)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void showLogin() {
        Log.d(TAG, "showLogin");
        fm.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showRegister() {
        Log.d(TAG, "showRegister");
        fm.beginTransaction()
                .replace(R.id.container, RegisterFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
