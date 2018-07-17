package com.bandhan.mantra.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bandhan.mantra.R;

public class Login extends AppCompatActivity {

    private EditText mEdtEmail;
    private EditText mEdtPwd;
    private Button mBtnLogin;
    private TextView textViewForgotPassword;
    private TextView textViewNewaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignViews();



    }

    private void assignViews() {
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mEdtPwd = (EditText) findViewById(R.id.edtPwd);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        textViewForgotPassword = (TextView) findViewById(R.id.TextViewForgotPassword);
        textViewNewaccount = (TextView) findViewById(R.id.TextViewNewaccount);
        setListners();
    }

    private void setListners(){
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
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

            }
        });
    }

    private void getEmailDialoug()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("RESET PASSWORD");
        alertDialog.setMessage("Enter Email");

        final EditText input = new EditText(Login.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setIcon(R.drawable.app_logo);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
                        if (email.compareTo("") == 0) {

                        }else {
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

    private void resetPassword(String email) {

    }

    private void signin() {
        startActivity(new Intent(Login.this, DashboardActivity.class));
    }
}
