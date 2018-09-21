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

public class ActivateCodeActivity extends AppCompatActivity {
    @NotEmpty(messageId = R.string.nonEmpty, order = 2)

    EditText _etCode;
    Button _btn_signup;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    boolean isRightToLeft  ;
    private static final String TAG = ActivateCodeActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_code);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        initFields();
        String code = getIntent().getStringExtra("CODE");
        _etCode.setText(code);
        _btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptVerify();
            }
        });
    }


    private void initFields(){
        _btn_signup = (Button) findViewById(R.id.btn_signup);
        _etCode = (EditText)findViewById(R.id.etCode);
        _progressBar=(ProgressBar) findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) findViewById(R.id.linearLayout4);
    }

    private void attemptVerify() {


        // Reset errors.
        _etCode.setError(null);



        // Store values at the time of the login attempt.
        String code = _etCode.getText().toString();



        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(code)) {
            _etCode.setError(getString(R.string.error_field_required));
            focusView = _etCode;
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
            verify(code);
        }
    }


    private void verify(final String code) {

        String url = Constants.VERIFY_URL;
        //+"code="+code;

        Log.e(TAG, "verify url "+url);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(Constants.androiStudioMode.equals("debug")) {
                    Toast.makeText(ActivateCodeActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", response);
                }
                Log.e("verify","verify response == " +response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status==1) {
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        int id = dataObj.getInt("id");
                        String name = dataObj.getString("name");
                        String phone = dataObj.getString("phone");
                        String home_phone = dataObj.getString("home_phone");
                        String thumbnail = dataObj.getString("thumbnail");
                        String state = dataObj.getString("state");
                        BaseApplication.session.createLoginSession(id,name,phone,home_phone,thumbnail,state);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(ActivateCodeActivity.this, MainActivity.class);
                startActivity(intent);
                //  _linearLayout.setVisibility(View.VISIBLE);
                _progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

                if (error instanceof AuthFailureError) {

                    AlertDialogManager.showAlertDialog(ActivateCodeActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(ActivateCodeActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(ActivateCodeActivity.this,getResources().getString(R.string.networkerror),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(ActivateCodeActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }
            }
        }) {
            /*     @Override
                public String getBodyContentType() {
                    return "application/json";
                }

               @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                */
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("code", code);
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
        BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


    }
}
