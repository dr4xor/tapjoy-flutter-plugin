package com.ventaapps.tapjoy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.tapjoy.TJAdUnitConstants;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

/** TapjoyPlugin */
public class TapjoyPlugin implements MethodCallHandler {
  /** Plugin registration. */
  static MethodChannel channel;
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
      if(Tapjoy.isConnected())
        result.success(null);
      else
        result.error(null,null,null);
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
      String placementName = call.argument("placementName");
      TJPlacementListener listener = call.argument("listener");
      TJPlacement getPlacement = Tapjoy.getPlacement(placementName, listener);
      Map<String, String> mapData = new HashMap<>();
      try {
        Field f = getPlacement.getClass().getDeclaredField("b");
        f.setAccessible(true);
        result.success(new String[]{mapData.put("pushID", getPlacement.pushId),
                                    mapData.put("getGUID", getPlacement.getGUID()),
                                    mapData.put("getName", getPlacement.getName()),});
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    } else if (call.method.equals("setUserID")) {
      String userID = call.argument("userID");
      Tapjoy.setUserID(userID);
    } else if (call.method.equals("setGcmSender")) {
      String senderID = call.argument("senderID");
      Tapjoy.setGcmSender(senderID);
    }
    else {
      result.notImplemented();
    }
  }
}
