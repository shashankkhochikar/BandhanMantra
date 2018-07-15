package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bandhan.mantra.R;

public class RecurringCampaignListActivity extends AppCompatActivity {

    private FloatingActionButton addRecurringCampaignsFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_campaign_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addRecurringCampaignsFab = (FloatingActionButton) findViewById(R.id.addRecurringCampaignsFab);
        addRecurringCampaignsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecurringCampaignListActivity.this,CreateRecurringCampaignActivity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
