package com.bandhan.mantra.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.adapter.TemplateListAdapter;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.TemplateItem;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
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

public class TemplatesListActivity extends BaseActivity {

    private ListView mTemplatesRecyclerView;
    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;
    public List<TemplateItem> templateList;
    private TemplateListAdapter templateListAdapter;
    private TemplateListAdapter.OnButtonActionListener onButtonActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();

        assignViews();
        GetTemplateListByClientId(clientId);
        onButtonActionListener = new TemplateListAdapter.OnButtonActionListener() {
            @Override
            public void onViewClicked(TemplateItem templateItem, int position) {
                showEditTemplateDialog(templateItem);
            }
        };

    }

    private void GetTemplateListByClientId(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId",accessId);
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Template/GetTemplateListByClientId",params);
            StringRequest GetTemplateListByClientIdRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(ContactListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {

                        templateList = new ArrayList<TemplateItem>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(Response);
                            Log.e(TemplatesListActivity.class.getName(),""+jsonArray.get(0).toString());
                            for(int i=0;i<jsonArray.length();i++){
                                TemplateItem tmptestData = new Gson().fromJson(jsonArray.get(i).toString(),TemplateItem.class);
                                templateList.add(tmptestData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        templateListAdapter = new TemplateListAdapter(TemplatesListActivity.this, templateList,onButtonActionListener);
                        mTemplatesRecyclerView.setAdapter(templateListAdapter);

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
            VolleySingleton.getInstance().addToRequestQueue(GetTemplateListByClientIdRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(ContactListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void createNewTemplate(final int clientId,final String title,String msg) {
        try {
            showBusyProgress();
            JSONObject params = new JSONObject();
            params.put("Id", "0");
            params.put("Title", title);
            params.put("ClientID", ""+clientId);
            params.put("Message", msg);
            final String requestBody = params.toString();

            StringRequest createTemplateRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Template/CreateTemplate", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(TemplatesListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        GetTemplateListByClientId(clientId);
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
                    Log.v(TemplatesListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
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
            VolleySingleton.getInstance().addToRequestQueue(createTemplateRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(TemplatesListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void assignViews() {
        mTemplatesRecyclerView = (ListView) findViewById(R.id.TemplatesListView);
    }

    private void showCreateTemplateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_template_custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText Title = (EditText) dialogView.findViewById(R.id.edtTemplateTitle);
        final EditText Msg = (EditText) dialogView.findViewById(R.id.edtTemplateMsg);

        //dialogBuilder.setTitle("Custom dialog");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                String title = Title.getText().toString();
                String msg = Msg.getText().toString();

                if (title.equals("") && msg.equals("")) {
                    showToast("fill all data");
                } else {
                    createNewTemplate(clientId,title,msg);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void showEditTemplateDialog(final TemplateItem templateItem) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_template_custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText Title = (EditText) dialogView.findViewById(R.id.edtTemplateTitle);
        final EditText Msg = (EditText) dialogView.findViewById(R.id.edtTemplateMsg);

        Title.setText(templateItem.getTitle());
        Msg.setText(templateItem.getMessage());

        //dialogBuilder.setTitle("Custom dialog");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                String title = Title.getText().toString();
                String msg = Msg.getText().toString();
                if (title.equals("") && msg.equals("")) {
                    showToast("fill all data");
                } else {
                    editTemplate(templateItem.getId(), clientId, title, msg);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void editTemplate(Integer id, final int clientId, String title, String msg) {
        try {
            showBusyProgress();
            JSONObject params = new JSONObject();
            params.put("Id", id);
            params.put("Title", title);
            params.put("ClientID", ""+clientId);
            params.put("Message", msg);
            final String requestBody = params.toString();

            StringRequest editTemplateRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Template/EditTemplate", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(TemplatesListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (Response.equals("")) {
                        GetTemplateListByClientId(clientId);
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
                    Log.v(TemplatesListActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
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
            VolleySingleton.getInstance().addToRequestQueue(editTemplateRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(TemplatesListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
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
                showCreateTemplateDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
