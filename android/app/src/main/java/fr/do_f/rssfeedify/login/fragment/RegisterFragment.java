package fr.do_f.rssfeedify.login.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
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
import fr.do_f.rssfeedify.api.json.login.RegisterResponse;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse.RegisterPost;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    @Bind(R.id.loading_spinner)
    ProgressBar     loading;

    @Bind(R.id.content)
    LinearLayout    content;

    @Bind(R.id.register_username)
    EditText        username;

    @Bind(R.id.register_password)
    EditText        password;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment_register, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        loading.setVisibility(View.GONE);
    }

    @OnClick(R.id.register_submit)
    public void onSubmit(View v)
    {
        if (username.getText().length() == 0
                || password.getText().length() == 0)
        {
            Snackbar.make(getView(), "blabla", Snackbar.LENGTH_SHORT).show();
            return ;
        }
        hideContent();
        RegisterPost p = new RegisterPost(username.getText().toString(),
                password.getText().toString());

        Call<RegisterResponse> call = RestClient.get().register(p);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                String msg = "";
                if (response.body().getMessage().equals("success")) {
                    msg = getResources().getString(R.string.login_register_success);
                    Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else {
                    if (response.body().getMessage().equals("bad_params")) //bad_params
                        msg = getResources().getString(R.string.login_register_error_bad_params);
                    else
                        msg = getResources().getString(R.string.login_register_error_already_exists);
                    showContent();
                    Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Snackbar.make(getView(), "onFailure", Snackbar.LENGTH_SHORT).show();
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
                        loading.setVisibility(View.GONE);
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
                        loading.setVisibility(View.VISIBLE);
                        loading.animate().alpha(1).setDuration(Utils.ANIM_DURATION);
                    }
                });
    }
}
