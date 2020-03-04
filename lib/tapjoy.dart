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

  static void actionComplete(String actionID) async {
    await _channel.invokeMethod('actionComplete', {'actionID': actionID});
  }

  static void addUserTag(String tag) async {
    await _channel.invokeMethod('addUserTag', {'tag': tag});
  }

  static Future<bool> belowConsentAge(bool isBelowConsentAge) async {
    bool belowConsentAge = await _channel.invokeMethod(
        'belowConsentAge', {'belowConsentAge': isBelowConsentAge});
    return belowConsentAge;
  }

  static void clearUserTags() async {
    await _channel.invokeMethod('clearUserTags');
  }

  static void endSession() async {
    await _channel.invokeMethod('endSession');
  }

  static Future<String> getSupportURL({String supportUrl}) async {
    String supportURL;
    if (supportUrl == null) {
      supportURL = await _channel.invokeMethod('getSupportURL');
    } else {
      supportURL = await _channel
          .invokeMethod('getSupportURL', {'supportURL': supportUrl});
    }
    return supportURL;
  }

  static Future<String> getUserToken() async {
    String userToken = await _channel.invokeMethod('getUserToken');
    return userToken;
  }

  static Future<bool> isLimitedConnected() async {
    bool isLimitedConnected = await _channel.invokeMethod('isLimitedConnected');
    return isLimitedConnected;
  }

  static Future<bool> isPushNotificationDisabled() async {
    bool isPushNotificationDisabled =
        await _channel.invokeMethod('isPushNotificationDisabled');
    return isPushNotificationDisabled;
  }

  static void loadSharedLibrary() async {
    await _channel.invokeMethod('loadSharedLibrary');
  }

  static void onActivityStart() async {
    await _channel.invokeMethod('onActivityStart');
  }

  static void onActivityStop() async {
    await _channel.invokeMethod('onActivityStop');
  }

  static void removeUserTag(String userTag) async {
    await _channel.invokeMethod('removeUserTag', {'userTag': userTag});
  }

  static void setAppDataVersion(String dataVersion) async {
    await _channel
        .invokeMethod('setAppDataVersion', {'dataVersion': dataVersion});
  }

  static Future<String> getVersion() async {
    String version = await _channel.invokeMethod('getVersion');
    return version;
  }

  static void setDeviceToken(String deviceToken) async {
    await _channel.invokeMethod('setDeviceToken', {'deviceToken': deviceToken});
  }

  static void setPushNotificationDisabled(bool disable) async {
    await _channel
        .invokeMethod('setPushNotificationDisabled', {'disable': disable});
  }

  static void setUserConsent(String consent) async {
    await _channel.invokeMethod('setUserConsent', {'consent': consent});
  }

  static void setUserCohortVariable(int cohortIndex, String cohortValue) async {
    await _channel.invokeMethod('setUserCohortVariable',
        {'cohortIndex': cohortIndex, 'cohortValue': cohortValue});
  }

  static void subjectToGDPR(bool gdprApplicable) async {
    await _channel
        .invokeMethod('subjectToGDPR', {'gdprApplicable': gdprApplicable});
  }

  static void startSession() async {
    await _channel.invokeMethod('startSession');
  }

  static void setUserLevel(int userLevel) async {
    await _channel.invokeMethod('setUserLevel', {'userLevel': userLevel});
  }
}
