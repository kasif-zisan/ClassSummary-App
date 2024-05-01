package com.example.summaryapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText etUserName, etEmail, etPhone, etUserId, etPW, etRPW;
    private CheckBox cbRemUserId, cbRemPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decideNavigation();

        setContentView(R.layout.activity_signup);

        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etUserId = findViewById(R.id.etUserId);
        etPW = findViewById(R.id.etPW);
        etRPW = findViewById(R.id.etRPW);

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

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignup();
            }
        });
    }

    private void processSignup(){
        String userName = etUserName.getText().toString().trim();
        String userEmail = etEmail.getText().toString().trim();
        String userPhone = etPhone.getText().toString().trim();
        String userId = etUserId.getText().toString().trim();
        String userPW = etPW.getText().toString().trim();
        String userRPW = etRPW.getText().toString().trim();
        String errMsg = "";
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        //validation check

        if(userName.length()<4 || userName.length() > 8){
            errMsg += "Invalid User Name\n";
        }
        if(userId.length()<4 || userId.length()>6){
            errMsg += "Invalid User ID\n";
        }
        Matcher matcher = pattern.matcher(userEmail);
        if(!matcher.matches()){
            errMsg += "Invalid Email Address\n";
        }
        if( (userPhone.startsWith(("+880")) && userPhone.length() == 14) ||
                (userPhone.startsWith(("880")) && userPhone.length() == 13) ||
                (userPhone.startsWith(("01")) && userPhone.length() == 11)){}
        else{
            errMsg += "Invalid Phone Number\n";
        }
        if(userPW.length() !=4 || !userPW.equals(userRPW)){
            errMsg += "Invalid Password\n";
        }
        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        //store data on shared preferences
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("USER_NAME", userName);
        e.putString("USER_ID", userId);
        e.putString("USER_EMAIL", userEmail);
        e.putString("USER_PHONE", userPhone);
        e.putString("PASSWORD", userPW);
        e.putBoolean("REM_USER", cbRemUserId.isChecked());
        e.putBoolean("REM_PASS", cbRemPass.isChecked());
        e.commit();
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    private void decideNavigation(){
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String userName = sp.getString("USER_NAME", "NOT-CREATED");
        if(!userName.equals("NOT-CREATED")){
            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

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