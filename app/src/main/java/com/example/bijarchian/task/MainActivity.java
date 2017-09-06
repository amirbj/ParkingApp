package com.example.bijarchian.task;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parkbob.ParkbobConfiguration;
import com.parkbob.ParkbobManager;
import com.parkbob.backend.entity.ParkingEventType;
import com.parkbob.models.RulesContext;
import com.parkbob.service.util.DeviceSupported;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mMap;
    private BottomSheetBehavior mBottomSheetBehavior1;
    public static RulesContext rulesContext;
    TextView address;
    ImageView navigationImg;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000  * 1;

    // Declaring a Location Manager
    protected LocationManager locationManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_content);
        setpermission();
        initMap();
        initbottomsheet();
        navigate(this);

    }

    private void navigate(final LocationListener listener) {
        navigationImg = (ImageView) findViewById(R.id.navigation_img);
        navigationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location loc = getLocation(listener);



            }
        });

    }


    private void initbottomsheet() {
        CoordinatorLayout cordinate = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        View bottomsheet = cordinate.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomsheet);
        mBottomSheetBehavior1.setHideable(true);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    address = (TextView) findViewById(R.id.addresstxt);
                    address.setText(rulesContext.getAddress().getShortDisplayAddress());
                    BottomSheetFragment fragment = new BottomSheetFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();


                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    private void initMap() {

        if (mMap == null) {
            FragmentManager fm = getSupportFragmentManager();
            SupportMapFragment map = (SupportMapFragment) fm.findFragmentById(R.id.map);
            map.getMapAsync(this);


        }

    }

    private void setpermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            ParkbobManager.getInstance().bindService(this);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ParkbobManager.getInstance().bindService(this);

            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {


                Marker marker = null;
                Location loc = new Location("location");
                loc.setLatitude(latLng.latitude);
                loc.setLongitude(latLng.longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .title("")
                        .position(latLng)
                        .snippet(""));


                ParkbobManager.getInstance().simulateParkingEvent(loc, ParkingEventType.IN);
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

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


    public class GetRuleContext extends AsyncTask<LatLng, Void, RulesContext> {


        @Override
        protected RulesContext doInBackground(LatLng... latLngs) {
            rulesContext = ParkbobManager.getInstance().getRulesContext(latLngs[0], 100);
            return rulesContext;
        }

        @Override
        protected void onPostExecute(RulesContext rulesContext) {
            super.onPostExecute(rulesContext);
            if (rulesContext != null)
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

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
