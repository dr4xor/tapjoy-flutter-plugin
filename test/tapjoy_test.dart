import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:tapjoy/tapjoy.dart';

void main() {
  const MethodChannel channel = MethodChannel('tapjoy');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Tapjoy.platformVersion, '42');
  });
}
