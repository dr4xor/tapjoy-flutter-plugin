package com.ventaapps.tapjoy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformView;

///////////////////   The first experiment code /////////////////////////////////

//public class TJPlacementPlugin implements MethodCallHandler {
//
//    static MethodChannel channel;
//    private final Activity activity;
//
//
//    public static void registerWith(Registrar registrar) {
//        channel = new MethodChannel(registrar.messenger(), "tJPlacement");
//        channel.setMethodCallHandler(new TJPlacementPlugin(registrar.activity()));
//    }
//
//    private TJPlacementPlugin(Activity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    public void onMethodCall(MethodCall call, Result result) {
//        if (call.method.equals("isContentAvailable")) {
//            String placementName = call.argument("placementName");
//            TJPlacement placement = TapjoyPlugin.placements.get(placementName);
//            boolean contentStatus = placement.isContentAvailable();
//            result.success(contentStatus);
//        }
//    }
//}


///////////////////////   The second experiment code  //////////////////////////////

public class TJPlacementPlugin extends TJPlacement {
    private final MethodChannel channel;
    private final HashMap args;
    private final Context context;
    private Activity activity;
    private final String placementName;
    private final TJPlacementListener listener;


    TJPlacementPlugin(Context context, HashMap args, BinaryMessenger messenger, Activity activity, String placementName, TJPlacementListener listener) {
        super(context, placementName, listener);
        this.channel = new MethodChannel(messenger,
                "tJPlacement");
        this.activity = activity;
        this.args = args;
        this.context = context;
        this.placementName = placementName;
        this.listener = listener;

    }


}