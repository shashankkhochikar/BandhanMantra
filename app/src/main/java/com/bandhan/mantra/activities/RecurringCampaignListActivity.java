package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.bandhan.mantra.adapter.RecurringCampaignListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.Item;
import com.bandhan.mantra.model.RecurringCampaignList;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RecurringCampaignListActivity extends BaseActivity {

    private ListView mRecurringCampaignListRecyclerView;
    private SwipeRefreshLayout mSwiperefresh;
    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;
    private RecurringCampaignListAdapter recurringCampaignListAdapter;
    private RecurringCampaignListAdapter.OnButtonActionListener onButtonActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_campaign_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();
        GetRecurringCampaignsbyClientId(clientId);
        onButtonActionListener = new RecurringCampaignListAdapter.OnButtonActionListener() {
            @Override
            public void onViewClicked(Item item, int position) {
                Intent editContactintent = new Intent(RecurringCampaignListActivity.this,CreateRecurringCampaignActivity.class);
                editContactintent.putExtra("recurringCampaignData",item);
                editContactintent.putExtra("clientId",clientId);
                editContactintent.putExtra("isEdit",true);
                startActivity(editContactintent);
            }
        };
        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwiperefresh.setRefreshing(true);
                GetRecurringCampaignsbyClientId(clientId);
            }
        });
    }

    private void assignViews() {
        mRecurringCampaignListRecyclerView = (ListView) findViewById(R.id.RecurringCampaignListRecyclerView);
        mSwiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
    }

    private void GetRecurringCampaignsbyClientId(final int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("ClientId", "" + clientId);
            String urlWithParams =createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "RecurringCampaign/GetRecurringCampaignsbyClientId", params);
//"http://35.231.207.193/msgAPI/api/Contact/GetGroupIdWisePresentContactsPagedListByClientId?ClientId=1";
            final JSONObject request = new JSONObject();
            Log.v(RecurringCampaignList.class.getName(),request.toString());

            GsonRequest<RecurringCampaignList> getRecurringCampaignsbyClientIdRequest = new GsonRequest<RecurringCampaignList>(
                    Request.Method.GET,
                    urlWithParams,
                    request.toString(),
                    RecurringCampaignList.class,
                    new Response.Listener<RecurringCampaignList>() {
                        @Override
                        public void onResponse(RecurringCampaignList response) {
                            hideBusyProgress();
                            Log.v(RecurringCampaignList.class.getName(), "onResponse"+response.toString());
                            if(response.getItems() != null)
                            {
                                recurringCampaignListAdapter = new RecurringCampaignListAdapter(RecurringCampaignListActivity.this,response.getItems(),onButtonActionListener);
                                mRecurringCampaignListRecyclerView.setAdapter(recurringCampaignListAdapter);
                                mSwiperefresh.setRefreshing(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideBusyProgress();
                    mSwiperefresh.setRefreshing(false);
                    Log.v(RecurringCampaignList.class.getName(), VolleySingleton.getErrorMessage(error).toString());
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));// Now you can use any deserializer to make sense of data
                            //JSONObject obj = new JSONObject(res);
                            Log.v(RecurringCampaignList.class.getName(), "onErrorResponse1 \n\n" + res);
                        } catch (UnsupportedEncodingException e1) { // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (Exception e2) {// returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }){
                /*@Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }*/
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };
            getRecurringCampaignsbyClientIdRequest.setRetryPolicy(VolleySingleton.getDefaultRetryPolice());
            getRecurringCampaignsbyClientIdRequest.setShouldCache(true);
            VolleySingleton.getInstance().addToRequestQueue(getRecurringCampaignsbyClientIdRequest);
        } catch (Exception e) {
            hideBusyProgress();
            mSwiperefresh.setRefreshing(false);
            Log.v(RecurringCampaignList.class.getName(), e.getMessage().toString());
            showToast("Something went wrong. Please try again later.");
        }
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
                Intent newCampaignintent = new Intent(RecurringCampaignListActivity.this,CreateRecurringCampaignActivity.class);
                newCampaignintent.putExtra("clientId",clientId);
                newCampaignintent.putExtra("isEdit",false);
                startActivity(newCampaignintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
