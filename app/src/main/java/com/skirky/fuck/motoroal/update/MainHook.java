package com.skirky.fuck.motoroal.update;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.content.Context;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 过滤不必要的应用
        if (!lpparam.packageName.equals("com.motorola.ccc.ota")) return;
        XposedBridge.log("[BypassOTACheck] Hooking Motorola OTA app");
        // 执行Hook
        hook(lpparam);
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod(
                "android.os.UpdateEngine",
                lpparam.classLoader,
                "verifyPayloadMetadata",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("[BypassOTACheck] Bypassing android.os.UpdateEngine.verifyPayloadMetadata");
                        param.setResult(true); // 直接返回true，绕过校验
                    }
                }
        );

    }
}