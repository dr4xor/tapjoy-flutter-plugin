import 'dart:async';
import 'package:flutter/services.dart';
import 'package:tapjoy/models.dart';


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
    return await _channel.invokeMethod('isConnected');
  }

  static Future<TJPlacement> getPlacement(
      String placementName, TJPlacementListener listener) async {
    final result = await _channel.invokeMethod<Map<dynamic, dynamic>>(
        'getPlacement', {"placementName": placementName});
    final name = result['Name'] as String;
    // return new TJPlacement(placementName);
  }

  static Future<bool> isContentAvailable() async {
    bool contentStatus = await _channel
        .invokeMethod('isContentAvailable', {'placementName': 'mohamed'});
    print('$contentStatus******************************************');
    return contentStatus;
  }

  static void setUserID(String userID) async {
    await _channel.invokeMethod('setUserID', {'userID': userID});
  }

  static void setGcmSender(String senderID) async {
    await _channel.invokeMethod('setGcmSender', {"senderID": senderID});
  }
}

class ActionRequest implements TJActionRequest {
  MethodCall call;
  ActionRequest(this.call);
  @override
  String getToken() {
    return call.arguments('');
  }

  @override
  String getRequestId() {
    return call.arguments('');
  }

  @override
  void completed() {
    call.arguments('');
  }

  @override
  void cancelled() {
    call.arguments('');
  }
}

abstract class TJPlacementListener {
  Future<Null> _handle(MethodCall call) async {
//    TJPlacementListener
    if (call.method == 'onRequestSuccess') {
      onRequestSuccess(TJPlacement(listener: this));
    } else if (call.method == 'onRequestFailure') {
      onRequestFailure(
        TJPlacement(listener: this),
        TJError(
            errorCode: call.arguments('code'),
            errorMessage: call.arguments('message')),
      );
    } else if (call.method == 'onContentReady') {
      onContentReady(TJPlacement(listener: this));
    } else if (call.method == 'onContentShow') {
      onContentShow(TJPlacement(listener: this));
    } else if (call.method == 'onContentDismiss') {
      onContentDismiss(TJPlacement(listener: this));
    } else if (call.method == 'onPurchaseRequest') {
      onPurchaseRequest(TJPlacement(listener: this), ActionRequest(call), "");
    } else if (call.method == 'onRewardRequest') {
      onRewardRequest(TJPlacement(listener: this), ActionRequest(call), "", 12);
    } else if (call.method == 'onClick') {
      onClick(TJPlacement(listener: this));
    }
  }

  void onRequestSuccess(TJPlacement tjPlacement);
  void onRequestFailure(TJPlacement tjPlacement, TJError tjError);
  void onContentReady(TJPlacement tjPlacement);
  void onContentShow(TJPlacement tjPlacement);
  void onContentDismiss(TJPlacement tjPlacement);
  void onPurchaseRequest(
      TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s);
  void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest,
      String s, int i);
  void onClick(TJPlacement tjPlacement);
}
