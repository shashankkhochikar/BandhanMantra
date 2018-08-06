package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.adapter.CouponCampaignListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.CouponCampaign;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponCampaignsListActivity extends BaseActivity {

    private ListView mCouponCampaignsListRecyclerView;
    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;
    public List<CouponCampaign> couponList;
    private CouponCampaignListAdapter couponCampaignListAdapter;
    private CouponCampaignListAdapter.OnButtonActionListener onButtonActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_campaigns_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();
        GetEcouponCampaignListByClientId(clientId);
        onButtonActionListener = new CouponCampaignListAdapter.OnButtonActionListener() {
            @Override
            public void onViewClicked(CouponCampaign couponCampaign, int position) {
                Intent editCouponCampaignActivity = new Intent(CouponCampaignsListActivity.this, CreateCouponCampaignActivity.class);
                editCouponCampaignActivity.putExtra("clientId", clientId);
                editCouponCampaignActivity.putExtra("couponData", couponCampaign);
                editCouponCampaignActivity.putExtra("isEdit",true);
                startActivity(editCouponCampaignActivity);
            }
        };

    }

    private void GetEcouponCampaignListByClientId(final int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId",accessId);
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "EcouponCampaign/GetEcouponCampaignListByClientId",params);
            StringRequest userResetPasswordRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CouponCampaignsListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {

                        couponList = new ArrayList<CouponCampaign>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(Response);
                            Log.e(ContactGroupsListActivity.class.getName(),""+jsonArray.get(0).toString());
                            for(int i=0;i<jsonArray.length();i++){
                                CouponCampaign tmptestData = new Gson().fromJson(jsonArray.get(i).toString(),CouponCampaign.class);
                                couponList.add(tmptestData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        couponCampaignListAdapter = new CouponCampaignListAdapter(CouponCampaignsListActivity.this, couponList,onButtonActionListener);
                        mCouponCampaignsListRecyclerView.setAdapter(couponCampaignListAdapter);

                    } else {
                        showToast("Invalid Response");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                    hideBusyProgress();
                    Log.v(CouponCampaignsListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(userResetPasswordRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CouponCampaignsListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void assignViews() {
        mCouponCampaignsListRecyclerView = (ListView) findViewById(R.id.CouponCampaignsListRecyclerView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_new:
                Intent createCouponCampaignActivity = new Intent(CouponCampaignsListActivity.this, CreateCouponCampaignActivity.class);
                createCouponCampaignActivity.putExtra("clientId", clientId);
                createCouponCampaignActivity.putExtra("isEdit",false);
                startActivity(createCouponCampaignActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
