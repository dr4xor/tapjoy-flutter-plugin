import 'package:flutter/services.dart';
import './tapjoy.dart';

class TJPlacement {
  final TJPlacementListener listener;
  String placementName = '';
  TJPlacement(this.placementName, {this.listener});
  static const MethodChannel _channel = const MethodChannel('tjPlacement');

  Future<bool> isContentAvailable() async {
    bool contentStatus = await _channel.invokeMethod(
        'isContentAvailable', {'placementName': this.placementName});
    print('$contentStatus******************************************');
    return contentStatus;
  }
}

class TJError {
  final int errorCode;
  final String errorMessage;
  TJError({this.errorCode, this.errorMessage});
}

abstract class TJActionRequest {
  String getRequestId();
  String getToken();
  void completed();
  void cancelled();
  TJActionRequest({getRequestId(), getToken(), completed(), cancelled()});
}
