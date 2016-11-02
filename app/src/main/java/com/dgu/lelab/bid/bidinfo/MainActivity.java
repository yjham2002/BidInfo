package com.dgu.lelab.bid.bidinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Context context;

    private TextView pbar;
    private ImageView black;

    public static ListViewAdapter bids;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _menuBtn01, _menuBtn02, _menuBtn03, _noticemore;
    private TextView _username, _useraccount, _noticetext;
    private FloatingActionButton _addButton;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.notice_more:
                startActivity(new Intent(this, NoticeActivity.class));
                break;
            case R.id.add:
                Intent i = new Intent(this, PrivateActivity.class);
                startActivity(i);
                break;
            case R.id.menu01:
                startActivity(new Intent(this, MypageActivity.class));
                closeDrawer();
                break;
            case R.id.menu02:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu03:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        context = this;

        pref = getSharedPreferences("BIDINFO", MODE_PRIVATE);
        prefEditor = pref.edit();

        bids = new ListViewAdapter(this, R.layout.listview_bid);

        pbar = (TextView)findViewById(R.id.pbar);
        black = (ImageView)findViewById(R.id.black);
        _menuBtn01 = (Button)findViewById(R.id.menu01);
        _menuBtn01.setOnClickListener(this);
        _menuBtn02 = (Button)findViewById(R.id.menu02);
        _menuBtn02.setOnClickListener(this);
        _menuBtn03 = (Button)findViewById(R.id.menu03);
        _menuBtn03.setOnClickListener(this);
        _addButton = (FloatingActionButton)findViewById(R.id.add);
        _addButton.setOnClickListener(this);
        _noticemore = (Button)findViewById(R.id.notice_more);
        _noticemore.setOnClickListener(this);
        _username = (TextView)findViewById(R.id.sName);
        _useraccount = (TextView)findViewById(R.id.sID);
        _noticetext = (TextView)findViewById(R.id.notice_text);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();

        Communicator.getHttp(URL.MAIN + URL.REST_NOTICE_RECENT, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);
                    String notice = json_list.getString("Content");
                    if(notice.length() > 30) notice = notice.substring(0, 25) + "...";
                    _noticetext.setText(notice);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Intent cmd = getIntent();
        Bundle cmdMsg = cmd.getExtras();
        if(cmdMsg == null) {
            // when the intent is null
            _useraccount.setText("error");
        }
        else {
            // when the intent is not null
            if(cmdMsg.getString("user_account").equals("")){ // when the length of user account is zero
                _useraccount.setText("Testing mode or Error");
            }else {
                _useraccount.setText(cmdMsg.getString("user_account"));
                _username.setText(cmdMsg.getString("user_name"));
            }
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 3) {
                    case 0:
                        return new fm_1();
                    case 1:
                        return new fm_2();
                    case 2:
                        return new fm_3();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "물품정보";
                    case 1:
                        return "공사정보";
                    case 2:
                        return "용역정보";
                }
                return "";
            }
        });


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.oil,
                                "http://fusible.com/wp-content/uploads/2011/09/groupon-goods.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green_teal,
                                "https://upload.wikimedia.org/wikipedia/commons/c/cf/Construction_workers_not_wearing_fall_protection_equipment.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.incimages.com/uploaded_files/image/970x450/handshake-pano_19966.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Intent i = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_in, R.anim.push_out);
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        Log.e("Drawer", "onPostCreate Called");
    }

    @Override
    public void onResume(){
        super.onResume();
        mDrawerToggle.syncState();
        loadList();
        HashMap<String, String> data = new HashMap<>();
        data.put("Token", pref.getString("Token", "#"));
        data.put("mid", Integer.toString(pref.getInt("id", 0)));
        new Communicator().postHttp(URL.MAIN + URL.REST_GCM_NEW, data, new Handler(){});
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    public boolean mFlag;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                mFlag=false;
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            if(!mFlag) {
                Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                finish();
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final String UPDATE_INTENT = "UPDATE_INTENT_FROM_ACTIVITY";

    public void setLoadScrren(boolean flag){
        if(flag){
            pbar.setVisibility(View.VISIBLE);
            black.setVisibility(View.VISIBLE);
        }else{
            pbar.setVisibility(View.INVISIBLE);
            black.setVisibility(View.INVISIBLE);
        }
    }

    public void closeDrawer() {
        mDrawer.closeDrawer(Gravity.LEFT);
    }

    public void loadList(){
        setLoadScrren(true);
        Communicator.getHttp(URL.MAIN + URL.REST_BOARD_ALL + "?id=" + pref.getInt("id", 0), new Handler(){
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
                    Toast.makeText(getApplicationContext(), "정보를 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(UPDATE_INTENT);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    setLoadScrren(false);
                }
            }
        });
    }

}