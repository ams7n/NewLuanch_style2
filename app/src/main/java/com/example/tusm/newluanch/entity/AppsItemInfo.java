package com.example.tusm.newluanch.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by tusm on 17/7/6.
 */

public class AppsItemInfo {
    private Drawable icon; // 存放图片
    private String label; // 存放应用程序名
    private String packageName; // 存放应用程序包名

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
