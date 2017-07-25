package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import exampfi.daria.chillaxlinuxtryandroid.MultiSharedPreferences;
import exampfi.daria.chillaxlinuxtryandroid.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public class MapsActivity extends FragmentActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;
    boolean fromMarker, toMarker;
    String from, to;
    LocationManager locationManager;
    Location myLocation;
    int PLACE_PICKER_REQUEST;
    String location_string;
    String appointment_adress;
    String appointment_name;
    View rootView;




    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fromMarker = false;
        toMarker = false;
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        PLACE_PICKER_REQUEST = 1;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (map != null) {
            map.setMyLocationEnabled(true);
        }
        if (map != null) {

            myLocation = map.getMyLocation();

            if (myLocation != null) {
                LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                location_string=myLatLng.toString();
                cashViewMultiValue(location_string,"USER_DATA","LOCATION");
            }
        }

        if (map == null) {
            return;
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {
                if ((fromMarker == false) && (toMarker == false)) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latlng);
                    markerOptions.title("" + latlng.latitude + " " + latlng.longitude);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.rsz_start_mark);
                    markerOptions.icon(bitmapDescriptor);
                    from = "" + latlng.latitude + "," + latlng.longitude;
                    map.addMarker(markerOptions);
                    fromMarker = true;
                } else {
                    if ((fromMarker == true) && (toMarker == false)) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latlng);
                        markerOptions.title("" + latlng.latitude + " " + latlng.longitude);
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.rsz_end_mark);
                        markerOptions.icon(bitmapDescriptor);
                        to = "" + latlng.latitude + "," + latlng.longitude;
                        map.addMarker(markerOptions);
                        toMarker = true;
                    } else {
                        if ((fromMarker == true) && (toMarker == true)) {
                            map.clear();
                            fromMarker = false;
                            toMarker = false;
                        }
                    }
                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onCLickPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onClickAppointment(View view) {
        Intent intent = new Intent(MapsActivity.this, Appointment.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                final CharSequence addressName = place.getName();
                final CharSequence addressAdress = place.getAddress();
                appointment_adress=addressAdress.toString();
                appointment_name=addressName.toString();
                SavePreferences("PLACE_NAME",appointment_name);
                SavePreferences("ADRESS",appointment_adress);
                String toastMsg = String.format("You're creating a meeting here: %s", place.getName());//we need to save the name of a place.
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MapsActivity.this, Appointment.class);
                startActivity(intent);
            }
        }
    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class RouteResponse {

        public List<Route> routes;

        public String getPoints() {
            return this.routes.get(0).overview_polyline.points;
        }

        class Route {
            OverviewPolyline overview_polyline;
        }

        class OverviewPolyline {
            String points;
        }
    }

    public interface RouteApi {
        @GET("/maps/api/directions/json")
        void getRoute(
                @Query(value = "origin", encodeValue = false) String position,
                @Query(value = "destination", encodeValue = false) String destination,
                @Query("sensor") boolean sensor,
                @Query("language") String language,
                Callback<RouteResponse> cb
        );
    }

    public void showRoute(View view) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RouteApi routeService = restAdapter.create(RouteApi.class);


        routeService.getRoute(from, to, true, "ru", new Callback<RouteResponse>() {
            public void success(RouteResponse arg0, Response arg1) {
                List<LatLng> mPoints = PolyUtil.decode(arg0.getPoints());
                PolylineOptions line = new PolylineOptions();
                line.width(4f).color(R.color.colorPrimary);//DOESN'T WORK
                LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                for (int i = 0; i < mPoints.size(); i++) {

                    line.add((LatLng) mPoints.get(i));
                    latLngBuilder.include((LatLng) mPoints.get(i));
                }
                map.addPolyline(line);
                int size = getResources().getDisplayMetrics().widthPixels;
                LatLngBounds latLngBounds = latLngBuilder.build();
                CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
                map.moveCamera(track);
            }

            public void failure(RetrofitError arg0) {
            }
        });
    }

    public void cashViewMultiValue(String mystring, String fileKey, String key)
    {

        if(!mystring.isEmpty())
        {
            MultiSharedPreferences settings = new MultiSharedPreferences(this, fileKey, key);
            settings.addValue(mystring);
        }
    }


    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void onClickGoBack(View view){
        Intent intent = new Intent(MapsActivity.this, FriendsOverviewActivity.class);
        startActivity(intent);
    }
}