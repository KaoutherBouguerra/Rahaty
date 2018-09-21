package art4muslim.macbook.rahatycustomer.fragments;




import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.ProductToCart;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import art4muslim.macbook.rahatycustomer.R;
import com.google.android.gms.common.api.GoogleApiClient;
import android.location.Location;
import android.location.LocationManager;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapCurrentFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Calendar myCalendar = Calendar.getInstance();
    boolean isRightToLeft  ;
   View v;
    View mapView;
    private static final String TAG = MapCurrentFragment.class.getSimpleName();
    private static final long INTERVAL = 0;
    private static final long FASTEST_INTERVAL = 0;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    GoogleMap m_map;
    Double client_lat,client_lng;
    public static Location mLocation;
    private boolean first= true;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    BaseApplication app;
    TextView txtDate, txtTime;
    LinearLayout _linearLayout;
    ImageView _img_pin;
    boolean isForEdit;

    int idCat;
    int idOrder;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        v = inflater.inflate(R.layout.fragment_map_current, container, false);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
       // getActivity().setTitle(getString(R.string.item_Notif));
        idCat = getArguments().getInt("ID");
        idOrder = getArguments().getInt("ORDER_ID");
        isForEdit = getArguments().getBoolean("EDITING");
        initFields();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();
            }

        };

        displayLocationSettingsRequest(getActivity());
        app = (BaseApplication)getActivity().getApplicationContext();
        Button _btnOrder = (Button) v.findViewById(R.id.btnOrder);
        _btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  passNewCourse();
            }
        });

        if (app.getOrderType()!=null)
        if (app.getOrderType().equals("current")){

            _linearLayout.setVisibility(View.GONE);
        } else{
            app.getProductsToCart().clear();
            _linearLayout.setVisibility(View.VISIBLE);
        }

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                // final int hour3 = mcurrentTime.get(Calendar.HOUR_OF_DAY+3);

                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+3);
                calendar.getTime();//your date +2 hours
                final int hour3 = calendar.get(Calendar.HOUR_OF_DAY);


                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getString(R.string.selectTime));
                mTimePicker.show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        return  v;
    }

    private void initFields(){
        txtDate = (TextView) v.findViewById(R.id.txtDate);
        txtTime = (TextView) v.findViewById(R.id.txtTime);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        _img_pin=(ImageView) v.findViewById(R.id.img_pin);
    }


    private void updateLabelDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDate.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        Log.d(TAG, "onLocationChanged ..............");
        if (first){
            displayCurrentLocation(mLocation.getLatitude(),mLocation.getLongitude());
            first = false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;
        if(m_map != null) {
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo  mg3 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(  isGPSEnabled&& ( mWifi.isConnected() ||  mg3.isConnected())){

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return;
                } else {
                    m_map.setMyLocationEnabled(true);
                }
            }

            m_map.getUiSettings().setMyLocationButtonEnabled(true);

            m_map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (latLng != null)
                        displayCurrentLocation(latLng.latitude, latLng.longitude);
                }
            });

         /*   m_map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng mapCenterLatLng = m_map.getCameraPosition().target;

                    Double latitude = mapCenterLatLng.latitude;
                    Double longitude = mapCenterLatLng.longitude;
                    client_lat = latitude;
                    client_lng =longitude;

                    if (!first) {
                        m_map.clear();
                        m_map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                                .title("My position"))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.pin));
                    }
                }
            });
            */

            m_map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng mapCenterLatLng = m_map.getCameraPosition().target;

                    client_lat = mapCenterLatLng.latitude;
                    client_lng = mapCenterLatLng.longitude;

                }
            });

        }
    }


    private void displayLocationSettingsRequest(final Context context) {


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        m_map.setMyLocationEnabled(true);
        Log.d(TAG, "Location update started ..............: ");
    }

    public void displayCurrentLocation(double latitude, double longitude) {


        if (mLocation!= null){


            Log.e(TAG, "displayCurrentLocation latitude === "+latitude);
            Log.e(TAG, "displayCurrentLocation longitude === "+longitude);

         //   BaseApplication.session.saveUserLat(latitude);
          //  BaseApplication.session.saveUserLng(longitude);

            m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            //  m_map.animateCamera();


        }

    }

    private void passNewCourse(){
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.loading), true);


        String url;
        if (isForEdit){
            url = Constants.EDIT_ORDER_URL;
        } else url = Constants.NEW_ORDERS_URL;


        Log.e(TAG,"passNewCourse url = "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                Log.e(TAG, "passNewCourse response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        JSONObject dataObj = resJsonObj.getJSONObject("data");
                        String id = dataObj.getString("id");

                        if (isForEdit){
                            app.getProductsToCart().clear();
                            AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getString(R.string.send_success),true,2);

                        } else showDialog(id);

                    }else{
                        String msg = resJsonObj.getString("msg");
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);
                    }
                    app.getProductsToCart().clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);
                dialog.dismiss();
                if (error instanceof AuthFailureError) {
                    // AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    //  AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    //   AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    //  AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }

            }
        }) {

         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                String accessId = BaseApplication.session.getAccessToken();
                if(Constants.androiStudioMode.equals("debug")){
                    Log.v("accessid", accessId);}
                headers.put("X-Auth-Token", "" + accessId);
                return headers;
            }
            */
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> params = new HashMap<>();
             Log.e(TAG, "client_lat ==  "+client_lat);
             Log.e(TAG, "client_lng ==  "+client_lng);
             params.put("client_Latitude", ""+client_lat);
             params.put("client_longitude", ""+client_lng);
             params.put("category_id", ""+idCat);
             params.put("type", "order");
             params.put("api_token", ""+ BaseApplication.session.getAccessToken());
             if (!isRightToLeft ) {
                 params.put("locale", "en");
             }else params.put("locale", "ar");

             if (app.getOrderType() != null)
             if (app.getOrderType().equals("last"))
                 params.put("scheduled_at", txtDate.getText().toString()+txtTime.getText().toString());

             if (isForEdit)
                 params.put("order_id", ""+idOrder);



                 for (int i =0; i < app.getProductsToCart().size();i++) {

                     ProductToCart productToCart = app.getProductsToCart().get(i);
                     int idProduct = productToCart.getProduct().getId();
                     int quantity = productToCart.getNum();
                     Log.e(TAG, "products[][id] ==  "+"products["+i+"][id]");
                     Log.e(TAG, "idProduct ==  "+idProduct);

                     Log.e(TAG, "products[][quantity] ==  "+"products["+i+"][quantity]");
                     Log.e(TAG, "quantity ==  "+quantity);

                     params.put("products["+i+"][id]", ""+idProduct);
                     params.put("products["+i+"][quantity]", ""+quantity);
                 }

             Log.e(TAG, "all params  ==  "+params.toString());

             return params;
         }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        // Adding request to request queue
        BaseApplication.getInstance().addToRequestQueue(hisRequest);
    }


    public void showDialog(String id) {


        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_success_order);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Button dialogButtonnon = (Button) dialog.findViewById(R.id.btn_accept);
        final TextView _txt_id_order = (TextView) dialog.findViewById(R.id.txt_id_order);
        _txt_id_order.setText(id);

        dialogButtonnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                MainFragment schedule1 = new MainFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();
            }
        });

        dialog.show();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_user).setVisible(false);

        if (!isRightToLeft ) {
            menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.backright));
        }else  menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.back));

        menu.findItem(R.id.item_back).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                MainFragment schedule1 = new MainFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();

                return false;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }
}
