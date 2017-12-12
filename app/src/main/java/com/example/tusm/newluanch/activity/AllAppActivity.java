package com.example.tusm.newluanch.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tusm.newluanch.TouchUtill;
import com.iboard.tusm.newluanch.R;
import com.example.tusm.newluanch.entity.AppsItemInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tusm on 17/7/6.
 */

public class AllAppActivity extends Activity {
    List<AppsItemInfo> list;
    private LinearLayout back;
    private GridView gridview;
    private PackageManager pManager;
    private  BaseAdapter baseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_allapp);
        back=(LinearLayout)findViewById(R.id.all_back);
       // LuancherUtill.ChangWallpaper(this,back);
        // 取得gridview
        gridview = (GridView) findViewById(R.id.gridview);

//        // 获取图片、应用名、包名
//        pManager = AllAppActivity.this.getPackageManager();
//        List<PackageInfo> appList = getAllApps(AllAppActivity.this);
//
       list = new ArrayList<AppsItemInfo>();
//
//        for (int i = 0; i < appList.size(); i++) {
//            PackageInfo pinfo = appList.get(i);
//            AppsItemInfo shareItem = new AppsItemInfo();
//            // 设置图片
//            shareItem.setIcon(pManager
//                    .getApplicationIcon(pinfo.applicationInfo));
//            // 设置应用程序名字
//            shareItem.setLabel(pManager.getApplicationLabel(
//                    pinfo.applicationInfo).toString());
//            // 设置应用程序的包名
//            shareItem.setPackageName(pinfo.applicationInfo.packageName);
//
//            list.add(shareItem);
//
//        }
        queryAppInfo();
        // 设置gridview的Adapter
        baseAdapter = new baseAdapter();
        gridview.setAdapter(baseAdapter);

        // 点击应用图标时，做出响应
        gridview.setOnItemClickListener(new ClickListener());
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                Intent uninstall_intent = new Intent();
                uninstall_intent.setAction(Intent.ACTION_DELETE);
                uninstall_intent.setData(Uri.parse("package:"+list.get(i).getPackageName()));
                startActivity(uninstall_intent);
                return true;
            }
        });

    }

    public static List<PackageInfo> getAllApps(Context context) {

        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);

            // 判断是否为非系统预装的应用程序
            // 这里还可以添加系统自带的，这里就先不添加了，如果有需要可以自己添加
//            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM)== 0) {
//                // 添加自己已经安装的应用程序
//                apps.add(pak);
//            }
            // if()里的值如果<=0则为自己装的程序，否则为系统工程自带
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                apps.add(pak);
            }else{

            }

        }
        return apps;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        queryAppInfo();
        baseAdapter = new baseAdapter();
        gridview.setAdapter(baseAdapter);

    }

    private class baseAdapter extends BaseAdapter {
        LayoutInflater inflater = LayoutInflater.from(AllAppActivity.this);

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                // 使用View的对象itemView与R.layout.item关联
                convertView = inflater.inflate(R.layout.layout_title, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView
                        .findViewById(R.id.apps_image);
                holder.label = (TextView) convertView
                        .findViewById(R.id.apps_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setImageDrawable(list.get(position).getIcon());
            holder.label.setText(list.get(position).getLabel().toString());

            return convertView;

        }

    }

    private class ViewHolder{
        private ImageView icon;
        private TextView label;
    }

    // 当用户点击应用程序图标时，将对这个类做出响应
    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

//          // 将应用所选的应用程序信息共享到Application中
//          MyApp appState = ((MyApp) getApplicationContext());
//          // 获取当前所在选项卡
//          String tab_id = appState.getTab_id();
//          // 设置所选应用程序信息
//          appState.set_AppInfo(tab_id, list.get(arg2).getLabel(), list.get(
//                  arg2).getIcon(), list.get(arg2).getPackageName());
            Intent intent = new Intent();
            intent = AllAppActivity.this.getPackageManager().getLaunchIntentForPackage(list.get(arg2).getPackageName());
            startActivity(intent);
            // 销毁当前Activity
//          finish();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        TouchUtill.SetTouchToAndroid();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("key",event.getAction()+"");
      if(event.getAction()==KeyEvent.KEYCODE_TV_INPUT){
          ComponentName componentwhite = new ComponentName("com.iboard.tusm.source_cn", "com.iboard.tusm.source_cn.MainActivity");
          Intent whilteintent = new Intent(Intent.ACTION_MAIN);
          whilteintent.addCategory(Intent.CATEGORY_LAUNCHER);
          whilteintent.setComponent(componentwhite);
          whilteintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
          AllAppActivity.this.startActivity(whilteintent);

      }

        return super.onKeyDown(keyCode, event);
    }

    public void queryAppInfo() {
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent,0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        if (list != null) {
            list.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 创建一个AppInfo对象，并赋值
                AppsItemInfo appInfo = new AppsItemInfo();
                appInfo.setLabel(appLabel);
                appInfo.setPackageName(pkgName);
                appInfo.setIcon(icon);
                if(!appLabel.equals("NewLuanch")&&!appLabel.equals("Source_CN")) {
                    list.add(appInfo); // 添加至列表中
                }
            }
        }
    }
}
