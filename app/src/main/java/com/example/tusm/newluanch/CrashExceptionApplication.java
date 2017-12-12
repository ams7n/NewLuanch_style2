package com.example.tusm.newluanch;

import android.app.Application;

/**
 * Created by zjx on 2017/4/5.
 */

public class CrashExceptionApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
    }
}
