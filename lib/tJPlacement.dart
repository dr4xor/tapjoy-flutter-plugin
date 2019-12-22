import 'dart:async';

import 'package:flutter/services.dart';

class TJPlacement {
  String placementName;
  static const MethodChannel _channel = const MethodChannel('tJPlacement');
  TJPlacement(this.placementName);

  // static void isContentAvailable() async {
  //   bool contentStatus = await _channel
  //       .invokeMethod('isContentAvailable', {'placementName': 'offerwall'});
  // }
}
