package com.example.summaryapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomSummaryAdapter extends ArrayAdapter<ClassSummary> {
    private final Context context;
    private final ArrayList<ClassSummary> values;

    public CustomSummaryAdapter(@NonNull Context context, @NonNull ArrayList<ClassSummary> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_lecture_item, parent, false);

        TextView lectureTitle = rowView.findViewById(R.id.tvLectureTitle);
        TextView lectureDate = rowView.findViewById(R.id.tvLectureDate);
        TextView lectureSummary = rowView.findViewById(R.id.tvLectureSummary);

        ClassSummary e = values.get(position);
        lectureTitle.setText(e.lecture);
        Date date = new Date(e.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = sdf.format(date);
        lectureDate.setText(dateString);
        lectureSummary.setText(e.topic);
        return rowView;
    }
}
