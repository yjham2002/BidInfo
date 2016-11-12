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
import java.util.HashMap;
import java.util.List;
import util.Communicator;
import util.URL;

public class CompanySearchFragment extends Fragment implements View.OnClickListener{

    private CompanyAdapter companyAdapter;
    private RecyclerView mRecyclerView;

    private EditText _query;
    private Button _search;

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

    public void setView(View v){
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        companyAdapter = new CompanyAdapter(getContext(), R.layout.listview_company);
        mRecyclerView.setAdapter(companyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        _query = (EditText)v.findViewById(R.id.view);
        _search = (Button)v.findViewById(R.id.bt_search);
        _search.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search_company, container, false);

        setView(rootView);

        return rootView;
    }

    public void search(String msg){
        if(msg.length() < 2) {
            Toast.makeText(getActivity(), "검색어를 두 글자 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("검색하는 중...");
        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("search", msg);
        new Communicator().postHttp(URL.MAIN + URL.REST_COMPANY_SEARCH, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    companyAdapter.mListData.clear();
                    companyAdapter.arraylist.clear();
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++) {
                        JSONObject json_list = json_arr.getJSONObject(i);
                        //Log.e("json", json_list.toString());
                        CompanyData cData = new CompanyData(json_list.getInt("id"), json_list.getInt("Pnum"), json_list.getString("Name"), json_list.getString("Rnum"),
                                json_list.getString("Rprt"), json_list.getString("Charge"), json_list.getString("Addr"), json_list.getString("Phone"), json_list.getString("Email"), json_list.getString("Expl"), json_list.getString("Date"), json_list.getString("hid"));
                        companyAdapter.addItem(cData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    progressDialog.dismiss();
                    companyAdapter.dataChange();
                }

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
