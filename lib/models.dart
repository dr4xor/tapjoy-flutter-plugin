import './tapjoy.dart';

class TJPlacement {
  final TJPlacementListener listener;
  TJPlacement({this.listener});
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
