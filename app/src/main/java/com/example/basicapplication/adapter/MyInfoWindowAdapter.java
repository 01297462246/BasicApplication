package com.example.basicapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basicapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    ArrayList<HashMap<String, String>> hashMapArrayList;
    public MyInfoWindowAdapter(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.context = context;
        this.hashMapArrayList = hashMapArrayList;
        System.out.println("HashMap:"+hashMapArrayList);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_detail_point, null, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        ImageView imgPlace = view.findViewById(R.id.imgPlace);
        String imgURL = null;
        try {
            for (int i = 0; i < hashMapArrayList.size(); i++) {
                if (((marker.getPosition().latitude + "").equals(hashMapArrayList.get(i).get("latitude")) && ((marker.getPosition().longitude + "").equals(hashMapArrayList.get(i).get("longitude"))))) {
                    imgURL = "https:" + hashMapArrayList.get(i).get("img_big");
                    tvTitle.setText(hashMapArrayList.get(i).get("title"));
                    tvCategory.setText(hashMapArrayList.get(i).get("category"));
                    tvAddress.setText(hashMapArrayList.get(i).get("address"));
                    Picasso.get()
                            .load(imgURL)
                            .resize(450, 230)
                            .centerCrop()
                            .into(imgPlace);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
