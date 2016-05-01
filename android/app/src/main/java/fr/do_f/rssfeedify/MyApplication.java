package fr.do_f.rssfeedify;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by do_f on 07/04/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
