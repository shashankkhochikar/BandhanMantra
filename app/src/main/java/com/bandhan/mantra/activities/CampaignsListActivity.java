package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bandhan.mantra.R;

public class CampaignsListActivity extends AppCompatActivity {

    private FloatingActionButton addCampaignsFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addCampaignsFab = (FloatingActionButton) findViewById(R.id.addCampaignsFab);
        addCampaignsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CampaignsListActivity.this,CreateCampaignActivity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
