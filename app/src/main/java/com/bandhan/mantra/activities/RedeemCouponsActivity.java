package com.bandhan.mantra.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bandhan.mantra.R;
import com.bandhan.mantra.baseclasses.BaseActivity;
import com.bandhan.mantra.model.UserData;
import com.bandhan.mantra.utils.SessionManager;
import com.bandhan.mantra.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RedeemCouponsActivity extends BaseActivity {

    private SessionManager sessionManager;
    public UserData userData;
    public int clientId = 0;

    private CardView mCardViewNotification;
    private TextInputEditText mEdtMobileNo;
    private TextInputEditText mEdtCode;
    private Button mBtnRedeem;
    private TextInputEditText mEdtResendMobileNo;
    private Button mBtnResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_coupons);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        userData = sessionManager.getLoggedUserData();
        clientId = userData.getId();

        assignViews();
        setListners();
    }

    private void setListners() {
        mBtnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mEdtMobileNo.getText().toString();
                String code = mEdtCode.getText().toString();
                if(mobile.equals("") && code.equals("")){
                    showToast("fill all data");
                }else {
                    RedeemCoupon(mobile,code,clientId,userData.getFirstName()+" "+userData.getLastName());
                }
            }
        });
        mBtnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void RedeemCoupon(String MobileNumber,String Code,int clientId,String userName){
        try {
            showBusyProgress();

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDate = df.format(c);

            JSONObject params = new JSONObject();
            params.put("Id", 0);
            params.put("MobileNumber", MobileNumber);
            params.put("IsRedeem", true);
            params.put("RedeemDateTime", formattedDate);
            params.put("Remark", "");
            params.put("IsExpired", true);
            params.put("SentDateTime", formattedDate);
            params.put("MessageId", "");
            params.put("MessageCount", Code);
            params.put("UserId", clientId);
            params.put("UserName", userName);
            params.put("CouponCampaignName", "");
            params.put("Message", "");
            params.put("EcouponCampaignId", "");
            params.put("ClientId", clientId);
            params.put("Amount", 0);
            params.put("ExpiresOn", formattedDate);
            params.put("RequiredCredits", 0);
            params.put("IsCouponSent", true);
            params.put("CreatedBy", userName);
            params.put("CreatedOn", formattedDate);
            params.put("BillNumber", "");
            params.put("BillDate", formattedDate);
            params.put("MinPurchaseAmount", 0);
            params.put("Status", "");

            final String requestBody = params.toString();

            StringRequest redeemCouponRequest = new StringRequest(Request.Method.POST, VolleySingleton.getWsBaseUrl() + "Coupon/RedeemCoupon", new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    hideBusyProgress();
                    Log.v(TemplatesListActivity.class.getName(), "onResponse :" + Response.toString());
                    if (!Response.equals("")) {
                        showToast("Ok\n"+Response);
                    } else {
                        showToast("Invalid Response \n"+Response);
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
            VolleySingleton.getInstance().addToRequestQueue(redeemCouponRequest);
        } catch (Exception e) {
            hideBusyProgress();
            Log.v(TemplatesListActivity.class.getName(), "Exception" + e.getMessage().toString());
            showToast("Exception " + e.getMessage().toString());
        }
    }

    private void assignViews() {
        mCardViewNotification = (CardView) findViewById(R.id.card_view_notification);
        mEdtMobileNo = (TextInputEditText) findViewById(R.id.edtMobileNo);
        mEdtCode = (TextInputEditText) findViewById(R.id.edtCode);
        mBtnRedeem = (Button) findViewById(R.id.btnRedeem);
        mEdtResendMobileNo = (TextInputEditText) findViewById(R.id.edtResendMobileNo);
        mBtnResend = (Button) findViewById(R.id.btnResend);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
