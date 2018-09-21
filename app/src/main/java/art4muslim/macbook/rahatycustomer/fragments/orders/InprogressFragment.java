package art4muslim.macbook.rahatycustomer.fragments.orders;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

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
import art4muslim.macbook.rahatycustomer.adapters.CustomListAdapter;
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.fragments.MainFragment;
import art4muslim.macbook.rahatycustomer.models.OrderModel;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class InprogressFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,ViewTreeObserver.OnScrollChangedListener{

    View v;
    private static final String TAG = InprogressFragment.class.getSimpleName();
    private List<OrderModel> myOrdersList = new ArrayList<OrderModel>();
    private static String url = "";
    private ProgressDialog pDialog;
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    SessionManager sessionman;
    boolean isRightToLeft;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_inprogress, container, false);
        listView = (ListView) v.findViewById(R.id.list);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();

        adapter = new CustomListAdapter(getActivity(), myOrdersList,"current",fragmentTransaction);
        listView.setAdapter(adapter);


      swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                 fetchOrders("0");
            }
        } );


        return v;
    }

    @Override
    public void onStart() {
        Log.e(TAG,"onStart");
        super.onStart();
       myOrdersList.clear();
        adapter.clear();
        listView.getViewTreeObserver().addOnScrollChangedListener(this);

        // fetchnewOrders();
    }

    @Override
    public void onStop() {
        Log.e(TAG,"onStop");
        super.onStop();
        listView.getViewTreeObserver().removeOnScrollChangedListener(this);

    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        Log.e(TAG,"onRefresh");
        myOrdersList.clear();
        adapter.clear();
        fetchOrders("0");
    }
    @Override
    public void onScrollChanged() {
       if (listView.getFirstVisiblePosition() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    private void fetchOrders(final String type){
        swipeRefreshLayout.setRefreshing(true);
        String url = Constants.GET_ORDERS_URL;
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();

        Log.e(TAG, "fetchOrders url "+url);
        adapter.clear();
        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "fetchOrders response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");

                        JSONObject ordersObj = resJsonObj.getJSONObject("data");
                        JSONArray catsArray = ordersObj.getJSONArray("data");

                        for (int i = 0; i<catsArray.length();i++){

                            JSONObject adrJsonObj = catsArray.getJSONObject(i);
                            int id = adrJsonObj.getInt("id");
                            String order_date = adrJsonObj.getString("created_at");
                            String category_id = adrJsonObj.getString("category_id");
                            String[] str = order_date.split(" ");
                            String state = adrJsonObj.getString("state");
                            String amount = adrJsonObj.getString("amount");
                            if (state.equals("applied"))
                                myOrdersList.add(new OrderModel(""+id,str[0],str[1],state,category_id));

                        }

                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
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
                params.put("api_token", ""+ BaseApplication.session.getAccessToken());
                params.put("is_scheduled", ""+ type);

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String type = intent.getStringExtra("TYPE");
            Log.e(TAG, "== broadcastReceiver ==");
            Log.e(TAG, "== type = "+type);
            fetchOrders(type);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ListOrdersFragment.BROADCAST_ACTION));

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
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
