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

    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        mList = new ArrayList<>();

        mList.add("아름다운");
        mList.add("깨끗한");
        mList.add("차가운");
        mList.add("뜨거운");
        mList.add("짜증나는");
        mList.add("쓰러질_것_같은");
        mList.add("배고프고_힘든");
        mList.add("너무_졸린");
        mList.add("뜨뜨미지근한");
        mList.add("달콤한");
        mList.add("머리가_아픈");
        mList.add("한숨나오는");
        mList.add("아...힘들다");
        mList.add("졸려죽겠다");
        mList.add("감기걸린");
        mList.add("아파죽겠는");


        GridAdapter adapter = new GridAdapter(getApplicationContext(), R.layout.grid_item, mList);
        GridView gv = (GridView)findViewById(R.id.gridView);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }

}
