package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.GroupContactsByClientIdData;
import com.bandhan.mantra.volley.GsonRequest;
import com.bandhan.mantra.volley.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactListActivity extends BaseActivity {

    private FloatingActionButton addContactFab;
    private Integer clientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addContactFab = (FloatingActionButton) findViewById(R.id.addContactFab);
        Bundle extras = getIntent().getExtras();
        clientId = extras.getInt("clientId");
        getGroupContactsByClientID(clientId);

        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactListActivity.this,CreateContactActivity.class));
            }
        });
    }
    private void getGroupContactsByClientID(Integer clientId){
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("Page", "1");
        requestMap.put("ItemsPerPage", "10");
        requestMap.put("SortBy", "");
        requestMap.put("Reverse", "true");
        requestMap.put("Search", "");

        Map<String, String> mainRequestMap = new HashMap<String, String>();
        mainRequestMap.put("pagingInfo", String.valueOf(requestMap));
        mainRequestMap.put("ClientId", ""+clientId);
        try {
            /*JsonObjectRequest getGroupContactsByClientIDRequest = new JsonObjectRequest(Request.Method.POST,
                    VolleySingleton.getWsBaseUrl()+"Contact/GetGroupIdWisePresentContactsPagedListByClientId", new JSONObject(mainRequestMap),
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), response.toString());
                    GroupContactsByClientIdData groupContactsByClientIdData = new Gson().fromJson(response,);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), error.getMessage());

                }
            });*/
            GsonRequest<GroupContactsByClientIdData> getGroupContactsByClientIDRequest = new GsonRequest<GroupContactsByClientIdData>(Request.Method.POST,
                    VolleySingleton.getWsBaseUrl() + "Contact/GetGroupIdWisePresentContactsPagedListByClientId", mainRequestMap.toString(), GroupContactsByClientIdData.class,
                    new Response.Listener<GroupContactsByClientIdData>() {
                        @Override
                        public void onResponse(GroupContactsByClientIdData response) {
                            showToast(""+response.getData().get(0).getId());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), VolleySingleton.getErrorMessage(error).toString());//error.getMessage().toString());
                }
            });
            getGroupContactsByClientIDRequest.setRetryPolicy(VolleySingleton.getDefaultRetryPolice());
            getGroupContactsByClientIDRequest.setShouldCache(true);
            VolleySingleton.getInstance().addToRequestQueue(getGroupContactsByClientIDRequest);
        }catch (Exception e){
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
}
