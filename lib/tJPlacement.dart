import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class TJPlacement {
  Uint8List platformPlacement;
  static const MethodChannel _channel = const MethodChannel('tJPlacement');
  TJPlacement (this.platformPlacement);

  void requestContent() async {
    await _channel.invokeMethod('requestContent', {'placement': platformPlacement});
  }
}
