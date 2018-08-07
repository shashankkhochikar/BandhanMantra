package com.bandhan.mantra.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.adapter.ContactListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.ContactGroupItemData;
import com.bandhan.mantra.model.Datum;
import com.bandhan.mantra.model.GroupContactsByClientIdData;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListActivity extends BaseActivity {

    private ListView contactListListView;
    private Integer clientId, groupId;
    private ContactListAdapter contactListAdapter;
    private ContactListAdapter.OnButtonActionListener onButtonActionListener;
    private ContactGroupItemData contactGroupItemData;
    private LinearLayout linearLayoutContactItem;
    private List<Datum> checkedContacts;
    int current_page = 1;
    private List<Datum> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contactListListView = (ListView) findViewById(R.id.ConactListRecyclerView);
        linearLayoutContactItem = (LinearLayout)findViewById(R.id.linearLayoutContactItem);
        checkedContacts = new ArrayList<Datum>();
        data = new ArrayList<Datum>();

        Bundle extras = getIntent().getExtras();
        clientId = extras.getInt("clientId");
        contactGroupItemData = (ContactGroupItemData) getIntent().getSerializableExtra("groupData");
        groupId = extras.getInt("groupId");
        getGroupContactsByClientID(clientId, groupId);

        onButtonActionListener = new ContactListAdapter.OnButtonActionListener() {
            @Override
            public void onCheckBoxPressed(Datum datum, int position, boolean isChecked) {
               if(isChecked == false){
                    checkedContacts.remove(datum);
                    /*if(checkedContacts.size()<0){
                        linearLayoutContactItem.setVisibility(View.GONE);
                    }else {
                        linearLayoutContactItem.setVisibility(View.VISIBLE);
                    }*/
                   showToast("size : "+checkedContacts.size()+"\npos : "+position+"\nisChecked : "+isChecked);
                }
                if(isChecked == true){
                    checkedContacts.add(datum);
                    /*if(checkedContacts.size()<0){
                        linearLayoutContactItem.setVisibility(View.GONE);
                    }else {
                        linearLayoutContactItem.setVisibility(View.VISIBLE);
                    }*/
                    showToast("size : "+checkedContacts.size()+"\npos : "+position+"\nisChecked : "+isChecked);
                }

            }

            @Override
            public void onDeleteButtonPressed(Datum datum, int position) {
                deleteSingleContact(datum,position);
            }

            @Override
            public void onViewClicked(Datum datum, int position) {
                Intent editContactintent = new Intent(ContactListActivity.this,CreateContactActivity.class);
                editContactintent.putExtra("contactData",datum);
                editContactintent.putExtra("clientId",clientId);
                editContactintent.putExtra("groupData", contactGroupItemData);
                editContactintent.putExtra("isEdit",true);
                startActivity(editContactintent);

            }
        };
        contactListListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = contactListListView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (contactListListView.getLastVisiblePosition() >= count - threshold ) {
                        Log.i(ContactListActivity.class.getName(), "loading more data");
                        // Execute LoadMoreDataTask AsyncTask
                        getGroupContactsByClientID(clientId, groupId);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void deleteSingleContact(Datum datum, final int position) {
        try {
            showBusyProgress();
            /*JSONObject params = new JSONObject();
            //params.put("id",""+datum.getId());
            final String requestBody = params.toString();*/

            Map<String, String> param = new HashMap<String, String>();
            param.put("id",""+datum.getId());
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Contact/RemoveContact",param);

            StringRequest removeContactRequest = new StringRequest(Request.Method.POST, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (Response.equals("")) {
                       showToast("Ok");
                       contactListAdapter.removeItem(position);
                    } else {
                        showToast("Invalid Response");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hideBusyProgress();
                    //Log.v(ContactListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(error).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(error).toString());

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,HttpHeaderParser.parseCharset(response.headers, "utf-8"));// Now you can use any deserializer to make sense of data
                            //JSONObject obj = new JSONObject(res);
                            Log.v(ContactListActivity.class.getName(), "onErrorResponse 1 \n\n" + res);
                        } catch (UnsupportedEncodingException e1) { // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (Exception e2) {// returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }


            };
            VolleySingleton.getInstance().addToRequestQueue(removeContactRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void getGroupContactsByClientID(final int clientId, final Integer groupId) {
        try {
            showBusyProgress();
            current_page += 1;
            Map<String, String> params = new HashMap<String, String>();
            params.put("ClientId", "" + clientId);
            String urlWithParams =createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Contact/GetGroupIdWisePresentContactsPagedListByClientId", params);
//"http://35.231.207.193/msgAPI/api/Contact/GetGroupIdWisePresentContactsPagedListByClientId?ClientId=1";
            final JSONObject request = new JSONObject();
            request.put("Page", current_page);
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
                                for(int i=0;i<response.getData().size();i++){
                                    data.add(response.getData().get(i));
                                }
                                contactListAdapter = new ContactListAdapter(ContactListActivity.this,data,onButtonActionListener);
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
                Intent newContactintent = new Intent(ContactListActivity.this,CreateContactActivity.class);
                newContactintent.putExtra("clientId",clientId);
                newContactintent.putExtra("groupData", contactGroupItemData);
                newContactintent.putExtra("isEdit",false);
                startActivity(newContactintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
