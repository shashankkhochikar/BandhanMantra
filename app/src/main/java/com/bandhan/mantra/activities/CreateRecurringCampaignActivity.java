package com.bandhan.mantra.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.Item;
import com.bandhan.mantra.volley.VolleySingleton;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateRecurringCampaignActivity extends BaseActivity {

    private LinearLayout mContainer;
    private TextInputEditText mEtdName;
    private Spinner mSpnCampaignGroup;
    private Spinner mSpnCampaignSubGroup;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonCampaign;
    private RadioButton mRadioButtonCoupon;
    private Spinner mSpnCampaignSubgroup;
    private Spinner mSpnCampaignTemplate;
    private TextInputEditText mEdtCampaignMsg;
    private RadioGroup mRadioGroupRecurrence;
    private RadioButton mRadioButtonDaily;
    private RadioButton mRadioButtonWeekly;
    private RadioButton mRadioButtonMonthly;
    private TextInputEditText mEtdRecurrenceDays;
    private RadioGroup mRadioGroupEndingDate;
    private RadioButton mRadioButtonNoEndDate;
    private RadioButton mRadioButtonSetEndDate;
    private TextInputEditText mEtdSetEndDate;
    private Button mBtnSubmit;
    private Item item;

    private ArrayAdapter TemplateListArrayAdapter;
    private ArrayAdapter GroupListArrayAdapter;
    private ArrayAdapter LanguageArrayAdapter;
    private JSONArray TemplateListjsonArray;
    private JSONArray GroupListjsonArray;

    private int clientId=0;
    private boolean isEdit = false;
    private SimpleDateFormat simpleDateFormat;
    private SpinnerDatePickerDialogBuilder ScheduledDatePickerDialogBuilder;
    private JSONObject selectedGroup;
    private JSONObject selectedTemplate;
    private String selectedLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recurring_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        Bundle extras = getIntent().getExtras();
        isEdit = extras.getBoolean("isEdit");
        if(isEdit){
            clientId=extras.getInt("clientId");
            item = (Item) getIntent().getSerializableExtra("recurringCampaignData");
           // setValues(campaignsListItem);
            for (int i = 0; i < mContainer.getChildCount();  i++ ){
                View view = mContainer.getChildAt(i);
                view.setEnabled(false); // Or whatever you want to do with the view.
            }
        }else if(!isEdit){
            clientId=extras.getInt("clientId");
           // clearData();
        }
        setGroupListSpinner(clientId);
        setTemplateListSpinner(clientId);
         /*setLanguageSpinner();
        setListners();*/
    }

    private void assignViews() {
        mContainer = (LinearLayout) findViewById(R.id.container);
        mEtdName = (TextInputEditText) findViewById(R.id.etdName);
        mSpnCampaignGroup = (Spinner) findViewById(R.id.spnCampaignGroup);
        mSpnCampaignSubGroup = (Spinner) findViewById(R.id.spnCampaignSubGroup);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioButtonCampaign = (RadioButton) findViewById(R.id.radioButtonCampaign);
        mRadioButtonCoupon = (RadioButton) findViewById(R.id.radioButtonCoupon);
        mSpnCampaignSubgroup = (Spinner) findViewById(R.id.spnCampaignSubgroup);
        mSpnCampaignTemplate = (Spinner) findViewById(R.id.spnCampaignTemplate);
        mEdtCampaignMsg = (TextInputEditText) findViewById(R.id.edtCampaignMsg);
        mRadioGroupRecurrence = (RadioGroup) findViewById(R.id.radioGroupRecurrence);
        mRadioButtonDaily = (RadioButton) findViewById(R.id.radioButtonDaily);
        mRadioButtonWeekly = (RadioButton) findViewById(R.id.radioButtonWeekly);
        mRadioButtonMonthly = (RadioButton) findViewById(R.id.radioButtonMonthly);
        mEtdRecurrenceDays = (TextInputEditText) findViewById(R.id.etdRecurrenceDays);
        mRadioGroupEndingDate = (RadioGroup) findViewById(R.id.radioGroupEndingDate);
        mRadioButtonNoEndDate = (RadioButton) findViewById(R.id.radioButtonNoEndDate);
        mRadioButtonSetEndDate = (RadioButton) findViewById(R.id.radioButtonSetEndDate);
        mEtdSetEndDate = (TextInputEditText) findViewById(R.id.etdSetEndDate);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
    }


    private void setTemplateListSpinner(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId",accessId);
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Template/GetTemplateListByClientId",params);
            Log.v(CreateRecurringCampaignActivity.class.getName(), "Req : "+urlWithParams);

            StringRequest GetTemplateListByClientIdRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateRecurringCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            TemplateListjsonArray = new JSONArray(Response);
                            String[] group = new String[TemplateListjsonArray.length()];
                            for(int i=0;i<TemplateListjsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) TemplateListjsonArray.getJSONObject(i);
                                group[i]=jsonObject.get("Message").toString();
                            }
                            TemplateListArrayAdapter = new ArrayAdapter(CreateRecurringCampaignActivity.this,android.R.layout.simple_spinner_item,group);
                            TemplateListArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            mSpnCampaignTemplate.setAdapter(TemplateListArrayAdapter);
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
                    Log.v(CreateRecurringCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetTemplateListByClientIdRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateRecurringCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void setGroupListSpinner(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId",accessId);
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Group/GetGroupListByClientIdForCampaign",params);
            Log.v(CreateRecurringCampaignActivity.class.getName(), "Req : "+urlWithParams);

            StringRequest GetGroupListByClientIdForCampaignRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateRecurringCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            GroupListjsonArray = new JSONArray(Response);
                            String[] group = new String[GroupListjsonArray.length()];
                            for(int i=0;i<GroupListjsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) GroupListjsonArray.getJSONObject(i);
                                group[i]=jsonObject.get("Name").toString();
                            }

                            GroupListArrayAdapter = new ArrayAdapter(CreateRecurringCampaignActivity.this,android.R.layout.simple_spinner_item,group);
                            GroupListArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            mSpnCampaignGroup.setAdapter(GroupListArrayAdapter);
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
                    Log.v(CreateRecurringCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetGroupListByClientIdForCampaignRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateRecurringCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
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
        getMenuInflater().inflate(R.menu.activity_create_contact_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_edit:
                for ( int i = 0; i < mContainer.getChildCount();  i++ ){
                    View view = mContainer.getChildAt(i);
                    view.setEnabled(true); // Or whatever you want to do with the view.
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
