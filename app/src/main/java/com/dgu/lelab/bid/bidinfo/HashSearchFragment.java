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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import util.Communicator;
import util.URL;

public class HashSearchFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    public ListViewAdapter bids;

    private EditText _query;
    private Button _search;
    private TextView _fixed;
    private ExpandableHeightGridView gv1;
    private List<String> mList1;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_search:
                String tempQuery = _query.getText().toString().trim().replaceAll(" ", "\\|");
                search(tempQuery);
                break;
            default: break;
        }
    }

    public void search(String msg){
        if(msg.length() < 1) {
            Toast.makeText(getActivity(), "검색어를 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        _fixed.setVisibility(View.GONE);
        gv1.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("검색 하는 중...");
        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("search", msg);
        new Communicator().postHttp(URL.MAIN + URL.REST_SEARCH, data, new Handler(){
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
                    Toast.makeText(getActivity(), "정보를 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    bids.dataChange();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void recommend(){
        for(int i = 0; i < 5; i++){
            String str = MainActivity.bids.mListData.get(new Random().nextInt(MainActivity.bids.mListData.size())).Title.trim();
            List<String> list = new ArrayList<>(Arrays.asList(str.split(" ")));
            String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
            String finalString = list.get(new Random().nextInt(list.size())).replaceAll(match, "");
            mList1.add(finalString);
        }
        for(int k = 0; k < mList1.size(); k++){
            if(mList1.get(k).length() <= 1) mList1.remove(k);
        }
    }

    public void onEmptySet(){
        _fixed.setVisibility(View.GONE);
        gv1.setVisibility(View.GONE);
    }

    public void setView(View v){
        mList1 = new ArrayList<>();

        if(MainActivity.bids.mListData.size() > 2) recommend();

        GridAdapter adapter1 = new GridAdapter(getActivity(), R.layout.grid_item2, mList1);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        bids = new ListViewAdapter(getActivity(), R.layout.listview_bid);
        mRecyclerView.setAdapter(bids);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        _query = (EditText)v.findViewById(R.id.view);
        _search = (Button)v.findViewById(R.id.bt_search);
        _search.setOnClickListener(this);
        _fixed = (TextView)v.findViewById(R.id.fixed);
        gv1 = (ExpandableHeightGridView)v.findViewById(R.id.gridView1);
        gv1.setExpanded(true);

        gv1.setAdapter(adapter1);
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _query.setText(mList1.get(position));
                search(mList1.get(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search_hash, container, false);

        setView(rootView);

        return rootView;
    }

}