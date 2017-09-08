package com.example.bijarchian.task;

import android.Manifest;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parkbob.ParkbobManager;
import com.parkbob.backend.entity.ParkingEventType;
import com.parkbob.models.RulesContext;


import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mMap;
    private BottomSheetBehavior mBottomSheetBehavior1;
    public static RulesContext rulesContext;
    TextView address;
    ImageView navigationImg;
    ProgressBar navBar;
    Snackbar snak;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude


    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // every one meter

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000  * 1; // every one second

    // Declaring a Location Manager
    protected LocationManager locationManager;
    Location loc = new Location("location");



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_content);
        setpermission();
        initMap();
        initbottomsheet();
        navigate(this);

    }


    //navigation from current location to selected location
    private void navigate(final LocationListener listener) {
        navigationImg = (ImageView) findViewById(R.id.navigation_img);
        navBar = (ProgressBar) findViewById(R.id.progressBar2);
        navBar.setVisibility(View.INVISIBLE);

        navigationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navBar = (ProgressBar) findViewById(R.id.progressBar2);
                navBar.setVisibility(View.INVISIBLE);
                view.setBackgroundColor(Color.BLUE);
                Location currentloc = getLocation(listener);
                if(canGetLocation) {
                    Navigate nav = new Navigate(view);
                    nav.execute(currentloc, loc);
                }
                else{

                    snak.make(view, "Location Service disabled", Snackbar.LENGTH_SHORT).show();
                }



            }
        });

    }

        //initilizing bottomsheet
    private void initbottomsheet() {
        CoordinatorLayout cordinate = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        View bottomsheet = cordinate.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomsheet);
        mBottomSheetBehavior1.setHideable(true);
        //hide the bottomsheet once the bottomsheet initilized
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //expand the bottomsheet once the data comming
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    address = (TextView) findViewById(R.id.addresstxt);
                    address.setText(rulesContext.getAddress().getShortDisplayAddress());
                    //instanciate the fragment tab inside bottomsheet
                    BottomSheetFragment fragment = new BottomSheetFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();


                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    //initilize map
    private void initMap() {

        if (mMap == null) {
            FragmentManager fm = getSupportFragmentManager();
            SupportMapFragment map = (SupportMapFragment) fm.findFragmentById(R.id.map);
            map.getMapAsync(this);



        }

    }
    // request permission for android devices 6 and higher
    private void setpermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //bind service
            ParkbobManager.getInstance().bindService(this);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //bind service
                ParkbobManager.getInstance().bindService(this);

            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


            //once map ready zoom and display default location
            loc.setLatitude(48.21173395015507);
            loc.setLongitude(16.377457603812218);
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(48.21173395015507,
                            16.377457603812218));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {


                Marker marker = null;
                //set selected location
                loc.setLatitude(latLng.latitude);
                loc.setLongitude(latLng.longitude);
                mMap.clear();
                //add marker to selected location
                mMap.addMarker(new MarkerOptions()
                        .title("")
                        .position(latLng)
                        .snippet(""));
                //zoom to seleted location
                CameraUpdate center=
                        CameraUpdateFactory.newLatLng(latLng);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(20);

                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
                // simulate parking events
                ParkbobManager.getInstance().simulateParkingEvent(loc, ParkingEventType.IN);

                //hide the bottomsheet in selecting new location
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

                //requesting Rules of seleted location based on LatLong
                new GetRuleContext().execute(latLng);


            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //getRulecontext need to run on background thread
    public class GetRuleContext extends AsyncTask<LatLng, Void, RulesContext> {

        //get data from server
        @Override
        protected RulesContext doInBackground(LatLng... latLngs) {
            rulesContext = ParkbobManager.getInstance().getRulesContext(latLngs[0], 100);
            return rulesContext;
        }

        @Override
        protected void onPostExecute(RulesContext rulesContext) {
            super.onPostExecute(rulesContext);
            if (rulesContext != null)
                //change the state of bottomsheet once data comming from the server
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
    }

    public class Navigate extends AsyncTask<Location, Void, PolylineOptions> {
        View view;
        ArrayList<LatLng> directionPoint = null;

        public Navigate(View view){
            this.view = view;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            navBar.setVisibility(View.VISIBLE);
            navBar.setIndeterminate(true);
        }

        @Override
        protected PolylineOptions doInBackground(Location... loc) {

            //drawing line from current location to selected location

            try {
                String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                        + "origin=" + loc[1].getLatitude() + "," + loc[1].getLongitude()
                        + "&destination=" + loc[0].getLatitude() + "," + loc[0].getLongitude()
                        + "&sensor=false&units=metric&mode=driving";

                Log.d("url", url);

                OkHttpClient httpClient = new OkHttpClient();

                final Request request = new Request.Builder().url(url).build();
                Response response = null;
                try {
                    //response from google map
                    response = httpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                InputStream res = response.body().byteStream();
                MapDirection md = new MapDirection();

                Document doc = md.getresponse(res);

                directionPoint = md.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(5).color(
                        Color.BLUE);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                return rectLine;

            }catch (NullPointerException e){
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(PolylineOptions rect) {
            super.onPostExecute(rect);
        if(rect != null) {

            //move camrea in map in order to display navigation between current and selected location
            Polyline polylin = mMap.addPolyline(rect);
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng latLngPoint : directionPoint)
                boundsBuilder.include(latLngPoint);

            int routePadding = 100;
            try {
                LatLngBounds latLngBounds = boundsBuilder.build();

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
            navBar.setVisibility(View.INVISIBLE);
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else{
            snak.make(view, "Can not get your Location", Snackbar.LENGTH_SHORT).show();
        }
        }
    }

    public Location getLocation(LocationListener locationListener) {
        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.

                        }
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }



}
