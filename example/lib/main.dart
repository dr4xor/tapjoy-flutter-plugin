import 'package:flutter/material.dart';
import 'package:tapjoy/models.dart';
import 'dart:async';
import 'package:tapjoy/tapjoy.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';

Future main() async {
  await DotEnv().load('.env');
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _tapjoyStatus = 'Initializing';
  var active;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    Tapjoy.setDebugEnabled(true);
    Tapjoy.connect(
      DotEnv().env['TAPJOY_KEY'],
      tapjoyConnectSuccess,
      () => setState(
        () {
          _tapjoyStatus = 'failed';
        },
      ),
    );
    if (!mounted) return;
  }

  void tapjoyConnectSuccess() {
    setState(() {
      _tapjoyStatus = 'connected';
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            RaisedButton(
              child: Text('is Content Available'),
              onPressed: () => TJPlacement('coinsscreen').isContentAvailable(),
            ),
            RaisedButton(
              child: Text('is Content Available'),
              onPressed: () =>
                  Tapjoy.getPlacement('coinsscreen', PlacementListener()),
            ),
            Center(
              child: Text('Tapjay Status = $_tapjoyStatus'),
            ),
          ],
        ),
      ),
    );
  }
}

class PlacementListener implements TJPlacementListener {
  @override
  void onClick(TJPlacement tjPlacement) {
    print('onClick');
  }

  @override
  void onContentDismiss(TJPlacement tjPlacement) {
    print('onContentDismiss');
  }

  @override
  void onContentReady(TJPlacement tjPlacement) {}

  @override
  void onContentShow(TJPlacement tjPlacement) {}

  @override
  void onPurchaseRequest(TJPlacement tjPlacement,
      TJActionRequest tjActionRequest, String productIds) {}

  @override
  void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {}

  @override
  void onRequestSuccess(TJPlacement tjPlacement) {}

  @override
  void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest,
      String itemId, int quantity) {}
}
