package fr.do_f.rssfeedify.main.feed.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse.*;


/**
 * Created by do_f on 06/04/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FeedAdapter";

    private onItemClickListener onItemClickListener;
    private List<Articles>      articles;

    public interface onItemClickListener {
        void onItemClick(Articles articles, View v);
    }

    public FeedAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_feed_adapter_feed, parent, false);
        CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
        return cellFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellFeedViewHolder) holder).bindView(articles.get(position));
    }

    @Override
    public int getItemCount() {
        int size = (articles == null) ? 0 : articles.size();
        return size;
    }

    public synchronized void refreshAdapter(List<Articles> newArticles, boolean wipedata) {
        if (articles != null) {
            if (wipedata)
                articles.clear();
            articles.addAll(newArticles);
        } else {
            articles = newArticles;
        }
        setImage();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setImage() {
        for (Articles a : this.articles) {
            //Log.d(TAG, "setImage");
            Document doc = Jsoup.parse(a.getFull());

            for (Element e : doc.getElementsByTag("img")) {
                if (e.attr("src").length() != 0 && e.attr("width").length() != 0) {
                    a.setUrl(e.attr("src"));
                    break;
                }
            }
        }
    }

    public class CellFeedViewHolder extends RecyclerView.ViewHolder implements Html.ImageGetter {

        @Bind(R.id.feed_title)
        TextView            title;

        @Bind(R.id.feed_desc)
        TextView            preview;

        @Bind(R.id.feed_image)
        SimpleDraweeView    image;

        @Bind(R.id.feed_read)
        TextView            read;

        @Bind(R.id.feed_link)
        TextView            link;

        private View v;

        public CellFeedViewHolder(View view) {
            super(view);
            v = view;
            ButterKnife.bind(this, view);
        }

        public void bindView(final Articles articles)
        {
            if (articles.getUrl() != null) {
                image.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(articles.getUrl());
                image.setImageURI(uri);
            } else {
                image.setVisibility(View.GONE);
            }

            if (articles.getStatus().equals("read")) {
                read.setVisibility(View.GONE);
            } else {
                read.setVisibility(View.VISIBLE);
            }

            link.setText(articles.getLink().substring(0, 30)+"...");
            preview.setText(Html.fromHtml(articles.getPreview().substring(0, 100).replace("<p>", "")+"...", this, null));

            title.setText(articles.getTitle());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(articles, v);
                        read.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public Drawable getDrawable(String source) {
            Drawable d = v.getResources().getDrawable(R.drawable.drawable_background_box);
            if (d != null) {
                d.setBounds(0, 0, 0, 0);
            }
            return d;
        }
    }
}
