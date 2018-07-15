package com.bandhan.mantra.baseclasses;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bandhan.mantra.activities.Login;
import com.bandhan.mantra.commons.AlertDialogManager;
import com.bandhan.mantra.commons.DialogManager;
import com.bandhan.mantra.commons.GlobalMethods;
import com.bandhan.mantra.commons.ToastMessage;
import com.bandhan.mantra.utils.SessionManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {


    private DialogManager dialogManager;

    private GlobalMethods globalMethods;

    protected Context mContext;

    private ToastMessage toastMessage;

    protected OnFragmentBackPressedListener onFragmentBackPressedListener;


    private LocalBroadcastManager localBroadcastManager;

    private SessionManager sessionManager;

    public static final int PICKFILE_RESULT_CODE = 1;

    public static final int PICKIMAGES_RESULT_CODE = 2;

    public static final int REQUEST_CAMERA = 3;


    private final static String OPR_BIND = "bind";
    private final static String OPR_WATERMARK_ADD = "watermark";
    private final static String OPR_CONCATENATE = "concatenate";
    private final static String OPR_VERSION = "version";
    private final static String OPR_TS = "ts";
    private final static String OPR_ALBUM = "album";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogManager = new DialogManager(this);
        globalMethods = new GlobalMethods();
        toastMessage = new ToastMessage(this);

        mContext = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.registerReceiver(mRandomNumberReceiver, new IntentFilter("BROADCAST_RANDOM_NUMBER"));
        sessionManager = new SessionManager(BaseActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        /*LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));*/

        //temporary code added for Limited edition build for session time out after 7 days
        compareCurrentDateWithInstalledDate();
    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    protected void showToast(String msg, int timeDuration) {
        toastMessage.showToastMsg(msg, timeDuration);
    }

    protected void showToast(String msg) {
        toastMessage.showToastMsg(msg, Toast.LENGTH_LONG);
    }

    protected void cancelToast() {
        toastMessage.cancelToast();
    }

    protected AlertDialogManager getAlertDialogManager() {
        return dialogManager.getAlertDialogManager();
    }

    protected GlobalMethods getGlobalMethods() {
        return globalMethods;
    }

    protected void showBusyProgress() {
        dialogManager.showBusyProgress();
    }

    protected void showBusyProgress(String message) {
        dialogManager.showBusyProgress(message);
    }

    protected void hideBusyProgress() {
        dialogManager.hideBusyProgress();
    }


    public interface OnFragmentBackPressedListener {
        public void doBack();
    }


    public void setOnFragmentBackPressedListener(OnFragmentBackPressedListener onFragmentBackPressedListener) {
        this.onFragmentBackPressedListener = onFragmentBackPressedListener;
    }


    protected SessionManager getSessionManager() {
        if (sessionManager != null) {
            return sessionManager;
        } else {
            sessionManager = new SessionManager(BaseActivity.this);
            return sessionManager;
        }
    }

    // Initialize a new BroadcastReceiver instance
    protected BroadcastReceiver mRandomNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get the received random number

            // Display a notification that the broadcast received
            showToast("Video processed successfully...", Toast.LENGTH_SHORT);
        }
    };

    protected void forceCrash(View view) {
        throw new RuntimeException("This is a test crash for Fabric");
    }

    protected boolean isTablet() {
        boolean xlarge = ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    protected boolean isChromebook() {
        boolean isChromebook = this.getPackageManager().hasSystemFeature("org.chromium.arc.device_management");
        if (isChromebook) {
            return true;
        }
        return false;
    }

    protected int getScreenOrientation() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRotation();
    }



    protected void openDocumentFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.google.android.apps.docs/files/");
        intent.setDataAndType(uri, "application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Open folder"), PICKFILE_RESULT_CODE);
    }

    protected void openImagesFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.google.android.apps.docs/files/");
        intent.setDataAndType(uri, "image/jpeg");
        startActivityForResult(Intent.createChooser(intent, "Open folder"), PICKIMAGES_RESULT_CODE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 200, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*public Uri getImageUriForImageChooserIntent(Context inContext) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
        File file = new File(VolleySingleton.getAppStorageTempDirectory(), fileName);
        return FileProvider.getUriForFile(inContext, BuildConfig.APPLICATION_ID + ".provider",file);
        //return Uri.fromFile(file);
    }*/

    protected boolean copyInputStreamToFile(Uri uri, File file) {
        OutputStream out = null;
        InputStream in = null;

        try {
            in = getContentResolver().openInputStream(uri);
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            if (out != null) {
                out.close();
            }

            in.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copy(File src, File dst) throws IOException {
        try ( InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    public void compareCurrentDateWithInstalledDate(){
        try {
            if (!sessionManager.getSessionAppInstalledDateString().isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date installedDate = dateFormat.parse(sessionManager.getSessionAppInstalledDateString());
                Date finishedDate = new Date(installedDate.getTime() + 604800000L); // 7 * 24 * 60 * 60 * 1000
                Date currentDate = Calendar.getInstance().getTime();
                boolean isSessionOut = currentDate.after(finishedDate);
                if(isSessionOut){
                    sessionManager.clearSessionCredentials();
                    sessionManager.clearSessionCredentials();
                    sessionManager.updateSessionAppInstalledDateTime("");
                    sessionManager.updateSessionAppIsActiveUser(false);
                    callLoginActivity();
                }
            }
        }catch (Exception e){
            Log.e("BaseActivity", "Exception found: " + e.getMessage());
        }
    }

    private void callLoginActivity() {
        Intent mainIntent = new Intent();
        mainIntent.setClass(BaseActivity.this, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }
}
