package com.example.summaryapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

public class ClassSummaryActivity extends AppCompatActivity {

    private EditText etDate, etLecture, etTopic, etSummary;
    private RadioButton rbTheory, rbLab;

    private String errMsg = "";
    private String course = "";

    private long dateLong;

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

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

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
                        String dateString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        try {
                            Date date = sdf.parse(dateString);
                            assert date != null;
                            dateLong = date.getTime();
                            etDate.setText(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
            dbHelper.insertLecture(summaryID, course, type, dateLong, lectureNumber, classTopic, classSummary);
            finish();
            
        }
        else{
            dbHelper.updateLecture(summaryID, course, type, dateLong, lectureNumber, classTopic, classSummary);
        }

        String[] keys = {"action", "sid", "semester", "id", "course", "type", "topic", "date", "lecture", "summary"};
        String[] values = {"backup", "2020-1-60-028", "2024-1", summaryID, course, type, classTopic, String.valueOf(dateLong), lectureNumber, classSummary};

        httpRequest(keys, values);

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


    private void httpRequest(final String[] keys, final String[] values){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data= RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


}