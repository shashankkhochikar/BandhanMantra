package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bandhan.mantra.R;

public class CouponCampaignsListActivity extends AppCompatActivity {

    private FloatingActionButton addCouponCampaignsFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_campaigns_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addCouponCampaignsFab = (FloatingActionButton) findViewById(R.id.addCouponCampaignsFab);
        addCouponCampaignsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponCampaignsListActivity.this,CreateCouponCampaignActivity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
