package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.adapter.ContactGropusListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.ContactGroupItem;
import com.bandhan.mantra.model.ContactGroupItemData;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.VolleySingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactGroupsListActivity extends BaseActivity {

    private FloatingActionButton addGroupFab;
    private ListView gropuListListView;
    private SessionManager sessionManager;
    public UserData userData;
    private ContactGropusListAdapter contactGropusListAdapter;
    private ContactGropusListAdapter.OnButtonActionListener onButtonActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_groups_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addGroupFab = (FloatingActionButton) findViewById(R.id.addGroupFab);
        gropuListListView = (ListView) findViewById(R.id.gropuListView);

        addGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        final Integer clientId = userData.getId();

        getGroupListByClientId(clientId);
        onButtonActionListener = new ContactGropusListAdapter.OnButtonActionListener() {
            @Override
            public void onDeleteButtonPressed(ContactGroupItemData contactGroupItemData, int position) {
                deleteGroupById(contactGroupItemData.getId(),position);
            }

            @Override
            public void onViewClicked(ContactGroupItemData contactGroupItemData, int position) {
                showGroupContactsByClientID(clientId,position);
            }
        };
    }

    private void getGroupListByClientId(Integer clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId",accessId);
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Group/GetGroupListByClientId",params);
            StringRequest userResetPasswordRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {

                        List<ContactGroupItemData> groupList = new ArrayList<ContactGroupItemData>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(Response);
                            Log.e(ContactGroupsListActivity.class.getName(),""+jsonArray.get(0).toString());
                            for(int i=0;i<jsonArray.length();i++){
                                ContactGroupItemData tmptestData = new Gson().fromJson(jsonArray.get(i).toString(),ContactGroupItemData.class);
                                groupList.add(tmptestData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        contactGropusListAdapter = new ContactGropusListAdapter(ContactGroupsListActivity.this, groupList,onButtonActionListener);
                        gropuListListView.setAdapter(contactGropusListAdapter);

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
            VolleySingleton.getInstance().addToRequestQueue(userResetPasswordRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }

    }

    private void deleteGroupById(final Integer grpId, final int position){
        try {
            showBusyProgress();
            StringRequest deleteGroupRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Group/RemoveGroup", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactGroupsListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        if (Response.equals("true")) {
                            contactGropusListAdapter.removeItem(position);
                            showToast("Group Deleted Successfully !!!");
                        } else if (Response.equals("false")){
                            showToast("Invalid Group");
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
                    Log.v(ContactGroupsListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", ""+grpId);
                    return params;
                }
            };
            VolleySingleton.getInstance().addToRequestQueue(deleteGroupRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactGroupsListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void showGroupContactsByClientID(Integer clientId, int position){
        Intent ContactListActivity = new Intent(ContactGroupsListActivity.this, ContactListActivity.class);
        ContactListActivity.putExtra("clientId", clientId);
        startActivity(ContactListActivity);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
