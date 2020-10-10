package com.example.basicapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplication.R;
import com.example.basicapplication.recycler.RecyclerViewClick;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterForAllSuggest extends RecyclerView.Adapter<AdapterForAllSuggest.MyViewHolder> {

    ArrayList<HashMap<String, String>> list;
    Activity activity;
    RecyclerView recyclerView;
    RecyclerViewClick recyclerViewClick;

    public AdapterForAllSuggest(RecyclerViewClick recyclerViewClick, ArrayList<HashMap<String, String>> list, Activity activity, RecyclerView recyclerView) {
        this.recyclerViewClick = recyclerViewClick;
        this.list = list;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    public AdapterForAllSuggest(ArrayList<HashMap<String, String>> list, Activity activity, RecyclerView recyclerView) {
        this.list = list;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.layout_item_console_suggest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_item_suggest;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try{
            HashMap<String, String> hashMap = list.get(position);
            holder.tvSubjectSuggest.setText(hashMap.get("title"));
            holder.tvAddressSuggest.setText(hashMap.get("address"));
            holder.tvCategorySuggest.setText(hashMap.get("category"));
            Picasso.get()
                    .load("https:" + hashMap.get("img"))
                    .resize(150, 150)
                    .centerCrop()
                    .into(holder.imgSubjectSuggest);
            if (position % 2 == 1){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) 1);
                holder.borderLayout.setLayoutParams(layoutParams);
            }else{
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) 2);
                holder.borderLayout.setLayoutParams(layoutParams);
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSubjectSuggest;
        TextView tvSubjectSuggest;
        TextView tvCategorySuggest;
        TextView tvAddressSuggest;
        LinearLayout borderLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSubjectSuggest = itemView.findViewById(R.id.imgSuggest);
            tvSubjectSuggest = itemView.findViewById(R.id.tvSubjectSuggest);
            tvCategorySuggest = itemView.findViewById(R.id.tvCategorySuggest);
            tvAddressSuggest = itemView.findViewById(R.id.tvAddressSuggest);
            borderLayout = itemView.findViewById(R.id.borderLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
