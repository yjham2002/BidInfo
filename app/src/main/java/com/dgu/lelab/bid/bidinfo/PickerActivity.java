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
    private List<String> mList1, mList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();


        mList1.add("교량설계");
        mList1.add("도로포장");
        mList1.add("가드레일");
        mList1.add("도로설계");
        mList1.add("보행로설계");
        mList1.add("중분대");
        mList1.add("구조검토");
        mList1.add("보도블럭");
        mList1.add("경계석");
        mList1.add("경관성평가");
        mList1.add("방음벽");
        mList1.add("차선");
        mList1.add("신도시계획");
        mList1.add("낙석방지책");

        mList2.add("교량설계");
        mList2.add("도로포장");
        mList2.add("가드레일");
        mList2.add("도로설계");
        mList2.add("보행로설계");
        mList2.add("중분대");
        mList2.add("구조검토");
        mList2.add("보도블럭");
        mList2.add("경계석");
        mList2.add("경관성평가");
        mList2.add("방음벽");
        mList2.add("차선");
        mList2.add("신도시계획");
        mList2.add("낙석방지책");


        GridAdapter adapter1 = new GridAdapter(getApplicationContext(), R.layout.grid_item, mList1);
        GridAdapter adapter2 = new GridAdapter(getApplicationContext(), R.layout.grid_item, mList2);
        gv1 = (ExpandableHeightGridView) findViewById(R.id.gridView1);
        gv2 = (ExpandableHeightGridView)findViewById(R.id.gridView2);

        gv1.setExpanded(true);
        gv2.setExpanded(true);

        gv1.setAdapter(adapter1);
        gv2.setAdapter(adapter2);
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
