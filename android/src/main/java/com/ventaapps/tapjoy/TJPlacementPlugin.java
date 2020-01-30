package com.ventaapps.tapjoy;

import android.app.Activity;
import android.util.Log;

import com.tapjoy.TJPlacement;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


public class TJPlacementPlugin implements MethodChannel.MethodCallHandler {
    private final MethodChannel channel;
    private Activity activity;


    TJPlacementPlugin(Activity activity, MethodChannel channel) {
        this.channel = channel;
        this.activity = activity;

    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("isContentAvailable")) {
            String placementName = call.argument("placementName");
            if (TapjoyPlugin.placements.containsKey(placementName)) {
                TJPlacement placement = TapjoyPlugin.placements.get(placementName);
                boolean outCome = placement.isContentAvailable();
            } else {
                result.success(false);
            }
        } else {
            result.notImplemented();
        }
    }
}