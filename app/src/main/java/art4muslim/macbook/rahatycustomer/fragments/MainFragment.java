package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
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
import art4muslim.macbook.rahatycustomer.adapters.CustomGrid;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.Category;
import art4muslim.macbook.rahatycustomer.models.SettingsModel;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainFragment extends Fragment {
    private FrameLayout redCircle;
    private TextView countTextView;
    private int alertCount = 0;
    GridView grid;
    BaseApplication app;

    View v;
    ArrayList<Category> cats = new ArrayList<Category>();
    private static final String TAG = MainFragment.class.getSimpleName();
    CustomGrid adapter;
    ProgressBar _progressBar;
    boolean isRightToLeft  ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_main, container, false);
        app = (BaseApplication) getActivity().getApplicationContext();
        app.setOrderType("current");
        initFields();
        sendREgistrationId();
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (app.getCategories().size()==0)
        adapter = new CustomGrid(getActivity(), cats,fragmentTransaction);
        else   adapter = new CustomGrid(getActivity(), app.getCategories(),fragmentTransaction);

        grid.setAdapter(adapter);
        getSettings();

        if (app.getCategories().size()==0)
             getCatogories();
      /*  grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                DetailSectionFragment schedule = new DetailSectionFragment();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule,"First Fragment");
                fragmentTransaction.commit();
            }
        });
        */
        return v;
    }

    private void initFields(){
        grid=(GridView)v.findViewById(R.id.grid);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
    }

    private void getCatogories(){

        String url = Constants.GET_CATEGORIES;

        Log.e(TAG, "getCatogories url "+url);
        _progressBar.setVisibility(VISIBLE);
        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getCatogories response === "+response.toString());
                _progressBar.setVisibility(GONE);
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                       // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject dataObject = resJsonObj.getJSONObject("data");
                        JSONArray dataArray = dataObject.getJSONArray("data");

                        for (int i = 0; i<dataArray.length();i++){

                            JSONObject adrJsonObj = dataArray.getJSONObject(i);
                            int id = adrJsonObj.getInt("id");
                            String ar_name = adrJsonObj.getString("ar_name");
                            String en_name = adrJsonObj.getString("en_name");
                            String thumbnail = adrJsonObj.getString("thumbnail");
                            String ar_descripation = adrJsonObj.getString("ar_descripation");
                            String en_descripation = adrJsonObj.getString("en_descripation");
                            int has_price = adrJsonObj.getInt("has_price");

                            cats.add(new Category(id,en_name,ar_name,thumbnail,has_price,ar_descripation));
                        }


                        app.setCategories(cats);
                        adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(GONE);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                 AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                     AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                      AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                       AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                      AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_user).setVisible(true);
        final MenuItem alertMenuItem = menu.findItem(R.id.action_user);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
        updateAlertIcon();
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

        menu.findItem(R.id.action_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                SearchFragment schedule1 = new SearchFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();

                return false;
            }
        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment schedule1 = new CartFragment();
                Bundle args = new Bundle();
                args.putBoolean("EDITING", false);
                schedule1.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();
            }
        });
        super.onPrepareOptionsMenu(menu);
    }
    private void updateAlertIcon() {
        // if alert count extends into two digits, just show the red circle
        alertCount = app.getProductsToCart().size();
      //  if (0 < alertCount && alertCount < 10) {
        countTextView.setText(String.valueOf(alertCount));
      //  } else {
         //   countTextView.setText("");
       // }

      //  redCircle.setVisibility((alertCount > 0) ? VISIBLE : GONE);
    }




    private void sendREgistrationId(){

        final String RegisterId = FirebaseInstanceId.getInstance().getToken();

        String url = Constants.REGISTER_TOKEN_URL;



        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e(TAG, "sendREgistrationId response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){

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
                Log.e(TAG, "token ==  "+RegisterId);
                params.put("token", ""+RegisterId);
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());
                params.put("locale", "ar");


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
    private void getSettings(){

        String url = Constants.GET_SETTINGS_URL;
        Log.e(TAG, "getSettings url "+url);
      //  _progressBar.setVisibility(View.VISIBLE);
        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getSettings response === "+response.toString());
               // _progressBar.setVisibility(View.GONE);
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject dataObject = resJsonObj.getJSONObject("data");
                        String app_commission = dataObject.getString("app_commission");
                        String facebook = dataObject.getString("facebook");
                        String google_plus = dataObject.getString("google_plus");
                        String linked_in = dataObject.getString("linked_in");
                        String instagram = dataObject.getString("instagram");
                        String youtube = dataObject.getString("youtube");
                        String twitter = dataObject.getString("twitter");
                        String ar_about_us = dataObject.getString("ar_about_us");
                        String ar_terms = dataObject.getString("ar_terms");
                        String en_about_us = dataObject.getString("en_about_us");
                        String en_terms = dataObject.getString("en_terms");
                        String ar_share_content = dataObject.getString("ar_share_content");
                        String en_share_content = dataObject.getString("en_share_content");
                        String delivery_cost = dataObject.getString("delivery_cost");
                        SettingsModel settingsModel = new SettingsModel(facebook,google_plus,linked_in,instagram,youtube,twitter
                                ,ar_about_us, ar_terms,en_about_us,en_terms,ar_share_content,en_share_content
                                ,delivery_cost,app_commission);
                        app.setSettingsModel(settingsModel);






                    }

                } catch (JSONException e) {
                   // _progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  _progressBar.setVisibility(View.GONE);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
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
}
