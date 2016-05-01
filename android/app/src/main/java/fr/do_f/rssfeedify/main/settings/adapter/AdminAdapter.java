package fr.do_f.rssfeedify.main.settings.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.users.DeleteUserResponse;
import fr.do_f.rssfeedify.api.json.users.UsersReponse.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by do_f on 23/04/16.
 */
public class AdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdminAdapter";

    private onActivictyInteraction  onActivictyInteraction;
    private int                     lastPosition = -1;
    private List<User>              users;
    private Context                 context;
    private View                    v;
    private View                    rootView;
    private String                  token;
    private String                  username;

    public AdminAdapter(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
        token = context
                .getSharedPreferences(Utils.SP, Context.MODE_PRIVATE)
                .getString(Utils.TOKEN, "null");
        username = context
                .getSharedPreferences(Utils.SP, Context.MODE_PRIVATE)
                .getString(Utils.USERNAME, "null");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.main_settings_adapter_admin, parent, false);
        return new CellAdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellAdminViewHolder) holder).bindView(users.get(position), position);
        setAnimation(((CellAdminViewHolder) holder).container, position);
    }

//    @Override
//    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//    }

    @Override
    public int getItemCount() {
        int size = (users == null) ? 0 : users.size();
        return size;
    }

    public void onItemDismiss(final int position) {

        final String swipedUsername = users.get(position).getUsername();

        if (swipedUsername.equals(username)) {
            Snackbar.make(rootView,
                            rootView.getResources().getString(R.string.snackbar_admin_selfdelete),
                            Snackbar.LENGTH_LONG)
                    .show();
            notifyDataSetChanged();
            return;
        }


        Snackbar snackbar = Snackbar
                .make(rootView, rootView.getResources().getString(R.string.snackbar_admin_delete), Snackbar.LENGTH_LONG)
                .setAction("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyDataSetChanged();
                    }
                });

        snackbar.show();
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(final Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (event == DISMISS_EVENT_TIMEOUT) {
                    Call<DeleteUserResponse> call = RestClient.get(token).deleteUser(swipedUsername);
                    call.enqueue(new Callback<DeleteUserResponse>() {
                        @Override
                        public void onResponse(Call<DeleteUserResponse> call, Response<DeleteUserResponse> response) {
                            if (response.body() != null) {
                                users.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                Log.d(TAG, "response == null : "+response.code());
                                snackbar.dismiss();
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteUserResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure : "+t.getMessage());
                            snackbar.dismiss();
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public synchronized void refreshAdapter(List<User> newUsers) {
        if (users != null) {
            users.clear();
            users.addAll(newUsers);
        } else {
            users = newUsers;
        }
        setColor();
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setColor() {
        for (User u : users) {
            if (u.getColor() == 0) {
                u.setColor(Utils.colors[random(0, Utils.colors.length - 1)]);
            }
        }
    } //http://www.tuxboard.com/feed/

    private int random(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public interface onActivictyInteraction {
        void onUserClick(User user);
    }

    public void setOnActivictyInteraction(AdminAdapter.onActivictyInteraction onActivictyInteraction) {
        this.onActivictyInteraction = onActivictyInteraction;
    }

    public class CellAdminViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.admin_circle)
        ImageView circle;

        @Bind(R.id.admin_circle_text)
        TextView circle_text;

        @Bind(R.id.admin_title)
        TextView            title;

        @Bind(R.id.mContainer)
        LinearLayout        container;

        private View v;

        public CellAdminViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            v = view;
        }

        public void bindView(final User user, final int position) {
            GradientDrawable bgShape = (GradientDrawable)circle.getBackground();
            bgShape.setColor(v.getResources().getColor(user.getColor()));
            circle_text.setText(user.getUsername().substring(0, 1).toUpperCase());
            title.setText(user.getUsername());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onActivictyInteraction != null)
                        onActivictyInteraction.onUserClick(user);
                }
            });
        }

        public int random(int min, int max) {
            int range = (max - min) + 1;
            return (int)(Math.random() * range) + min;
        }
    }
}
