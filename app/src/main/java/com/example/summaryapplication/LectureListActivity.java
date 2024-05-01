package com.example.summaryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LectureListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        TextView tvCourse = findViewById(R.id.tvCourse);
        String courseName = getIntent().getStringExtra("Course");
        if (courseName != null) {
            tvCourse.setText(courseName + " Course Summary");
        }
        Button btnNew = findViewById(R.id.btnNew);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureListActivity.this, ClassSummaryActivity.class);
                intent.putExtra("Course", courseName);
                startActivity(intent);
                finish();
            }
        });

        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}