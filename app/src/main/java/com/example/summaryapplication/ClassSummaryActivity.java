package com.example.summaryapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ClassSummaryActivity extends AppCompatActivity {

    private EditText etDate, etLecture, etTopic, etSummary;
    private RadioButton rb477, rb479, rb489, rb495, rbTheory, rbLab;
    private RadioGroup courseGroup1, courseGroup2;

    private String errMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_summary);
        rb477 = findViewById(R.id.CSE477);
        rb479 = findViewById(R.id.CSE479);
        rb489 = findViewById(R.id.CSE489);
        rb495 = findViewById(R.id.CSE495);
        rbTheory = findViewById(R.id.theory);
        rbLab = findViewById(R.id.lab);
        etDate = findViewById(R.id.date);
        etLecture = findViewById(R.id.lecture);
        etTopic = findViewById(R.id.topic);
        etSummary = findViewById(R.id.summary);
        courseGroup1 = findViewById(R.id.courseGroup1);
        courseGroup2 = findViewById(R.id.courseGroup2);

        String courseName = getIntent().getStringExtra("Course");
        if (courseName != null) {
            switch (courseName) {
                case "CSE477":
                    rb477.setChecked(true);
                    break;
                case "CSE479":
                    rb479.setChecked(true);
                    break;
                case "CSE489":
                    rb489.setChecked(true);
                    break;
                case "CSE495":
                    rb495.setChecked(true);
                    break;
            }
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

        boolean cse477 = rb477.isChecked();
        boolean cse479 = rb479.isChecked();
        boolean cse489 = rb489.isChecked();
        boolean cse495 = rb495.isChecked();

        boolean theory = rbTheory.isChecked();
        boolean lab = rbLab.isChecked();

        if (lectureNumber.equals("0") && lectureNumber.equals("50")){
            errMsg+="Invalid Lecture Number\n";
        }
        if (classSummary.length()<5){
            errMsg+="Class Summary Too Short\n";
        }

        int selectedCourseId1 = courseGroup1.getCheckedRadioButtonId();
        int selectedCourseId2 = courseGroup2.getCheckedRadioButtonId();

        if (selectedCourseId1 != -1 && selectedCourseId2 != -1) {
            errMsg += "More than one course selected\n";
        }



        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        String course = "";
        if (cse477){course+="CSE477";}
        else if(cse479){course+="CSE479";}
        else if(cse489){course+="CSE489";}
        else{course+="CSE495";}

        String type = "";
        if(theory){type+="Theory";}
        else{type+="Lab";}

        /*SharedPreferences sp = this.getSharedPreferences("class_summary", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("CSE477", rb477.isChecked());
        e.putBoolean("CSE479", rb479.isChecked());
        e.putBoolean("CSE489", rb489.isChecked());
        e.putBoolean("CSE495", rb495.isChecked());
        e.putBoolean("Theory", rbTheory.isChecked());
        e.putBoolean("Lab", rbLab.isChecked());
        e.putString("Class_Date", classDate);
        e.putString("Class_Lecture", lectureNumber);
        e.putString("Class_Topic", classTopic);
        e.putString("Class_Summary", classSummary);
        e.commit();
        Intent i = new Intent(ClassSummaryActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        */

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

        Intent i = new Intent(ClassSummaryActivity.this, ClassSummary.class);
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