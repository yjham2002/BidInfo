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
import android.text.SpannableString;
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

    private ImageView _menuBtn02; // Menu Buttons

    private Button _menuBtn01, _menuBtn03, _noticemore, _attend, _likes, _comment, _edit, _company;
    private TextView _username, _useraccount, _noticetext;
    private FloatingActionButton _addButton;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.menu_comment:
                startActivity(new Intent(this, CommentActivity.class));
                break;
            case R.id.menu_edit:
                Intent intent = new Intent(this, PickerActivity.class);
                PickerFragment.mode = true;
                //intent.putExtra("mode", true);
                startActivity(intent);
                break;
            case R.id.menu_company:
                RegisterActivity.mode = true;
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.notice_more:
                startActivity(new Intent(this, NoticeActivity.class));
                break;
            case R.id.add:
                Intent i = new Intent(this, PrivateActivity.class);
                startActivity(i);
                break;
            //case R.id.menu01:
                //startActivity(new Intent(this, MypageActivity.class));
                //closeDrawer();
                //break;
            case R.id.menu02:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu03:
                startActivity(new Intent(this, LoginActivity.class));
                prefEditor.putBoolean("auto", false);
                prefEditor.commit();
                finish();
                break;
            case R.id.menu_like:
                startActivity(new Intent(this, LikeActivity.class));
                break;
            case R.id.menu_attend:
                startActivity(new Intent(this, AttendActivity.class));
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
        //_menuBtn01 = (Button)findViewById(R.id.menu01);
        //_menuBtn01.setOnClickListener(this);
        _comment = (Button)findViewById(R.id.menu_comment);
        _comment.setOnClickListener(this);
        _edit = (Button)findViewById(R.id.menu_edit);
        _edit.setOnClickListener(this);
        _company = (Button)findViewById(R.id.menu_company);
        _company.setOnClickListener(this);
        _attend = (Button)findViewById(R.id.menu_attend);
        _attend.setOnClickListener(this);
        _likes = (Button)findViewById(R.id.menu_like);
        _likes.setOnClickListener(this);
        _menuBtn02 = (ImageView)findViewById(R.id.menu02);
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
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.coral),
                                getResources().getDrawable(R.drawable.bg_gr1));
                    case 1:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.coral),
                                getResources().getDrawable(R.drawable.bg_gr2));
                    case 2:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.coral),
                                getResources().getDrawable(R.drawable.bg_gr3));
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
                        if(json_list.getInt("Type") == -1) continue;
                        BidInfo bidinfo = new BidInfo(json_list.getInt("likecount"), json_list.getInt("commentcount"), json_list.getInt("id"), json_list.getInt("Type")
                                , json_list.getString("Url"), json_list.getString("Title"), json_list.getString("Refer"), json_list.getString("BidNo"), json_list.getString("Bstart")
                                , json_list.getString("Bexpire"), json_list.getString("PDate"), json_list.getString("Dept"), json_list.getString("Charge"), json_list.getString("Date"));
                        if(bidinfo.Title.contains("용역") && (bidinfo.Type == 3 || bidinfo.Type == 7 || bidinfo.Type == 11)) bidinfo.Type -= 1;
                        if(bidinfo.Title.contains("공사") && (bidinfo.Type == 3 || bidinfo.Type == 7 || bidinfo.Type == 11)) bidinfo.Type -= 2;
                        if(bidinfo.Title.contains("물품") && (bidinfo.Type == 3 || bidinfo.Type == 7 || bidinfo.Type == 11)) bidinfo.Type -= 3;
                        bids.addItem(bidinfo);
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "정보를 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    if(bids.mListData.size() < 5 && bids.mListData.size() != 0) Toast.makeText(getApplicationContext(), "키워드를 통해 추출한 정보가 너무 적습니다.\n마이페이지에서 키워드를 수정하시기 바랍니다.", Toast.LENGTH_LONG).show();
                    else if(bids.mListData.size() == 0) Toast.makeText(getApplicationContext(), "키워드에 일치하는 정보가 존재하지 않거나,\n정보를 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UPDATE_INTENT);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    setLoadScrren(false);
                }
            }
        });
    }

}