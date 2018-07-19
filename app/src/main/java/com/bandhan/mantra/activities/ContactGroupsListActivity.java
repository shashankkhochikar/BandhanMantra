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
                startActivity(new Intent(ContactGroupsListActivity.this,ContactListActivity.class));
            }
        });
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        Integer clientId = userData.getId();
        getGroupListByClientId(clientId);
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
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            ContactGroupItem clientes= new Gson().fromJson(Response, ContactGroupItem.class);
                            groupList.addAll(clientes.getContactGroupItemData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //ContactGroupItem contactGroupItem  = new Gson().fromJson(Response, ContactGroupItem.class);
                        //groupList.addAll(contactGroupItem.getContactGroupItemData());

                        ContactGropusListAdapter contactGropusListAdapter = new ContactGropusListAdapter(ContactGroupsListActivity.this, groupList);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
