package com.dgu.lelab.bid.bidinfo;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Communicator;
import util.URL;

public class fm_3 extends Fragment implements View.OnClickListener{

    public RecyclerView mRecyclerView;
    public ListViewAdapter testAdapter;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewMaterialAdapter rAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_1, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
/**
 - 입찰정보(물품0/공사1/용역2/미분류3)
 - 계약정보(물품4/공사5/용역6/미분류7)
 - 민간정보(물품8/공사9/용역10/미분류11)
 */
        testAdapter = new ListViewAdapter(rootView.getContext(), R.layout.listview_bid);
        rAdapter = new RecyclerViewMaterialAdapter(testAdapter);
        mRecyclerView.setAdapter(rAdapter);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        return rootView;
    }

    private static final String UPDATE_INTENT = "UPDATE_INTENT_FROM_ACTIVITY";

    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(UPDATE_INTENT));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){
                case UPDATE_INTENT: loadList(); break;
                default: break;
            }
        }
    };

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    public void loadList(){
        testAdapter.mListData.clear();
        for(int i = 0; i < MainActivity.bids.mListData.size(); i++){
            switch (MainActivity.bids.mListData.get(i).Type){
                //case 0:case 4:case 8: testAdapter.addItem(MainActivity.bids.mListData.get(i)); break;
                //case 1:case 5:case 9: testAdapter.addItem(MainActivity.bids.mListData.get(i)); break;
                case 2:case 6:case 10: testAdapter.addItem(MainActivity.bids.mListData.get(i)); break;
                case 3:case 7:case 11: {
                    testAdapter.addItem(MainActivity.bids.mListData.get(i));
                    break;
                }
                default: break;
            }
        }
        testAdapter.notifyDataSetChanged();
        rAdapter.notifyDataSetChanged();
        //mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(testAdapter));
        //layoutManager.scrollToPosition(0);
    }

    public void onClick(View v) {
    }

}