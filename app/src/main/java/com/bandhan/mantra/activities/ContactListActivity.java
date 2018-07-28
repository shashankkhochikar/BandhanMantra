package com.bandhan.mantra.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bandhan.mantra.R;
import com.bandhan.mantra.adapter.ContactListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.Datum;
import com.bandhan.mantra.model.GroupContactsByClientIdData;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ContactListActivity extends BaseActivity {

    private ListView contactListListView;
    private Integer clientId, groupId;
    private ContactListAdapter contactListAdapter;
    private ContactListAdapter.OnButtonActionListener onButtonActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contactListListView = (ListView) findViewById(R.id.ConactListRecyclerView);
        Bundle extras = getIntent().getExtras();
        clientId = extras.getInt("clientId");
        groupId = extras.getInt("groupId");
        getGroupContactsByClientID(clientId, groupId);

        onButtonActionListener = new ContactListAdapter.OnButtonActionListener() {
            @Override
            public void onCheckBoxPressed(Datum datum, int position, boolean isChecked) {

            }

            @Override
            public void onDeleteButtonPressed(Datum datum, int position) {

            }

            @Override
            public void onViewClicked(Datum datum, int position) {

            }
        };
    }

    private void getGroupContactsByClientID(final int clientId, final Integer groupId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("ClientId", "" + clientId);
            String urlWithParams =createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Contact/GetGroupIdWisePresentContactsPagedListByClientId", params);
//"http://35.231.207.193/msgAPI/api/Contact/GetGroupIdWisePresentContactsPagedListByClientId?ClientId=1";
            final JSONObject request = new JSONObject();
            request.put("Page", "1");
            request.put("ItemsPerPage", "10");
            request.put("SortBy", "");
            request.put("Reverse", "true");
            request.put("Search", "");
            request.put("GroupId", ""+groupId);
            Log.v(ContactListActivity.class.getName(),request.toString());

            GsonRequest<GroupContactsByClientIdData> getGroupContactsByClientIDRequest = new GsonRequest<GroupContactsByClientIdData>(
                    Request.Method.POST,
                    urlWithParams,
                    request.toString(),
                    GroupContactsByClientIdData.class,
                    new Response.Listener<GroupContactsByClientIdData>() {
                        @Override
                        public void onResponse(GroupContactsByClientIdData response) {
                            hideBusyProgress();
                            Log.v(ContactListActivity.class.getName(), "onResponse"+response.toString());
                            if(response.getData() != null)
                            {
                                contactListAdapter = new ContactListAdapter(ContactListActivity.this,response.getData(),onButtonActionListener);
                                contactListListView.setAdapter(contactListAdapter);
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
                //getGroupNameDialouge();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
