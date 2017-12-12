package com.example.tusm.newluanch.broadcast;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ethernet.EthernetManager;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.example.tusm.newluanch.activity.MainActivity;
import com.example.tusm.newluanch.entity.LuancherUtill;

/**
 * Created by tusm on 17/7/8.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private String TAG ="Receiver";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        sharedPreferences = context.getSharedPreferences("LauncherShare", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.e("TAG", "wifiState: 断开" );
                    if(MainActivity.iMwifi!=null) {
                        MainActivity.IconChange(4);
                    }
                    break;
                case  WifiManager.WIFI_STATE_ENABLED:
                    if(MainActivity.iMwifi!=null) {
                        MainActivity.IconChange(6);
                    }
                    break;



            }
        }
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case 10:
                    Log.i(TAG, "STATE_OFF 手机蓝牙关闭");
                    if(MainActivity.iMblue!=null){
                        MainActivity.IconChange(9);
                    }

                    break;

                case 12:
                    Log.i(TAG, "STATE_ON 手机蓝牙开启");
                    if(MainActivity.iMblue!=null) {
                        MainActivity.IconChange(8);
                    }
                    break;

            }
        }
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            if(MainActivity.iMeth!=null) {
                MainActivity.IconChange(LuancherUtill.CheckNetwork(context));
            }

        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            Parcelable parcelableExtra = intent
//                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if (null != parcelableExtra) {
//                // 获取联网状态的NetWorkInfo对象
//                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                //获取的State对象则代表着连接成功与否等状态
//                NetworkInfo.State state = networkInfo.getState();
//                //判断网络是否已经连接
//                boolean isConnected = state == NetworkInfo.State.CONNECTED;
//                Log.e("TAG", "wifiState: 连上有效网络" );
//                if (isConnected) {
//                } else {
//
//                }
//            }
//        }

//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            editor.putInt("open", 1);
//            editor.commit();
//
//        }

    }

}
