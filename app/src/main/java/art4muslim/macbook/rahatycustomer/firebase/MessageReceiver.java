package art4muslim.macbook.rahatycustomer.firebase;

/**
 * Created by macbook on 13/02/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import art4muslim.macbook.rahatycustomer.MainActivity;
import art4muslim.macbook.rahatycustomer.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MessageReceiver extends FirebaseMessagingService {
    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("Notification","DATA = "+remoteMessage.getData());
        Log.e("Notification","DATA Notification = "+remoteMessage.getNotification());
        final String title = remoteMessage.getData().get("title");
        final String message = remoteMessage.getData().get("body");
        Map<String, String> data = remoteMessage.getData();
        showNotifications(title, message,data);
    }

    private void showNotifications(String title, String msg, Map<String, String> data ) {
        Intent intent = new Intent(this, MainActivity.class);

        int  customerId = 0;
        Object objCustomerId  = data.get("CustomerId");
        if (objCustomerId  != null) {
            customerId  = Integer.valueOf(objCustomerId.toString());
        }

        String OrderId = null;
        String status = null;
        String category_id = null;



        String order =""  ;
        Object objorder  = data.get("order");
        if (objorder  != null) {
            order  =  objorder.toString() ;
        }
        try {
            JSONObject jsonObject = new JSONObject(order);
            OrderId = jsonObject.getString("id");
            status = jsonObject.getString("state");
            category_id = jsonObject.getString("category_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }





        int DriverId = 0;
        Object objDriverId  = data.get("DriverId");
        if (objDriverId  != null) {
            DriverId  = Integer.valueOf(objDriverId.toString());
        }


        Log.i("onMessageReceived",OrderId+"  THis is OrderId");
        Log.i("onMessageReceived",customerId+"  THIs is CustomerId");

        Log.i("onMessageReceived",status +"  THIs is status ");



        intent.putExtra("isFromNorif", true );
        intent.putExtra("orderId", OrderId );
        intent.putExtra("status", status );
        intent.putExtra("ID_CAT", category_id );

        intent.putExtra("driverId", ""+DriverId );


        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentText(msg)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLights(Color.YELLOW, 1000, 300)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }
}
