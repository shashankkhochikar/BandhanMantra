package com.bandhan.mantra.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactGroupsListActivity extends BaseActivity {

    private ListView gropuListListView;
    private SessionManager sessionManager;
    public UserData userData;
    private ContactGropusListAdapter contactGropusListAdapter;
    private ContactGropusListAdapter.OnButtonActionListener onButtonActionListener;
    public int clientId = 0;
    public List<ContactGroupItemData> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_groups_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gropuListListView = (ListView) findViewById(R.id.gropuListView);
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();

        getGroupListByClientId(clientId);
        onButtonActionListener = new ContactGropusListAdapter.OnButtonActionListener() {
            @Override
            public void onEditButtonPressed(ContactGroupItemData contactGroupItemData, int position) {
                showEditGroupNameDialouge(contactGroupItemData);
            }

            @Override
            public void onDeleteButtonPressed(ContactGroupItemData contactGroupItemData, int position) {
                deleteGroupById(contactGroupItemData.getId(),position);
            }

            @Override
            public void onViewClicked(ContactGroupItemData contactGroupItemData, int position) {
                showGroupContactsByClientID(clientId,contactGroupItemData.getId());
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

                        groupList = new ArrayList<ContactGroupItemData>();
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

    private void showGroupContactsByClientID(int clientId, int grpId){
        Intent ContactListActivity = new Intent(ContactGroupsListActivity.this, ContactListActivity.class);
        ContactListActivity.putExtra("clientId", clientId);
        ContactListActivity.putExtra("groupId", grpId);
        startActivity(ContactListActivity);
    }

    private void createNewGroup(final int clientId,final String groupName) {
        try {
            showBusyProgress();
            JSONObject params = new JSONObject();
            params.put("Id", "0");
            params.put("Name", groupName);
            params.put("ClientID", ""+clientId);
            params.put("ContactCount", "0");
            params.put("Contacts", "");
            final String requestBody = params.toString();

            StringRequest createGroupRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Group/CreateGroup", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactGroupsListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        getGroupListByClientId(clientId);
                        showToast("Ok");
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
            VolleySingleton.getInstance().addToRequestQueue(createGroupRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactGroupsListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void getGroupNameDialouge() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContactGroupsListActivity.this);
        alertDialog.setTitle("New Group");
        alertDialog.setMessage("Enter Name");

        final EditText input = new EditText(ContactGroupsListActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(10,0,10,0);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);

        alertDialog.setIcon(R.drawable.app_logo);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        if(!name.equals("")){
                            createNewGroup(clientId,name);
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
/*************************************************************************************/
    private void editGroup(final int clientId,final String groupName,final int groupId) {
        try {
            showBusyProgress();
            JSONObject params = new JSONObject();
            params.put("Id",""+groupId);
            params.put("Name", groupName);
            params.put("ClientID", ""+clientId);
            params.put("ContactCount", "0");
            params.put("Contacts", "");
            final String requestBody = params.toString();

            StringRequest editGroupRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Group/EditGroup", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactGroupsListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("An error occurred, please try again or contact the administrator.")) {
                        getGroupListByClientId(clientId);
                        showToast("Ok");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                    hideBusyProgress();
                    Log.v(ContactGroupsListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
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
            VolleySingleton.getInstance().addToRequestQueue(editGroupRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactGroupsListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void showEditGroupNameDialouge(final ContactGroupItemData contactGroupItemData) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContactGroupsListActivity.this);
        alertDialog.setTitle("Edit Group");
        alertDialog.setMessage("Enter New Name");

        final EditText input = new EditText(ContactGroupsListActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(10,0,10,0);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setText(contactGroupItemData.getName().toString());
        alertDialog.setView(input);

        alertDialog.setIcon(R.drawable.app_logo);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        if(!name.equals("")){
                            editGroup(clientId,name,contactGroupItemData.getId());
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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
                getGroupNameDialouge();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
