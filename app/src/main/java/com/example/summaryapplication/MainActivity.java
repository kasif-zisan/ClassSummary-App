package com.example.summaryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCSE475 = findViewById(R.id.btnCSE477);
        Button btnCSE477 = findViewById(R.id.btnCSE479);
        Button btnCSE489 = findViewById(R.id.btnCSE489);
        Button btnCSE495 = findViewById(R.id.btnCSE495);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String course = b.getText().toString();
                Intent intent = new Intent(MainActivity.this, LectureListActivity.class);
                intent.putExtra("Course", course);
                startActivity(intent);
                finish();
            }
        };

        btnCSE475.setOnClickListener(listener);
        btnCSE477.setOnClickListener(listener);
        btnCSE489.setOnClickListener(listener);
        btnCSE495.setOnClickListener(listener);


    }
}