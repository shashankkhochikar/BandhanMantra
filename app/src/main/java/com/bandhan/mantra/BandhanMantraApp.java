package com.bandhan.mantra;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.VolleySingleton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BandhanMantraApp extends Application {

    private static final String TAG = BandhanMantraApp.class.getSimpleName();

    public static String APP_VERSION = "1.0";
    public static String ANDROID_ID = "0000000000000000";
    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        VolleySingleton.createInstance(getApplicationContext());
        File yourAppStorageDir = new File(Environment.getExternalStorageDirectory(), "/" + getResources().getString(R.string.app_name) + "/");
        /*if (!yourAppStorageDir.exists()) {
            boolean isDirCreated = yourAppStorageDir.mkdirs();
            Log.d(TAG, "App mediaStorageDirectory created :" + isDirCreated);
        }*/

//        initImageLoader(getApplicationContext());


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        String dateString = dateFormat.format(calendar.getTime());
        /*//
        sessionManager = new SessionManager(this);
        sessionManager.updateSessionAppInstalledDateTime(dateString);
        sessionManager.updateSessionAppIsActiveUser(true);*/

        Log.d(TAG, "App Installed Date String :" + dateString);

    }

}
