package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PickerActivity extends Activity {

    private ExpandableHeightGridView gv1, gv2;
    private List<KeywordItem> mList1, mList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();

        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계"));
        mList2.add(new KeywordItem("교량설계"));
        mList1.add(new KeywordItem("교량설계", true));
        mList2.add(new KeywordItem("교량설계"));


        final PickGridAdapter adapter1 = new PickGridAdapter(getApplicationContext(), R.layout.grid_item, mList1);
        PickGridAdapter adapter2 = new PickGridAdapter(getApplicationContext(), R.layout.grid_item, mList2);
        gv1 = (ExpandableHeightGridView) findViewById(R.id.gridView1);
        gv2 = (ExpandableHeightGridView)findViewById(R.id.gridView2);

        gv1.setExpanded(true);
        gv2.setExpanded(true);

        gv1.setAdapter(adapter1);
        gv2.setAdapter(adapter2);
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mList1.get(position).isSelected = mList1.get(position).isSelected ? false : true;
                adapter1.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
        gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }

}
