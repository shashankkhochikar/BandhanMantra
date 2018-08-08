package com.bandhan.mantra.activities;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.CouponCampaign;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
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

public class CreateCouponCampaignActivity extends BaseActivity {

    private LinearLayout mContainer;
    private TextInputEditText mEtdcouponName;
    private Spinner mSpnCampaignGroup;
    private Spinner mSpnCampaignTemplate;
    private TextInputEditText mEdtCampaignMsg;
    private TextInputEditText mEdtMinPurchaseAmt;
    private EditText mEdtExpiryAfter;
    private Spinner mSpnExpiresAfter;
    private TextInputEditText mEdtExpiryDate;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonNow;
    private RadioButton mRadioButtonLater;
    private TextInputEditText mEdtScheduledDate;
    private TextInputEditText mEdtScheduledTime;
    private TextInputEditText mEdtCampaignRemark;
    private Button mBtnSubmit;
    private LinearLayout linearLayoutScheduleDateTime;

    private ArrayAdapter TemplateListArrayAdapter;
    private ArrayAdapter GroupListArrayAdapter;
    private JSONArray TemplateListjsonArray;
    private JSONArray GroupListjsonArray;

    private int clientId=0;
    private boolean isEdit = false;
    private SimpleDateFormat simpleDateFormat;
    private CouponCampaign couponCampaign;
    private SpinnerDatePickerDialogBuilder ScheduledDatePickerDialogBuilder,ExpiryDatePickerDialogBuilder;
    private JSONObject selectedGroup;
    private JSONObject selectedTemplate;
    private String selectedLanguageId;

