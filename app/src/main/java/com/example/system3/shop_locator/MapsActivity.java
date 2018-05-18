package com.example.system3.shop_locator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleMap mMap;
    public static Double latitude=0.0,longitude=0.0;
    Double latitude1=0.0,longitude1=0.0;
    Marker mark;
    ImageButton select;
    GPSTracker gps;
    static String searchlocatoin;
    EditText location;
    public static String lat,log;
    List<Address>addresslist = new ArrayList<>();
    PlaceAutocompleteFragment placeAutoComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gps = new GPSTracker(MapsActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // location=(EditText)findViewById(R.id.Tlocation);
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("requested location"));
                mMap.setOnMapClickListener(MapsActivity.this);
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 170f));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                latitude=place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;
                Log.d("Maps", "Place selected: " + place.getName());

            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
        // search=(Button) findViewById(R.id.Bsearch);
      /*  search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                searchlocatoin=location.getText().toString();
                if (searchlocatoin!=null||!searchlocatoin.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addresslist = geocoder.getFromLocationName(searchlocatoin, 1);
                        Address address = new Address(Locale.getDefault());
                        if (address!=null){
                            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latlng).title("requested location"));
                            mMap.setOnMapClickListener(MapsActivity.this);
                            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 170f));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latlng)      // Sets the center of the map to location user
                                    .zoom(17)                   // Sets the zoom
                                    .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
        select=(ImageButton) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(latitude==0.0||longitude==0.0){
                    Toast.makeText(MapsActivity.this, "No location", Toast.LENGTH_SHORT).show();

                }
                else {
                    final AlertDialog.Builder bu = new AlertDialog.Builder(MapsActivity.this);
                    bu.setTitle("LOCATION");
                    bu.setMessage("Latitude: "+""+latitude+"\nLongitude: "+""+longitude);
                    bu.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            lat=""+latitude;
                            log=""+longitude;
                            finish();
                           //startActivity(new Intent(MapsActivity.this,Shop_Register.class));
                        }
                    });
                    bu.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.dismiss();
                        }
                    });
                    bu.create();
                    bu.show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // maplat=
        // Add a marker in Sydney and move the camera
         latitude1 = gps.getLatitude();
         longitude1 = gps.getLongitude();
            Log.e("latit",""+latitude1);
        Log.e("long",""+longitude1);

        if (latitude1==0.0&&longitude==0.0) {
            LatLng sydney = new LatLng(10.5276, 76.2144);
            mMap.addMarker(new MarkerOptions().position(sydney).title("your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setOnMapClickListener(this);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8.0f));
        }
        else {
            LatLng sydney = new LatLng(latitude1, longitude1);
            mMap.addMarker(new MarkerOptions().position(sydney).title("your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setOnMapClickListener(this);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(mark!=null){
            mark.remove();
        }
            mark= mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            lat=""+latLng.latitude;
            log=""+latLng.longitude;
            latitude=latLng.latitude;
            longitude=latLng.longitude;
    }


}
