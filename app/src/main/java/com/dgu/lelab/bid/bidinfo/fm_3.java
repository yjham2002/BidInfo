package com.dgu.lelab.bid.bidinfo;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        for(int i = 0; i < IntroActivity.bids.mListData.size(); i++){
            switch (IntroActivity.bids.mListData.get(i).Type){
                case 2:case 6:case 10:case 3:case 7:case 11: testAdapter.addItem(IntroActivity.bids.mListData.get(i)); break;
                default: break;
            }
        }
        mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(testAdapter));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        return rootView;
    }

    public void onClick(View v) {
    }

}