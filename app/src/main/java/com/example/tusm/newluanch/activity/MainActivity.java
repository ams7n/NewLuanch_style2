package com.example.tusm.newluanch.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tusm.newluanch.TouchUtill;
import com.iboard.tusm.newluanch.R;
import com.example.tusm.newluanch.dialog.SourceDialog;
import com.example.tusm.newluanch.adapter.ViewPagerFragmentAdapter;
import com.example.tusm.newluanch.entity.LuancherUtill;
import com.example.tusm.newluanch.fragment.CalendarFragment;
import com.example.tusm.newluanch.fragment.DateFragment;
import com.example.tusm.newluanch.fragment.MemoFragment;
import com.example.tusm.newluanch.fragment.MemoFragmentStyle;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private String TAG = "luanch";
    public RelativeLayout datelayout;
    public RelativeLayout calendarlayout;
    public RelativeLayout memolayout;
    private RelativeLayout Main_back;
    private LinearLayout set, all, white, file, soure;
    private TextView tx_set, tx_all, tx_white, tx_file, tx_source;
    private int oldPosition = 0;// 记录上一次点的位�?
    private int currentItem; // 当前页面
    public static ImageView iMwifi, iMeth, iMblue;
    private ViewPager mViewPager;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ArrayList<View> dots;
    public static MainActivity instance = null;
    private int foucsposition = 0;
    private boolean touchInviewPage = true;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean firstOpen = false;
    private static boolean PowerOn = true;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_main);
        initFragmetList();
        instance=this;
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager, mFragmentList);
        initView();
        initViewPager();
        IconChange(LuancherUtill.CheckNetwork(this));
        IconChange(3);
        sharedPreferences = getSharedPreferences("LauncherShare", Activity.MODE_PRIVATE);
        firstOpen = sharedPreferences.getBoolean("first", false);
        editor = sharedPreferences.edit();


        if (!firstOpen) {
            editor.putBoolean("first", true);
            editor.commit();
            try {
                LuancherUtill.setWallpaper(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LuancherUtill.ChangWallpaper(this, Main_back);
        }


    }

    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.ViewPagerLayout);
        datelayout = (RelativeLayout) findViewById(R.id.time);
        calendarlayout = (RelativeLayout) findViewById(R.id.calendat);
        memolayout = (RelativeLayout) findViewById(R.id.memo);
        iMwifi = (ImageView) findViewById(R.id.icon_wifi);
        iMeth = (ImageView) findViewById(R.id.icon_eth);
        iMblue = (ImageView) findViewById(R.id.icon_blue);
        set = (LinearLayout) findViewById(R.id.setting);
        all = (LinearLayout) findViewById(R.id.allapp);
        Main_back = (RelativeLayout) findViewById(R.id.main_back);
        white = (LinearLayout) findViewById(R.id.whiteboard);
        file = (LinearLayout) findViewById(R.id.file);
        soure = (LinearLayout) findViewById(R.id.source);
        tx_set = (TextView) findViewById(R.id.textView_set);
        tx_white = (TextView) findViewById(R.id.textView_board);
        tx_source = (TextView) findViewById(R.id.textView_source);
        tx_all = (TextView) findViewById(R.id.textView_app);
        tx_file = (TextView) findViewById(R.id.textView_file);
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));
        set.setOnFocusChangeListener(this);
        all.setOnFocusChangeListener(this);
        white.setOnFocusChangeListener(this);
        soure.setOnFocusChangeListener(this);
        file.setOnFocusChangeListener(this);
        set.setFocusable(true);
        all.setFocusable(true);
        white.setFocusable(true);
        file.setFocusable(true);
        soure.setFocusable(true);
        set.setOnClickListener(this);
        all.setOnClickListener(this);
        white.setOnClickListener(this);
        file.setOnClickListener(this);
        soure.setOnClickListener(this);
        iMwifi.setOnClickListener(this);
        iMeth.setOnClickListener(this);
        iMblue.setOnClickListener(this);
    }

    public void initViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPagetOnPagerChangedLisenter());
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mViewPager.setCurrentItem(0);
        dots.get(0).setBackgroundResource(R.drawable.dot_focused);
    }

    public void initFragmetList() {
        Fragment time = new DateFragment();
        Fragment calenday = new CalendarFragment();
        Fragment memo = new MemoFragmentStyle();
        mFragmentList.add(time);
        mFragmentList.add(calenday);
        mFragmentList.add(memo);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings");
                Intent setintent = new Intent(Intent.ACTION_MAIN);
                setintent.addCategory(Intent.CATEGORY_LAUNCHER);
                setintent.setComponent(componentName);
                setintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                MainActivity.this.startActivity(setintent);
                break;
            case R.id.allapp:
                Intent intent = new Intent(MainActivity.this, AllAppActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.whiteboard:
                if (checkApkExist(MainActivity.this, "com.iboard.whiteboard")) {
                    ComponentName componentwhite = new ComponentName("com.iboard.whiteboard", "com.iboard.whiteboard.MainActivity");
                    Intent whilteintent = new Intent(Intent.ACTION_MAIN);
                    whilteintent.addCategory(Intent.CATEGORY_LAUNCHER);
                    whilteintent.setComponent(componentwhite);
                    whilteintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    MainActivity.this.startActivity(whilteintent);
//                    Intent broadcast = new Intent();
//                    broadcast.setAction("iBoard.whiteboard.android");
              //      MainActivity.this.sendBroadcast(broadcast);
                }else{
                    if(toast!=null) {
                        toast.cancel();
                        toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.uninstanll), Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.uninstanll), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case R.id.file:
                ComponentName componentfile = new ComponentName("com.estrongs.android.pop", "com.estrongs.android.pop.view.FileExplorerActivity");
                Intent fileintent = new Intent(Intent.ACTION_MAIN);
                fileintent.addCategory(Intent.CATEGORY_LAUNCHER);
                fileintent.setComponent(componentfile);
                fileintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                MainActivity.this.startActivity(fileintent);
                break;
            case R.id.source:
                Intent Sourceintent = new Intent(MainActivity.this, SourceDialog.class);
                MainActivity.this.startActivity(Sourceintent);
                break;
            case R.id.icon_blue:
                Intent blueIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(blueIntent);
                break;
            case R.id.icon_wifi:
                Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(wifiIntent);
                break;
            case R.id.icon_eth:
                 Intent ethIntent = new Intent(Settings.ACTION_SETTINGS);
               startActivity(ethIntent);

                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {

            case R.id.setting:
                if (b) {
                    tx_set.setTextColor(getResources().getColor(R.color.foutext));

                } else {
                    tx_set.setTextColor(getResources().getColor(R.color.white));


                }
                break;
            case R.id.allapp:
                if (b) {
                    tx_all.setTextColor(getResources().getColor(R.color.foutext));
                } else {
                    tx_all.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.whiteboard:
                if (b) {
                    tx_white.setTextColor(getResources().getColor(R.color.foutext));
                } else {
                    tx_white.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.file:
                if (b) {
                    tx_file.setTextColor(getResources().getColor(R.color.foutext));
                } else {
                    tx_file.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.source:
                if (b) {
                    tx_source.setTextColor(getResources().getColor(R.color.foutext));
                } else {
                    tx_source.setTextColor(getResources().getColor(R.color.white));
                }
                break;

        }
    }

    class ViewPagetOnPagerChangedLisenter implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dots.get(oldPosition).setBackgroundResource(
                    R.drawable.dot_normal);
            dots.get(position)
                    .setBackgroundResource(R.drawable.dot_focused);

            oldPosition = position;
            currentItem = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getKeyCode() == 66) {
                if (!touchInviewPage) {
                    setDownfocus(foucsposition);
                }
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN://按向下键
                    setfoucs(foucsposition);
                    touchInviewPage = false;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    foucsposition = 0;
                    mViewPager.requestFocus();
                    touchInviewPage = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (touchInviewPage) {
                        int lPage = mViewPager.getCurrentItem();
                        if (lPage != 0) {
                            mViewPager.setCurrentItem(lPage - 1);
                        }
                    } else {
                        if (foucsposition != 0) {
                            foucsposition--;
                            setfoucs(foucsposition);
                        }
                    }

                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (touchInviewPage) {
                        int rPage = mViewPager.getCurrentItem();
                        if (rPage != mFragmentList.size() - 1) {
                            mViewPager.setCurrentItem(rPage + 1);
                        }
                    } else {
                        foucsposition++;
                        setfoucs(foucsposition);
                    }
                    break;
                case KeyEvent.KEYCODE_TV_INPUT:
                    Intent Sourceintent = new Intent(MainActivity.this, SourceDialog.class);
                    MainActivity.this.startActivity(Sourceintent);

                    break;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    private void setfoucs(int position) {
        switch (position) {
            case 0:
                set.requestFocus();
                break;
            case 1:
                white.requestFocus();
                break;
            case 2:
                file.requestFocus();
                break;
            case 3:
                soure.requestFocus();
                break;
            case 4:
                all.requestFocus();
                break;
        }
    }

    private void setDownfocus(int position) {
        switch (position) {
            case 0:
                set.performClick();
                break;
            case 1:
                white.performClick();
                break;
            case 2:
                file.performClick();
                break;
            case 3:
                soure.performClick();
                break;
            case 4:
                all.performClick();
                break;
        }
    }

    public static void IconChange(int icon) {
        switch (icon) {
            case 0:
                iMwifi.setImageResource(R.drawable.wifi_disable);
                iMeth.setImageResource(R.drawable.cabe_disable);
                break;
            case 1:
                iMwifi.setImageResource(R.drawable.wifi_eable);
                iMeth.setImageResource(R.drawable.cabe_disable);
                break;
            case 2:
                iMwifi.setImageResource(R.drawable.wifi_disable);
                iMeth.setImageResource(R.drawable.cabe_enable);
                break;
            case 3:
                if (LuancherUtill.BlueState()) {
                    iMblue.setImageResource(R.drawable.bluetooth_on);
                } else {
                    iMblue.setImageResource(R.drawable.bluetooth_disconnecte);
                }
                break;
            case 4:
                iMwifi.setImageResource(R.drawable.wifi_disable);
                break;
            case 5:
                iMeth.setImageResource(R.drawable.cabe_disable);
                break;
            case 6:
                iMwifi.setImageResource(R.drawable.wifi_eable);
                break;
            case 7:
                iMeth.setImageResource(R.drawable.cabe_enable);
                break;
            case 8:
                iMblue.setImageResource(R.drawable.bluetooth_on);
                break;
            case 9:
                iMblue.setImageResource(R.drawable.bluetooth_disconnecte);
                break;
        }
    }


    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if ((PowerOn)&&(TvManager.getInstance().getEnvironment("poweron_mode").equals("tv_mode")))
            {
                Log.i(TAG, "<<<<<<----------PowerOn == true ---------->>>>>");
                PowerOn = false;
                ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui",
                        "com.mstar.tv.tvplayer.ui.RootActivity");
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(componentName);
                intent.putExtra("isPowerOn", true);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                MainActivity.this.startActivity(intent);

            }else{
                LuancherUtill. ChangWallpaper(this,Main_back);
                TouchUtill.SetTouchToAndroid();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }

    }


}
