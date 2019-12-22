package com.ventaapps.tapjoy;

import android.app.Activity;
import android.util.Log;

import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * TJPlacementPlugin
 */
public class TJPlacementPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    static MethodChannel channel;
    private final Activity activity;

    public static void registerWith(Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "tJPlacement");
        channel.setMethodCallHandler(new TJPlacementPlugin(registrar.activity()));
    }

    private TJPlacementPlugin(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
//        if (call.method.equals("requestContent")) {

//            if(placement != null){
//                placement.requestContent();
//        }
        if (call.method.equals("isContentAvailable")) {
            String placementName = call.argument("placementName");
            TJPlacement placement = TapjoyPlugin.placements.get(placementName);
            boolean contentStatus = placement.isContentAvailable();
            result.success(contentStatus);
        }
    }
}
