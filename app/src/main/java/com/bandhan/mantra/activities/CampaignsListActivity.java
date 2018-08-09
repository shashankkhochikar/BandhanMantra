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
import com.bandhan.mantra.R;
import com.bandhan.mantra.adapter.CampaignListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.CampaignsListByClientIdData;
import com.bandhan.mantra.model.CampaignsListItem;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampaignsListActivity extends BaseActivity {

    private ListView CampaignListListView;
    private SwipeRefreshLayout mSwiperefresh;
    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;
    private CampaignListAdapter campaignListAdapter;
    private CampaignListAdapter.OnButtonActionListener onButtonActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CampaignListListView =(ListView)findViewById(R.id.CampaignListRecyclerView);
        mSwiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();
        getCampaignsListByClientID(clientId);
        onButtonActionListener = new CampaignListAdapter.OnButtonActionListener() {
            @Override
            public void onViewClicked(CampaignsListItem campaignsListItem, int position) {
                //showToast(""+campaignsListItem.getGroupName());
                Intent editContactintent = new Intent(CampaignsListActivity.this,CreateCampaignActivity.class);
                editContactintent.putExtra("campaignData",campaignsListItem);
                editContactintent.putExtra("clientId",clientId);
                editContactintent.putExtra("isEdit",true);
                startActivity(editContactintent);
            }
        };
        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwiperefresh.setRefreshing(true);
                getCampaignsListByClientID(clientId);
            }
        });
    }

    private void getCampaignsListByClientID(final int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("ClientId", "" + clientId);
            String urlWithParams =createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Campaign/GetCampaignPagedListbyClientId", params);
//"http://35.231.207.193/msgAPI/api/Contact/GetGroupIdWisePresentContactsPagedListByClientId?ClientId=1";
            final JSONObject request = new JSONObject();
            request.put("Page", "1");
            request.put("ItemsPerPage", "10");
            request.put("SortBy", "");
            request.put("Reverse", "true");
            request.put("Search", "");
            Log.v(ContactListActivity.class.getName(),request.toString());

            GsonRequest<CampaignsListByClientIdData> getGroupContactsByClientIDRequest = new GsonRequest<CampaignsListByClientIdData>(
                    Request.Method.POST,
                    urlWithParams,
                    request.toString(),
                    CampaignsListByClientIdData.class,
                    new Response.Listener<CampaignsListByClientIdData>() {
                        @Override
                        public void onResponse(CampaignsListByClientIdData response) {
                            hideBusyProgress();
                            Log.v(ContactListActivity.class.getName(), "onResponse"+response.toString());
                            if(response.getData() != null)
                            {
                                campaignListAdapter = new CampaignListAdapter(CampaignsListActivity.this,response.getData(),onButtonActionListener);
                                CampaignListListView.setAdapter(campaignListAdapter);
                                mSwiperefresh.setRefreshing(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideBusyProgress();
                    mSwiperefresh.setRefreshing(false);
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
            getGroupContactsByClientIDRequest.setRetryPolicy(VolleySingleton.getDefaultRetryPolice());
            getGroupContactsByClientIDRequest.setShouldCache(true);
            VolleySingleton.getInstance().addToRequestQueue(getGroupContactsByClientIDRequest);
        } catch (Exception e) {
            hideBusyProgress();
            mSwiperefresh.setRefreshing(false);
            Log.v(ContactListActivity.class.getName(), e.getMessage().toString());
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
                Intent newCampaignintent = new Intent(CampaignsListActivity.this,CreateCampaignActivity.class);
                newCampaignintent.putExtra("clientId",clientId);
                newCampaignintent.putExtra("isEdit",false);
                startActivity(newCampaignintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
