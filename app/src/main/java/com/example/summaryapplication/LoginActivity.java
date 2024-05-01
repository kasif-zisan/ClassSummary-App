package com.example.summaryapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserId, etPW;
    private CheckBox cbRemUserId, cbRemPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserId = findViewById(R.id.etUserId);
        etPW = findViewById(R.id.etPW);

        cbRemUserId =  findViewById(R.id.cbRemUserId);
        cbRemPass = findViewById(R.id.cbRemPass);

        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isRemUserIdChecked = sp.getBoolean("REM_USER", false);
        boolean isRemPassChecked = sp.getBoolean("REM_PASS", false);

        cbRemUserId.setChecked(isRemUserIdChecked);
        cbRemPass.setChecked(isRemPassChecked);

        if (isRemUserIdChecked) {
            String spUserId = sp.getString("USER_ID", "CREATED");
            etUserId.setText(spUserId);
        }
        if (isRemPassChecked) {
            String spPW = sp.getString("PASSWORD", "CREATED");
            etPW.setText(spPW);
        }


        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });
    }
    private void processLogin(){
        String userId = etUserId.getText().toString().trim();
        String userPW = etPW.getText().toString().trim();
        String errMsg = "";

        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String spUserId = sp.getString("USER_ID", "CREATED");
        String spPW = sp.getString("PASSWORD", "CREATED");

        if (!userId.equals(spUserId)){
            errMsg+="User ID not found\n";
        }
        if(!userPW.equals(spPW)){
            errMsg+="Password not found\n";
        }
        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("REM_USER", cbRemUserId.isChecked());
        e.putBoolean("REM_PASS", cbRemPass.isChecked());
        e.commit();

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showErrorDialog(String errMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errMsg);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}