package com.example.tusm.newluanch.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.tusm.newluanch.TouchUtill;
import com.example.tusm.newluanch.entity.LuancherUtill;
import com.iboard.tusm.newluanch.R;
import com.example.tusm.newluanch.activity.AllAppActivity;
import com.example.tusm.newluanch.activity.MainActivity;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.TvOsType;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import net.robinx.lib.blur.widget.BlurDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tusm on 17/7/4.
 */

public class SourceDialog extends Activity implements View.OnClickListener {
    private ImageView tv, av, home, media, hdml1,
            hdml2, hdml3, hdml4, vga1, vga2, vga3, ypbpr1,dtv,vga4;
    private RelativeLayout surfacelayout;
    private LinearLayout source_back;
    private ImageView back;
    private TextView xinhao;
    private ImageView surfacei;
    private SurfaceView surfaceView;
    private SurfaceHolder.Callback callback;
    private WindowManager.LayoutParams sLayoutParams;
    WindowManager.LayoutParams surfaceParams;
    private WindowManager wm;
    private TvCommonManager tvCommonManager;
    private int mHDMI0_SEL = 81;
    private int mHDML1_sel = 80;
    private int mVGA_SEL = 82;
    private int mVGA_SW = 107;
    private int mVGA_GPIO3 = 109; // SW_330
    private Bitmap mbitmap;
    private Bitmap newbitmap;
    private TimerTask timerTask;
    private Timer timer;
    private int gpio;
    private boolean mIsSignalStatus;
    // vag1=1 vga2=2 vga3=3   hdml3=5 ops=6 hdml1 & hdml2 & yrpb &atv  =4   tv=10 usb=11 home=12
    private int PresentSource;
    private int SourceKey ;
    private  View[] list;
    private int FactoryMeum = 0;
    private boolean ishow = false;
    private boolean fastChannle = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.source_xml);
        init();
        list = new View[]{ tv, av, home, media, hdml1,
                hdml2, hdml3, hdml4, vga1, vga2, vga3, ypbpr1,dtv,vga4};
        BlurDrawable blurDrawable = new BlurDrawable(back);
        blurDrawable.setDrawOffset(back.getLeft(), back.getTop()  );
        blurDrawable.setCornerRadius(2);
        blurDrawable.setBlurRadius(10);
        blurDrawable.setOverlayColor(Color.parseColor("#594a4a4a"));
        source_back.setBackground(blurDrawable);
        tvCommonManager = TvCommonManager.getInstance();
        handlertv.postDelayed(handlerRuntv, 100);


    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        tv = (ImageView) findViewById(R.id.source_tv);
        home = (ImageView) findViewById(R.id.source_home);
        av = (ImageView) findViewById(R.id.source_av);
        media = (ImageView) findViewById(R.id.source_media);
        xinhao = (TextView) findViewById(R.id.textView_xinhao);
        hdml1 = (ImageView) findViewById(R.id.source_hdml1);
        hdml2 = (ImageView) findViewById(R.id.source_hdml2);
        dtv= (ImageView) findViewById(R.id.source_dtv);
        hdml3 = (ImageView) findViewById(R.id.source_hdml3);
        hdml4 = (ImageView) findViewById(R.id.source_hdml4);
        vga1 = (ImageView) findViewById(R.id.source_vga1);
        vga2 = (ImageView) findViewById(R.id.source_vga2);
        vga3 = (ImageView) findViewById(R.id.source_vga3);
        vga4 = (ImageView) findViewById(R.id.source_vga4);
        ypbpr1 = (ImageView) findViewById(R.id.source_yrpb);
        surfacei = (ImageView) findViewById(R.id.surface);
        source_back= (LinearLayout)findViewById(R.id.small_back);
        surfacelayout = (RelativeLayout) findViewById(R.id.surfacelayout);
        back.setOnClickListener(this);
        surfacelayout.setOnClickListener(this);
        tv.setOnClickListener(this);
        av.setOnClickListener(this);
        home.setOnClickListener(this);
        media.setOnClickListener(this);
        hdml1.setOnClickListener(this);
        hdml2.setOnClickListener(this);
        hdml3.setOnClickListener(this);
        hdml4.setOnClickListener(this);
        vga1.setOnClickListener(this);
        vga2.setOnClickListener(this);
        vga3.setOnClickListener(this);
        vga4.setOnClickListener(this);
        dtv.setOnClickListener(this);
        ypbpr1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.surfacelayout:
                if (SourceKey != 0) {
                    setKeyCode(SourceKey);
                }
                break;
            case R.id.source_media:
                SourceKey = 334;
                PresentSource = 11;
                surfaceView.setVisibility(View.GONE);
                xinhao.setVisibility(View.GONE);
                surfacei.setBackground(getResources().getDrawable(R.drawable.usb));
                setMode();
                media.setFocusableInTouchMode(true);
                LuancherUtill.Source = "media";
                fastChannle = true;
                break;
            case R.id.source_tv:
                SourceKey = 324;
                PresentSource = 10;
                setMode();
                tv.setFocusableInTouchMode(true);
                SetInputSource();
                setPipscale();
                surfaceView.setVisibility(View.VISIBLE);
                LuancherUtill.Source = "tv";
                break;
            case R.id.source_hdml1: //一体机插hdml2
                setMode();
                hdml1.setFocusableInTouchMode(true);
                SourceKey = 328;
                PresentSource = 4;
                try {
                    setGpioDeviceStatus(mHDMI0_SEL, false);
                    TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_HDMI2");
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI2);
                    setPipscale();
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "hdml1";
                break;
            case R.id.source_hdml2:
                setMode();
                hdml2.setFocusableInTouchMode(true);
                SourceKey = 329;
                PresentSource = 4;
                try {
                    setGpioDeviceStatus(mHDMI0_SEL, false);
                    TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_HDMI1");
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI);
                    setPipscale();
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "hdml2";
                break;
            case R.id.source_hdml3:
                setMode();
                hdml3.setFocusableInTouchMode(true);
                SourceKey = 330;
                PresentSource = 5;
                gpio = getGpioDeviceStatus(102);
                try {
                    setGpioDeviceStatus(mHDML1_sel, false);
                    TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_HDMI3");
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI3);
                    setPipscale();

                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "hdml3";
                break;
            case R.id.source_hdml4:
                setMode();
                hdml4.setFocusableInTouchMode(true);
                SourceKey = 327;
                PresentSource = 6;
                try {
                    setGpioDeviceStatus(mHDML1_sel, true);
                    TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_HDMI_OPS");
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI3);
                    setPipscale();
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "hdml4";
                break;
            case R.id.source_vga1:
                setMode();
                vga1.setFocusableInTouchMode(true);
                SourceKey = 331;
                PresentSource = 1;
                try {
                    if (TvManager.getInstance() != null) {
                        if ((!(TvManager.getInstance().getEnvironment("Ext_HDMI").equals("Ext_VGA1")))) {
                            setGpioDeviceStatus(mVGA_SEL, false);
                            TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_VGA1");
                            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_VGA);
                            setPipscale();
                        }
                    }
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "vga1";
                break;
            case R.id.source_vga2:
                setMode();
                vga2.setFocusableInTouchMode(true);
                SourceKey = 332;
                PresentSource = 2;
                try {
                    if (TvManager.getInstance() != null) {
                        if ((!(TvManager.getInstance().getEnvironment("Ext_HDMI").equals("Ext_VGA2")))) {
                            setGpioDeviceStatus(mVGA_SEL, true);
                            setGpioDeviceStatus(mVGA_GPIO3, false);
                            setGpioDeviceStatus(mVGA_SW, false);
                            TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_VGA2");
                            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_VGA);
                            setPipscale();

                        }
                    }
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "vga2";
                break;
            case R.id.source_vga3:
                setMode();
                vga3.setFocusableInTouchMode(true);
                SourceKey = 333;
                PresentSource = 3;
                try {
                    if (TvManager.getInstance() != null) {
                        if ((!(TvManager.getInstance().getEnvironment("Ext_HDMI").equals("Ext_VGA3")))) {
                            setGpioDeviceStatus(mVGA_SEL, true);
                            setGpioDeviceStatus(mVGA_GPIO3, false);
                            setGpioDeviceStatus(mVGA_SW, true);
                          TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_VGA3");
                            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_VGA);
                            setPipscale();

                        }
                    }
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "vga3";
                break;
            case R.id.source_av :
                setMode();
                av.setFocusableInTouchMode(true);
                SourceKey = 325;
                PresentSource = 4;
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                setPipscale();
                LuancherUtill.Source = "av";
                break;
            case R.id.source_yrpb:
                setMode();
                ypbpr1.setFocusableInTouchMode(true);
                SourceKey = 326;
                PresentSource = 4;
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_YPBPR);
                setPipscale();
                LuancherUtill.Source = "yrpb";
                break;
            case R.id.source_home:
                SourceKey = 999;
                PresentSource = 12;
                surfaceView.setVisibility(View.GONE);
                xinhao.setVisibility(View.GONE);
                Bitmap bm=takeScreenShot(MainActivity.instance);
                BitmapDrawable bd=new BitmapDrawable(bm);
                surfacei.setBackgroundDrawable(bd);
                setMode();
                home.setFocusableInTouchMode(true);
                LuancherUtill.Source = "home";
                fastChannle = true;
                break;
            case R.id.back:
                if(ishow) {
                    if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE) {
                        TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                    }
                    remoview();
                    finish();
                    back.setEnabled(false);
                }
                break;
            case R.id.source_vga4:
                setMode();
                vga4.setFocusableInTouchMode(true);
                SourceKey = 302;
                PresentSource = 7;
                try {
                    if (TvManager.getInstance() != null) {
                     //   Toast.makeText(SourceDialog.this,TvManager.getInstance().getEnvironment("Ext_HDMI"),Toast.LENGTH_SHORT).show();
                        if ((!(TvManager.getInstance().getEnvironment("Ext_HDMI").equals("Ext_VGA4")))) {
                            setGpioDeviceStatus(mVGA_SEL, true);
                            setGpioDeviceStatus(mVGA_GPIO3, true);
                            setGpioDeviceStatus(mVGA_SW, true);
                            TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_VGA4");
                            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_VGA);
                            setPipscale();
                        }
                    }
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                LuancherUtill.Source = "vga4";
                break;
            case R.id.source_dtv:
                setMode();
                dtv.setFocusableInTouchMode(true);
                SourceKey = 303;
                PresentSource = 4;
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                setPipscale();
                LuancherUtill.Source = "dtv";
                break;
        }

    }


    Handler handlertv = new Handler();
    Runnable handlerRuntv = new Runnable() {

        @Override
        public void run() {
            try {
                surfaceView = new SurfaceView(SourceDialog.this);
                openSurfaceView();
                TvManager.getInstance().setEnvironment("Ext_HDMI", "Ext_HDMI_OFF");
                if(LuancherUtill.Source!=null&&LuancherUtill.Source.equals("")){
                    SetInputSource();
                    setPipscale();
                    SourceKey = 324;
                }else if(LuancherUtill.Source!=null&&!LuancherUtill.Source.equals("")){
                    handlertv.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         MemorySource(LuancherUtill.Source);
                     }
                 },  500);
                }
                surfacelayout.setFocusable(true);
                tv.setFocusable(true);
                av.setFocusable(true);
                media.setFocusable(true);
                home.setFocusable(true);
                hdml1.setFocusable(true);
                hdml2.setFocusable(true);
                hdml3.setFocusable(true);
                hdml4.setFocusable(true);
                vga1.setFocusable(true);
                vga2.setFocusable(true);
                vga3.setFocusable(true);
                vga4.setFocusable(true);
                dtv.setFocusable(true);
                ypbpr1.setFocusable(true);
                startHerartBeatThread();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handlertv.removeCallbacks(handlerRuntv);
        }
    };

    private void openSurfaceView() {
        surfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceParams = new WindowManager.LayoutParams();

        surfaceParams.x = 826;

        surfaceParams.y = 330;

        surfaceParams.width = 710;

        surfaceParams.height = 420;

        surfaceParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;// WindowManager.LayoutParams.TYPE_APPLICATION
        // ;//WindowManager.LayoutParams.TYPE_WALLPAPER;
        surfaceParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        surfaceParams.gravity = Gravity.TOP | Gravity.LEFT;
        callback = new android.view.SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    TvManager.getInstance().getPlayerManager().setDisplay(holder);
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }

        };
        surfaceView.getHolder().addCallback(
                (android.view.SurfaceHolder.Callback) callback);
        wm = (WindowManager)  getSystemService( WINDOW_SERVICE);
        // wm = (WindowManager) getApplicationContext().getSystemService(
        // WINDOW_SERVICE);
         wm.addView(surfaceView, surfaceParams);
              ishow = true;

    }

    private void SetInputSource() {
        try {

            TvCommonManager.getInstance().setInputSource(TvOsType.EnumInputSource.E_INPUT_SOURCE_ATV);
            int curSource = 1;
            TvOsType.EnumInputSource currentSource = TvCommonManager.getInstance().getCurrentInputSource();

            if (TvOsType.EnumInputSource.values()[curSource] == TvOsType.EnumInputSource.E_INPUT_SOURCE_ATV) {
                int channel = TvManager.getInstance().getChannelManager().getCurrChannelNumber();
                if ((channel < 0) || (channel > 255)) {
                    channel = 0;
                }
                try {
                    if (TvManager.getInstance() != null) {
                        TvManager.getInstance().getChannelManager().selectProgram(channel, (short) MEMBER_SERVICETYPE.E_SERVICETYPE_ATV.ordinal(), 0x00);
                    }
                } catch (TvCommonException e) {
                    // TODO: e exception
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    private void setPipscale() {
        try {
            VideoWindowType videoWindowType = new VideoWindowType();
            videoWindowType.x = 826;
            videoWindowType.y = 330;
            videoWindowType.width = 710;
            videoWindowType.height = 420;
            TvManager.getInstance().getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
            TvManager.getInstance().getPictureManager().setDisplayWindow(videoWindowType);
            TvManager.getInstance().getPictureManager().scaleWindow();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }

    }

    public enum MEMBER_SERVICETYPE {
        E_SERVICETYPE_ATV, // /< Service type ATV
        E_SERVICETYPE_DTV, // /< Service type DTV
        E_SERVICETYPE_RADIO, // /< Service type Radio
        E_SERVICETYPE_DATA, // /< Service type Data
        E_SERVICETYPE_UNITED_TV, // /< Service type United TV
        E_SERVICETYPE_INVALID, // /< Service type INVALID
    }



    public void remoview() {
        SourceKey = 0;
         if(surfaceView!=null && ishow &&!this.isFinishing()&&surfaceView.getVisibility()==View.VISIBLE) {
             wm.removeView(surfaceView);
         }
        if (timerTask != null) {
            timerTask.cancel();
            timer = null;
            timerTask = null;
        }


    }

    public static boolean setGpioDeviceStatus(int mGpio, boolean bEnable) {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().setGpioDeviceStatus(mGpio, bEnable);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getGpioDeviceStatus(int mGpio) {
        try {
            if (TvManager.getInstance() != null) {

                return TvManager.getInstance().getGpioDeviceStatus(mGpio);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取屏幕长和高
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;
        Bitmap bmp = Bitmap.createBitmap(b1, 0, 0, width, height);
        view.destroyDrawingCache();
        return bmp;
    }

    /*
    检测有没信号
     */
    private void startHerartBeatThread() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       switch (PresentSource) {
                           case 1://vag1
                               gpio = getGpioDeviceStatus(105);
                               if (gpio == 0) {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               } else {
                                   mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                                   if (mIsSignalStatus) {
                                       surfaceView.setVisibility(View.VISIBLE);
                                   }else{
                                       xinhao.setVisibility(View.VISIBLE);
                                       surfacei.setBackgroundColor(Color.BLACK);
                                       surfaceView.setVisibility(View.GONE);
                                   }
                               }
                               break;
                           case 2://vag2
                               gpio = getGpioDeviceStatus(110);
                               if (gpio == 1) {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               } else {
                                   mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                                   if (mIsSignalStatus) {
                                       surfaceView.setVisibility(View.VISIBLE);
                                   }else{
                                       xinhao.setVisibility(View.VISIBLE);
                                       surfacei.setBackgroundColor(Color.BLACK);
                                       surfaceView.setVisibility(View.GONE);
                                   }
                               }
                               break;
                           case 3://vag3
                               gpio = getGpioDeviceStatus(106);
                               if (gpio == 1) {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               } else {
                                   mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                                   if (mIsSignalStatus) {
                                       surfaceView.setVisibility(View.VISIBLE);
                                   }else{
                                       xinhao.setVisibility(View.VISIBLE);
                                       surfacei.setBackgroundColor(Color.BLACK);
                                       surfaceView.setVisibility(View.GONE);
                                   }
                               }
                               break;
                           case 4: //hdml1&2
                               mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                               if (mIsSignalStatus) {
                                   surfaceView.setVisibility(View.VISIBLE);

                               } else {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               }
                               break;
                           case 5://hdml3
                               gpio = getGpioDeviceStatus(102);
                               if (gpio == 0) {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               } else {
                                   mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                                   if (mIsSignalStatus) {
                                       surfaceView.setVisibility(View.VISIBLE);
                                   }else{
                                       xinhao.setVisibility(View.VISIBLE);
                                       surfacei.setBackgroundColor(Color.BLACK);
                                       surfaceView.setVisibility(View.GONE);
                                   }
                               }
                               break;
                           case 6://hdml4
                               gpio = getGpioDeviceStatus(104);
                                if (gpio == 9) {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               } else {
                                   mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                                   if (mIsSignalStatus) {
                                       surfaceView.setVisibility(View.VISIBLE);
                                   }else{
                                       xinhao.setVisibility(View.VISIBLE);
                                       surfacei.setBackgroundColor(Color.BLACK);
                                       surfaceView.setVisibility(View.GONE);
                                   }
                               }
                               break;
                           case 7://vag4
                               gpio = getGpioDeviceStatus(108);
                               if (gpio == 0) {
                                   xinhao.setVisibility(View.VISIBLE);
                                   surfacei.setBackgroundColor(Color.BLACK);
                                   surfaceView.setVisibility(View.GONE);
                               } else {
                                   mIsSignalStatus = tvCommonManager.isSignalStable(tvCommonManager.getCurrentTvInputSource());
                                   if (mIsSignalStatus) {
                                       surfaceView.setVisibility(View.VISIBLE);
                                   }else{
                                       xinhao.setVisibility(View.VISIBLE);
                                       surfacei.setBackgroundColor(Color.BLACK);
                                       surfaceView.setVisibility(View.GONE);
                                   }
                               }
                               break;
                       }
                   }
               });
            }
        };
        timer.schedule(timerTask, 0, 2000);
    }



    private void setKeyCode(final int SourceCode) {
        if (SourceCode == 999) {
            if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE){
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
            }
            remoview();
            finish();

        } else {
            new Thread() {
                public void run() {
                    try {
//                        handlertv.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                               finish();
//                            }
//                        },1500);
                        remoview();
                        Instrumentation inst
                                = new Instrumentation();
                        inst.sendKeyDownUpSync(SourceCode);
                        surfacelayout.setEnabled(false);
                    } catch
                            (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


        }
    }
 private  void  setMode(){
     for(int i=0;i<list.length;i++){
         list[i].setFocusable(false);
         list[i].setFocusableInTouchMode(false);
     }

 }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_DOWN://按向下键
                for(int i=0;i<list.length;i++){
                    list[i].setFocusable(true);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                for(int i=0;i<list.length;i++){
                    list[i].setFocusable(true);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                for(int i=0;i<list.length;i++){
                    list[i].setFocusable(true);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                for(int i=0;i<list.length;i++){
                    list[i].setFocusable(true);
                }
                break;
            case KeyEvent.KEYCODE_2:
                FactoryMeum++;
                //  surfacelayout.setFocusable(true);
                break;
            case KeyEvent.KEYCODE_5:
                FactoryMeum++;
                //  surfacelayout.setFocusable(true);
                break;
            case KeyEvent.KEYCODE_8:
                FactoryMeum++;
                //  surfacelayout.setFocusable(true);
                break;
            case KeyEvent.KEYCODE_0:
                if(FactoryMeum==3){
                    remoview();
                    finish();
                    Intent   Factoryintent = new Intent(
                            "mstar.tvsetting.factory.intent.action.MainmenuActivity");
                     this.startActivity(Factoryintent);
                    FactoryMeum = 0;
                }else{
                    FactoryMeum = 0;
                }
                //  surfacelayout.setFocusable(true);
                break;
            case KeyEvent.KEYCODE_BACK:
                if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE){
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                }
                remoview();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void MemorySource(String source){
        if(source.equals("hdml1")){
            hdml1.performClick();
        }
        if(source.equals("hdml2")){
            hdml2.performClick();
        }
        if(source.equals("hdml3")){
            hdml3.performClick();
        }
        if(source.equals("hdml4")){
            hdml4.performClick();
        }
        if(source.equals("vga1")){
            vga1.performClick();
        }
        if(source.equals("vga2")){
            vga2.performClick();
        }
        if(source.equals("vga3")){
            vga3.performClick();
        }
        if(source.equals("vga4")){
            vga4.performClick();
        }
        if(source.equals("av")){
            av.performClick();
        }
        if(source.equals("tv")){
            tv.performClick();
        }
        if(source.equals("home")){
            home.performClick();
        }
        if(source.equals("yrpb")){
            ypbpr1.performClick();
        }
        if(source.equals("dtv")){
            dtv.performClick();
        }
        if(source.equals("media")){
            media.performClick();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            if(fastChannle){
                fastChannle = false;
                return super.dispatchTouchEvent(ev);
            }
            if (TouchUtill.isAllowDispatchEvent(ev)) {
                return super.dispatchTouchEvent(ev);
            }
            return false;


    }


}

