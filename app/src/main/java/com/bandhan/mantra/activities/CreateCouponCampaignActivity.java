package com.bandhan.mantra.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bandhan.mantra.R;

public class CreateCouponCampaignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coupon_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
