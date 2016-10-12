package com.dgu.lelab.bid.bidinfo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import util.Communicator;
import util.URL;

public class IntroActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;
    private Handler h;
    private int delayTime = 2500, delayTime2 = 1000;
    private ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        prefs = getSharedPreferences("UnivTable", MODE_PRIVATE);
        prefEditor = prefs.edit();

        iv = (ImageView)findViewById(R.id.imageView);
        iv.setDrawingCacheEnabled(true);
        h = new Handler();
        h.postDelayed(intro, delayTime2);
/*        Communicator.getHttp(URL.MAIN + URL.REST_BOARD_ALL, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                bids.mListData.clear();
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++){
                        JSONObject json_list = json_arr.getJSONObject(i);
                        bids.addItem(new BidInfo(json_list.getInt("likecount"), json_list.getInt("commentcount"), json_list.getInt("id"), json_list.getInt("Type")
                                , json_list.getString("Url"), json_list.getString("Title"), json_list.getString("Refer"), json_list.getString("BidNo"), json_list.getString("Bstart")
                                , json_list.getString("Bexpire"), json_list.getString("PDate"), json_list.getString("Dept"), json_list.getString("Charge"), json_list.getString("Date")));
                    }
                    h.post(intro);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "정보를 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    h.postDelayed(exit, delayTime);
                    e.printStackTrace();
                }
            }
        });*/

        //AnimationSet animset = new AnimationSet(false);
        //Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_anim);
        //animset.addAnimation(anim2);
        //iv.startAnimation(animset);

/*
        animset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                h.postDelayed(intro, delayTime);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
*/
    }

    Runnable exit = new Runnable() {
        public void run() {
            System.exit(0);
        }
    };

    Runnable intro = new Runnable() {
        public void run() {
            Intent i = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(intro);
    }
}
