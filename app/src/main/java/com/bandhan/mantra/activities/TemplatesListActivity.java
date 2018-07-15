package com.bandhan.mantra.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bandhan.mantra.R;

public class TemplatesListActivity extends AppCompatActivity {

    private FloatingActionButton addTemplatesFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addTemplatesFab= (FloatingActionButton)findViewById(R.id.addTemplatesFab);
        addTemplatesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTemplateDialog();
            }
        });
    }

    private void showCreateTemplateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_template_custom_dialog, null);
        dialogBuilder.setView(dialogView);

        //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        //dialogBuilder.setTitle("Custom dialog");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
