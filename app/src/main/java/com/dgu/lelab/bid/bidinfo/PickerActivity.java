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

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    public static ArrayList<KeywordItem> selected_goods, selected_const, selected_service;

    private ExpandableHeightGridView gv1, gv2;
    private List<KeywordItem> mList1, mList2;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment temp;
            switch(position){
                case 0: temp = new PickerFragment(); break;
                case 1: temp = new PickerFragment(); break;
                case 2: temp = new PickerFragment(); break;
                default: temp = null; break;
            }
            return temp;
        }

        @Override
        public int getCount() {
            return 3;
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
                default: break;
            }
            return title;
        }
    }

}
