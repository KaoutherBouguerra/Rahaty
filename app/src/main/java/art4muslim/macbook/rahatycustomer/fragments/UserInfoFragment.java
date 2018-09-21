package art4muslim.macbook.rahatycustomer.fragments;


import android.app.ProgressDialog;
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
import java.util.HashMap;
import java.util.Map;

import static art4muslim.macbook.rahatycustomer.session.SessionManager.KEY_NAME;
import static art4muslim.macbook.rahatycustomer.session.SessionManager.Key_UserPhone;
import static art4muslim.macbook.rahatycustomer.session.SessionManager.Key_UserPhoneHome;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {


    View v;
    boolean isRightToLeft;
    private static final String TAG = UserInfoFragment.class.getSimpleName();
    EditText _etName, _etPhone, _etPhoneHome, _etPassword, _etReTypePassword;
    Button _btnregp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_user_info, container, false);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        getActivity().setTitle(getString(R.string.profile));
        initFields();

        fillUserInformation();

        _btnregp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO check password
                // TODO validate fields
                attemptUpdate();

            }
        });
        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initFields(){

        _etName = (EditText)v.findViewById(R.id.etName);
        _etPhone = (EditText)v.findViewById(R.id.etPhone);
        _etPhoneHome = (EditText)v.findViewById(R.id.etPhoneHome);
        _etPassword = (EditText)v.findViewById(R.id.etPassword);
        _etReTypePassword = (EditText)v.findViewById(R.id.etReTypePassword);
        _btnregp = (Button)v.findViewById(R.id.btnregp);

    }
    private void attemptUpdate() {


        // Reset errors.
        _etName.setError(null);
        _etPhone.setError(null);
        _etPhoneHome.setError(null);
        _etPassword.setError(null);
        _etReTypePassword.setError(null);


        // Store values at the time of the login attempt.
        String name =  _etName.getText().toString();
        String phone = _etPhone.getText().toString();
        String homePhone = _etPhoneHome.getText().toString();
        String password = _etPassword.getText().toString();
        String retrypassword = _etReTypePassword.getText().toString();



        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            _etName.setError(getString(R.string.error_field_required));
            focusView = _etName;
            cancel = true;
        }

        if (TextUtils.isEmpty(homePhone)) {
            _etPhoneHome.setError(getString(R.string.error_field_required));
            focusView = _etPhoneHome;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            _etPhone.setError(getString(R.string.error_field_required));
            focusView = _etPhone;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            _etPassword.setError(getString(R.string.error_field_required));
            focusView = _etPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            _etReTypePassword.setError(getString(R.string.error_field_required));
            focusView = _etReTypePassword;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            if (password.equals(retrypassword))
                updateProfile(Constants.PROFILE_URL);
            else {
                _etPassword.setError(getString(R.string.error_password_match));
                _etReTypePassword.setText("");
                _etPassword.setText("");
            }
        }
    }
    private void fillUserInformation(){
        _etName.setText(BaseApplication.session.getUserDetails().get(KEY_NAME));
        _etPhone.setText(BaseApplication.session.getUserDetails().get(Key_UserPhone));
        _etPhoneHome.setText(BaseApplication.session.getUserDetails().get(Key_UserPhoneHome));
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


    private void updateProfile(String url){

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.loading), true);
        Log.e(TAG, "updateProfile url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "updateProfile response === "+response.toString());
                dialog.dismiss();
                try {
                    JSONObject resJsonObj = new JSONObject(response);
                    String status = resJsonObj.getString("status");
                    if (status.equals("1")){
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.messagesucess),true,1);
                        BaseApplication.session.createLoginSession(BaseApplication.session.getUserId().get("ID"), _etName.getText().toString(), _etPhone.getText().toString(), _etPhoneHome.getText().toString(),"","");

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
                params.put("thumbnail", "");
                params.put("locale", "ar");
                params.put("name", _etName.getText().toString());
                params.put("phone", _etPhone.getText().toString());
                params.put("home_phone", _etPhoneHome.getText().toString());
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
