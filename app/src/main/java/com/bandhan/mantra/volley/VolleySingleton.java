package com.bandhan.mantra.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bandhan.mantra.BandhanMantraApp;
import com.bandhan.mantra.R;

import java.io.File;
import java.io.IOException;


/**
 * Created by rajk
 */
public class VolleySingleton {


    public static final String PACKAGE_NAME = BandhanMantraApp.class.getPackage().getName();
    private static final String TAG = BandhanMantraApp.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private static String wsBaseUrl;

    private static String edbrixServerUrl;

    private static VolleySingleton mInstance;
    static Context mContext;

    public static VolleySingleton getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Please call `createInstance(...)` first");
        }
        return mInstance;
    }

    public static VolleySingleton createInstance(Context context) {
        if (mInstance != null) {
            throw new IllegalStateException("An instance has already been created");
        }
        mInstance = new VolleySingleton(context);
        return mInstance;
    }

    public static String getWsBaseUrl() {
        return wsBaseUrl;
    }

    public static String getEdbrixServerUrl() {
        return edbrixServerUrl;
    }

    public static File getAppStorageDirectory() {
        File yourAppStorageDir = new File(Environment.getExternalStorageDirectory(), "/" + mContext.getResources().getString(R.string.app_name) + "/");
        if (!yourAppStorageDir.exists()) {
            boolean isDirCreated = yourAppStorageDir.mkdirs();
            if (!isDirCreated) {
                return null;
            }
        }
        return yourAppStorageDir;
    }

    public static File getAppStorageTempDirectory() {
        File yourAppTempStorageDir = new File(Environment.getExternalStorageDirectory(), "/" + mContext.getResources().getString(R.string.app_name) + "/temp/");
        if (!yourAppTempStorageDir.exists()) {
            boolean isDirCreated = yourAppTempStorageDir.mkdirs();
            if (!isDirCreated) {
                return null;
            }
        }
        return yourAppTempStorageDir;
    }

    public static File getTempPDFFile() {
        try {
            File tempPDFFile = new File(Environment.getExternalStorageDirectory(), "/" + mContext.getResources().getString(R.string.app_name) + "/tempFile.pdf");
            if (!tempPDFFile.exists()) {
                boolean isFileCreated = tempPDFFile.createNewFile();
                if (!isFileCreated) {
                    return null;
                }
            }
            return tempPDFFile;
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean removedTempPDFFile() {
        try {
            if (getTempPDFFile().exists()) {
                return getTempPDFFile().delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String getErrorMessage(VolleyError volleyError) {
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }

    private VolleySingleton(Context context) {
        mContext = context;
        wsBaseUrl = "http://35.231.207.193/msgAPI/api/";
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Method provides defaultRetryPolice.
     * First Attempt = 14+(14*1)= 28s.
     * Second attempt = 28+(28*1)= 56s.
     * then invoke Response.ErrorListener callback.
     *
     * @return DefaultRetryPolicy object
     */
    public static DefaultRetryPolicy getDefaultRetryPolice() {
        return new DefaultRetryPolicy(14000, 2, 1);
    }

}
