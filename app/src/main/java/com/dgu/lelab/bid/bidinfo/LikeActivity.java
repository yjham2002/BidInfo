package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import util.Communicator;
import util.URL;

public class LikeActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private RecyclerView mRecyclerView;
    public ListViewAdapter bids;

    private Button _close;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    public void loadList(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("불러오는 중...");
        progressDialog.show();
        Communicator.getHttp(URL.MAIN + URL.REST_USER_LIKES + Integer.toString(pref.getInt("id", 0)), new Handler(){
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
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "정보를 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    bids.dataChange();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        bids = new ListViewAdapter(this, R.layout.listview_bid);
        mRecyclerView.setAdapter(bids);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        _close = (Button)findViewById(R.id.bt_exit);
        _close.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadList();
    }

}
