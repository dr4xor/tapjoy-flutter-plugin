package com.ventaapps.tapjoy;

import android.app.Activity;

import com.tapjoy.TJConnectListener;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import java.util.Hashtable;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** TJPlacementPlugin */
public class TJPlacementPlugin implements MethodCallHandler {
  /** Plugin registration. */
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
 if (call.method.equals("connect")) {
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
