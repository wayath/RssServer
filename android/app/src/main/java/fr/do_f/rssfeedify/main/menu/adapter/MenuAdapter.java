package fr.do_f.rssfeedify.main.menu.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.feeds.DeleteFeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;
import fr.do_f.rssfeedify.api.json.users.DeleteUserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by do_f on 17/04/16.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MenuAdapter";

    private onItemClickListener onItemClickListener;
    private List<Feed>          feed;
    private View                rootView;
    private String              token;
    private Context             context;

    public MenuAdapter(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
        token = context
                .getSharedPreferences(Utils.SP, Context.MODE_PRIVATE)
                .getString(Utils.TOKEN, "null");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_adapter, parent, false);
        CellMenuViewHolder cellMenuViewHolder = new CellMenuViewHolder(v);
        return cellMenuViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellMenuViewHolder) holder).bindView(feed.get(position), position);
        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        int size = (feed == null) ? 0 : feed.size();
        return size;
    }

    public synchronized void refreshAdapter(List<Feed> newFeed) {
        if (feed != null) {
            feed.clear();
            feed.addAll(newFeed);
        } else {
            feed = newFeed;
        }
        notifyDataSetChanged();
    }

    public void onItemDismiss(final int position) {
        Snackbar snackbar = Snackbar
                .make(rootView, rootView.getResources().getString(R.string.snackbar_menu_delete), Snackbar.LENGTH_LONG)
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
                    Call<DeleteFeedResponse> call = RestClient.get(token).deleteFeed(feed.get(position).getId());
                    call.enqueue(new Callback<DeleteFeedResponse>() {
                        @Override
                        public void onResponse(Call<DeleteFeedResponse> call, Response<DeleteFeedResponse> response) {
                            if (response.body() != null) {
                                feed.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                Log.d(TAG, "response == null : "+response.code());
                                snackbar.dismiss();
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteFeedResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure : "+t.getMessage());
                            snackbar.dismiss();
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
        void setCurrentPosition(int position);
    }

    class CellMenuViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.menu_circle)
        ImageView           circle;

        @Bind(R.id.menu_circle_text)
        TextView            circle_text;

        @Bind(R.id.menu_title)
        TextView            title;

        @Bind(R.id.menu_news)
        TextView            news;

        private View v;

        private Boolean alreadySet;

        public CellMenuViewHolder(View v) {
            super(v);
            this.alreadySet = false;
            ButterKnife.bind(this, v);
            this.v = v;
        }

        public void bindView(final Feed feed, final int position) {
            if (!alreadySet) {
                GradientDrawable bgShape = (GradientDrawable)circle.getBackground();
                bgShape.setColor(v.getResources().getColor(Utils.colors[random(0, Utils.colors.length-1)]));
                alreadySet = true;
            }
            news.setText(String.valueOf(feed.getNewArticles()));
            circle_text.setText(feed.getName().substring(0, 1));
            title.setText(feed.getName());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.setCurrentPosition(position);
                    return false;
                }
            });
        }

        public int random(int min, int max)
        {
            int range = (max - min) + 1;
            return (int)(Math.random() * range) + min;
        }
    }
}
