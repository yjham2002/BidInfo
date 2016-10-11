package com.dgu.lelab.bid.bidinfo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private SharedPreferences pref, prefSet;
    private SharedPreferences.Editor prefEditor;
    private static final int NOTICECODE = 2014112021;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map<String, String> data = message.getData();
        String title = data.get("title");
        String msg = data.get("message");

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        prefSet = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         *  Manager have to send title(#keyword) and msg(keyword set with delimeter \\|) when the manager want to send keyword notification.
         * */
        Log.e("FCM Message", from + "/" + title + " : " + msg);
        if(prefSet.getBoolean("notification_push", true) && title != null) {
            if(title.equals("#keyword")){
                if(prefSet.getBoolean("keyword_push", true) && isKeyword(msg)) {
                    notifyReg("키워드가 포함된 공고 게시됨", "#" + msg.trim().replaceAll("\\|", " #"));
                }
            }else{
                notifyReg(title, msg);
            }
        }

    }

    public boolean isKeyword(String msg){
        String rawHash = pref.getString("hid", "#");
        if(rawHash.equals("#")) return false;
        else {
            List<String> list = new ArrayList<String>(Arrays.asList(msg.trim().split("\\|")));
            for(String token : list) if(rawHash.contains(token)) return true;
            return false;
        }
    }

    public void notifyReg(String title, String msg){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setTicker(getResources().getString(R.string.app_name));
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTICECODE, builder.build());
    }

}
