package com.bandhan.mantra.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.ContactGroupItemData;
import com.bandhan.mantra.model.Datum;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateContactActivity extends BaseActivity {

    private TextInputEditText mEdtName;
    private TextInputEditText mEdtLastName;
    private TextInputEditText mEdtMobile;
    private TextInputEditText mEdtEmail;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;
    private TextInputEditText mEdtBdate;
    private TextInputEditText mEdtAniversary;
    private Button mBtnSubmit;
    private LinearLayout container;
    private Datum contactData;
    private SimpleDateFormat simpleDateFormat;
    private int clientId=0;
    private boolean isEdit = false;

    private SpinnerDatePickerDialogBuilder AniversaryDatePickerDialogBuilder,BdatePickerDialogBuilder;
    private ContactGroupItemData contactGroupItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        setListners();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        Bundle extras = getIntent().getExtras();
        isEdit = extras.getBoolean("isEdit");
        if(isEdit){
            clientId=extras.getInt("clientId");
            contactData = (Datum) getIntent().getSerializableExtra("contactData");
            contactGroupItemData = (ContactGroupItemData) getIntent().getSerializableExtra("groupData");
            setValues(contactData);
            for ( int i = 0; i < container.getChildCount();  i++ ){
                View view = container.getChildAt(i);
                view.setEnabled(false); // Or whatever you want to do with the view.
            }
        }else if(!isEdit){
            clientId=extras.getInt("clientId");
            contactGroupItemData = (ContactGroupItemData) getIntent().getSerializableExtra("groupData");
            clearData();
        }
    }

    private void setListners() {

        AniversaryDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder().context(CreateContactActivity.this);
        mEdtAniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AniversaryDatePickerDialogBuilder.spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(1980, 0, 1)
                        .build().show();
            }
        });
        AniversaryDatePickerDialogBuilder.callback(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                mEdtAniversary.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });
