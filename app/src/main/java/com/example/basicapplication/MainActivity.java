package com.example.basicapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.basicapplication.adapter.AdapterDashboard;
import com.example.basicapplication.recycler.RecyclerViewClick;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewClick{

    RecyclerViewClick recyclerViewClick;
    RecyclerView rvDashBoard;
    AdapterDashboard adapterDashboard;
    ArrayList<String> arrayList;

    String TAG = "DASHBOARD";

    private void init() {
        rvDashBoard = findViewById(R.id.rvDashBoard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setRvDashBoard();
        getToken();
    }

    public void setRvDashBoard() {
        arrayList = new ArrayList<>();
        arrayList.add("Locator");
        arrayList.add("Direction");
//        arrayList.add("C");
//        arrayList.add("D");
//        arrayList.add("D");
//        arrayList.add("D");
        adapterDashboard = new AdapterDashboard(this, arrayList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvDashBoard.setLayoutManager(gridLayoutManager);
        rvDashBoard.setAdapter(adapterDashboard);
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0){
            Intent intent = new Intent(this, MapsActivity.class);
            intent = intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }else if (position == 1){
            Intent intent = new Intent(this, DirectionActivity.class);
            intent = intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(int position) {
        Toast.makeText(this,"Long Clicked"+position,Toast.LENGTH_SHORT).show();
    }

    public void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        System.out.println("Token: "+ msg);
                    }
                });
        if (getIntent() != null && getIntent().hasExtra("key1")) {
            for (String key : getIntent().getExtras().keySet()) {
                Log.d(TAG, "onCreate: Key: " + key + " Data: " + getIntent().getExtras().getString(key));
            }
        }
    }
}