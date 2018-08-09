package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;

public class NotificationsActivity extends BaseActivity {

    private ListView mNotificationListRecyclerView;
    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        if(!isNetworkConnected()){
            showToast("Check Internet Connection");
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();
    }

    private void assignViews() {
        mNotificationListRecyclerView = (ListView) findViewById(R.id.NotificationListRecyclerView);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
