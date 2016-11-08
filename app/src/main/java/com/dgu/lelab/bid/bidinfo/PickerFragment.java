package com.dgu.lelab.bid.bidinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PickerFragment extends Fragment {

    private TextView _title;

    private PickGridAdapter adapter1;

    private ExpandableHeightGridView gv1;
    private List<KeywordItem> mList1;

    public void init(View v){
        mList1 = new ArrayList<>();

        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_picker, container, false);

        init(rootView);

        return rootView;
    }
}
