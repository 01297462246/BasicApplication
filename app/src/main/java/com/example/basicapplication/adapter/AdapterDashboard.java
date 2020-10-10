package com.example.basicapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplication.R;
import com.example.basicapplication.recycler.RecyclerViewClick;

import java.util.ArrayList;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.ViewHolder> {

    ArrayList<String> list;
    Context context;
    RecyclerViewClick recyclerViewClick;

    public AdapterDashboard(RecyclerViewClick recyclerViewClick, ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        this.recyclerViewClick = recyclerViewClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CardView cardView = (CardView) layoutInflater.inflate(R.layout.item_layout_selected, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tempt = list.get(position).trim();
        holder.tvSubject.setText(tempt);

        if (position == 0) {
            holder.imgSubject.setImageResource(R.drawable.ic_map);
        } else if (position == 1) {
            holder.imgSubject.setImageResource(R.drawable.ic_directions);
        } else if (position == 2) {
            //holder.imgSubject.setImageResource(R.drawable.ic_history);
        } else if (position == 3) {

        } else if (position == 4) {

        } else if (position == 5) {

        } else if (position == 6) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;
        ImageView imgSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            imgSubject = itemView.findViewById(R.id.imgSubject);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClick.onItemClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewClick.onLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
