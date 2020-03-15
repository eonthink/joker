package com.demo.joker.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppGlobals {
    private static Application sApplication;
    public static Application getsApplication(){
        if(sApplication ==null){
            try {
                Method method= Class.forName("android.app.activityThread").getDeclaredMethod("currentApplication");
               sApplication= (Application) method.invoke(null,null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return sApplication;
    }
}
