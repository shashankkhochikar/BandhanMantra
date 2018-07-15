package com.bandhan.mantra.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bandhan.mantra.R;

public class ContactListActivity extends AppCompatActivity {

    private FloatingActionButton addContactFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addContactFab = (FloatingActionButton) findViewById(R.id.addContactFab);
        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactListActivity.this,CreateContactActivity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
