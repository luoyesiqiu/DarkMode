package com.luoye.darkmode.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import com.luoye.darkmode.fragment.SettingsFragment;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage{
    private  final  int NightColor =0xff8A8A8A;//0xFF373737;
    private  final  int GreenColor =0xFFcaEAce;
    private  int globalColor;
    private Properties properties;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        ClassLoader loader=loadPackageParam.classLoader;

        properties=new Properties();
        try {
            properties.load(new FileReader(SettingsFragment.PROP_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //globalColor= NightColor;
        if (!loadPackageParam.packageName.equals("com.android.systemui")) {
            hookBackgroundColor(loader);
            hookDrawColor(loader);
            hookBackgroundDrawable(loader);
        }

    }

    private void hookDrawColor(ClassLoader classLoader){
        XposedHelpers.findAndHookMethod("android.graphics.Canvas", classLoader, "drawColor", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if(properties.getProperty("open")==null||properties.getProperty("open").equals("false")){
                    return ;
                }

                switch (properties.getProperty("mode")){
                    case "0":
                        globalColor=NightColor;
                        break;
                    case "1":
                        globalColor=GreenColor;
                        break;
                }

                int curColor=(int)param.args[0];

                int red = ((curColor&0xff0000)>>16);
                int green = ((curColor&0xff00)>>8);
                int blue = (curColor&0x0000ff);
                if(red>=235&&green>=235&&blue>=235) {
                    param.args[0] = globalColor;
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }

    private void hookBackgroundColor(ClassLoader classLoader){
        XposedHelpers.findAndHookMethod("android.view.View", classLoader, "setBackgroundColor", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if(properties.getProperty("open")==null||properties.getProperty("open").equals("false")){
                    return ;
                }

                switch (properties.getProperty("mode")){
                    case "0":
                    globalColor=NightColor;
                    break;
                    case "1":
                        globalColor=GreenColor;
                        break;
                }

                int curColor=(int)param.args[0];

                int red = ((curColor&0xff0000)>>16);
                int green = ((curColor&0xff00)>>8);
                int blue = (curColor&0x0000ff);
                if(red>=235&&green>=235&&blue>=235) {
                    param.args[0] = globalColor;
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }

    private void hookBackgroundDrawable(ClassLoader classLoader){
        XposedHelpers.findAndHookMethod("android.view.View", classLoader, "setBackgroundDrawable", Drawable.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if(properties.getProperty("open")==null||properties.getProperty("open").equals("false")){
                    return ;
                }

                switch (properties.getProperty("mode")){
                    case "0":
                        globalColor=NightColor;
                        break;
                    case "1":
                        globalColor=GreenColor;
                        break;
                }
                param.args[0] = new ColorDrawable(globalColor);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }
}
