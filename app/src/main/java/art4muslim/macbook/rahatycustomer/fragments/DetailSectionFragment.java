package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import art4muslim.macbook.rahatycustomer.adapters.CustomGridDetails;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.Product;
import art4muslim.macbook.rahatycustomer.session.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSectionFragment extends Fragment {


    View v;
    GridView grid;
    ArrayList<Product> products = new ArrayList<Product>();


    int id;
    int has_price;
    String title, keyword;
    CustomGridDetails adapter;
    ProgressBar _progressBar;
    private static final String TAG = DetailSectionFragment.class.getSimpleName();
    BaseApplication app;
    TextView _txt_last,_txt_current;
    String languageToLoad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_detail_section, container, false);
        app = (BaseApplication)getActivity().getApplicationContext();
        initFields();

        id = getArguments().getInt("ID");
        has_price = getArguments().getInt("HAS_PRICE");
        title = getArguments().getString("TITLE");
        keyword = getArguments().getString("KEYWORD");
        getActivity().setTitle(title);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        languageToLoad = BaseApplication.session.getKey_LANGUAGE();

        if (languageToLoad.equals(Constants.arabic)){
            _txt_current.setBackgroundResource(R.drawable.round_left_rect_shape_b);
            _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            _txt_last.setBackgroundResource(R.drawable.round_right_rect_shape_w);
            _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        } else {
            _txt_current.setBackgroundResource(R.drawable.round_right_rect_shape_b);
            _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            _txt_last.setBackgroundResource(R.drawable.round_left_rect_shape_w);
            _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        }

        adapter = new CustomGridDetails(getActivity(), products,fragmentTransaction,app,id);

        grid.setAdapter(adapter);
        getProducts();
        app.setOrderType("current");

        _txt_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (languageToLoad.equals(Constants.arabic)){
                    _txt_current.setBackgroundResource(R.drawable.round_left_rect_shape_b);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_last.setBackgroundResource(R.drawable.round_right_rect_shape_w);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }else{
                    _txt_current.setBackgroundResource(R.drawable.round_right_rect_shape_b);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_last.setBackgroundResource(R.drawable.round_left_rect_shape_w);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }

                app.setOrderType("current");
            }
        });

        _txt_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageToLoad.equals(Constants.arabic)){
                    _txt_last.setBackgroundResource(R.drawable.round_right_rect_shape_b);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_current.setBackgroundResource(R.drawable.round_left_rect_shape_w);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }else{
                    _txt_last.setBackgroundResource(R.drawable.round_left_rect_shape_b);
                    _txt_last.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                    _txt_current.setBackgroundResource(R.drawable.round_right_rect_shape_w);
                    _txt_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }

                app.setOrderType("last");
            }
        });


        return v;
    }
    private void initFields(){
        grid=(GridView)v.findViewById(R.id.grid);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _txt_current = (TextView)v.findViewById(R.id.txt_current);
        _txt_last = (TextView)v.findViewById(R.id.txt_last);
    }

    private void getProducts(){

        String url = Constants.GET_PRODUCTS_URL;
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken()+"&"+keyword+"=ar&"+category_id;
        _progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "getProducts url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getProducts response === "+response.toString());
                _progressBar.setVisibility(View.GONE);
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject dataObj = resJsonObj.getJSONObject("data");
                        JSONArray catsArray = dataObj.getJSONArray("data");

                        for (int i = 0; i<catsArray.length();i++){

                            JSONObject adrJsonObj = catsArray.getJSONObject(i);
                            int id = adrJsonObj.getInt("id");
                            String ar_name = adrJsonObj.getString("ar_name");
                            String en_name = adrJsonObj.getString("en_name");
                            String thumbnail = adrJsonObj.getString("thumbnail");
                            JSONObject categoryObj = adrJsonObj.getJSONObject("category");
                            String has_price = categoryObj.getString("has_price");
//                            String ar_descripation = adrJsonObj.getString("ar_descripation");
  //                          String en_descripation = adrJsonObj.getString("en_descripation");
                            int price = adrJsonObj.getInt("price");

                            products.add(new Product(id,en_name,ar_name,""+price,thumbnail,has_price));
                        }


                     //   app.setCategories(cats);
                        adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
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
                if (keyword!=null)
                    if (!keyword.equals("null"))
                        params.put("keyword", keyword);

                params.put("category_id", ""+id);
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());
                params.put("locale", "ar");
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
