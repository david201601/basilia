package com.example.davidfoo.basilia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.util.List;

/**
 * @author david.foo
 */
public class ServiceEngine extends Service {

    private NotificationManager notificationManager;
    private static RemoteViews notificationView;
    private static NotificationCompat.Builder mBuilder;
    private static Notification notification;
    private WifiManager wifi;
    private boolean AnimStart = false;
    private boolean AnimToggle = false;

    String wifis[];

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        Log.d("SERVICE ", "SERVICE START");
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.noti_view);

        mBuilder = new NotificationCompat.Builder(this);
        /*Must HAVE for it to appear*/
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //mBuilder.setContentTitle("Notification Alert, Click Me!");
        //mBuilder.setContentText("Hi, This is Android Notification Detail!");
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);

        mBuilder.setContent(notificationView);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET);

            notificationView.setInt(R.id.ll_noti_view, "setBackgroundColor", Color.BLACK);
            notificationView.setTextColor(R.id.tv_noti_title, Color.WHITE);
            notificationView.setTextColor(R.id.tv_noti_desc, Color.WHITE);



        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        //this.adapter = new SimpleAdapter(WiFiDemo.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        //lv.setAdapter(this.adapter);
        //if(AnimStart = false) {
        //    startAnimate();
        //}

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
          //      results = wifi.getScanResults();
          //      size = results.size();
                int rssi = wifi.getConnectionInfo().getRssi();
                int level = wifi.calculateSignalLevel(rssi, 5);
                notificationView.setImageViewResource(R.id.iv_noti_icon,R.mipmap.ic_launcher);
                notificationView.setTextViewText(R.id.tv_noti_title, wifi.getScanResults().get(0).SSID.toString());
                notificationView.setTextViewText(R.id.tv_noti_desc, String.valueOf(wifi.getScanResults().get(0).level));

                updateNotification();

                // Create layout inflator object to inflate toast.xml file
                LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                // Call toast.xml file for toast layout
                View toastRoot = inflater.inflate(R.layout.noti_view, null);
                TextView tv_Title_Toast = (TextView)toastRoot.findViewById(R.id.tv_noti_title);
                tv_Title_Toast.setText(wifi.getScanResults().get(0).SSID.toString());
                TextView tv_Desc_Toast = (TextView) toastRoot.findViewById(R.id.tv_noti_desc);
                tv_Desc_Toast.setText(String.valueOf(wifi.getScanResults().get(0).level));

                Toast toast = new Toast(c);

                // Set layout to toast
                toast.setView(toastRoot);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                        0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                //android.R.drawable.btn_star_big_off;
               // Toast.makeText(c, String.valueOf(wifi.getScanResults()),Toast.LENGTH_SHORT).show();
                if(AnimStart == false) {
                    startAnimate();
                    AnimStart = true;
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        return Service.START_STICKY;
    }
    //Notification needs to be update manually
    private void updateNotification(){

        int api = Build.VERSION.SDK_INT;
/*        // update the icon
        mRemoteViews.setImageViewResource(R.id.notif_icon, R.drawable.icon_off2);
        // update the title
        mRemoteViews.setTextViewText(R.id.notif_title, getResources().getString(R.string.new_title));
        // update the content
        mRemoteViews.setTextViewText(R.id.notif_content, getResources().getString(R.string.new_content_text));
*/
        // update the notification
        if (api < Build.VERSION_CODES.HONEYCOMB) {
            notificationManager.notify(37713, notification);
        }else if (api >= Build.VERSION_CODES.HONEYCOMB) {
            notificationManager.notify(37713, mBuilder.build());
        }

    }

    //Notification needs to be update manually
    private void startAnimate(){
        //final Intent intent = new Intent();
        //intent.putExtra("frame",false);
        //AnimStart = true;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
            //    Log.d("frame123","frame123 "+AnimToggle);
                if(AnimToggle ==  false){
                notificationView.setImageViewResource(R.id.iv_noti_icon,android.R.drawable.btn_star_big_off);
                    AnimToggle = true;
            }else{
                    notificationView.setImageViewResource(R.id.iv_noti_icon,android.R.drawable.btn_star_big_on);
                    AnimToggle= false;
                }
                int api = Build.VERSION.SDK_INT;
                if (api < Build.VERSION_CODES.HONEYCOMB) {
                    notificationManager.notify(37713, notification);
                }else if (api >= Build.VERSION_CODES.HONEYCOMB) {
                    notificationManager.notify(37713, mBuilder.build());
                }
                handler.postDelayed(this,500);
            }
        };
        handler.postDelayed(runnable,0);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
