package com.ventaapps.tapjoy;

import android.app.Activity;
import android.util.Log;

import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * TJPlacementPlugin
 */
public class TJPlacementPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
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
        if (call.method.equals("requestContent")) {
            try {
                TJPlacement placement = (TJPlacement) deserialize((byte[]) call.argument("placement"));
                placement.requestContent();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            result.notImplemented();
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
