package com.ventaapps.tapjoy;

import android.app.Activity;

import com.tapjoy.TJConnectListener;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import java.util.Hashtable;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class TJPlacementListenerPlugin implements MethodChannel.MethodCallHandler {
    static MethodChannel channel;
    private final Activity activity;

    public static void registerWith(PluginRegistry.Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "tJPlacementListener");
        channel.setMethodCallHandler(new TJPlacementListenerPlugin(registrar.activity()));
    }

    private TJPlacementListenerPlugin(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("connect")) {

        } else {
            result.notImplemented();
        }
    }
}
