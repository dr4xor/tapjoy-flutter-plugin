import 'package:flutter/services.dart';
import './tapjoy.dart';
import './tj_actionRequest.dart';
import './tj_error.dart';

typedef void OnRequestSuccess(TJPlacement tjPlacement);
typedef void OnRequestFailure(TJPlacement tjPlacement, TJError tjError);
typedef void OnContentReady(TJPlacement tjPlacement);
typedef void OnContentShow(TJPlacement tjPlacement);
typedef void OnContentDismiss(TJPlacement tjPlacement);
typedef void OnPurchaseRequest(TJPlacement tjPlacement,
    TJActionRequest tjActionRequest, String productIds);
typedef void OnRewardRequest(TJPlacement tjPlacement,
    TJActionRequest tjActionRequest, String itemId, int quantity);
typedef void OnClick(TJPlacement tjPlacement);

class TJPlacement {
  String placementName = '';
  static MethodChannel _channel;
  OnRequestSuccess onRequestSuccess;
  OnRequestFailure onRequestFailure;
  OnContentReady onContentReady;
  OnContentShow onContentShow;
  OnContentDismiss onContentDismiss;
  OnPurchaseRequest onPurchaseRequest;
  OnRewardRequest onRewardRequest;
  OnClick onClick;

  TJPlacement(this.placementName,
      {this.onRequestSuccess,
      this.onRequestFailure,
      this.onContentReady,
      this.onContentShow,
      this.onPurchaseRequest,
      this.onRewardRequest,
      this.onClick}) {
    _channel = MethodChannel("TJPlacement_" + this.placementName);
    _channel.setMethodCallHandler(this._handle);
  }

  Future<Null> _handle(MethodCall call) async {
    if (call.method == 'onRequestSuccess') {
      onRequestSuccess(this);
    } else if (call.method == 'onRequestFailure') {
      onRequestFailure(
        this,
        TJError(
            errorCode: call.arguments('code'),
            errorMessage: call.arguments('message')),
      );
    } else if (call.method == 'onContentReady') {
      if (onContentReady != null) onContentReady(this);
    } else if (call.method == 'onContentShow') {
      onContentShow(this);
    } else if (call.method == 'onContentDismiss') {
      onContentDismiss(this);
    } else if (call.method == 'onPurchaseRequest') {
      String productId = call.arguments('productId');
      onPurchaseRequest(this, ActionRequest(call), productId);
    } else if (call.method == 'onRewardRequest') {
      String itemId = call.arguments('itemId');
      int quantity = call.arguments('quantity');
      onRewardRequest(this, ActionRequest(call), itemId, quantity);
    } else if (call.method == 'onClick') {
      onClick(this);
    }
  }

  Future<bool> isContentAvailable() async {
    bool contentStatus = await _channel.invokeMethod('isContentAvailable');
    return contentStatus;
  }

  Future<bool> isContentReady() async {
    bool contentStatus = await _channel
        .invokeMethod('isContentReady', {'placementName': this.placementName});
    return contentStatus;
  }

  Future<void> requestContent() async {
    if (await Tapjoy.isConnected()) {
      void contentStatus = await _channel.invokeMethod(
          'requestContent', {'placementName': this.placementName});
      return contentStatus;
    } else {
      print('content not ready');
    }
  }

  Future<void> showContent() async {
    if (await this.isContentAvailable() || await this.isContentReady()) {
      void contentStatus = await _channel
          .invokeMethod('showContent', {'placementName': this.placementName});
      return contentStatus;
    } else {
      print('no content to show, or it has not yet downloaded');
    }
  }
}

class ActionRequest implements TJActionRequest {
  MethodCall call;
  ActionRequest(this.call);
  @override
  String getToken() {
    return call.arguments('token');
  }

  @override
  String getRequestId() {
    return call.arguments('requestId');
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
