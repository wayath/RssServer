package fr.do_f.rssfeedify;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
//        Type listType = new TypeToken<List<FeedResponse.Articles>>() {}.getType();
//        Utils.read(mContext.getApplicationContext(), "do_f-Home", listType);
    }
}

//compile 'org.mockito:mockito-all:1.10.19'
