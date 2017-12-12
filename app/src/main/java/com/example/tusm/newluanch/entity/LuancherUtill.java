package com.example.tusm.newluanch.entity;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import com.iboard.tusm.newluanch.R;

import java.io.IOException;

/**
 * Created by tusm on 17/7/8.
 */

public class LuancherUtill {
    private static String TAG="luancher";
    public static  WallpaperManager wallpaperManager;
    public  static boolean wifiOpen;
    public  static boolean ethernetOpen;
    public  static  boolean blueOPen;
    public static String Source = "";
    /*
   网络查询
    */
    public static int CheckNetwork(Context context){
        int isConnect = 0;
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    Log.e(TAG, "当前WiFi连接可用 ");
                    isConnect=  1;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    Log.e(TAG, "当前有线网络连接可用 ");
                    isConnect =  2;
                }
            } else {
                Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                isConnect =  0;
            }
        }else{
            isConnect =  0;
        }
        return isConnect;
    }
    /*
壁纸替换
 */
    public static void ChangWallpaper(Context context,View v){
         wallpaperManager = WallpaperManager.getInstance(context);

        WallpaperInfo wallInfo = wallpaperManager.getWallpaperInfo();// live wallpapers
        if (wallInfo == null)
        {
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
            Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
            v.setBackgroundDrawable(new BitmapDrawable(v.getResources(), bm));
        }
        else
        {
            v.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        }

    }
    public static  boolean BlueState(){
        boolean isOpen ;
        BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
        if(blueadapter==null){
            isOpen = false;
        }else{
            if(blueadapter.isEnabled()){
                isOpen = true;
            }else{
                isOpen = false  ;
            }
        }
        return  isOpen;
    }
    @SuppressWarnings("ResourceType")
    public static  void setWallpaper (Context context) throws IOException {
        wallpaperManager = WallpaperManager.getInstance(context);
        wallpaperManager.setResource(R.drawable.background_image);

    }

    public static boolean isWifiOpen() {
        return wifiOpen;
    }

    public static void setWifiOpen(boolean wifiOpen) {
        LuancherUtill.wifiOpen = wifiOpen;
    }

    public static boolean isEthernetOpen() {
        return ethernetOpen;
    }

    public static void setEthernetOpen(boolean ethernetOpen) {
        LuancherUtill.ethernetOpen = ethernetOpen;
    }

    public static boolean isBlueOPen() {
        return blueOPen;
    }

    public static void setBlueOPen(boolean blueOPen) {
        LuancherUtill.blueOPen = blueOPen;
    }
}
