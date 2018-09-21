package art4muslim.macbook.rahatycustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button _btn_signup;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;

    @NotEmpty(messageId =  R.string.validation_mobile, order = 1)
    protected EditText inputPhone;
    boolean isRightToLeft;

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);

        initFields();
        _btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptPassword();
            }
        });
    }

    private void initFields() {
        inputPhone = (EditText) findViewById(R.id.etphone);
        _btn_signup = (Button) findViewById(R.id.btn_signup);
        _progressBar=(ProgressBar) findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) findViewById(R.id.linearLayout4);

    }


    private void attemptPassword() {


        // Reset errors.
        inputPhone.setError(null);


        // Store values at the time of the login attempt.
        String phone = inputPhone.getText().toString();



        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            inputPhone.setError(getString(R.string.error_field_required));
            focusView = inputPhone;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            _linearLayout.setVisibility(View.GONE);
            _progressBar.setVisibility(View.VISIBLE);
            forgotPassword(phone);
        }
    }


    private void forgotPassword(final String phone) {

        String url = Constants.RESET_PASSWORD_URL;
        //+"name=admin"+"&phone="+phone+"&home_phone="+phone+"&password="+password+"&terms=1";

        Log.e(TAG, "forgotPassword url "+url);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(Constants.androiStudioMode.equals("debug")) {
                    Toast.makeText(ForgotPasswordActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", response);
                }
                Log.e("forgotPassword","forgotPassword response == " +response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {

                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {


                        String msg = jsonObject.getString("msg");
                        AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this,getResources().getString(R.string.app_name),msg,false,4);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    _progressBar.setVisibility(View.GONE);
                    _linearLayout.setVisibility(View.VISIBLE);
                }
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

                if (error instanceof AuthFailureError) {

                    AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this,getResources().getString(R.string.networkerror),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);

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
        BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


    }


}
