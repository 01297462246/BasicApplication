package com.example.basicapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;

    EditText edtStart;
    EditText edtDestination;
    Button btnSearch;

    public void init(){
        edtStart = findViewById(R.id.edtStart);
        edtDestination = findViewById(R.id.edtDestination);
        btnSearch = findViewById(R.id.btnSearch);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDirection);
        mapFragment.getMapAsync(this);
        init();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtStart.getText().toString().trim().equals("") || edtDestination.getText().toString().trim().equals("")){
                    Toast.makeText(getApplication(),"Please, check again text box",Toast.LENGTH_SHORT).show();
                }else {
                    direction(edtStart.getText().toString(),edtDestination.getText().toString());
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private void direction(String start,String destination){
        try{
            String uriString =
                    "https://www.google.co.in/maps/dir/"+start+"/"+destination;
            System.out.println("LINKED:"+uriString);
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            //set flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            System.out.println("EROREE"+e.getMessage());
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent =new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
