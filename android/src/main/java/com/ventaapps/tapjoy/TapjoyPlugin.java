package com.ventaapps.tapjoy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.tapjoy.TJConnectListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

import java.util.Hashtable;

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
      Log.d("Sab7", "onMethodCall: " + android.os.Build.VERSION.RELEASE);
      result.success("Android " + android.os.Build.VERSION.RELEASE);
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
    } else {
      result.notImplemented();
    }
  }
}
