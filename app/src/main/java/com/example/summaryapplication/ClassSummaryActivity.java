package com.example.summaryapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ClassSummaryActivity extends AppCompatActivity {

    private EditText etDate, etLecture, etTopic, etSummary;
    private RadioButton rbTheory, rbLab;

    private String errMsg = "";
    private String course = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_summary);
        rbTheory = findViewById(R.id.theory);
        rbLab = findViewById(R.id.lab);
        etDate = findViewById(R.id.date);
        etLecture = findViewById(R.id.lecture);
        etTopic = findViewById(R.id.topic);
        etSummary = findViewById(R.id.summary);

        course = getIntent().getStringExtra("Course");
        TextView tvCourse = findViewById(R.id.etCourse);
        if (course != null) {
            tvCourse.setText(course);
        }



        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ClassSummaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSummary();
            }
        });

    }
    private void processSummary(){
        String classDate = etDate.getText().toString();
        String lectureNumber = etLecture.getText().toString();
        String classTopic = etTopic.getText().toString();
        String classSummary = etSummary.getText().toString();

        boolean theory = rbTheory.isChecked();
        boolean lab = rbLab.isChecked();

        if (lectureNumber.equals("0") && lectureNumber.equals("50")){
            errMsg+="Invalid Lecture Number\n";
        }
        if (classSummary.length()<5){
            errMsg+="Class Summary Too Short\n";
        }



        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        String type = "";
        if(theory){type+="Theory";}
        else{type+="Lab";}



        String summaryID = "";
        ClassSummaryDB dbHelper = new ClassSummaryDB(this);
        if (summaryID.isEmpty()){
            summaryID = classTopic + System.currentTimeMillis();
            dbHelper.insertLecture(summaryID, course, type, classDate, lectureNumber, classTopic, classSummary);
            finish();
            
        }
        else{
            dbHelper.updateLecture(summaryID, course, type, classDate, lectureNumber, classTopic, classSummary);
        }

        //Intent i = new Intent(ClassSummaryActivity.this, PrintClassSummary.class);
        Intent i = new Intent(ClassSummaryActivity.this, LectureListActivity.class);
        i.putExtra("Course", course);
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