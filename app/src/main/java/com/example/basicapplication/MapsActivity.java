package com.example.basicapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicapplication.adapter.AdapterForAllSuggest;
import com.example.basicapplication.adapter.CustomAdapter;
import com.example.basicapplication.adapter.MyInfoWindowAdapter;
import com.example.basicapplication.adapter.Subject;
import com.example.basicapplication.maps.PermissionUtils;
import com.example.basicapplication.parse.JsonParser;
import com.example.basicapplication.recycler.RecyclerViewClick;
import com.example.basicapplication.service.RequestLocationInBackground;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    private GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;

    RelativeLayout overlay;
    ImageButton btnMyLocation;
    EditText edtSearch;
    ImageButton btnSearch;
    GridView gvMenuSuggest;
    ImageButton btnListCategory;
    ImageButton btnSwitch;
    RecyclerView rvConsoleSuggest;
    LinearLayout layoutRvConsoleSuggest;
    RelativeLayout formMenu;
    ImageButton btnCancelMenu;
    RelativeLayout formDetail;
    TextView tvTitle;
    TextView tvCategory;
    TextView tvAddress;

    Circle myCircle;
    Marker myMarker;
    ArrayList<Circle> circles;
    ArrayList<Marker> markers;
    ArrayList<Subject> list;
    String category;
    HashMap<String, String> hashMapList;
    ArrayList<HashMap<String, String>> maps;
    ArrayList<HashMap<String, String>> listItemPlace = new ArrayList<>();
    AdapterForAllSuggest adapterForAllSuggest;
    ArrayList<Marker> choiceList = new ArrayList<>();
    Marker myMarkerChoice;

    boolean checkConsoleSuggest = false;
    boolean checkMenuExists = false;

    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                getCurrentLocation();
            }
        }
    };

    private void innit() {
        overlay = findViewById(R.id.overlay);
        btnListCategory = findViewById(R.id.btnListCategory);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        gvMenuSuggest = findViewById(R.id.gvMenuSuggest);
        btnSwitch = findViewById(R.id.btnSwitch);
        rvConsoleSuggest = findViewById(R.id.rvConsoleSuggest);
        layoutRvConsoleSuggest = findViewById(R.id.layoutRvConsoleSuggest);
        formMenu = findViewById(R.id.formMenu);
        btnCancelMenu = findViewById(R.id.btnCancelMenu);
        formDetail = findViewById(R.id.formDetail);
        tvTitle = findViewById(R.id.tvTitle);
        tvAddress = findViewById(R.id.tvAddress);
        tvCategory = findViewById(R.id.tvCategory);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestNowLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        innit();
        circles = new ArrayList<>();
        markers = new ArrayList<>();
        enableMyLocation();
        readInfoPlace();
        searchPlace();
        showMenuCategory();
        map.setOnMarkerClickListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnInfoWindowClickListener(this);
        setupForRecyclerSuggest();

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                // map.setMyLocationEnabled(true);
                btnMyLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myMarker != null) {
                            myMarker.remove();
                            myCircle.remove();
                        }
                        getMyLocation(map);
                    }
                });
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    //Set my location and move to it
    private void getMyLocation(final GoogleMap googleMap) {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
                    myCircle = map.addCircle(new CircleOptions()
                            .center(point)
                            .radius(100)
                            .strokeColor(Color.parseColor("#21B8F3"))
                            .fillColor(Color.parseColor("#4847C4F4")));

                    myMarker = map.addMarker(new MarkerOptions()
                            .position(point)
                            .title(getAddress(point)));

                } else {
                    Log.d("TAG", "location was null");
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.getMessage());
            }
        });

    }

    public String getAddress(LatLng point) {
        String address = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.getMessage();
        }
        return address;
    }

    public LatLng getLocation(String namePlace) {
        LatLng point = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(namePlace, 1);
            Address address = addresses.get(0);
            point = new LatLng(address.getLatitude(), address.getLongitude());
        } catch (Exception e) {
            e.getMessage();
        }
        return point;
    }

    public void readInfoPlace() {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.getTitle();
                marker.getPosition();
            }
        });
    }

    public void searchPlace() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                category = null;
                if (!edtSearch.getText().toString().trim().equals("")) {
                    LatLng place = getLocation(edtSearch.getText().toString().trim());
                    if (place == null) {
                        Toast.makeText(getApplication(), "Please, try again", Toast.LENGTH_SHORT).show();
                    } else {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(place)
                                .title(edtSearch.getText().toString().trim());
                        map.addMarker(markerOptions);
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 17));
                    }
                }
            }
        });
    }

    public void showMenuCategory() {
        list = new ArrayList<>();
        list.add(new Subject("Nhà Hàng", 1));
        list.add(new Subject("Cà Phê", 2));
        list.add(new Subject("Giải trí", 3));
        list.add(new Subject("ATM", 4));
        list.add(new Subject("Trạm xăng", 5));
        list.add(new Subject("Dịch vụ y tế", 6));
        list.add(new Subject("Khách sạn & Du lịch", 7));
        list.add(new Subject("Làm đẹp & Spa", 8));
        list.add(new Subject("Cửa hàng & Siêu thị", 9));
        list.add(new Subject("Các công ty dịch vụ", 10));
        list.add(new Subject("Thắng cảnh", 11));
        list.add(new Subject("Giáo dục", 12));
        list.add(new Subject("Quán rượu", 13));
        list.add(new Subject("Thể dục và Thể thao", 14));
        CustomAdapter adapter = new CustomAdapter(this, list, R.layout.layout_item_suggest);
        gvMenuSuggest.setAdapter(adapter);
        btnListCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMarker != null || myCircle != null){
                    myMarker.remove();
                    myCircle.remove();
                }
                overlay.setVisibility(View.GONE);
                formMenu.setVisibility(View.VISIBLE);
                ObjectAnimator
                        showMenu = ObjectAnimator.ofFloat(formMenu, "translationY", -2000f, 0);
                showMenu.setDuration(800);
                showMenu.setInterpolator(new AccelerateDecelerateInterpolator());
                showMenu.start();
                formMenu.setVisibility(View.VISIBLE);
            }
        });
        actionMenu();
    }

    public void actionMenu() {
        gvMenuSuggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = (position + 1) + "";
                edtSearch.setText(list.get(position).getSubject());
                checkMenuExists = false;
                removePointNearMyPlace();
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                getPointWithCategoryNearMyPlace(category, bounds.southwest.latitude + "", bounds.southwest.longitude + "", bounds.northeast.latitude + "", bounds.northeast.longitude + "");
                TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -2000);
                animate.setDuration(800);
                gvMenuSuggest.startAnimation(animate);

                ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(btnSwitch, "rotation", 360f);
                rotationAnimator.setDuration(800);
                ObjectAnimator translateAnimator =
                        ObjectAnimator.ofFloat(btnSwitch, "translationY", 500f,0);
                translateAnimator.setDuration(800);

                AnimatorSet set = new AnimatorSet();
                set.play(rotationAnimator);
                set.start();
                btnSwitch.setVisibility(View.VISIBLE);
                formMenu.setVisibility(View.GONE);
                overlay.setVisibility(View.VISIBLE);
                turnOnConsoleSuggest();
            }
        });
        btnCancelMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -2000);
                animate.setDuration(500);
                formMenu.startAnimation(animate);
                formMenu.setVisibility(View.GONE);
                overlay.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getPointWithCategoryNearMyPlace(String category, String latitudeS, String longitudeS, String latitudeN, String longitudeN) {
        String url = null;
        url =
                "https://map.coccoc.com/map/search.json?category=" + category
                        + "&borders=" + latitudeS
                        + "%2C" + longitudeS
                        + "%2C" + latitudeN
                        + "%2C" + longitudeN;
        PointNearMyPlace pointNearMyPlace = new PointNearMyPlace();
        pointNearMyPlace.execute(url);
    }

    public void removePointNearMyPlace() {
        for (Marker marker : markers) {
            marker.remove();
        }
    }

    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        removePointNearMyPlace();
        if (category != null) {
            getPointWithCategoryNearMyPlace(category, bounds.southwest.latitude + "", bounds.southwest.longitude + "", bounds.northeast.latitude + "", bounds.northeast.longitude + "");
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        if (checkConsoleSuggest == true){
            turnOffConsoleSuggest();
        }
        return true;
    }

    public void setupForRecyclerSuggest() {
        RecyclerViewClick recyclerViewClick = new RecyclerViewClick() {
            @Override
            public void onItemClick(int position) {
                choiceAnItemInRecyclerView(position);
            }

            @Override
            public void onLongClick(int position) {

            }
        };
        LinearLayoutManager relativeLayout = new LinearLayoutManager(MapsActivity.this);
        rvConsoleSuggest.setLayoutManager(relativeLayout);
        if (listItemPlace != null) {
            adapterForAllSuggest = new AdapterForAllSuggest(recyclerViewClick, listItemPlace, MapsActivity.this, rvConsoleSuggest);
            rvConsoleSuggest.setAdapter(adapterForAllSuggest);
            btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checkConsoleSuggest) {
                        turnOnConsoleSuggest();
                        layoutRvConsoleSuggest.setVisibility(View.VISIBLE);
                    } else {
                        turnOffConsoleSuggest();
                    }
                }
            });
        }
    }

    public void turnOnConsoleSuggest() {
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 900);
        relativeLayoutParams.setMargins(0, 60, 0, 0);
        layoutRvConsoleSuggest.setLayoutParams(relativeLayoutParams);
        ObjectAnimator layoutRv =
                ObjectAnimator.ofFloat(layoutRvConsoleSuggest, "translationY", 700, 0);
        //layoutRv.setDuration(800);
        ObjectAnimator btnSwitchOff =
                ObjectAnimator.ofFloat(btnSwitch, "rotation", 0, 180f);
        btnSwitchOff.setDuration(800);
        ObjectAnimator btnSwitchOff1 =
                ObjectAnimator.ofFloat(btnSwitch, "translationY", 900, 0);
        btnSwitchOff.setDuration(800);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(layoutRv).with(btnSwitchOff1);
        animatorSet.play(layoutRv).with(btnSwitchOff);
        animatorSet.start();
        checkConsoleSuggest = true;
    }

    public void turnOffConsoleSuggest() {
        int modifierY = 900;
        ObjectAnimator layoutRv =
                ObjectAnimator.ofFloat(layoutRvConsoleSuggest, "translationY", -50, modifierY);
        // layoutRv.setDuration(800);
        ObjectAnimator btnSwitchOff =
                ObjectAnimator.ofFloat(btnSwitch, "rotation", 180f, 360f);
        btnSwitchOff.setDuration(800);
        ObjectAnimator btnSwitchOff1 =
                ObjectAnimator.ofFloat(btnSwitch, "translationY", 0, modifierY - 130);
        btnSwitchOff.setDuration(800);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(layoutRv).with(btnSwitchOff1);
        animatorSet.play(layoutRv).with(btnSwitchOff);
        animatorSet.start();
        checkConsoleSuggest = false;
        // layoutRvConsoleSuggest.setVisibility(View.GONE);
    }

    public void choiceAnItemInRecyclerView(int position){
        removePointNearMyPlace();
        Double lat = Double.parseDouble(maps.get(position).get("latitude"));
        Double longitude = Double.parseDouble(maps.get(position).get("longitude"));
        String name = maps.get(position).get("title");
        LatLng latLngChoose = new LatLng(lat, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngChoose, 18));
        Marker myChoice = map.addMarker(new MarkerOptions()
                .position(latLngChoose)
                .zIndex(5.0f)
                .title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        myChoice.showInfoWindow();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        for (Marker marker1 : markers){
//            if (marker1 == marker){
//                System.out.println("Call Tag"+marker1.getTag());
//                tvTitle.setText(marker1.getTitle());
//                tvCategory.setText("");
//            }
//        }
       // formDetail.setVisibility(View.VISIBLE);
    }

    public void requestNowLocation(){
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
    }

    private void checkSettingAndStartLocationUpdate() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException exception = (ResolvableApiException) e;
                    try {
                        exception.startResolutionForResult(MapsActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("TAG", "location: " + location.toString());
                } else {
                    Log.d("TAG", "location was null");
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSettingAndStartLocationUpdate();
    }

    @SuppressLint("NewApi")
    private void startServiceBackground() {
        Intent serviceIntent = new Intent(this, RequestLocationInBackground.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop called");
        startServiceBackground();
    }
    class PointNearMyPlace extends AsyncTask<String, Void, String> {

        //            public static final String REQUEST_METHOD = "GET";
//            public static final int READ_TIMEOUT = 15000;
//            public static final int CONNECTION_TIMEOUT = 15000;
        private String getData(String string) throws IOException {
            URL url = new URL(string);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestMethod(REQUEST_METHOD);
//        httpURLConnection.setReadTimeout(READ_TIMEOUT);
//        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String data = builder.toString();
            reader.close();
            return data;
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = getData(strings[0]);
                System.out.println(strings[0] + "RRURL");
                System.out.println("Data: " + data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParser jsonParser = new JsonParser();
            maps = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                maps = jsonParser.parseResult(jsonObject);
                HashMap<String, String> hashMapList = new HashMap<>();
                try {
                    listItemPlace.clear();
                    for (int i = 0; i < maps.size(); i++) {
                        hashMapList = maps.get(i);
                        listItemPlace.add(hashMapList);
                        System.out.println("list called" + listItemPlace.toString());
                        double latitude = Double.parseDouble(hashMapList.get("latitude"));
                        double longitude = Double.parseDouble(hashMapList.get("longitude"));
                        String title = hashMapList.get("title");
                        String category = hashMapList.get("category");
                        String id = hashMapList.get("id");
                        LatLng latLng = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(title);
                        Marker marker = map.addMarker(markerOptions);
                        marker.setTag(marker.getTitle());
                        markers.add(marker);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (adapterForAllSuggest != null) {
                adapterForAllSuggest.notifyDataSetChanged();
            }
            if (listItemPlace != null){
                map.setInfoWindowAdapter(new MyInfoWindowAdapter(MapsActivity.this,listItemPlace));
            }
        }
    }
}