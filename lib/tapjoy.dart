import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

import 'tJPlacement.dart';
import 'tJPlacementListener.dart';
import 'dart:convert';

class Tapjoy {
  static const MethodChannel _channel = const MethodChannel('tapjoy');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void connect(
      String tapjoyKey, Function connectSuccess, Function connectFail) {
    _channel.setMethodCallHandler((MethodCall methodCall) {
      switch (methodCall.method) {
        case 'connect':
          if (methodCall.arguments == "success") {
            connectSuccess();
          } else {
            connectFail();
          }
          break;
        default:
      }
      return;
    });
    _channel.invokeMethod('connect', {"tapjoyKey": tapjoyKey});
  }

  static void setDebugEnabled(bool isDebug) {
    _channel.invokeMethod('setDebugEnabled', {"isDebug": isDebug});
  }

  static Future<bool> isConnected() async {
    return _channel.invokeMethod('isConnected');
  }

  static Future<TJPlacement> getPlacement(
      String placementName, TJPlacementListener listener) async {
    // TJPlacement tjPlacement = await _channel.invokeMethod<TJPlacement>(
    //     'getPlacement', {"placementName": placementName}, "listener": listener});
    final result = await _channel.invokeMethod<Map<dynamic, dynamic>>(
        'getPlacement', {"placementName": placementName});
    // final gUID = result['GUID'] as String;
    // final id = result['Id'] as String;
    // final name = result['Name'] as String;
    // final videoListener = result['VideoListener'];
    // final corePlacement = result['CorePlacement'];
    return new TJPlacement(result['placement']);
  }

  static void setUserID(String userID) async {
    await _channel.invokeMethod('setUserID', {'userID': userID});
  }

  static void setGcmSender(String senderID) async {
    await _channel.invokeMethod('setGcmSender', {"senderID": senderID});
  }
}
