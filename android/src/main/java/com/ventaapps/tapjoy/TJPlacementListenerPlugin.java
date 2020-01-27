package com.ventaapps.tapjoy;

import android.app.Activity;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;

import java.util.Hashtable;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class TJPlacementListenerPlugin implements MethodCallHandler, TJPlacementListener {
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


    //   TJPlacementListener Methods.

    @Override
    public void onRequestSuccess(TJPlacement tjPlacement) {
        TJPlacementListener placementListener = this;
        TJPlacement p = new TJPlacement(this.activity, "MyPlacementName", placementListener);
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
}
