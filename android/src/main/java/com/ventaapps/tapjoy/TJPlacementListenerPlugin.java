package com.ventaapps.tapjoy;

import android.app.Activity;
import android.content.Context;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.TJPlacementVideoListener;
import com.tapjoy.Tapjoy;

import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.BinaryMessenger;

import java.util.HashMap;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class TJPlacementListenerPlugin implements MethodChannel.MethodCallHandler, TJPlacementListener {
    static MethodChannel channel;
    private Activity activity;
    private String placementName;

    TJPlacementListenerPlugin(Activity activity, BinaryMessenger messenger, String placementName) {
        this.activity = activity;
        this.placementName = placementName;
        channel = new MethodChannel(messenger, "TJPlacement_" + placementName);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {

//        tjPlacement.setAuctionData();
//        tjPlacement.getListener();
//        tjPlacement.getVideoListener();

        if (TapjoyPlugin.placements.containsKey(this.placementName)) {
            TJPlacement placement = TapjoyPlugin.placements.get(placementName);
            if (call.method.equals("isContentReady")) {
                result.success(placement.isContentReady());
            } else if (call.method.equals("isContentAvailable")) {
                result.success(placement.isContentAvailable());
            } else if (call.method.equals("requestContent")) {
                placement.requestContent();
            } else if (call.method.equals("showContent")) {
                placement.showContent();
            } else if (call.method.equals("setMediationName")) {
                String mediationName = call.argument("mediationName");
                placement.setMediationName(mediationName);
            } else if (call.method.equals("setMediationId")) {
                String mediationId = call.argument("mediationId");
                placement.setMediationId(mediationId);
            } else if (call.method.equals("setAdapterVersion")) {
                String adapterVersion = call.argument("adapterVersion");
                placement.setAdapterVersion(adapterVersion);
            } else if (call.method.equals("isLimited")) {
                result.success(placement.isLimited());
            } else if (call.method.equals("getName")) {
                result.success(placement.getName());
            } else if (call.method.equals("getGUID")) {
                result.success(placement.getGUID());
            } else if (call.method.equals("setVideoListener")) {
                placement.setVideoListener(new TJPlacementVideoListener() {
                    @Override
                    public void onVideoStart(TJPlacement tjPlacement) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                channel.invokeMethod("onVideoStart", null);
                            }
                        });
                    }

                    @Override
                    public void onVideoError(TJPlacement tjPlacement, String s) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                channel.invokeMethod("onVideoError", null);
                            }
                        });
                    }

                    @Override
                    public void onVideoComplete(TJPlacement tjPlacement) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                channel.invokeMethod("onVideoComplete", null);
                            }
                        });
                    }
                });
            } else {
                result.notImplemented();
            }
        } else {
            result.success(false);
        }
    }

    @Override
    public void onRequestSuccess(TJPlacement tjPlacement) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onRequestSuccess", null);
            }
        });
    }

    @Override
    public void onRequestFailure(TJPlacement tjPlacement, final TJError tjError) {
        this.activity.runOnUiThread(new Runnable() {
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
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onContentReady", null);
            }
        });
    }

    @Override
    public void onContentShow(TJPlacement tjPlacement) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onContentShow", null);
            }
        });
    }

    @Override
    public void onContentDismiss(TJPlacement tjPlacement) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channel.invokeMethod("onContentDismiss", null);
            }
        });
    }

    @Override
    public void onPurchaseRequest(TJPlacement tjPlacement, final TJActionRequest tjActionRequest, final String productId) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put("requestId", tjActionRequest.getRequestId());
                arguments.put("token", tjActionRequest.getToken());
                arguments.put("productId", productId);
                channel.invokeMethod("", arguments);
            }
        });
    }

    @Override
    public void onRewardRequest(final TJPlacement tjPlacement, final TJActionRequest tjActionRequest, final String itemId, final int quantity) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put("requestId", tjActionRequest.getRequestId());
                arguments.put("token", tjActionRequest.getToken());
                arguments.put("itemId", itemId);
                arguments.put("quantity", quantity);
                channel.invokeMethod("onRewardRequest", arguments);
            }
        });
    }

    @Override
    public void onClick(final TJPlacement tjPlacement) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                channel.invokeMethod("onClick", null);
            }
        });
    }
}
