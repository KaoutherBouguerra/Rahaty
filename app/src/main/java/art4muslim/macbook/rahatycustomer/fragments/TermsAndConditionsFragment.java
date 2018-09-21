package art4muslim.macbook.rahatycustomer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import art4muslim.macbook.rahatycustomer.application.BaseApplication;
import art4muslim.macbook.rahatycustomer.models.SettingsModel;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConditionsFragment extends Fragment {

    private static final String TAG = TermsAndConditionsFragment.class.getSimpleName();
    View v;
    ProgressBar _progressBar;
    TextView _txt_about;
    BaseApplication app;
    boolean isRightToLeft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        app = (BaseApplication) getActivity().getApplicationContext();
        getActivity().setTitle(getString(R.string.txt_terms));
        initFields();
        if (app.getSettingsModel()==null)
            getSettings();
        else   {
            if (!isRightToLeft ) {
                _txt_about.setText(Html.fromHtml(app.getSettingsModel().getEn_terms()));
            }else   _txt_about.setText(Html.fromHtml(app.getSettingsModel().getAr_terms()));
        }



        return v;
    }
    private void initFields(){
        _txt_about=(TextView)v.findViewById(R.id.txt_about);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
    }
    private void getSettings(){

        String url = Constants.GET_SETTINGS_URL;
        Log.e(TAG, "getSettings url "+url);
        _progressBar.setVisibility(View.VISIBLE);
        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getSettings response === "+response.toString());
                _progressBar.setVisibility(View.GONE);
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
                        _txt_about.setText(Html.fromHtml(ar_terms));





                    }

                } catch (JSONException e) {
                    _progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
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
