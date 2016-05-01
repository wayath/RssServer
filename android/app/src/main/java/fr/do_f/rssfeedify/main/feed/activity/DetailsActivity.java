package fr.do_f.rssfeedify.main.feed.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse.*;
import fr.do_f.rssfeedify.api.json.feeds.article.ReadArticleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements Html.ImageGetter {

    private static final String     TAG = "DetailsActivity";
    private static final String     ARG_ARTICLE = "article";

    @Bind(R.id.toolbar)
    Toolbar                 toolbar;

    @Bind(R.id.feed_details_image)
    SimpleDraweeView        image;

    @Bind(R.id.feed_title)
    TextView                title;

    @Bind(R.id.feed_details_content)
    TextView                content;

    @Bind(R.id.feed_link)
    TextView                link;

    private Articles        articles;

    public static void newActivity(Activity activity, View v, Articles articles) {
        Intent i = new Intent(activity, DetailsActivity.class);
        i.putExtra(ARG_ARTICLE, articles);
        //activity.startActivity(i);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,

                /*new Pair<View, String>(v.findViewById(R.id.feed_image),
//                        activity.getString(R.string.transition_feed_image)),*/
//                new Pair<View, String>(v.findViewById(R.id.feed_date),
//                        activity.getString(R.string.transition_feed_date)),
                new Pair<View, String>(v.findViewById(R.id.feed_link_img),
                        activity.getString(R.string.transition_feed_link_img))
//                new Pair<View, String>(v.findViewById(R.id.feed_title),
//                        activity.getString(R.string.transition_feed_title))
        );

        ActivityCompat.startActivity(activity, i, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_feed_activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        articles = (Articles) getIntent().getSerializableExtra(ARG_ARTICLE);

        parseIframe();

        title.setText(articles.getTitle());
        String html = "<a href=\""+articles.getLink()+"\" >"+articles.getLink().substring(0, 30)+"..."+"</a>";
        link.setText(Html.fromHtml(html));
        link.setMovementMethod(LinkMovementMethod.getInstance());
        content.setText(Html.fromHtml(articles.getFull(), this, null));
        content.setMovementMethod(LinkMovementMethod.getInstance());

        if (articles.getStatus().equals("new"))
            markArticleAsRead();

        //getWindow().setEnterTransition(TransitionUtils.makeEnterTransition());
        //getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(this));
        //setEnterSharedElementCallback(new EnterSharedElementCallback(this));

        if (articles.getUrl() != null) {
            image.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(articles.getUrl());
            image.setImageURI(uri);
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void parseIframe() {
        Document doc = Jsoup.parse(articles.getFull());
        Elements elements = doc.select("iframe");

        for (Element e : doc.getElementsByTag("iframe")) {
            e.attr("href", e.attr("src"));
            e.append("Cliquez ici pour afficher le contenu");
        }

        elements.tagName("a");
        articles.setFull(doc.html());
    }

    private void markArticleAsRead() {
        String token = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE).getString(Utils.TOKEN, "null");
        Call<ReadArticleResponse> call = RestClient.get(token).readArticle(articles.getId());
        call.enqueue(new Callback<ReadArticleResponse>() {
            @Override
            public void onResponse(Call<ReadArticleResponse> call, Response<ReadArticleResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "SUCCESS");
                } else {
                    Log.d(TAG, "PASSCCESS + "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ReadArticleResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });

    }

    @Override
    public Drawable getDrawable(String source) {
        Drawable d = getResources().getDrawable(R.drawable.drawable_background_box);
        if (d != null) {
            d.setBounds(0, 0, 0, 0);
        }
        return d;
    }
}
