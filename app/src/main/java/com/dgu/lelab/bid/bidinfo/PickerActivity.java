package com.dgu.lelab.bid.bidinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PickerActivity extends FragmentActivity implements View.OnClickListener {

    public static GridAdapter adapter1;

    private ExpandableHeightGridView gv1;
    public static List<String> mList1;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    public static ArrayList<KeywordItem> selected_goods, selected_const, selected_service, selected_etc;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            default: break;
        }
    }

    public void init(){
        selected_const = new ArrayList<>();
        selected_goods = new ArrayList<>();
        selected_service = new ArrayList<>();
        selected_etc = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mList1 = new ArrayList<>();

        adapter1 = new GridAdapter(this, R.layout.grid_item, mList1);
        gv1 = (ExpandableHeightGridView)findViewById(R.id.gridView1);
        gv1.setExpanded(true);
        gv1.setAdapter(adapter1);

        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mList1.remove(position);
                adapter1.notifyDataSetChanged();
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment temp;
            Bundle args = new Bundle();
            args.putInt("key", position);
            switch(position){
                case 0: temp = new PickerFragment(); break;
                case 1: temp = new PickerFragment(); break;
                case 2: temp = new PickerFragment(); break;
                case 3: temp = new PickerFragment(); break;
                default:temp = new PickerFragment(); break;
            }
            temp.setArguments(args);
            return temp;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:
                    title = "물품";
                    break;
                case 1:
                    title = "공사";
                    break;
                case 2:
                    title = "용역";
                    break;
                case 3:
                    title = "기타";
                    break;
                default: break;
            }
            return title;
        }
    }

}