///
        BdatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder().context(CreateContactActivity.this);
        mEdtBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BdatePickerDialogBuilder.spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(1980, 0, 1)
                        .build().show();
            }
        });
        BdatePickerDialogBuilder.callback(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                mEdtBdate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        updateContact(getValues(contactData,clientId),contactGroupItemData);
                    }
                }else if(!isEdit){
                    if(validate()){
                        //showToast(""+getValues(contactData,clientId));
                        createNewContact(getValues(contactData,clientId),contactGroupItemData);
                    }
                }
            }
        });
    }

    private void clearData(){
        mEdtName.setText("");
        mEdtLastName.setText("");
        mEdtMobile.setText("");
        mEdtEmail.setText("");
        mRadioGroup.clearCheck();
        mEdtBdate.setText("");
        mEdtAniversary.setText("");
    }

    private void setValues(Datum contactData) {
        mEdtName.setText(contactData.getFirstName());
        mEdtLastName.setText(contactData.getLastName());
        mEdtMobile.setText(contactData.getMobileNumber());
        mEdtEmail.setText(contactData.getEmail());
        if(contactData.getGender().equals("Male")){
            mRadioButtonMale.setChecked(true);
        }else if(contactData.getGender().equals("Female")){
            mRadioButtonFemale.setChecked(true);
        }
        mEdtBdate.setText(contactData.getBirthDate());
        mEdtAniversary.setText(contactData.getAnniversaryDate());
    }

    private JSONObject getValues(Datum contactData, int clientId){
        try {
            JSONObject values = new JSONObject();
            if(isEdit){
                values.put("Id",""+contactData.getId());
                values.put("Name",""+contactData.getId());
            }else if(!isEdit){
                values.put("Id","0");
                values.put("Name",mEdtName.getText()+" "+mEdtLastName.getText().toString());
            }

            values.put("BirthDate",""+mEdtBdate.getText().toString());
            values.put("AnniversaryDate",""+mEdtAniversary.getText().toString());
            values.put("Email",""+mEdtEmail.getText().toString());
            values.put("MobileNumber",""+mEdtMobile.getText().toString());
            if(mRadioButtonMale.isChecked()){
                values.put("Gender",""+mRadioButtonMale.getText().toString());
            }else if(mRadioButtonFemale.isChecked()){
                values.put("Gender",""+mRadioButtonFemale.getText().toString());
            }
            values.put("ClientId",""+clientId);
            values.put("FirstName",""+mEdtName.getText().toString());
            values.put("LastName",""+mEdtLastName.getText().toString());
            return values;
        }catch (JSONException ex){
            showToast("getValues JSONException\n"+ex.getMessage().toString());
            return null;
        }

    }

    private boolean validate() {
        boolean bln = true;

        String firstName = mEdtName.getText().toString().trim();
        String lastName = mEdtLastName.getText().toString().trim();
        String mobile = mEdtMobile.getText().toString().trim();
        String email = mEdtEmail.getText().toString().trim();

        String birthdate = mEdtBdate.getText().toString().trim();
        String aniversaryDate = mEdtAniversary.getText().toString().trim();

        if (firstName.length() == 0) {
            mEdtName.setBackgroundResource(R.drawable.red_border);
            mEdtName.setHint("Set First Name");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (lastName.length() == 0) {
            mEdtLastName.setBackgroundResource(R.drawable.red_border);
            mEdtLastName.setHint("Set Last Name");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (mobile.length() == 0) {
            mEdtMobile.setBackgroundResource(R.drawable.red_border);
            mEdtMobile.setHint("Set Mobile No");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        }else if (email.length() == 0) {
            mEdtEmail.setHint("Set Email");
            mEdtEmail.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEdtEmail.setError("Invalid Email");
            mEdtEmail.setBackgroundResource(R.drawable.red_border);
//            getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.invalid_email), true, null).show();
            bln = false;
        }else if (birthdate.length() == 0) {
            mEdtBdate.setHint("Set Birth Date");
            mEdtBdate.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }else if (aniversaryDate.length() == 0) {
            mEdtAniversary.setHint("Set Aniversary Date");
            mEdtAniversary.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }else if(mRadioGroup.getCheckedRadioButtonId() == -1){
            showToast("Select Gender");
            bln = false;
        }
        return bln;
    }

    private void assignViews() {
        mEdtName = (TextInputEditText) findViewById(R.id.edtName);
        mEdtLastName = (TextInputEditText) findViewById(R.id.edtLastName);
        mEdtMobile = (TextInputEditText) findViewById(R.id.edtMobile);
        mEdtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        mRadioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        mEdtBdate = (TextInputEditText) findViewById(R.id.edtBdate);
        mEdtAniversary = (TextInputEditText) findViewById(R.id.edtAniversary);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        container = (LinearLayout)findViewById(R.id.container);
    }

    private void updateContact(JSONObject jsonObject, ContactGroupItemData contactGroupItemData) {
        try {
            showBusyProgress();
            JSONObject params = jsonObject;
            JSONObject groupDetail = new JSONObject();
            groupDetail.put("Id",contactGroupItemData.getId());
            groupDetail.put("Name",contactGroupItemData.getName());
            groupDetail.put("ClientID",contactGroupItemData.getClientID());
            groupDetail.put("ContactCount","0");

            JSONArray groupDetailsArray = new JSONArray();
            groupDetailsArray.put(groupDetail);


            params.put("Groups",groupDetailsArray);
            final String requestBody = params.toString();

            StringRequest updateContactRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Contact/EditContact", new Response.Listener<String>() {
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
            VolleySingleton.getInstance().addToRequestQueue(updateContactRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(CreateContactActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void createNewContact(JSONObject jsonObject, ContactGroupItemData contactGroupItemData) {
        try {
            showBusyProgress();
            JSONObject params = jsonObject;
            JSONObject groupDetail = new JSONObject();
            groupDetail.put("Id",contactGroupItemData.getId());
            groupDetail.put("Name",contactGroupItemData.getName());
            groupDetail.put("ClientID",contactGroupItemData.getClientID());
            groupDetail.put("ContactCount","0");

            JSONArray groupDetailsArray = new JSONArray();
            groupDetailsArray.put(groupDetail);


            params.put("Groups",groupDetailsArray);
            final String requestBody = params.toString();

            StringRequest createContactRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Contact/CreateContact", new Response.Listener<String>() {
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
            VolleySingleton.getInstance().addToRequestQueue(createContactRequest);
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