    private SessionManager sessionManager;
    public UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coupon_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        assignViews();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Bundle extras = getIntent().getExtras();
        isEdit = extras.getBoolean("isEdit");
        if(isEdit){
            clientId=extras.getInt("clientId");
            couponCampaign = (CouponCampaign) getIntent().getSerializableExtra("couponData");
            setValues(couponCampaign);
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
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        //updateCampaign(getValues(campaignsListItem,clientId),campaignsListItem);
                    }
                }else if(!isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        createCouponCampaign(getValues(couponCampaign,clientId),clientId);
                    }
                }
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {
                    if(mRadioButtonNow.isChecked()){
                        linearLayoutScheduleDateTime.setVisibility(View.GONE);

                    }
                    if(mRadioButtonLater.isChecked()){
                        linearLayoutScheduleDateTime.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        ScheduledDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder().context(CreateCouponCampaignActivity.this);
        mEdtScheduledDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduledDatePickerDialogBuilder.spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(1980, 0, 1)
                        .build().show();
            }
        });
        ScheduledDatePickerDialogBuilder.callback(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                mEdtScheduledDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });

        ExpiryDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder().context(CreateCouponCampaignActivity.this);
        mEdtExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpiryDatePickerDialogBuilder.spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(1980, 0, 1)
                        .build().show();
            }
        });
        ExpiryDatePickerDialogBuilder.callback(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                mEdtExpiryDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });

        mEdtScheduledTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateCouponCampaignActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mEdtScheduledTime.setText(hourOfDay + ":" + minute);
                            }
                        },mHour, mMinute, false);
                timePickerDialog.show();
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

    private void setValues(CouponCampaign couponCampaign) {
        mEtdcouponName.setText(couponCampaign.getTitle());
        mEdtCampaignMsg.setText(couponCampaign.getMessage());
        mEdtMinPurchaseAmt.setText(couponCampaign.getMinPurchaseAmount().toString());
        mEdtExpiryAfter.setText("");
        mEdtExpiryDate.setText(couponCampaign.getExpiresOn());
        mEdtScheduledDate.setText(couponCampaign.getSendOn());
        mEdtScheduledTime.setText(couponCampaign.getScheduleTime());
        mEdtCampaignRemark.setText(couponCampaign.getRemark());
    }

    private void clearData() {
        mEtdcouponName.setText("");
        mEdtCampaignMsg.setText("");
        mEdtMinPurchaseAmt.setText("");
        mEdtExpiryAfter.setText("");
        mEdtExpiryDate.setText("");
        mRadioGroup.clearCheck();
        mEdtScheduledDate.setText("");
        mEdtScheduledTime.setText("");
        mEdtCampaignRemark.setText("");
    }

    private boolean validate() {
        boolean bln = true;

        String CouponName = mEtdcouponName.getText().toString().trim();
        String CampaignMsg = mEdtCampaignMsg.getText().toString().trim();
        String MinPurchaseAmt = mEdtMinPurchaseAmt.getText().toString().trim();
        String ExpiryAfter = mEdtExpiryAfter.getText().toString().trim();
        String ExpiryDate = mEdtExpiryDate.getText().toString().trim();
        String CampaignRemark = mEdtCampaignRemark.getText().toString().trim();
        String ScheduledTime = mEdtScheduledTime.getText().toString().trim();

        if (CouponName.length() == 0) {
            mEtdcouponName.setBackgroundResource(R.drawable.red_border);
            //mEdtName.setHint("Set First Name");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (CampaignMsg.length() == 0) {
            mEdtCampaignMsg.setBackgroundResource(R.drawable.red_border);
            //mEdtLastName.setHint("Set Last Name");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (MinPurchaseAmt.length() == 0) {
            mEdtMinPurchaseAmt.setBackgroundResource(R.drawable.red_border);
            //mEdtMinPurchaseAmt.setHint("Set Mobile No");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (ExpiryAfter.length() == 0) {
            //mEdtExpiryAfter.setHint("Set Email");
            mEdtExpiryAfter.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        } else if (ExpiryDate.length() == 0) {
            //mEdtEmail.setError("Invalid Email");
            mEdtExpiryDate.setBackgroundResource(R.drawable.red_border);
//            getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.invalid_email), true, null).show();
            bln = false;
        }else if (CampaignRemark.length() == 0) {
            //mEdtBdate.setHint("Set Birth Date");
            mEdtCampaignRemark.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }else if (ScheduledTime.length() == 0) {
            //mEdtAniversary.setHint("Set Aniversary Date");
            mEdtScheduledTime.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }else if(mRadioGroup.getCheckedRadioButtonId() == -1){
            showToast("Select Schedule Type");
            bln = false;
        }
        return bln;
    }

    private void assignViews() {
        mContainer = (LinearLayout) findViewById(R.id.container);
        mEtdcouponName = (TextInputEditText) findViewById(R.id.etdcouponName);

        mSpnCampaignGroup = (Spinner) findViewById(R.id.spnCampaignGroup);
        mSpnCampaignTemplate = (Spinner) findViewById(R.id.spnCampaignTemplate);

        mEdtCampaignMsg = (TextInputEditText) findViewById(R.id.edtCampaignMsg);
        mEdtMinPurchaseAmt = (TextInputEditText) findViewById(R.id.edtMinPurchaseAmt);
        mEdtExpiryAfter = (EditText) findViewById(R.id.edtExpiryAfter);
        mSpnExpiresAfter = (Spinner) findViewById(R.id.spnExpiresAfter);

        mEdtExpiryDate = (TextInputEditText) findViewById(R.id.edtExpiryDate);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioButtonNow = (RadioButton) findViewById(R.id.radioButtonNow);
        mRadioButtonLater = (RadioButton) findViewById(R.id.radioButtonLater);
        mEdtScheduledDate = (TextInputEditText) findViewById(R.id.edtScheduledDate);
        mEdtScheduledTime = (TextInputEditText) findViewById(R.id.edtScheduledTime);
        mEdtCampaignRemark = (TextInputEditText) findViewById(R.id.edtCampaignRemark);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        linearLayoutScheduleDateTime = (LinearLayout)findViewById(R.id.linearLayoutScheduleDateTime);
    }

    private JSONObject getValues(CouponCampaign couponCampaign, int clientId){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c);

        final Calendar c1 = Calendar.getInstance();
        int mHour = c1.get(Calendar.HOUR_OF_DAY);
        int mMinute = c1.get(Calendar.MINUTE);
        String formattedTime = mHour+":"+mMinute;

        try {
            JSONObject values = new JSONObject();
            if(isEdit){
                values.put("Id",couponCampaign.getId());
                values.put("ReceipentNumber",couponCampaign.getRecipientsCount());
            }else if(!isEdit){
                values.put("Id",0);
                values.put("ReceipentNumber","0");

            }
            values.put("Title",mEtdcouponName.getText().toString());
            values.put("CreatedDate",formattedDate);

            values.put("RecipientsCount",0);
            values.put("Message",mEdtCampaignMsg.getText().toString());
            values.put("ExpiresOn",mEdtExpiryDate.getText().toString());
            values.put("SendOn",formattedDate);
            //values.put("ScheduleTime",mEdtScheduledDate.getText().toString());
            values.put("IsScheduled",0);
            if(mRadioButtonLater.isChecked()){
                values.put("IsScheduled",true);
                values.put("ScheduleTime",""+mEdtScheduledTime.getText().toString());
            }else if(mRadioButtonNow.isChecked()){
                values.put("IsScheduled",false);
                values.put("ScheduleTime",formattedTime);
            }

            values.put("Status","unsend");
            values.put("Remark",mEdtCampaignRemark.getText().toString());
            values.put("IPAddress","");
            values.put("ClientId",clientId);
            values.put("ReedeemedCount",0);
            values.put("TotalCount",0);
            values.put("CreatedBy",clientId);
            values.put("GroupId",selectedGroup.getInt("Id"));
            values.put("Group",selectedGroup.getString("Name"));
            values.put("GroupContactCount",selectedGroup.getInt("ContactCount"));
            values.put("ForAllContact",true);
            values.put("TemplateDTO",selectedTemplate);
            values.put("MinPurchaseAmount",0);
            values.put("ConsumedCredits",0);
            values.put("CreditsDiffrence",0);
            values.put("IsReconcile",true);
            values.put("ReconcileDate",formattedDate);
            values.put("RequiredCredits",0);
            values.put("RecurringCampaignId",0);
            values.put("SuccessCount",0);
            values.put("FailureCount",0);
            values.put("PendingCount",0);
            values.put("UpdatedDate",formattedDate);
            values.put("UserName",userData.getFirstName()+" "+userData.getLastName());

            return values;
        }catch (JSONException ex){
            showToast("getValues JSONException\n"+ex.getMessage().toString());
            return null;
        }

    }

    private void createCouponCampaign(JSONObject jsonObject,int clientId){
        try {
            showBusyProgress();
            JSONObject params = jsonObject;

            final String requestBody = params.toString();
            Log.v(CreateCouponCampaignActivity.class.getName(),"createCouponCampaign\n"+requestBody);

            StringRequest createCouponCampaignRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "EcouponCampaign/CreateEcouponCampaign?accessId="+accessId, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateCouponCampaignActivity.class.getName(), "onResponse :" + Response.toString());
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
                    Log.v(CreateCouponCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
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
            VolleySingleton.getInstance().addToRequestQueue(createCouponCampaignRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateCouponCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void setTemplateListSpinner(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId",accessId);
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Template/GetTemplateListByClientId",params);
            Log.v(CreateCouponCampaignActivity.class.getName(), "Req : "+urlWithParams);

            StringRequest GetTemplateListByClientIdRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateCouponCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            TemplateListjsonArray = new JSONArray(Response);
                            String[] group = new String[TemplateListjsonArray.length()];
                            for(int i=0;i<TemplateListjsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) TemplateListjsonArray.getJSONObject(i);
                                group[i]=jsonObject.get("Message").toString();
                            }
                            TemplateListArrayAdapter = new ArrayAdapter(CreateCouponCampaignActivity.this,android.R.layout.simple_spinner_item,group);
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
                    Log.v(CreateCouponCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetTemplateListByClientIdRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateCouponCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
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
            Log.v(CreateCouponCampaignActivity.class.getName(), "Req : "+urlWithParams);

            StringRequest GetGroupListByClientIdForCampaignRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateCouponCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            GroupListjsonArray = new JSONArray(Response);
                            String[] group = new String[GroupListjsonArray.length()];
                            for(int i=0;i<GroupListjsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) GroupListjsonArray.getJSONObject(i);
                                group[i]=jsonObject.get("Name").toString();
                            }

                            GroupListArrayAdapter = new ArrayAdapter(CreateCouponCampaignActivity.this,android.R.layout.simple_spinner_item,group);
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
                    Log.v(CreateCouponCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetGroupListByClientIdForCampaignRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateCouponCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
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
