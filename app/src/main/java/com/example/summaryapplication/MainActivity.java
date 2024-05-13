package com.example.summaryapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            }
        };

        btnCSE475.setOnClickListener(listener);
        btnCSE477.setOnClickListener(listener);
        btnCSE489.setOnClickListener(listener);
        btnCSE495.setOnClickListener(listener);

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] keys = {"action", "sid", "semester"};
        String[] values = {"restore", "2020-1-60-028", "2024-1"};
        httpRequest(keys, values);
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
                   updateLocalDBByServerData(data);
                }
            }
        }.execute();
    }

    private void updateLocalDBByServerData (String data){
        System.out.println("found");
        try{
            ClassSummaryDB summaryDB = new ClassSummaryDB(MainActivity.this);
            JSONObject jo = new JSONObject(data);
            if(jo.has("classes")){
                JSONArray ja = jo.getJSONArray("classes");
                for(int i=0; i<ja.length(); i++){
                    JSONObject summary = ja.getJSONObject(i);
                    String id = summary.getString("id");
                    String course = summary.getString("course");
                    String topic = summary.getString("topic");
                    String type = summary.getString("type");
                    long date = summary.getLong("date");
                    String lecture = summary.getString("lecture");
                    String sum = summary.getString("summary");

                    String summaryID = topic + System.currentTimeMillis();
                    summaryDB.insertLecture(summaryID, course, type, date, lecture, topic, sum);

                }
            }
        }catch(Exception e){}
    }


}