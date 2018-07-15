package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bandhan.mantra.R;

public class ContactGroupsListActivity extends AppCompatActivity {

    private FloatingActionButton addGroupFab;
    private RecyclerView gropuListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_groups_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addGroupFab = (FloatingActionButton) findViewById(R.id.addGroupFab);
        addGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactGroupsListActivity.this,ContactListActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
