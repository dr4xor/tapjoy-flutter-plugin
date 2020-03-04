import 'dart:async';
import 'package:flutter/services.dart';
import 'package:tapjoy/tj_placement.dart';

class Tapjoy {
  static const MethodChannel _channel = const MethodChannel('tapjoy');

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
    bool result = await _channel.invokeMethod('isConnected');
    print(result);
    return result;
  }

  static Future<TJPlacement> getPlacement(String placementName,
      {OnRequestSuccess onRequestSuccess,
      OnRequestFailure onRequestFailure,
      OnContentReady onContentReady,
      OnContentShow onContentShow,
      OnContentDismiss onContentDismiss,
      OnPurchaseRequest onPurchaseRequest,
      OnRewardRequest onRewardRequest,
      OnClick onClick}) async {
    TJPlacement tjPlacement = new TJPlacement(placementName,
        onRequestSuccess: onRequestSuccess,
        onRequestFailure: onRequestFailure,
        onContentReady: onContentReady,
        onContentShow: onContentShow,
        onPurchaseRequest: onPurchaseRequest,
        onRewardRequest: onRewardRequest,
        onClick: onClick);
    final result = await _channel
        .invokeMethod('getPlacement', {"placementName": placementName});
    return tjPlacement;
  }

  static void setActivity() async {
    await _channel.invokeMethod('setActivity');
  }

  static void setUserID(String userID) async {
    await _channel.invokeMethod('setUserID', {'userID': userID});
  }

  static void setGcmSender(String senderID) async {
    await _channel.invokeMethod('setGcmSender', {"senderID": senderID});
  }
}

// class ActionRequest implements TJActionRequest {
//   MethodCall call;
//   ActionRequest(this.call);
//   @override
//   String getToken() {
//     return call.arguments('token');
//   }

//   @override
//   String getRequestId() {
//     return call.arguments('requestId');
//   }

//   @override
//   void completed() {
//     call.arguments('');
//   }

//   @override
//   void cancelled() {
//     call.arguments('');
//   }
// }
