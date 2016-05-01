package fr.do_f.rssfeedify.login.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.login.LoginResponse;
import fr.do_f.rssfeedify.api.json.login.LoginResponse.*;
import fr.do_f.rssfeedify.api.json.users.GetUserReponse;
import fr.do_f.rssfeedify.main.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String        TAG = "LoginFragment";

    @Bind(R.id.loading_spinner)
    ProgressBar loading;

    @Bind(R.id.content)
    LinearLayout content;

    @Bind(R.id.login_username)
    EditText        username;

    @Bind(R.id.login_password)
    EditText        password;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loading.setVisibility(View.GONE);
    }

    @OnClick(R.id.login_submit)
    public void onSubmit(View v)
    {
        if (username.getText().length() == 0
                || password.getText().length() == 0)
        {
            Snackbar.make(getView(), "blabla", Snackbar.LENGTH_SHORT).show();
            return ;
        }
        hideContent();
        LoginPost p = new LoginPost(username.getText().toString(), password.getText().toString());
        Call<LoginResponse> call = RestClient.get().login(p);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() == null) {
                    String msg = getResources().getString(R.string.login_error_bad_credentials);
                    Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
                    showContent();
                } else {
                    SharedPreferences sp = getActivity().getSharedPreferences(Utils.SP, Context.MODE_PRIVATE);
                    sp.edit().putString(Utils.TOKEN, response.body().getToken()).apply();
                    sp.edit().putString(Utils.USERNAME, username.getText().toString()).apply();
                    MainActivity.newActivity(getActivity());
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Snackbar.make(getView(), "onFailure "+t.getMessage(), Snackbar.LENGTH_SHORT).show();
                showContent();
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
