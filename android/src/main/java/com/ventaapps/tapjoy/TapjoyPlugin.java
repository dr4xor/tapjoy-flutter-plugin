package com.ventaapps.tapjoy;

import android.app.Activity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.tapjoy.TJConnectListener;

import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class TapjoyPlugin implements MethodCallHandler {

    static MethodChannel channel;
    static private Registrar registrar;
    static Map<String, TJPlacement> placements = new HashMap<>();
    private final Activity activity;

    public static void registerWith(Registrar registrar) {
        TapjoyPlugin.registrar = registrar;
        channel = new MethodChannel(registrar.messenger(), "tapjoy");
        channel.setMethodCallHandler(new TapjoyPlugin(registrar.activity()));
    }

    private TapjoyPlugin(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("setDebugEnabled")) {
            boolean isDebug = call.argument("isDebug");
            Tapjoy.setDebugEnabled(isDebug);
            result.success(null);
        } else if (call.method.equals("setActivity")) {
            Tapjoy.setActivity(this.activity);
            result.success(null);
        } else if (call.method.equals("isConnected")) {
            if (Tapjoy.isConnected())
                result.success(true);
            else
                result.error(null, null, null);
        } else if (call.method.equals("connect")) {
            Hashtable<String, Object> connectFlags = new Hashtable<>();
            String tapjoyKey = call.argument("tapjoyKey");
            result.success(Tapjoy.connect(activity.getApplicationContext(), tapjoyKey, connectFlags, new TJConnectListener() {
                @Override
                public void onConnectSuccess() {
                    channel.invokeMethod("connect", "success");
                }

                @Override
                public void onConnectFailure() {
                    channel.invokeMethod("connect", "fail");
                }
            }));
//            Tapjoy.setActivity(this.activity);
        } else if (call.method.equals("getPlacement")) {
            String placementName = call.argument("placementName");
            TJPlacement placement = Tapjoy.getPlacement(placementName, new TJPlacementListenerPlugin(this.registrar, placementName));
            placements.put(placement.getName(), placement);
            result.success(true);
        } else if (call.method.equals("setUserID")) {
            String userID = call.argument("userID");
            Tapjoy.setUserID(userID);
            result.success(true);
        } else if (call.method.equals("setGcmSender")) {
            String senderID = call.argument("senderID");
            Tapjoy.setGcmSender(senderID);
            result.success(null);
        } else {
            result.notImplemented();
        }
    }

}
