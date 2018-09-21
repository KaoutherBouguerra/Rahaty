package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

   EditText _et_title,_et_FeedBack;
   Button _btn_SendComplain;
   boolean isRightToLeft  ;
   private static String TAG = ContactUsFragment.class.getSimpleName();
   View v;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_contact_us, container, false);
        initFields();
        getActivity().setTitle(getString(R.string.item_contact));
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);

        _btn_SendComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendMAil();
            }
        });


        return v;
    }
    private void attemptSendMAil() {
        // Reset errors.
        _et_title.setError(null);
        _et_FeedBack.setError(null);


        // Store values at the time of the login attempt.
        String title = _et_title.getText().toString();
        String content = _et_FeedBack.getText().toString();


        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(title)) {
            _et_title.setError(getString(R.string.error_field_required));
            focusView = _et_title;
            cancel = true;
        }
        if (TextUtils.isEmpty(content)) {
            _et_FeedBack.setError(getString(R.string.error_field_required));
            focusView = _et_FeedBack;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            _linearLayout.setVisibility(View.GONE);

            _progressBar.setVisibility(View.VISIBLE);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);
            sendMessage();
        }
    }
    private void initFields(){
        _et_title = v.findViewById(R.id.et_title);
        _et_FeedBack = v.findViewById(R.id.et_FeedBack);
        _btn_SendComplain = v.findViewById(R.id.btn_SendComplain);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout4);
    }
    private void sendMessage(){
        String title = null;
        String content = null;
        try {
            content = "&content="+ URLEncoder.encode(_et_FeedBack.getText().toString(), "utf-8");
            title ="&title="+URLEncoder.encode(_et_title.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String url = Constants.CONTACT_US_URL;
                //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken()+ title+content+type+sender_id;


        _progressBar.setVisibility(View.VISIBLE);
        _linearLayout.setVisibility(View.GONE);
        Log.e(TAG, "sendMessage url "+url);

        final String finalTitle = title;
        final String finalContent = content;
        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "sendMessage response === "+response.toString());

                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        // JSONObject res = resJsonObj.getJSONObject("response");
                        String msg = resJsonObj.getString("msg");
                          AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,true,4);

                          _et_FeedBack.setText("");
                          _et_title.setText("");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);
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
             params.put("title", finalTitle);
             params.put("content", finalContent);
             params.put("api_token", ""+ BaseApplication.session.getAccessToken());
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
