package com.demo.joker.utils;

import android.util.DisplayMetrics;

import com.demo.libcommon.utils.AppGlobals;

public class PixUtils {
    public static int dp2px(int dpValue){
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return (int) (metrics.density*dpValue+0.5f);
    }

    public  static int getScreenWidth(){
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public  static int getScreenHeight(){
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
