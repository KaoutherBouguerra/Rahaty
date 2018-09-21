package art4muslim.macbook.rahatycustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import art4muslim.macbook.rahatycustomer.R;
import art4muslim.macbook.rahatycustomer.models.Notification;
import art4muslim.macbook.rahatycustomer.session.SessionManager;

import java.util.List;

public class CustomNotifListAdapter extends BaseAdapter {
	private Activity _mContext;
	private LayoutInflater inflater;
	private List<Notification> myOrderModelItems;
	private String activityName;
	FragmentTransaction fragmentTransaction;
	SessionManager session;

	public CustomNotifListAdapter(Activity activity, List<Notification> myOrderModelItems,
                                  String activityName, FragmentTransaction fragmentTransaction) {
		this._mContext = activity;
		this.myOrderModelItems = myOrderModelItems;
		this.activityName=activityName;
		this.fragmentTransaction =fragmentTransaction;
		session = new SessionManager(_mContext);
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
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) _mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row_notif, null);

		TextView _txt_order_number = (TextView) convertView.findViewById(R.id.txt_order_number);
		TextView date = (TextView) convertView.findViewById(R.id.txt_date);
		TextView time = (TextView) convertView.findViewById(R.id.txt_time);
		LinearLayout _linear_cancel = (LinearLayout) convertView.findViewById(R.id.linear_cancel);
		final Notification m = myOrderModelItems.get(position);
		if (!myOrderModelItems.isEmpty()){
			// getting movie data for the row


			// thumbnail image
			// thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

			// title

			_txt_order_number.setText(_mContext.getResources().getString(R.string.requestNumber)+" "+m.getIdOder());

			// rating
			date.setText(m.getDate());
			time.setText(m.getTime());




			}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//		Intent intent=new Intent(_mContext, OrderDetailsAcitivty.class);
				//	intent.putExtra("RequestNumber",m.getOrderId());
				//		_mContext.startActivity(intent);
				/*if(m.getStatus_id().equals("1") ){
					OrderDetailsFragment schedule = new OrderDetailsFragment();
					Bundle args =new Bundle();
					args.putInt("RequestNumber", Integer.parseInt(m.getRequest_id()));
					//args.putString("LAT", m.getWorker_mapx() );
					//args.putString("LNG", m.getWorker_mapy() );
					args.putString("MY_LAT", ""+session.getUserMapx());
					args.putString("MY_LNG", ""+session.getUserMapy());
					args.putString("PHOTO", ""+m.getRequest_photo1());
 					args.putString("TYPE","NEW");
					schedule.setArguments(args);
					//FragmentTransaction fragmentTransaction =  _mContext.getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.frame,schedule,"myOrders Fragment");
					fragmentTransaction.commit();
				}
				*/




			}
		});






		return convertView;
	}
	public void clear() {
		// TODO Auto-generated method stub
		myOrderModelItems.clear();

	}

}