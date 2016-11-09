package com.dgu.lelab.bid.bidinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import util.KEYWORDS;

public class PickerFragment extends Fragment {

    View rootView;

    private TextView _title;

    private String[][] dataSet;
    private String titleData;

    private PickGridAdapter adapter1;

    private ExpandableHeightGridView gv1;
    private List<KeywordItem> mList1;

    public void init(View v){

        mList1 = new ArrayList<>();

        for(String[] dimen : dataSet){
            for(String token : dimen){
                mList1.add(new KeywordItem(token));
            }
        }

        _title = (TextView)v.findViewById(R.id.pick_title);
        _title.setText(titleData);
        adapter1 = new PickGridAdapter(getActivity(), R.layout.grid_item, mList1);
        gv1 = (ExpandableHeightGridView) v.findViewById(R.id.gridView1);
        gv1.setExpanded(true);
        gv1.setAdapter(adapter1);

        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mList1.get(position).isSelected = mList1.get(position).isSelected ? false : true;
                adapter1.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        final ScrollView scrollview = ((ScrollView) rootView.findViewById(R.id.scrollview));
        scrollview.post(new Runnable() { @Override public void run() { scrollview.fullScroll(ScrollView.FOCUS_UP); } });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = getArguments().getInt("key");

        switch (position){
            case 0:
                dataSet = KEYWORDS.KEY_GOODS;
                titleData = "물품";
                break;
            case 1:
                dataSet = KEYWORDS.KEY_CONSTRUCTION;
                titleData = "공사";
                break;
            case 2:
                dataSet = KEYWORDS.KEY_SERVICE;
                titleData = "용역";
                break;
            default: dataSet = null; break;
        }

        rootView = inflater.inflate(R.layout.fragment_picker, container, false);

        init(rootView);

        return rootView;
    }
}
