package com.daemin.map;

/**
 * Created by hernia on 2015-10-12.
 */

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.daemin.event.SendPlaceEvent;
import com.daemin.timetable.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

public class MapActivity extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
        , LocationListener {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleMap map;
    private Button btSavePlace,btClear,btSearch;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private MarkerOptions markerOptions;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);
        initSetting();
        mAutocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    btClear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mAutocompleteView.getText().toString().equals(""))
                    btClear.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);


            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            /*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));*/

            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon01));
            marking(place.getLatLng());
            places.release();
        }
    };
    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void displayLocation() {

        // Get the button view
        // and next place it, for exemple, on bottom right (as Google Maps app)
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            marking(loc);
        }else {
            Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
    private void marking(LatLng latLng){
        markerOptions.position(latLng);
        map.clear();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        map.addMarker(markerOptions);
    }
    //지오코딩
    private void findGeoPoint(String address) {
        Geocoder geocoder = new Geocoder(this);
        Address addr;
        try {
            List<Address> listAddress = geocoder.getFromLocationName(address, 1);
            if (listAddress.size() > 0) { // 주소값이 존재 하면
                addr = listAddress.get(0); // Address형태로
                marking(new LatLng(addr.getLatitude(),addr.getLongitude()));
                //Log.d("Response", "주소로부터 취득한 위도 : " + lat + ", 경도 : " + lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //역지오코딩
    private void findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    // 전송할 주소 데이터
                    bf.append(currentLocationAddress);
                }
            }

        } catch (IOException e) {
            Toast.makeText(this, "주소취득 실패"
                    , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        mAutocompleteView.setText("");
        mAutocompleteView.setText(bf.toString());
    }
    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        // Displaying the new location on UI
        displayLocation();
    }

    private void initSetting() {

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        markerOptions = new MarkerOptions();
        // Enable Zoom
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon01));
                findAddress(latLng.latitude, latLng.longitude);
                markerOptions.position(latLng);
                map.clear();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                map.addMarker(markerOptions);
            }
        });
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        btSavePlace = (Button) findViewById(R.id.btSavePlace);
        btSavePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = mAutocompleteView.getText().toString();
                if (!place.equals(null)) {
                    EventBus.getDefault().post(new SendPlaceEvent(place));
                    finish();
                }
            }
        });
        btClear = (Button) findViewById(R.id.btClear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
                btClear.setVisibility(View.INVISIBLE);
            }
        });
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findGeoPoint(mAutocompleteView.getText().toString());
            }
        });
    }
}