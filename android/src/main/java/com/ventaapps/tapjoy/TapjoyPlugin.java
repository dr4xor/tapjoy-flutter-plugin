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

/**
 * TapjoyPlugin
 */
public class TapjoyPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    static MethodChannel channel;
    static Map<String, TJPlacement> placements = new HashMap<>();
    private final Activity activity;

    public static void registerWith(Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "tapjoy");
        channel.setMethodCallHandler(new TapjoyPlugin(registrar.activity()));
    }

    private TapjoyPlugin(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("setDebugEnabled")) {
            boolean isDebug = call.argument("isDebug");
            Tapjoy.setDebugEnabled(isDebug);
        } else if (call.method.equals("setActivity")) {
            Tapjoy.setActivity(this.activity);
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
                    channel.invokeMethod("connect", "success");
                }

                @Override
                public void onConnectFailure() {
                    channel.invokeMethod("connect", "fail");
                }
            });
        } else if (call.method.equals("getPlacement")) {
            TJPlacementListener placementListener = new TJPlacementListener() {
                @Override
                public void onRequestSuccess(TJPlacement tjPlacement) {
                    channel.invokeMethod("placementRequestSuccess", tjPlacement.getName());
                }

                @Override
                public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {

                }

                @Override
                public void onContentReady(TJPlacement tjPlacement) {
                }

                @Override
                public void onContentShow(TJPlacement tjPlacement) {

                }

                @Override
                public void onContentDismiss(TJPlacement tjPlacement) {

                }

                @Override
                public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {

                }

                @Override
                public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {

                }

                @Override
                public void onClick(TJPlacement tjPlacement) {

                }
            };
            String placementName = call.argument("placementName");
//      TJPlacementListener placementListener = call.argument("listener");
            TJPlacement placement = Tapjoy.getPlacement(placementName, placementListener);
            this.placements.put(placementName, placement);
            Map<String, String> mapRef = new HashMap<>();
            mapRef.put("Name", placement.getName());
            result.success(mapRef);
        } else if (call.method.equals("isContentAvailable")) {
            String placementName = call.argument("placementName");
//            TJPlacement placement = TapjoyPlugin.placements.get(placementName);
//            boolean contentStatus = placement.isContentAvailable();
            TJPlacementListener placementListener = new TJPlacementListener() {
                @Override
                public void onRequestSuccess(TJPlacement tjPlacement) {
//                    channel.invokeMethod("placementRequestSuccess", tjPlacement.getName());
                }

                @Override
                public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {

                }

                @Override
                public void onContentReady(TJPlacement tjPlacement) {

                }

                @Override
                public void onContentShow(TJPlacement tjPlacement) {

                }

                @Override
                public void onContentDismiss(TJPlacement tjPlacement) {

                }

                @Override
                public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {

                }

                @Override
                public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {

                }

                @Override
                public void onClick(TJPlacement tjPlacement) {

                }
            };
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
}
