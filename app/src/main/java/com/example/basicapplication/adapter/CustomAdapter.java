package com.example.basicapplication.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.basicapplication.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Subject> subjects;
    private Activity activity;
    int layout_ID;
    public CustomAdapter(Activity context, ArrayList<Subject> subjects, int resource) {
        this.subjects = subjects;
        this.activity = context;
        this.layout_ID = resource;
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subjects.get(position).imgResource;
    }
    Holder holder;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
         holder = new Holder();
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(layout_ID,parent,false);
            holder.subject = convertView.findViewById(R.id.tvSubject);
            holder.imgSubject = convertView.findViewById(R.id.imgSubject);
            holder.layoutSuggest = convertView.findViewById(R.id.layoutSuggest);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        Subject newSubject = subjects.get(position);
        if (newSubject.imgResource == 1){
            holder.imgSubject.setImageResource(R.drawable.ic_restaurant_cutlery_circular_symbol_of_a_spoon_and_a_fork_in_a_circle);
        }else if(newSubject.imgResource == 2){
            holder.imgSubject.setImageResource(R.drawable.ic_coffee_breaks);
        }else if(newSubject.imgResource == 3){
            holder.imgSubject.setImageResource(R.drawable.ic_theater);
        }else if(newSubject.imgResource == 4){
            holder.imgSubject.setImageResource(R.drawable.ic_credit_card);
        }else if(newSubject.imgResource == 5){
            holder.imgSubject.setImageResource(R.drawable.ic_fuel);
        }else if(newSubject.imgResource == 6){
            holder.imgSubject.setImageResource(R.drawable.ic_plus);
        }else if(newSubject.imgResource == 7){
            holder.imgSubject.setImageResource(R.drawable.ic_sleep);
        }else if(newSubject.imgResource == 8){
            holder.imgSubject.setImageResource(R.drawable.ic_spa);
        }else if(newSubject.imgResource == 9){
            holder.imgSubject.setImageResource(R.drawable.ic_cart);
        }else if(newSubject.imgResource == 10){
            holder.imgSubject.setImageResource(R.drawable.ic_support);
        }else if(newSubject.imgResource == 11){
            holder.imgSubject.setImageResource(R.drawable.ic_ferris_wheel);
        }else if(newSubject.imgResource == 12){
            holder.imgSubject.setImageResource(R.drawable.ic_graduate);
        }else if(newSubject.imgResource == 13){
            holder.imgSubject.setImageResource(R.drawable.ic_martini);
        }else if(newSubject.imgResource == 14){
            holder.imgSubject.setImageResource(R.drawable.ic_bike);
        }
        holder.subject.setText(newSubject.subject);

        return convertView;
    }

    class Holder {
        private TextView subject;
        private ImageView imgSubject;
        LinearLayout layoutSuggest;
    }
}
