package com.bandhan.mantra.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.DashboardDetails;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout mContainer;
    private TextView mTxtSmsBal;
    private TextView mTxtPendingCamp;
    private TextView mTxtPendingCoupon;
    private TextView mTxtPendingSMS;
    private TextView mTxtRewordPoints;
    private TextView mTxtDiscountAmt;
    private TableLayout mSimpleTableLayout;
    private TableRow mSimpleTableRow;
    private TextView mTxtActiv1;
    private TextView mTxtActiv2;
    private TextView mTxtActiv3;
    private TextView mTxtActiv4;
    private TextView mTxtActiv5;

    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;
    private DashboardDetails dashboardDetails;
    private TextView mTxtUsername;
    private TextView mTxtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assignViews();

        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getDashboardDetails(clientId);
        mTxtUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserName);
        mTxtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        mTxtUsername.setText(""+userData.getFirstName());
        mTxtEmail.setText(""+userData.getEmail());
    }


    private void assignViews() {
        mContainer = (LinearLayout) findViewById(R.id.container);
        mTxtSmsBal = (TextView) findViewById(R.id.txt_smsBal);
        mTxtPendingCamp = (TextView) findViewById(R.id.txt_PendingCamp);
        mTxtPendingCoupon = (TextView) findViewById(R.id.txt_PendingCoupon);
        mTxtPendingSMS = (TextView) findViewById(R.id.txt_PendingSMS);
        mTxtRewordPoints = (TextView) findViewById(R.id.txt_RewordPoints);
        mTxtDiscountAmt = (TextView) findViewById(R.id.txt_DiscountAmt);
        mSimpleTableLayout = (TableLayout) findViewById(R.id.simpleTableLayout);
        mSimpleTableRow = (TableRow) findViewById(R.id.simpleTableRow);
        mTxtActiv1 = (TextView) findViewById(R.id.txt_activ1);
        mTxtActiv2 = (TextView) findViewById(R.id.txt_activ2);
        mTxtActiv3 = (TextView) findViewById(R.id.txt_activ3);
        mTxtActiv4 = (TextView) findViewById(R.id.txt_activ4);
        mTxtActiv5 = (TextView) findViewById(R.id.txt_activ5);
    }


    private void getDashboardDetails(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId", accessId);
            params.put("ClientId", "" + clientId);
            params.put("TopRecentActivities", "5");
            params.put("TopRecentDataForTables", "5");

            String urlWithParams =createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Dashboard/GetDashboardDetailsByClientId", params);
            final JSONObject request = new JSONObject();
            Log.v(ContactListActivity.class.getName(),urlWithParams.toString());

            GsonRequest<DashboardDetails> getDashboardDetailsRequest = new GsonRequest<DashboardDetails>(
                    Request.Method.POST,
                    urlWithParams,
                    request.toString(),
                    DashboardDetails.class,
                    new Response.Listener<DashboardDetails>() {
                        @Override
                        public void onResponse(DashboardDetails response) {
                            hideBusyProgress();
                            Log.v(ContactListActivity.class.getName(), "onResponse"+response.getSMSCredits().toString());
                            if(response.getSMSCredits() == null) {
                                showToast("Some Error Occures");
                            }else {
                                dashboardDetails = response;
                                setValues(dashboardDetails);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), VolleySingleton.getErrorMessage(error).toString());
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,HttpHeaderParser.parseCharset(response.headers, "utf-8"));// Now you can use any deserializer to make sense of data
                            //JSONObject obj = new JSONObject(res);
                            Log.v(ContactListActivity.class.getName(), "onErrorResponse1 \n\n" + res);
                        } catch (UnsupportedEncodingException e1) { // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (Exception e2) {// returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };
            getDashboardDetailsRequest.setRetryPolicy(VolleySingleton.getDefaultRetryPolice());
            getDashboardDetailsRequest.setShouldCache(true);
            VolleySingleton.getInstance().addToRequestQueue(getDashboardDetailsRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactListActivity.class.getName(), e.getMessage().toString());
            showToast("Something went wrong. Please try again later.");
        }
    }

    private void setValues(DashboardDetails dashboardDetails) {
        mTxtSmsBal.setText(dashboardDetails.getSMSCredits().toString());
        mTxtPendingCamp.setText(dashboardDetails.getPendingCampaigns().toString());
        mTxtPendingCoupon.setText(dashboardDetails.getPendingCouponCampaigns().toString());
        mTxtPendingSMS.setText(dashboardDetails.getPendingMessage().toString());
        mTxtRewordPoints.setText(dashboardDetails.getPendingMessage().toString());
        mTxtDiscountAmt.setText(dashboardDetails.getPendingMessage().toString());

        mTxtActiv1.setText(dashboardDetails.getActivityLogs().get(0).getEntityType()+" "+dashboardDetails.getActivityLogs().get(0).getEntityName()+" is "+dashboardDetails.getActivityLogs().get(0).getOperationType());
        mTxtActiv2.setText(dashboardDetails.getActivityLogs().get(1).getEntityType()+" "+dashboardDetails.getActivityLogs().get(1).getEntityName()+" is "+dashboardDetails.getActivityLogs().get(1).getOperationType());
        mTxtActiv3.setText(dashboardDetails.getActivityLogs().get(2).getEntityType()+" "+dashboardDetails.getActivityLogs().get(2).getEntityName()+" is "+dashboardDetails.getActivityLogs().get(2).getOperationType());
        mTxtActiv4.setText(dashboardDetails.getActivityLogs().get(3).getEntityType()+" "+dashboardDetails.getActivityLogs().get(3).getEntityName()+" is "+dashboardDetails.getActivityLogs().get(3).getOperationType());
        mTxtActiv5.setText(dashboardDetails.getActivityLogs().get(4).getEntityType()+" "+dashboardDetails.getActivityLogs().get(4).getEntityName()+" is "+dashboardDetails.getActivityLogs().get(4).getOperationType());


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            startActivity(new Intent(DashboardActivity.this,ContactGroupsListActivity.class));
        } else if (id == R.id.nav_campaigns) {
            startActivity(new Intent(DashboardActivity.this,CampaignsListActivity.class));
        } else if (id == R.id.nav_r_campaigns) {
            startActivity(new Intent(DashboardActivity.this,RecurringCampaignListActivity.class));
        } else if (id == R.id.nav_coupons) {
            startActivity(new Intent(DashboardActivity.this,CouponCampaignsListActivity.class));
        } else if (id == R.id.nav_redeem_coupons) {
            startActivity(new Intent(DashboardActivity.this,RedeemCouponsActivity.class));
        } else if (id == R.id.nav_templates) {
            startActivity(new Intent(DashboardActivity.this,TemplatesListActivity.class));
        }else if (id == R.id.nav_notifications) {
            startActivity(new Intent(DashboardActivity.this,NotificationsActivity.class));

        }else if (id == R.id.nav_buy_credit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
