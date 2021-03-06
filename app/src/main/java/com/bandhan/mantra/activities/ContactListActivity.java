package com.bandhan.mantra.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
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
    int current_page = 0;
    private List<Datum> data;
    private Button btnRemoveFromGroup,btnAddToGroup;

    private ArrayAdapter GroupListArrayAdapter;
    private JSONArray GroupListjsonArray;
    private JSONObject selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        if(!isNetworkConnected()){
            showToast("Check Internet Connection");
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contactListListView = (ListView) findViewById(R.id.ConactListRecyclerView);
        linearLayoutContactItem = (LinearLayout)findViewById(R.id.linearLayoutContactItem);
        btnRemoveFromGroup = (Button)findViewById(R.id.btnRemoveFromGroup);
        btnAddToGroup = (Button)findViewById(R.id.btnAddToGroup);
        checkedContacts = new ArrayList<Datum>();
        data = new ArrayList<Datum>();

        Bundle extras = getIntent().getExtras();
        clientId = extras.getInt("clientId");
        contactGroupItemData = (ContactGroupItemData) getIntent().getSerializableExtra("groupData");
        groupId = extras.getInt("groupId");
        getGroupContactsByClientID(clientId, contactGroupItemData.getId());
        setListner();

        onButtonActionListener = new ContactListAdapter.OnButtonActionListener() {
            @Override
            public void onCheckBoxPressed(Datum datum, int position, boolean isChecked)
            {
                if (isChecked) {
                    checkedContacts.add(datum);
                    if(checkedContacts.size()>1){
                        linearLayoutContactItem.setVisibility(View.VISIBLE);
                    }
                    showToast(""+checkedContacts.size());
                } else if (!isChecked) {
                    checkedContacts.remove(datum);
                    if(checkedContacts.size()<1){
                        linearLayoutContactItem.setVisibility(View.GONE);
                    }
                    showToast(""+checkedContacts.size());
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

    private void setGroupList(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId", accessId);
            params.put("ClientId", "" + clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Group/GetGroupListByClientIdForCampaign", params);
            Log.v(ContactListActivity.class.getName(), "Req : " + urlWithParams);

            StringRequest GetGroupListByClientIdForCampaignRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            GroupListjsonArray = new JSONArray(Response);
                            String[] group = new String[GroupListjsonArray.length()];
                            for (int i = 0; i < GroupListjsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) GroupListjsonArray.getJSONObject(i);
                                group[i] = jsonObject.get("Name").toString();
                            }

                            GroupListArrayAdapter = new ArrayAdapter(ContactListActivity.this, android.R.layout.simple_spinner_item, group);
                            GroupListArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            //Setting the ArrayAdapter data on the Spinner
                            //mSpnCampaignGroup.setAdapter(GroupListArrayAdapter);

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(ContactListActivity.this);
                            builderSingle.setIcon(R.drawable.app_logo);
                            builderSingle.setTitle("Select Group :-");

                            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builderSingle.setAdapter(GroupListArrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //String strName = GroupListArrayAdapter.getItem(which);
                                    try {
                                        selectedGroup = GroupListjsonArray.getJSONObject(which);//parent.getItemAtPosition(position).toString();
                                        ContactsAddToGroup(selectedGroup);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        showToast(e.getMessage().toString());
                                    }
                                }
                            });
                            builderSingle.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("Invalid Response");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetGroupListByClientIdForCampaignRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void ContactsAddToGroup(JSONObject selectedGroup) {
        try {
            showBusyProgress();
            Map<String, String> param = new HashMap<String, String>();
            param.put("GroupId",selectedGroup.get("Id").toString());
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Group/ContactsAddToGroup",param);


            List<String> tmp = new ArrayList<String>();
            for(int i =0;i<checkedContacts.size();i++){
                tmp.add(checkedContacts.get(i).getId().toString());
            }
            final String requestBody = tmp.toString();//params.toString();
            Log.v(ContactListActivity.class.getName(),"Url : "+urlWithParams+"\nParams : "+requestBody);

            StringRequest contactsAddToGroupRequest = new StringRequest(Request.Method.POST, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateContactActivity.class.getName(), "onResponse :" + Response.toString());
                    if (Response.equals("")) {
                        //getGroupListByClientId(clientId);
                        showToast("Ok");
                        finish();
                    } else {
                        showToast("Invalid Response");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                    hideBusyProgress();
                    Log.v(CreateContactActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            VolleySingleton.getInstance().addToRequestQueue(contactsAddToGroupRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateContactActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void ContactsRemoveFromGroup(Integer selectedGroup) {
        try {
            showBusyProgress();
            Map<String, String> param = new HashMap<String, String>();
            param.put("GroupId", String.valueOf(selectedGroup));
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Group/ContactsRemoveFromGroup",param);

            List<String> tmp = new ArrayList<String>();
            for(int i =0;i<checkedContacts.size();i++){
                tmp.add(checkedContacts.get(i).getId().toString());
            }
            final String requestBody = tmp.toString();//params.toString();
            Log.v(ContactListActivity.class.getName(),"Url : "+urlWithParams+"\nParams : "+requestBody);

            StringRequest contactsRemoveFromGroupRequest = new StringRequest(Request.Method.POST, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateContactActivity.class.getName(), "onResponse :" + Response.toString());
                    if (Response.equals("")) {
                        //getGroupListByClientId(clientId);
                        showToast("Ok");
                        finish();
                    } else {
                        showToast("Invalid Response");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                    hideBusyProgress();
                    Log.v(CreateContactActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            VolleySingleton.getInstance().addToRequestQueue(contactsRemoveFromGroupRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateContactActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void setListner(){
        btnAddToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGroupList(clientId);
            }
        });
        btnRemoveFromGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsRemoveFromGroup(contactGroupItemData.getId());
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
            request.put("Reverse", "false");
            request.put("Search", "");
            request.put("GroupId", groupId);
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
