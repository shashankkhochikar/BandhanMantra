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
import com.bandhan.mantra.model.CampaignsListItem;
import com.bandhan.mantra.volley.VolleySingleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class CreateCampaignActivity extends BaseActivity {

    String[] languages = {"ENGLISH","AMHARIC","ARABIC","BENGALI","CHINESE","GREEK","GUJARATI","HINDI","KANNADA","MALAYALAM","MARATHI","NEPALI","ORIYA","PERSIAN","PUNJABI","RUSSIAN","SANSKRIT","SERBIAN","SINHALESE","TAMIL","TELUGU","TIGRINYA","URDU"};
    String[] languagesId = {"en","am","ar","bn","zh","el","gu","hi","kn","ml","mr","ne","or","fa","pa","ru","sa","sr","si","ta","te","ti","ur"};
    private TextInputEditText mEtdName;
    private Spinner mSpnCampaignGroup;
    private Spinner mSpnCampaignTemplate;
    private Spinner mSpnCampaignLanguage;
    private TextInputEditText mEdtCampaignMsg;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonNow;
    private RadioButton mRadioButtonLater;
    private TextInputEditText mEdtScheduledDate;
    private TextInputEditText mEdtScheduledTime;
    private TextInputEditText mEdtCampaignRemark;
    private Button mBtnSubmit;
    private LinearLayout container;
    private LinearLayout linearLayoutScheduleDateTime;

    private ArrayAdapter TemplateListArrayAdapter;
    private ArrayAdapter GroupListArrayAdapter;
    private ArrayAdapter LanguageArrayAdapter;
    private JSONArray TemplateListjsonArray;
    private JSONArray GroupListjsonArray;

    private CampaignsListItem campaignsListItem;
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
        setContentView(R.layout.activity_create_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignViews();

        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        Bundle extras = getIntent().getExtras();
        isEdit = extras.getBoolean("isEdit");

        if(isEdit){
            clientId=extras.getInt("clientId");
            campaignsListItem = (CampaignsListItem) getIntent().getSerializableExtra("campaignData");
            setValues(campaignsListItem);
            for (int i = 0; i < container.getChildCount();  i++ ){
                View view = container.getChildAt(i);
                view.setEnabled(false); // Or whatever you want to do with the view.
            }
        }else if(!isEdit){
            clientId=extras.getInt("clientId");
            clearData();
        }
        setGroupListSpinner(clientId);
        setLanguageSpinner();
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
                   if(mRadioButtonNow.isChecked()){
                       linearLayoutScheduleDateTime.setVisibility(View.GONE);

                   }
                   if(mRadioButtonLater.isChecked()){
                       linearLayoutScheduleDateTime.setVisibility(View.VISIBLE);
                   }
               }
           }
       });
        ScheduledDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder().context(CreateCampaignActivity.this);
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

        mEdtScheduledTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateCampaignActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mEdtScheduledTime.setText(hourOfDay + ":" + minute);
                            }
                        },mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        mSpnCampaignLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguageId = languagesId[position];//parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        //updateContact(getValues(contactData,clientId),contactGroupItemData);
                    }
                }else if(!isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        createCampaign(getValues(campaignsListItem,clientId),clientId);
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean bln = true;

        String name = mEtdName.getText().toString().trim();
        String campaignMsg = mEdtCampaignMsg.getText().toString().trim();
        String campaignRemark = mEdtCampaignRemark.getText().toString().trim();
        String scheduledDate = mEdtScheduledDate.getText().toString().trim();
        String scheduledTime = mEdtScheduledTime.getText().toString().trim();


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
            mEdtCampaignRemark.setBackgroundResource(R.drawable.red_border);
            mEdtCampaignRemark.setHint("SetRemark");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (selectedGroup.equals("")) {
            showToast("Select Group");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (selectedLanguageId.equals("")) {
            showToast("Select Language");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (selectedTemplate.equals("")) {
            showToast("Select Template");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if(mRadioButtonLater.isChecked()){
            if (scheduledDate.length() == 0) {
                mEdtScheduledDate.setBackgroundResource(R.drawable.red_border);
                mEdtScheduledDate.setHint("Set Date");
                // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
                bln = false;
            } else if (scheduledTime.length() == 0) {
                mEdtScheduledTime.setBackgroundResource(R.drawable.red_border);
                mEdtScheduledTime.setHint("Set Time");
                // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
                bln = false;
            }
        }
        return bln;
    }

    private void setLanguageSpinner(){
        LanguageArrayAdapter = new ArrayAdapter(CreateCampaignActivity.this,android.R.layout.simple_spinner_item,languages);
        LanguageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpnCampaignLanguage.setAdapter(LanguageArrayAdapter);
    }

    private void setTemplateListSpinner(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId","1");
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Template/GetTemplateListByClientId",params);
            Log.v(CreateCampaignActivity.class.getName(), "Req : "+urlWithParams);

            StringRequest GetTemplateListByClientIdRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            TemplateListjsonArray = new JSONArray(Response);
                            String[] group = new String[TemplateListjsonArray.length()];
                            for(int i=0;i<TemplateListjsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) TemplateListjsonArray.getJSONObject(i);
                                group[i]=jsonObject.get("Message").toString();
                            }
                            TemplateListArrayAdapter = new ArrayAdapter(CreateCampaignActivity.this,android.R.layout.simple_spinner_item,group);
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
                    Log.v(CreateCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetTemplateListByClientIdRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void setGroupListSpinner(int clientId) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessId","1");
            params.put("ClientId",""+clientId);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "Group/GetGroupListByClientIdForCampaign",params);
            Log.v(CreateCampaignActivity.class.getName(), "Req : "+urlWithParams);

            StringRequest GetGroupListByClientIdForCampaignRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateCampaignActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        try {
                            GroupListjsonArray = new JSONArray(Response);
                            String[] group = new String[GroupListjsonArray.length()];
                            for(int i=0;i<GroupListjsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) GroupListjsonArray.getJSONObject(i);
                                group[i]=jsonObject.get("Name").toString();
                            }

                            GroupListArrayAdapter = new ArrayAdapter(CreateCampaignActivity.this,android.R.layout.simple_spinner_item,group);
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
                    Log.v(CreateCampaignActivity.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(GetGroupListByClientIdForCampaignRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateCampaignActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void setValues(CampaignsListItem campaignsListItem) {
        mEtdName.setText(campaignsListItem.getName());
        mEdtScheduledDate.setText(campaignsListItem.getScheduledDate());
        mEdtScheduledTime.setText(campaignsListItem.getScheduledTime());
        mEdtCampaignRemark.setText(campaignsListItem.getRemark());
        mEdtCampaignMsg.setText(campaignsListItem.getMessage());

       /* int spinnerPosition = GroupListArrayAdapter.getPosition(campaignsListItem.getGroupName());
        mSpnCampaignGroup.setSelection(spinnerPosition);

        int spinnerPosition1 = LanguageArrayAdapter.getPosition(campaignsListItem.getLanguageCode());
        mSpnCampaignGroup.setSelection(spinnerPosition1);*/

    }

    private void assignViews() {
        mEtdName = (TextInputEditText) findViewById(R.id.etdName);

        mSpnCampaignGroup = (Spinner) findViewById(R.id.spnCampaignGroup);
        mSpnCampaignTemplate = (Spinner) findViewById(R.id.spnCampaignTemplate);
        mSpnCampaignLanguage = (Spinner) findViewById(R.id.spnCampaignLanguage);

        mEdtCampaignMsg = (TextInputEditText) findViewById(R.id.edtCampaignMsg);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioButtonNow = (RadioButton) findViewById(R.id.radioButtonNow1);
        mRadioButtonLater = (RadioButton) findViewById(R.id.radioButtonLater1);

        mEdtScheduledDate = (TextInputEditText) findViewById(R.id.edtScheduledDate);
        mEdtScheduledTime = (TextInputEditText) findViewById(R.id.edtScheduledTime);
        mEdtCampaignRemark = (TextInputEditText) findViewById(R.id.edtCampaignRemark);

        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        container=(LinearLayout)findViewById(R.id.container);
        linearLayoutScheduleDateTime = (LinearLayout)findViewById(R.id.linearLayoutScheduleDateTime);
    }

    private void clearData(){
        mEtdName.setText("");
        mEdtCampaignMsg.setText("");
        mRadioGroup.clearCheck();
        mEdtScheduledDate.setText("");
        mEdtScheduledTime.setText("");
        mEdtCampaignRemark.setText("");
    }


    private JSONObject getValues(CampaignsListItem campaignsListItem, int clientId){
        try {
            JSONObject values = new JSONObject();
            if(isEdit){
                values.put("Id",""+campaignsListItem.getId());
                values.put("Name",""+campaignsListItem.getName());
            }else if(!isEdit){
                values.put("Id","0");
                values.put("Name",""+mEtdName.getText());
            }

            values.put("Message",""+mEdtCampaignMsg.getText().toString());

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            String formattedDate = df.format(c);
            values.put("CreatedDate",""+formattedDate);
            if(mRadioButtonLater.isChecked()){
                values.put("IsScheduled","true");
                values.put("ScheduledDate",""+mEdtScheduledDate.getText().toString());
                values.put("ScheduledTime",""+mEdtScheduledTime.getText().toString());
            }else if(mRadioButtonNow.isChecked()){
                values.put("IsScheduled","false");
                values.put("ScheduledDate","");
                values.put("ScheduledTime","");
            }

            values.put("Remark",""+mEdtCampaignRemark.getText().toString());
            values.put("ClientId",""+clientId);
            values.put("LanguageCode",""+selectedLanguageId);
            values.put("GroupId",""+selectedGroup.get("Id"));
            values.put("GroupName",""+selectedGroup.get("Name"));
            values.put("TemplateDTO",selectedTemplate);

            return values;
        }catch (JSONException ex){
            showToast("getValues JSONException\n"+ex.getMessage().toString());
            return null;
        }

    }

    private void createCampaign(JSONObject jsonObject,int clientId){
        try {
            showBusyProgress();
            JSONObject params = jsonObject;

            final String requestBody = params.toString();
            Log.v(CreateCampaignActivity.class.getName(),requestBody);


            StringRequest createCampaignRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Campaign/CreateCampaign", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(CreateContactActivity.class.getName(), "onResponse :" + Response.toString());
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
            VolleySingleton.getInstance().addToRequestQueue(createCampaignRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateContactActivity.class.getName(), "Exception" + e.getMessage().toString());
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
                for ( int i = 0; i < container.getChildCount();  i++ ){
                    View view = container.getChildAt(i);
                    view.setEnabled(true); // Or whatever you want to do with the view.
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
