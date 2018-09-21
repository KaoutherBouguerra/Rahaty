package art4muslim.macbook.rahatycustomer.fragments.orders;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import art4muslim.macbook.rahatycustomer.R;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.fragments.CartFragment;
import art4muslim.macbook.rahatycustomer.models.Product;
import art4muslim.macbook.rahatycustomer.models.ProductToCart;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.dionsegijn.konfetti.KonfettiView;

import static android.content.Context.LOCATION_SERVICE;
import static art4muslim.macbook.rahatycustomer.session.Constants.KEY_API_TOKEN;
import static art4muslim.macbook.rahatycustomer.session.Constants.baseUrlImages;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    ArrayList<ProductToCart> productsToCart = new ArrayList<ProductToCart>();
    View v;
    private static final String TAG = OrderDetailsFragment.class.getSimpleName();
    Button _btn_done, _btn_sum, _btn_call,_btn_cancel;
    TextView _txt_customer_name,_txtPhone, _grid_text;
    String id, status_, idCat;
    TextView _txt_price, _txt_product;
    RelativeLayout _relative1;
    String phone;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    boolean isRightToLeft  ;
    ImageView _grid_image, _imDriver;
    private static final long INTERVAL = 0;
    private static final long FASTEST_INTERVAL = 0;
    View mapView;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    GoogleMap m_map;
    Double client_lat,client_lng;
    public static Location mLocation;
    private boolean first= true;
    LinearLayout _linearProducts;
    ProgressBar _progressBar;
    ImageView _img_edit;
    KonfettiView _viewKonfetti;
    final int REQUEST_PERMISSION_CALL = 1001;
    RelativeLayout _relativeLayoutGlob,_relativeNameCustomer;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static BaseApplication baseApplication;
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

        v = inflater.inflate(R.layout.fragment_order_details, container, false);
        initFields();
        baseApplication = (BaseApplication)getActivity().getApplicationContext();

        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        displayLocationSettingsRequest(getActivity());
        id = getArguments().getString("ORDER_ID");
        idCat = getArguments().getString("ID");
        status_ = getArguments().getString("STATUS");
        getActivity().setTitle(getString(R.string.txt_order_detail)+" "+id);
        fetchDetailsOrders();
        switch (status_) {
            case "pending":
                _btn_done.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1f);
                _btn_cancel.setLayoutParams(layoutParams2);
                break;
            case "applied":
                _btn_done.setVisibility(View.VISIBLE);
                _img_edit.setVisibility(View.GONE);
                break;
            default:
                _btn_done.setVisibility(View.GONE);
                _btn_cancel.setVisibility(View.GONE);
                _img_edit.setVisibility(View.GONE);
                break;
        }
        _btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        _btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = Constants.CANCEL_ORDER_URL;

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.ordercanceled))
                        .setMessage(getResources().getString(R.string.txt_confirm_cancel))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                applyForOrder(url,false);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        _img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseApplication.setProductsToCart(productsToCart);
                CartFragment schedule1 = new CartFragment();
                Bundle args = new Bundle();
                args.putBoolean("EDITING", true);
                args.putInt("ORDER_ID", Integer.parseInt(id));
                args.putInt("ID", Integer.parseInt(idCat));
                schedule1.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();
            }
        });

        if (status_.equals("delivered")){
            _btn_cancel.setVisibility(View.GONE);
            _btn_done.setVisibility(View.GONE);
        }
        if (status_.equals("pending")){
            _relativeNameCustomer.setVisibility(View.GONE);
        }

        _btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);

                    return;
                } else {

                    if (!phone.isEmpty()) {
                        Intent intent = new Intent();
                        Uri uri = Uri.parse("tel:" + phone.trim());
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        return v;
    }

    private  void initFields(){

        _btn_done = (Button)v.findViewById(R.id.btn_done);
        _btn_cancel = (Button)v.findViewById(R.id.btn_cancel);
        _btn_call = (Button)v.findViewById(R.id.btn_call);
        _btn_sum = (Button)v.findViewById(R.id.btn_sum);
        _txt_customer_name = (TextView)v.findViewById(R.id.txt_customer_name);
        _txtPhone = (TextView)v.findViewById(R.id.txtPhone);
        _linearProducts = (LinearLayout) v.findViewById(R.id.linearProducts);
        _grid_image = (ImageView)v.findViewById(R.id.grid_image);
        _imDriver = (ImageView)v.findViewById(R.id.imDriver);
        _img_edit = (ImageView)v.findViewById(R.id.img_edit);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _relativeLayoutGlob = (RelativeLayout) v.findViewById(R.id.relativeLayoutGlob);
        _relativeNameCustomer = (RelativeLayout) v.findViewById(R.id.relative2);
        _viewKonfetti = (KonfettiView) v.findViewById(R.id.viewKonfetti);
        _grid_text = (TextView)v.findViewById(R.id.grid_text);
        _relative1 = (RelativeLayout)v.findViewById(R.id.relative1);
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
            //displayCurrentLocation(mLocation.getLatitude(),mLocation.getLongitude());
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

          /*  m_map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (latLng != null)
                        displayCurrentLocation(latLng.latitude, latLng.longitude);
                }
            });
            */


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

            //  BaseApplication.session.saveUserLat(latitude);
            //  BaseApplication.session.saveUserLng(longitude);

            m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            //  m_map.animateCamera();


        }

    }

    public void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_cadeau);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Button dialogButtonoui = (Button) dialog.findViewById(R.id.btn_accept);
        Button dialogButtonnon = (Button) dialog.findViewById(R.id.btn_refuse);



        dialogButtonoui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                applyForOrder(Constants.CONFIRM_ORDER_URL,true);
            }
        });

        dialogButtonnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                applyForOrder(Constants.CONFIRM_ORDER_URL,false);
            }
        });

        dialog.show();

    }

    private void fetchDetailsOrders() {

        _relativeLayoutGlob.setVisibility(View.GONE);
        _progressBar.setVisibility(View.VISIBLE);
        String locale = "&locale=ar";
        if (!isRightToLeft ) {
            locale= "&locale=en";
        }

        String url = Constants.GET_DETAILS_ORDERS_URL+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken()+"&id="+id+locale;

        Log.e(TAG, "fetchDetailsOrders url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "fetchDetailsOrders response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject catsArray = resJsonObj.getJSONObject("data");

                        int id = catsArray.getInt("id");
                        String created_at = catsArray.getString("created_at");
                        String updated_at = catsArray.getString("updated_at");
                        String order_cost = catsArray.getString("order_cost");
                        String delivery_cost = catsArray.getString("delivery_cost");
                        String amount = catsArray.getString("amount");
                        String scheduled_at = catsArray.getString("scheduled_at");
                        boolean is_scheduled = catsArray.getBoolean("is_scheduled");
                        String state = catsArray.getString("state");
                        String expect_time = catsArray.getString("expect_time");
                        String delivery_time = catsArray.getString("delivery_time");
                        String driver_id = catsArray.getString("driver_id");
                        String client_Latitude = catsArray.getString("client_Latitude");
                        String client_id = catsArray.getString("client_id");
                        String app_commission = catsArray.getString("app_commission");
                        String client_longitude = catsArray.getString("client_longitude");
                        String category_id = catsArray.getString("category_id");
                        String descripation = catsArray.getString("descripation");
                        String type = catsArray.getString("type");

                        if (status_.equals("applied")) {

                            JSONObject driver = catsArray.getJSONObject("driver");
                            String drivername = driver.getString("name");
                            String driverphone = driver.getString("phone");
                            String driverthumbnail = driver.getString("thumbnail");

                            _txt_customer_name.setText(drivername);
                            _txtPhone.setText(driverphone);
                            phone = driverphone;
                            Picasso.with(getActivity())
                                    .load(baseUrlImages+driverthumbnail)
                                    .fit()
                                    .into(_grid_image);

                        }
                        JSONArray products = catsArray.getJSONArray("products");

                        for (int i =0;i<products.length();i++){
                            JSONObject jsonObject = products.getJSONObject(i);
                            int idProduct= jsonObject.getInt("id");
                            String ar_name= jsonObject.getString("ar_name");
                            String en_name= jsonObject.getString("en_name");
                            String price= jsonObject.getString("price");
                            String thumbnail= jsonObject.getString("thumbnail");
                            JSONObject pivotObj = jsonObject.getJSONObject("pivot");
                            int quantity = pivotObj.getInt("quantity");

                            JSONObject category = jsonObject.getJSONObject("category");
                            String has_price = category.getString("has_price");
                            String ar_name_cat= category.getString("ar_name");
                            String en_name_cat= category.getString("en_name");
                            String thumbnail_cat= category.getString("thumbnail");
                            if (!isRightToLeft ) {
                                _grid_text.setText(en_name_cat);
                            }else  _grid_text.setText(ar_name_cat);

                            Picasso.with(getActivity())
                                    .load(baseUrlImages+thumbnail_cat)
                                    .fit()
                                    .into(_grid_image);

                            productsToCart.add(new ProductToCart(new Product(idProduct,en_name,ar_name,""+price,thumbnail,has_price), quantity));
                            //baseApplication.getProductsToCart().add(new ProductToCart(new Product(idProduct,en_name,ar_name,""+price,thumbnail,has_price), quantity));

                            if (isRightToLeft)
                            addViews(ar_name,price,quantity);
                            else  addViews(en_name,price,quantity);

                        }
                        if (products.length()==0){
                            _relative1.setVisibility(View.GONE);
                        }
                        m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(client_Latitude), Double.parseDouble(client_longitude)), 15));
                        m_map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(client_Latitude), Double.parseDouble(client_longitude)))
                                )
                                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.pin));

                       // _txt_customer_name.setText(name);
                        _btn_sum.setText(order_cost+" "+getString(R.string.ryal));
                      //  _btn_sum.setText(delivery_cost+" "+getString(R.string.ryal));

                    }
                    _relativeLayoutGlob.setVisibility(View.VISIBLE);
                    _progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _relativeLayoutGlob.setVisibility(View.GONE);
                _progressBar.setVisibility(View.VISIBLE);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

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

                ListOrdersFragment schedule = new ListOrdersFragment();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace( R.id.frame,schedule,"home Fragment");
                fragmentTransaction.commit();
                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    private void applyForOrder(String url, final boolean isClientParticipate  ){

        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
               getResources().getString(R.string.loading), true);
        Log.e(TAG, "applyForOrder url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "applyForOrder response === "+response.toString());
                dialog.dismiss();
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        String msg = resJsonObj.getString("msg");

                        if (isClientParticipate){
                            applyForOffer();
                        } else
                            AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,true,1);
                    }else{
                        String msg = resJsonObj.getString("msg");
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);

                    }

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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());

                params.put("order_id", ""+ id);
                // params.put("expect_time", ""+time);

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

    private void addViews(String ar_name, String price, int quantity){

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.products, null);
        _linearProducts.addView(v);
        _linearProducts.requestLayout();
        _txt_product = (TextView)v.findViewById(R.id.txt_product);
        _txt_price = (TextView)v.findViewById(R.id.txt_price);

        _txt_product.setText(ar_name);
        int p = Integer.parseInt(price)*quantity;
        _txt_price.setText(p+" "+getString(R.string.ryal));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CALL:
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);

                    return;
                } else {
                    if (!phone.isEmpty()) {
                        Intent intent = new Intent();
                        Uri uri = Uri.parse("tel:" + phone.trim());
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }

                break;


        }
    }


    private void applyForOffer( ){

        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.loading), true);
        Log.e(TAG, "applyForOrder url "+Constants.OFFER_URL);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, Constants.OFFER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "applyForOrder response === "+response.toString());
                dialog.dismiss();
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        String msg = resJsonObj.getString("msg");

                            AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,true,1);
                    }else{
                        String msg = resJsonObj.getString("msg");
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);

                    }

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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());

                params.put("offer_id", ""+ id);

                if (!isRightToLeft ) {
                    params.put("locale", "en");
                }else params.put("locale", "ar");

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

}
