package fr.do_f.rssfeedify;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import fr.do_f.rssfeedify.api.json.feeds.FeedResponse.*;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;

/**
 * Created by do_f on 05/04/16.
 */
public class Utils {

    private static final String     TAG = "Utils";

    public static final String      SP = "fr.do_f.rssfeedify";
    public static final String      TOKEN = "fr.do_f.rssfeedify.token";
    public static final String      USERNAME = "fr.do_f.rssfeedify.username";

    public static final String      HOME = "home";
    public static final String      FEEDBYID = "feedbyid";

    public static final String      FILE_MENU = "drawerfile";

    public static final int         ANIM_DURATION = 250;
    public static final int         REQUEST_CODE = 1337;
    public static final int         RESULT_DELETE = 420;

    public static final String      FEED_FRAGMENT = "feed_fragment";

    public static final int[]       colors = {
                R.color.md_red_500,
                R.color.md_red_A200,
                R.color.md_pink_500,
                R.color.md_pink_A200,
                R.color.md_purple_500,
                R.color.md_deep_purple_500,
                R.color.md_deep_purple_A200,
                R.color.md_indigo_500,
                R.color.md_indigo_A200,
                R.color.md_blue_500,
                R.color.md_blue_A200,
                R.color.md_light_blue_500,
                R.color.md_light_blue_A200,
                R.color.md_cyan_500,
                R.color.md_teal_500,
                R.color.md_green_500,
                R.color.md_green_A200,
                R.color.md_light_green_500,
                R.color.md_lime_500,
                R.color.md_amber_500,
                R.color.md_amber_A200,
                R.color.md_orange_500,
                R.color.md_orange_A200,
                R.color.md_deep_orange_500,
                R.color.md_deep_orange_A200
    };

    public static <T> void write(Context context, Collection<T> list, String file) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        String  username = context.getSharedPreferences(SP, Context.MODE_PRIVATE).getString(USERNAME, "null");
        String fileName = username+"-"+file;
        // Create a file in the Internal Storage

        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            Log.d(TAG, "write success for : "+fileName);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> read(Context context, String tmpfile, Type listType) {

        String  username = context.getSharedPreferences(SP, Context.MODE_PRIVATE).getString(USERNAME, "null");
        String fileName = username+"-"+tmpfile;

        BufferedReader input = null;
        File file = null;
        try {
            file = new File(context.getFilesDir(), fileName); // Pass getFilesDir() and "MyFile" to read file

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }

            Log.d(TAG, "read success for : "+fileName+" size : "+buffer.toString().length());
            Gson gson = new Gson();
            List<T> articles = gson.fromJson(buffer.toString(), listType);

            return articles;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
