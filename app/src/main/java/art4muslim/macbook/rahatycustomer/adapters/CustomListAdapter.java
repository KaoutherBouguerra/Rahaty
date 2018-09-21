package art4muslim.macbook.rahatycustomer.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import art4muslim.macbook.rahatycustomer.fragments.orders.OrderDetailsFragment;
import art4muslim.macbook.rahatycustomer.models.OrderModel;
import art4muslim.macbook.rahatycustomer.session.Constants;
import art4muslim.macbook.rahatycustomer.session.SessionManager;
import art4muslim.macbook.rahatycustomer.utils.circularimageview.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomListAdapter extends BaseAdapter {
	private Activity _mContext;
	private LayoutInflater inflater;
	private List<OrderModel> myOrderModelItems;
	private String activityName;
	FragmentTransaction fragmentTransaction;
	SessionManager session;
	String languageToLoad;
	boolean isRightToLeft  ;
	private static final String TAG = CustomListAdapter.class.getSimpleName();
	public CustomListAdapter(Activity activity, List<OrderModel> myOrderModelItems,
                             String activityName, FragmentTransaction fragmentTransaction) {
		this._mContext = activity;
		this.myOrderModelItems = myOrderModelItems;
		isRightToLeft = _mContext.getResources().getBoolean(R.bool.is_right_to_left);
		this.activityName=activityName;
		this.fragmentTransaction =fragmentTransaction;
		session = new SessionManager(_mContext);
		languageToLoad = BaseApplication.session.getKey_LANGUAGE();
	}

	@Override
	public int getCount() {
		return myOrderModelItems.size();
	}

	@Override
	public Object getItem(int location) {
		return myOrderModelItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) _mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		TextView _txt_order_number = (TextView) convertView.findViewById(R.id.txt_order_number);
		TextView date = (TextView) convertView.findViewById(R.id.txt_date);
		TextView time = (TextView) convertView.findViewById(R.id.txt_time);
		final LinearLayout _linear_cancel = (LinearLayout) convertView.findViewById(R.id.linear_cancel);
		final RelativeLayout _rel = (RelativeLayout) convertView.findViewById(R.id.rel);
		final OrderModel m = myOrderModelItems.get(position);
		if (!myOrderModelItems.isEmpty()){
			// getting movie data for the row


			// thumbnail image
			// thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

			// title

			_txt_order_number.setText(_mContext.getResources().getString(R.string.requestNumber)+" "+m.getId());

			if (m.getStatus().equals("applied")) {

				if (languageToLoad.equals(Constants.arabic)){
					_linear_cancel.setBackgroundResource(R.drawable.round_right_rect_shape_darkblue);
					_rel.setBackgroundResource(R.drawable.round_left_rect_shape_blue);
				}else{
					_linear_cancel.setBackgroundResource(R.drawable.round_left_rect_shape_darkblue);
					_rel.setBackgroundResource(R.drawable.round_right_rect_shape_blue);
				}


			}
			else if (m.getStatus().equals("delivered")) {
				if (languageToLoad.equals(Constants.arabic)){
					_linear_cancel.setBackgroundResource(R.drawable.round_right_rect_shape_darkgreen);
					_rel.setBackgroundResource(R.drawable.round_left_rect_shape_green);
				}else{
					_linear_cancel.setBackgroundResource(R.drawable.round_left_rect_shape_darkgreen);
					_rel.setBackgroundResource(R.drawable.round_right_rect_shape_green);
				}

			}else if (m.getStatus().equals("pending")){
				if (languageToLoad.equals(Constants.arabic)){
					_linear_cancel.setBackgroundResource(R.drawable.round_right_rect_shape_red);
					_rel.setBackgroundResource(R.drawable.round_left_rect_shape_yellow);
				}else{
					_linear_cancel.setBackgroundResource(R.drawable.round_left_rect_shape_red);
					_rel.setBackgroundResource(R.drawable.round_right_rect_shape_yellow);
				}
			}
			_linear_cancel.post(new Runnable() {
				@Override
				public void run() {

				}
			});

			_rel.post(new Runnable(){
				public void run(){
					int height = _rel.getHeight();
					_linear_cancel.setMinimumHeight(height);


				}
			});
			// rating
			date.setText(m.getDate());
			time.setText(m.getTime());


			}

		_rel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				OrderDetailsFragment schedule = new OrderDetailsFragment();
				String id =  m.getId();
				String idCat =  m.getIdCat();
				Bundle args =new Bundle();
				args.putString("ORDER_ID", id);
				args.putString("ID", idCat);
				args.putString("STATUS", m.getStatus());
				schedule.setArguments(args);
				//FragmentTransaction fragmentTransaction =  _mContext.getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.frame,schedule,"myOrders Fragment");
				fragmentTransaction.commit();


			}
		});

		_linear_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id =  m.getId();
				String url;
				if (m.getStatus().equals("delivered")) {
					url = Constants.DELETE_ORDER_URL;


				}else{
					url = Constants.CANCEL_ORDER_URL;

				}

				applyForOrder(url,id,position);


			}
		});


		return convertView;
	}
	public void clear() {
		// TODO Auto-generated method stub
		myOrderModelItems.clear();

	}
	private void applyForOrder(String url, final String id, final int position){
		final ProgressDialog dialog = ProgressDialog.show(_mContext, "",
				_mContext.getResources().getString(R.string.loading), true);
		//+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();

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
						myOrderModelItems.remove(position);
						notifyDataSetChanged();
						AlertDialogManager.showAlertDialog(_mContext,_mContext.getResources().getString(R.string.app_name),_mContext.getResources().getString(R.string.success_delete_order),true,0);
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