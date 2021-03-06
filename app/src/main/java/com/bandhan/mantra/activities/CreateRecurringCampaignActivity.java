package com.bandhan.mantra.activities;

import android.annotation.SuppressLint;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.commons.WeekdaysWidget;
import com.bandhan.mantra.model.Item;
import com.bandhan.mantra.volley.VolleySingleton;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateRecurringCampaignActivity extends BaseActivity {

    private LinearLayout mContainer;
    private TextInputEditText mEtdName;
    private Spinner mSpnCampaignGroup;
    private Spinner mSpnCampaignSubgroup;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonCampaign;
    private RadioButton mRadioButtonCoupon;
    private LinearLayout mLinearLayoutExpireAfter;
    private EditText mEdtExpiryAfter;
    private Spinner mSpnExpiresAfter;
    private TextInputEditText mEdtMinPurchaseAmt;
    private Spinner mSpnCampaignTemplate;
    private TextInputEditText mEdtCampaignMsg;
    private RadioGroup mRadioGroupRecurrence;
    private RadioButton mRadioButtonDaily;
    private RadioButton mRadioButtonWeekly;
    private RadioButton mRadioButtonMonthly;
    private LinearLayout mRecurrenceDays;
    private TextInputEditText mEtdRecurrenceDays;
    private LinearLayout mRecurrenceMonths;
    private TextInputEditText mEtdRecurrenceMonths;
    private LinearLayout mRecurrenceWeeks;
    private WeekdaysWidget mWidget1;
    private LinearLayout mLinearLayoutSetEndDate;
    private RadioGroup mRadioGroupEndingDate;
    private RadioButton mRadioButtonNoEndDate;
    private RadioButton mRadioButtonSetEndDate;
    private TextInputEditText mEtdSetEndDate;
    private Button mBtnSubmit;
    private Item item;

    private WeekdaysWidget widget;
    private ArrayAdapter TemplateListArrayAdapter;
    private ArrayAdapter GroupListArrayAdapter;
    private ArrayAdapter LanguageArrayAdapter;
    private JSONArray TemplateListjsonArray;
    private JSONArray GroupListjsonArray;

    private int clientId=0;
    private boolean isEdit = false;
    private SimpleDateFormat simpleDateFormat;
    private SpinnerDatePickerDialogBuilder EndDatePickerDialogBuilder;
    private JSONObject selectedGroup;
    private JSONObject selectedTemplate;
    private String selectedLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recurring_campaign);
        if(!isNetworkConnected()){
            showToast("Check Internet Connection");
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        widget = (WeekdaysWidget) findViewById(R.id.widget1);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Bundle extras = getIntent().getExtras();
        isEdit = extras.getBoolean("isEdit");
        if(isEdit){
            clientId=extras.getInt("clientId");
            item = (Item) getIntent().getSerializableExtra("recurringCampaignData");
            setValues(item);
            for (int i = 0; i < mContainer.getChildCount();  i++ ){
                View view = mContainer.getChildAt(i);
                view.setEnabled(false); // Or whatever you want to do with the view.
            }
        }else if(!isEdit){
            clientId=extras.getInt("clientId");
            clearData();
        }
        setGroupListSpinner(clientId);
        setTemplateListSpinner(clientId);
       
        setListners();
    }

    private void setListners() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if(mRadioButtonCoupon.isChecked()){
                        mLinearLayoutExpireAfter.setVisibility(View.VISIBLE);
                    }else {
                        mLinearLayoutExpireAfter.setVisibility(View.GONE);
                    }
                }
            }
        });
        mRadioGroupRecurrence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if (mRadioButtonDaily.isChecked()) {
                        mRecurrenceDays.setVisibility(View.VISIBLE);
                        mRecurrenceMonths.setVisibility(View.GONE);
                        mRecurrenceWeeks.setVisibility(View.GONE);
                    }
                    if (mRadioButtonWeekly.isChecked()) {
                        mRecurrenceDays.setVisibility(View.GONE);
                        mRecurrenceMonths.setVisibility(View.GONE);
                        mRecurrenceWeeks.setVisibility(View.VISIBLE);
                    }
                    if (mRadioButtonMonthly.isChecked()) {
                        mRecurrenceDays.setVisibility(View.GONE);
                        mRecurrenceMonths.setVisibility(View.VISIBLE);
                        mRecurrenceWeeks.setVisibility(View.GONE);
                    }
                }
            }
        });
        mRadioGroupEndingDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if(mRadioButtonSetEndDate.isChecked()){
                        //mLinearLayoutSetEndDate.setVisibility(View.VISIBLE);
                        mEtdSetEndDate.setVisibility(View.VISIBLE);
                    }else {
                        //mLinearLayoutSetEndDate.setVisibility(View.GONE);
                        mEtdSetEndDate.setVisibility(View.GONE);
                    }
                }
            }
        });

        EndDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder().context(CreateRecurringCampaignActivity.this);
        mEtdSetEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndDatePickerDialogBuilder.spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(1980, 0, 1)
                        .build().show();
            }
        });
        EndDatePickerDialogBuilder.callback(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                mEtdSetEndDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        //updateCampaign(getValues(campaignsListItem,clientId),campaignsListItem);
                    }
                }else if(!isEdit){
                    /*if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        createRecurringCampaign(getValues(item,clientId));
                    }*/
                    createRecurringCampaign(getValues(item,clientId));
                }
            }
        });
        mSpnCampaignGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedGroup = GroupListjsonArray.getJSONObject(position);//parent.getItemAtPosition(position).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(e.getMessage().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpnCampaignTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedTemplate =TemplateListjsonArray.getJSONObject(position);//parent.getItemAtPosition(position).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(e.getMessage().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void assignViews() {
        mContainer = (LinearLayout) findViewById(R.id.container);
        mEtdName = (TextInputEditText) findViewById(R.id.etdName);
        mSpnCampaignGroup = (Spinner) findViewById(R.id.spnCampaignGroup);
        mSpnCampaignSubgroup = (Spinner) findViewById(R.id.spnCampaignSubgroup);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioButtonCampaign = (RadioButton) findViewById(R.id.radioButtonCampaign);
        mRadioButtonCoupon = (RadioButton) findViewById(R.id.radioButtonCoupon);
        mLinearLayoutExpireAfter = (LinearLayout) findViewById(R.id.linearLayoutExpireAfter);
        mEdtExpiryAfter = (EditText) findViewById(R.id.edtExpiryAfter);
        mSpnExpiresAfter = (Spinner) findViewById(R.id.spnExpiresAfter);
        mEdtMinPurchaseAmt = (TextInputEditText) findViewById(R.id.edtMinPurchaseAmt);
        mSpnCampaignTemplate = (Spinner) findViewById(R.id.spnCampaignTemplate);
        mEdtCampaignMsg = (TextInputEditText) findViewById(R.id.edtCampaignMsg);
        mRadioGroupRecurrence = (RadioGroup) findViewById(R.id.radioGroupRecurrence);
        mRadioButtonDaily = (RadioButton) findViewById(R.id.radioButtonDaily);
        mRadioButtonWeekly = (RadioButton) findViewById(R.id.radioButtonWeekly);
        mRadioButtonMonthly = (RadioButton) findViewById(R.id.radioButtonMonthly);
        mRecurrenceDays = (LinearLayout) findViewById(R.id.RecurrenceDays);
        mEtdRecurrenceDays = (TextInputEditText) findViewById(R.id.etdRecurrenceDays);
        mRecurrenceMonths = (LinearLayout) findViewById(R.id.RecurrenceMonths);
        mEtdRecurrenceMonths = (TextInputEditText) findViewById(R.id.etdRecurrenceMonths);
        mRecurrenceWeeks = (LinearLayout) findViewById(R.id.RecurrenceWeeks);
        mWidget1 = (WeekdaysWidget) findViewById(R.id.widget1);
        mLinearLayoutSetEndDate = (LinearLayout) findViewById(R.id.linearLayoutSetEndDate);
        mRadioGroupEndingDate = (RadioGroup) findViewById(R.id.radioGroupEndingDate);
        mRadioButtonNoEndDate = (RadioButton) findViewById(R.id.radioButtonNoEndDate);
        mRadioButtonSetEndDate = (RadioButton) findViewById(R.id.radioButtonSetEndDate);
        mEtdSetEndDate = (TextInputEditText) findViewById(R.id.etdSetEndDate);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    private void clearData(){
        mEtdName.setText("");
        mEdtCampaignMsg.setText("");
        mEtdRecurrenceDays.setText("");
        mEtdSetEndDate.setText("");
        mRadioGroup.clearCheck();
        mRadioGroupRecurrence.clearCheck();
        mRadioGroupEndingDate.clearCheck();
    }

    private void setValues(Item item) {
        mEtdName.setText(item.getName());
        mEdtCampaignMsg.setText(item.getMessage());
        mEtdRecurrenceDays.setText("");
        mEtdSetEndDate.setText(item.getEndDate());
    }

    private boolean validate() {
        boolean bln = true;

        String name = mEtdName.getText().toString().trim();
        String campaignMsg = mEdtCampaignMsg.getText().toString().trim();
        String campaignRemark = mEtdRecurrenceDays.getText().toString().trim();


        if (name.length() == 0) {
            mEtdName.setBackgroundResource(R.drawable.red_border);
            mEtdName.setHint("Set Name");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (campaignMsg.length() == 0) {
            mEdtCampaignMsg.setBackgroundResource(R.drawable.red_border);
            mEdtCampaignMsg.setHint("Set Message");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (campaignRemark.length() == 0) {
            mEtdRecurrenceDays.setBackgroundResource(R.drawable.red_border);
            mEtdRecurrenceDays.setHint("Set Days");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (selectedGroup.equals("")) {
            showToast("Select Group");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (selectedTemplate.equals("")) {
            showToast("Select Template");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if(mRadioGroup.getCheckedRadioButtonId() == -1){
            showToast("Select Type");
            bln = false;
        }
        else if(mRadioGroupRecurrence.getCheckedRadioButtonId() == -1){
            showToast("Select Recurrence Type");
            bln = false;
        }
        else if(mRadioGroupEndingDate.getCheckedRadioButtonId() == -1){
            showToast("Select Ending Date Type");
            bln = false;
        }

        return bln;
    }

    private JSONObject getValues(Item item, int clientId){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c);

        final Calendar c1 = Calendar.getInstance();
        int mHour = c1.get(Calendar.HOUR_OF_DAY);
        int mMinute = c1.get(Calendar.MINUTE);
        String formattedTime = convertTimeTo12hrs(mHour,mMinute);//mHour+":"+mMinute;

        try {
            JSONObject values = new JSONObject();
            if(isEdit){
                values.put("Id",item.getId());
            }else if(!isEdit){
                values.put("Id",0);

            }
            values.put("Name",mEtdName.getText());
            values.put("Message",mEdtCampaignMsg.getText());
            values.put("StartDate",formattedDate);
            values.put("StartTime",formattedTime);
            if(mRadioButtonSetEndDate.isChecked()) {
                values.put("IsEndDate",true);
                values.put("EndDate", mEtdSetEndDate.getText().toString());
                values.put("EndTime", formattedTime);
            }else if(mRadioButtonNoEndDate.isChecked()){
                values.put("IsEndDate",false);
                values.put("EndDate", formattedDate);
                values.put("EndTime", formattedTime);
            }
            values.put("IPAddress","");
            if(mRadioButtonCoupon.isChecked()){
                values.put("IsCoupon",true);
            }else {
                values.put("IsCoupon",false);
            }
            values.put("CouponExpire",0);

            int selectedId = mRadioGroupRecurrence.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            values.put("CouponExpireType",radioButton.getText());

            values.put("MinPurchaseAmount",0);
            values.put("CreatedDate",formattedDate);
            values.put("SendDate",formattedTime);

            values.put("IsBirthday",false);
            values.put("IsAnniversary",false);
            values.put("GenderType","None");
            values.put("IsRecurring",false);
            if(mRadioButtonDaily.isChecked()){
                values.put("IsDay",true);
                values.put("DayValue",mEtdRecurrenceDays.getText());

                values.put("IsMonth",false);
                values.put("IsWeek",false);

                values.put("Sunday",false);
                values.put("Monday",false);
                values.put("Tuesday",false);
                values.put("Wednesday",false);
                values.put("Thursday",false);
                values.put("Friday",false);
                values.put("Saturday",false);

                values.put("MonthValue",0);
                values.put("MonthDayValue",0);
                //values.put("WeekValue","");

            }else if(mRadioButtonMonthly.isChecked()){
                values.put("IsMonth",true);
                values.put("MonthValue",mEtdRecurrenceMonths.getText().toString());
                values.put("MonthDayValue",0);

                values.put("IsDay",false);
                values.put("IsWeek",false);

                values.put("Sunday",false);
                values.put("Monday",false);
                values.put("Tuesday",false);
                values.put("Wednesday",false);
                values.put("Thursday",false);
                values.put("Friday",false);
                values.put("Saturday",false);

                values.put("MonthValue",0);
                values.put("MonthDayValue",0);
                values.put("DayValue",0);
                //values.put("WeekValue","");

            }else if(mRadioButtonWeekly.isChecked()){
                values.put("IsWeek",true);
                values.put("IsDay",false);
                values.put("IsMonth",false);

                values.put("MonthValue",0);
                values.put("MonthDayValue",0);
                values.put("DayValue",0);

                //values.put("WeekValue","");
                for(int i=0;i<widget.getSelectedDays().size();i++)
                {
                    int dayNumber = widget.getSelectedDays().get(i);
                    switch (dayNumber){
                        case 1:values.put("Sunday",true);

                            values.put("Monday",false);
                            values.put("Tuesday",false);
                            values.put("Wednesday",false);
                            values.put("Thursday",false);
                            values.put("Friday",false);
                            values.put("Saturday",false);
                            break;
                        case 2:values.put("Monday",true);

                            values.put("Sunday",false);
                            values.put("Tuesday",false);
                            values.put("Wednesday",false);
                            values.put("Thursday",false);
                            values.put("Friday",false);
                            values.put("Saturday",false);
                            break;
                        case 3:values.put("Tuesday",true);

                            values.put("Sunday",false);
                            values.put("Monday",false);
                            values.put("Wednesday",false);
                            values.put("Thursday",false);
                            values.put("Friday",false);
                            values.put("Saturday",false);
                            break;
                        case 4:values.put("Wednesday",true);

                            values.put("Sunday",false);
                            values.put("Monday",false);
                            values.put("Tuesday",false);
                            values.put("Thursday",false);
                            values.put("Friday",false);
                            values.put("Saturday",false);
                            break;
                        case 5:values.put("Thursday",true);

                            values.put("Sunday",false);
                            values.put("Monday",false);
                            values.put("Tuesday",false);
                            values.put("Wednesday",false);
                            values.put("Friday",false);
                            values.put("Saturday",false);
                            break;
                        case 6:values.put("Friday",true);

                            values.put("Sunday",false);
                            values.put("Monday",false);
                            values.put("Tuesday",false);
                            values.put("Wednesday",false);
                            values.put("Thursday",false);
                            values.put("Saturday",false);
                            break;
                        case 7:values.put("Saturday",true);

                            values.put("Sunday",false);
                            values.put("Monday",false);
                            values.put("Tuesday",false);
                            values.put("Wednesday",false);
                            values.put("Thursday",false);
                            values.put("Friday",false);
                            break;
                        default:
                            values.put("Sunday",false);
                            values.put("Monday",false);
                            values.put("Tuesday",false);
                            values.put("Wednesday",false);
                            values.put("Thursday",false);
                            values.put("Friday",false);
                            values.put("Saturday",false);
                            break;
                    }
                }
            }else {
                values.put("IsDay",false);
                values.put("IsMonth",false);
                values.put("IsWeek",false);
            }

            values.put("ClientId",clientId);
            values.put("CreatedBy",clientId);

            selectedGroup = GroupListjsonArray.getJSONObject(mSpnCampaignGroup.getSelectedItemPosition());
            values.put("GroupId",selectedGroup.get("Id"));
            if(selectedGroup.get("Name").equals("All Contacts")){
                showToast("Please Select Group");
                return null;
            }
            values.put("GroupName",""+selectedGroup.get("Name"));
            values.put("GroupContactCount",selectedGroup.get("ContactCount"));
            values.put("IsActive",true);
            values.put("ForAllContact",true);
            values.put("LastCampaignId","");

            selectedTemplate =TemplateListjsonArray.getJSONObject(mSpnCampaignTemplate.getSelectedItemPosition());
            values.put("TemplateDTO",selectedTemplate);

            return values;
        }catch (JSONException ex){
            showToast("getValues JSONException\n"+ex.getMessage().toString());
            return null;
        }

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

    private void createRecurringCampaign(JSONObject jsonObject){
        try {
            showBusyProgress();
            JSONObject params = jsonObject;

            final String requestBody = params.toString();
            Log.v(CreateRecurringCampaignActivity.class.getName(),"RecurringCampaign\n"+requestBody);

            StringRequest createRecurringCampaignRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "RecurringCampaign/CreateRecurringCampaign?accessId="+accessId, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateRecurringCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
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
                    Log.v(CreateRecurringCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
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
            VolleySingleton.getInstance().addToRequestQueue(createRecurringCampaignRequest);
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
