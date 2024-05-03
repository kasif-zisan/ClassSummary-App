package com.example.summaryapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LectureListActivity extends AppCompatActivity {

    private ArrayList<ClassSummary> lectures;
    private ListView lvLectureList;
    private CustomSummaryAdapter csAdapter;
    String courseName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        TextView tvCourse = findViewById(R.id.tvCourse);
        courseName = getIntent().getStringExtra("Course");
        if (courseName != null) {
            tvCourse.setText(courseName + " Course Summary");
        }

        lvLectureList = findViewById(R.id.listView);
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

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lectures = new ArrayList<>();
        csAdapter = new CustomSummaryAdapter(this, lectures);
        lvLectureList.setAdapter(csAdapter);
        loadClassSummary();
    }
    private void loadClassSummary(){
        String q = "SELECT * FROM summaries WHERE course ='"+courseName+"';";
        ClassSummaryDB db = new ClassSummaryDB(this);
        Cursor cursor = db.selectLectures(q);
        lectures.clear();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String course = cursor.getString(1);
                String type = cursor.getString(2);
                String date = cursor.getString(3);
                String lecture = cursor.getString(4);
                String topic = cursor.getString(5);
                String summary = cursor.getString(6);

                ClassSummary cs = new ClassSummary(id, course, type, date, lecture, topic, summary);
                lectures.add(cs);
            }
            csAdapter.notifyDataSetInvalidated();
            csAdapter.notifyDataSetChanged();
        }

    }
}