package com.example.tusm.newluanch;

import android.view.MotionEvent;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

/**
 * Created by tusm on 17/8/4.
 */

public class TouchUtill {
    private static int  mTouchSelA =88;
    private static int  mTouchSelB   =89;
    public static void SetTouchToAndroid()
    {

        setGpioDeviceStatus(mTouchSelA,false);
        setGpioDeviceStatus(mTouchSelB,false);

    }

    /** 最大同时点击数 */
    private static final int MAX_POINT_NUMBER = 1;

    /** 两次点击最小时间间隔 */
    private static final int MIN_TIME_INTERVAL =1000;

    /** 记录上一次Touch Down的时间 */
    private static long mLastTouchDownTime;

    public static boolean isAllowDispatchEvent(MotionEvent event) {
        // forbid touch event according point index
        if (event.getActionIndex() >= MAX_POINT_NUMBER) {
            return false; // 屏蔽多点事件
        }
        // time interval
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (Math.abs(event.getDownTime() - mLastTouchDownTime) < MIN_TIME_INTERVAL) {
                return false; // 两次点击时间间隔过短，屏蔽该事件
            } else {
                mLastTouchDownTime = event.getDownTime(); // 记录第一次Touch Down时间点
            }
        }
        return true;
    }


    public  static boolean setGpioDeviceStatus(int mGpio, boolean bEnable){
        try {
            if(TvManager.getInstance() != null ){
                return	TvManager.getInstance().setGpioDeviceStatus(mGpio, bEnable);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }


}
