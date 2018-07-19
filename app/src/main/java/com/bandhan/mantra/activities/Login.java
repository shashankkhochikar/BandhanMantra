package com.bandhan.mantra.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.VolleySingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login extends BaseActivity {

    private EditText mEdtEmail;
    private EditText mEdtPwd;
    private Button mBtnLogin;
    private TextView textViewForgotPassword;
    private TextView textViewNewaccount;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignViews();
        sessionManager = new SessionManager(this);
        if(sessionManager.hasSessionCredentials()){
            String username = sessionManager.getSessionUsername();
            String password = sessionManager.getSessionPassword();
            signin(username,password);
        }
    }

    private void assignViews() {
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mEdtPwd = (EditText) findViewById(R.id.edtPwd);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        textViewForgotPassword = (TextView) findViewById(R.id.TextViewForgotPassword);
        textViewNewaccount = (TextView) findViewById(R.id.TextViewNewaccount);
        setListners();
    }

    private void setListners() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtEmail.getWindowToken(), 0);

                if (validate()) {
                    String username = mEdtEmail.getText().toString();
                    String password = mEdtPwd.getText().toString();
                    signin(username, password);
                }
            }
        });
        textViewNewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmailDialouge();
            }
        });
    }

    private void getEmailDialouge() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("RESET PASSWORD");
        alertDialog.setMessage("Enter Email");

        final EditText input = new EditText(Login.this);
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
                        String email = input.getText().toString();
                        if(validateEmail(email)){
                            resetPassword(email);
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

    private void resetPassword(final String email) {
        try {
            showBusyProgress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("Email",email);
            String urlWithParams = createStringQueryBuilder(VolleySingleton.getWsBaseUrl() + "User/ForgotPassword",params);
            StringRequest userResetPasswordRequest = new StringRequest(Request.Method.GET, urlWithParams, new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(Login.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        if (Response.equals("true")) {
                            showToast("Email Sent !!!");
                        } else if (Response.equals("false")){
                            showToast("Invalid Email");
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
                    Log.v(Login.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(userResetPasswordRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(Login.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private boolean validate() {
        boolean bln = true;
        String email = mEdtEmail.getText().toString().trim();
        String pwd = mEdtPwd.getText().toString().trim();

        if (email.length() == 0) {
            mEdtEmail.setHint("Set Email");
            mEdtEmail.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEdtEmail.setError("Invalid Email");
            mEdtEmail.setBackgroundResource(R.drawable.red_border);
//            getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.invalid_email), true, null).show();
            bln = false;
        } else if (pwd.length() == 0) {
            mEdtPwd.setHint("Set Password");
            mEdtPwd.setBackgroundResource(R.drawable.red_border);
            //getAlertDialogManager().Dialog(getResources().getString(R.string.error), getResources().getString(R.string.username_blank), true, null).show();
            bln = false;
        }
        return bln;
    }

    private boolean validateEmail(String email) {
        boolean bln = true;
        if (email.length() == 0) {
            getAlertDialogManager().Dialog("Email", "Set Email", true, null).show();
            bln = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getAlertDialogManager().Dialog("Email", "Invalid Email", true, null).show();
            bln = false;
        }
        return bln;
    }

    private void signin(final String username, final String password) {
        try {
            showBusyProgress();
            StringRequest userLoginRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "User/UserSignIn", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(Login.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        UserData userData = new Gson().fromJson(Response, UserData.class);
                        if (userData.getId() != 0) {

                            sessionManager.updateLoggedUserData(userData);
                            sessionManager.updateSessionUsername(userData.getEmail().toString());
                            sessionManager.updateSessionPassword(userData.getPassword().toString());
                            finish();
                            startActivity(new Intent(Login.this, DashboardActivity.class));
                        } else {
                            showToast("Invalid User");
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
                    Log.v(Login.class.getName(), "onErrorResponse" + VolleySingleton.getErrorMessage(e).toString());
                    //showToast("onErrorResponse " + VolleySingleton.getErrorMessage(e).toString());
                }
            }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("accessId", accessId);
                    params.put("Email", username);
                    params.put("Password", password);
                    return params;
                }
            };
            VolleySingleton.getInstance().addToRequestQueue(userLoginRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(Login.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
        //startActivity(new Intent(Login.this, DashboardActivity.class));
    }
}
