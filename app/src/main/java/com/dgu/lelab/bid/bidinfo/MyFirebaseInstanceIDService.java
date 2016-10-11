package com.dgu.lelab.bid.bidinfo;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

import util.Communicator;
import util.URL;

/**
 * Created by HP on 2016-10-06.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    public static String Token = "";

    @Override
    public void onTokenRefresh() {
        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FCM Token", refreshedToken);
        prefEditor.putString("Token" ,refreshedToken);
        prefEditor.commit();
    }
}