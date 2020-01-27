package com.ventaapps.tapjoy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJAdUnitConstants;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJCorePlacement;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyAuctionFlags;
import com.tapjoy.TapjoyConnectFlag;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;


public class TapjoyPlugin implements MethodCallHandler, TJPlacementListener {

    static MethodChannel channel;
    static Map<String, TJPlacement> placements = new HashMap<>();
    private final Activity activity;
    private TJPlacementListener placementListener = this;

    public static void registerWith(Registrar registrar) {
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
                result.success(null);
            else
                result.error(null, null, null);
        } else if (call.method.equals("connect")) {
            Hashtable<String, Object> connectFlags = new Hashtable<>();
            String tapjoyKey = call.argument("tapjoyKey");
            Tapjoy.connect(this.activity, tapjoyKey, connectFlags, new TJConnectListener() {
                @Override
                public void onConnectSuccess() {
                    TJPlacement p = new TJPlacement(activity, "MyPlacementName", placementListener);
                    channel.invokeMethod("connect", "success");
                }

                @Override
                public void onConnectFailure() {
                    channel.invokeMethod("connect", "fail");
                }
            });
        } else if (call.method.equals("getPlacement")) {
            String placementName = call.argument("placementName");
            TJPlacement placement = Tapjoy.getPlacement(placementName, placementListener);
            Map<String, String> mapRef = new HashMap<>();
            mapRef.put("Name", placement.getName());
            result.success(mapRef);
        } else if (call.method.equals("isContentAvailable")) {
            String placementName = call.argument("placementName");
            TJPlacement placement = Tapjoy.getPlacement(placementName, placementListener);
            boolean contentStatus = placement.isContentAvailable();
            Log.e("mohamed", String.valueOf(contentStatus));
            result.success(contentStatus);
        } else if (call.method.equals("setUserID")) {
            String userID = call.argument("userID");
            Tapjoy.setUserID(userID);
        } else if (call.method.equals("setGcmSender")) {
            String senderID = call.argument("senderID");
            Tapjoy.setGcmSender(senderID);
        } else {
            result.notImplemented();
        }
    }

    //  TJPlacementListener Methods
    @Override
    public void onRequestSuccess(TJPlacement tjPlacement) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onRequestSuccess", null);
            }
        });
    }

    @Override
    public void onRequestFailure(TJPlacement tjPlacement, final TJError tjError) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put("code", tjError.code);
                arguments.put("message", tjError.message);
                channel.invokeMethod("onRequestFailure", arguments);
            }
        });
    }

    @Override
    public void onContentReady(TJPlacement tjPlacement) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onContentReady", null);
            }
        });
    }

    @Override
    public void onContentShow(TJPlacement tjPlacement) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onContentShow", null);
            }
        });
    }

    @Override
    public void onContentDismiss(TJPlacement tjPlacement) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onContentDismiss", null);
            }
        });
    }

    @Override
    public void onPurchaseRequest(TJPlacement tjPlacement, final TJActionRequest tjActionRequest, final String s) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put("requestId", tjActionRequest.getRequestId());
                arguments.put("token", tjActionRequest.getToken());
                arguments.put("s", s);
                channel.invokeMethod("", arguments);
            }
        });
    }

    @Override
    public void onRewardRequest(final TJPlacement tjPlacement, final TJActionRequest tjActionRequest, final String s, final int i) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put("requestId", tjActionRequest.getRequestId());
                arguments.put("token", tjActionRequest.getToken());
                arguments.put("s", s);
                arguments.put("i", i);
                channel.invokeMethod("onRewardRequest", arguments);
            }
        });
    }

    @Override
    public void onClick(final TJPlacement tjPlacement) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                channel.invokeMethod("onClick", null);
            }
        });
    }
}
