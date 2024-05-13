package com.example.summaryapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PrintClassSummary extends AppCompatActivity {

    private ClassSummaryDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ClassSummaryDB(this);
        loadClassSummaries();
    }
    private void loadClassSummaries() {
        String query = "SELECT * FROM summaries;";
        Cursor cursor = db.selectLectures(query);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String course = cursor.getString(1);
                String type = cursor.getString(2);
                long date = cursor.getLong(3);
                String lecture = cursor.getString(4);
                String topic = cursor.getString(5);
                String summary = cursor.getString(6);

                // Print the data to the console
                System.out.println("ID: " + id);
                System.out.println("Course: " + course);
                System.out.println("Type: " + type);
                System.out.println("Date: " + date);
                System.out.println("Lecture: " + lecture);
                System.out.println("Topic: " + topic);
                System.out.println("Summary: " + summary);

//                Log.d("ClassSummary", "ID: " + id + ", Course: " + course + ", Type: " + type + ", Date: " + date + ", Lecture: " + lecture + ", Topic: " + topic + ", Summary: " + summary);
            }
        }


    }

}