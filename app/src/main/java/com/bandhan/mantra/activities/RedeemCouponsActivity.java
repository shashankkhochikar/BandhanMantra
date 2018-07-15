package com.bandhan.mantra.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bandhan.mantra.R;

public class RedeemCouponsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_coupons);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
