package com.bandhan.mantra.activities;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends BaseActivity {

    private CardView mCardViewNotification;
    private TextInputEditText mEdtFirstName;
    private TextInputEditText mEdtLasNameName;
    private TextInputEditText mEdtMobile;
    private TextInputEditText mEdtEmail;
    private TextInputEditText mEdtPwd;
    private TextInputLayout mEtLoginPasswordLayout;
    private TextInputEditText mEdtCompny;
    private TextInputEditText mEdtAddress;
    private AppCompatButton mBtnOtp;
    private EditText mEdtOtp;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void assignViews() {
        mCardViewNotification = (CardView) findViewById(R.id.card_view_notification);
        mEdtFirstName = (TextInputEditText) findViewById(R.id.edtFirstName);
        mEdtLasNameName = (TextInputEditText) findViewById(R.id.edtLasNameName);
        mEdtMobile = (TextInputEditText) findViewById(R.id.edtMobile);
        mEdtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        mEdtPwd = (TextInputEditText) findViewById(R.id.edtPwd);
        mEtLoginPasswordLayout = (TextInputLayout) findViewById(R.id.etLoginPasswordLayout);
        mEdtCompny = (TextInputEditText) findViewById(R.id.edtCompny);
        mEdtAddress = (TextInputEditText) findViewById(R.id.edtAddress);
        mBtnOtp = (AppCompatButton) findViewById(R.id.btnOtp);
        mEdtOtp = (EditText) findViewById(R.id.edtOtp);
        mBtnRegister = (Button) findViewById(R.id.btnRegister);
        setListeners();
    }

    private void setListeners() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtFirstName.getWindowToken(), 0);

                if (validate()) {
                    String firstName = mEdtFirstName.getText().toString().trim();
                    String lastName = mEdtLasNameName.getText().toString().trim();
                    String mobile = mEdtMobile.getText().toString().trim();
                    String email = mEdtEmail.getText().toString().trim();
                    String pwd = mEdtPwd.getText().toString().trim();
                    String company = mEdtCompny.getText().toString().trim();
                    String address = mEdtAddress.getText().toString().trim();
                    String otp = mEdtOtp.getText().toString().trim();
                    registerUser(firstName, lastName, mobile, email, pwd, company, address, otp);
                }else {
                    showToast("Please Fill All Data !!");
                }
            }
        });
    }

    private boolean validate() {
        boolean bln = true;

        String firstName = mEdtFirstName.getText().toString().trim();
        String lastName = mEdtLasNameName.getText().toString().trim();
        String mobile = mEdtMobile.getText().toString().trim();
        String email = mEdtEmail.getText().toString().trim();
        String pwd = mEdtPwd.getText().toString().trim();
        String company = mEdtCompny.getText().toString().trim();
        String address = mEdtAddress.getText().toString().trim();
        String otp = mEdtOtp.getText().toString().trim();

        if (firstName.length() == 0) {
            mEdtFirstName.setBackgroundResource(R.drawable.red_border);
            mEdtFirstName.setHint("Set First Name");
            // getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.password_blank), true, null).show();
            bln = false;
        } else if (lastName.length() == 0) {
            mEdtLasNameName.setBackgroundResource(R.drawable.red_border);
            mEdtLasNameName.setHint("Set Last Name");
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
        }else if (pwd.length() == 0) {
            mEdtPwd.setHint("Set Password");
            mEdtPwd.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        } else if (company.length() == 0) {
            mEdtCompny.setHint("Set Company");
            mEdtCompny.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }else if (address.length() == 0) {
            mEdtAddress.setHint("Set Address");
            mEdtAddress.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }else if (otp.length() == 0) {
            mEdtOtp.setHint("Enter OTP");
            mEdtOtp.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }
        return bln;
    }

    private void registerUser(String firstName, String lastName, String mobile, String email, String pwd, String company, String address, String otp) {

        Map<String, String> UserDTOMap = new HashMap<String, String>();
        UserDTOMap.put("Company", company);
        UserDTOMap.put("FirstName", firstName);
        UserDTOMap.put("LastName", lastName);
        UserDTOMap.put("Email", email);
        UserDTOMap.put("Address", address);
        UserDTOMap.put("Mobile", mobile);
        UserDTOMap.put("pwd", pwd);
        UserDTOMap.put("otp", otp);

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("accessId", "1");
        requestMap.put("UserDTO", String.valueOf(UserDTOMap));

        try {
            showBusyProgress();
            JsonObjectRequest userRegistrationRequest = new JsonObjectRequest(Request.Method.POST,
                    VolleySingleton.getWsBaseUrl() + "User/CreateUser",
                    new JSONObject(requestMap), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v(Registration.class.getName(), "onResponse :"+response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideBusyProgress();
                    Log.v(Registration.class.getName(), "onErrorResponse"+VolleySingleton.getErrorMessage(error).toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            VolleySingleton.getInstance().addToRequestQueue(userRegistrationRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(Registration.class.getName(), "Exception"+e.getMessage().toString());
        }
    }
}
