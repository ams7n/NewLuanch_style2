package com.example.tusm.newluanch.fragment;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.tusm.newluanch.activity.MainActivity;
import com.iboard.tusm.newluanch.R;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

/**
 * Created by tusm on 17/7/4.
 */

public class DateFragment extends Fragment implements View.OnClickListener {
    private View v;
    private TextClock  time,day;
    private LinearLayout opslayout;
    private ImageView Imwin,Imquery,Impop;
    private PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
           v = inflater.inflate(R.layout.fragment_time,null);
            time=(TextClock)v.findViewById(R.id.textClock);
            day=(TextClock)v.findViewById(R.id.textView_small);
            opslayout=(LinearLayout)v.findViewById(R.id.ops_layout);
            Imwin = (ImageView)v.findViewById(R.id.imageView_windows);
            Imquery = (ImageView)v.findViewById(R.id.imageView_query);
            Impop= (ImageView)v.findViewById(R.id.imageView_query_pop);
            time.setOnClickListener(this);
            day.setOnClickListener(this);
            Imquery.setOnClickListener(this);
            Imwin.setOnClickListener(this);
            boolean ops = OpsIsOpen();
             if(ops){
                 opslayout.setVisibility(View.VISIBLE);

             }else{
                 opslayout.setVisibility(View.GONE);
             }
            }


            return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textClock ||view.getId()==R.id.textView_small) {
            Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
            startActivity(intent);
        }
        if(view.getId()==R.id.imageView_query){
//            if(Impop.getVisibility()==View.INVISIBLE){
//                Impop.setVisibility(View.VISIBLE);
//            }else{
//                Impop.setVisibility(View.INVISIBLE);
//            }

            if (null == popupWindow || !popupWindow.isShowing()) {

                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.query_meassage);
                popupWindow = new PopupWindow(imageView, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                int[] location = new int[2];
                Imquery.getLocationOnScreen(location);
                popupWindow.showAtLocation(Imquery, Gravity.NO_GRAVITY, location[0]-130, location[1]-90);



            } else {
                popupWindow.dismiss();
            }

        }
        if(view.getId()==R.id.imageView_windows){

            Intent intent = new Intent("com.mstar.android.intent.action.OPS_BUTTON");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }
    /*
         ops检测
      */
    public int  getGpioDeviceStatus(int pinId){
        int ret = 0;
        try
        {
            if (TvManager.getInstance() != null )
            {
                ret = TvManager.getInstance().
                        getGpioDeviceStatus(pinId);
            }
        }
        catch (TvCommonException e)
        {
            e.printStackTrace();
        }
        return ret;
    }
    public boolean OpsIsOpen(){
        int state85 = getGpioDeviceStatus(85);
        int state86 = getGpioDeviceStatus(86);
        if (state86 == 1) {
            return false;
        } else {
            if (state85 == 0) {
                return false;
            }
        }
        return true;
    }

}
