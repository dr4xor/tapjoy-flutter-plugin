import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class Tapjoy {
  static const MethodChannel _channel =
      const MethodChannel('tapjoy');
  static BuildContext context;
  static Function connectSuccess;
  static Function connectFail;

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<dynamic> myUtilsHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'connect':
        if(methodCall.arguments == "success") {
          connectSuccess();
        } else {
          connectFail();
        }
        break;
      default:
      // todo - throw not implemented
    }
  }
  // static Future connect(BuildContext _context, Function _connectSuccess, Function _connectFail) async {
  static Future connect(String tapjoyKey, Function _connectSuccess, Function _connectFail) async {
    // context = _context;
    connectSuccess = _connectSuccess;
    connectFail = _connectFail;
    _channel.setMethodCallHandler(myUtilsHandler);
    // final String version = await _channel.invokeMethod('connect', [context, tapjoyKey]);
    final String version = await _channel.invokeMethod('connect', {"tapjoyKey": tapjoyKey});
    return version;
  }
}
