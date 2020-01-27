import 'package:flutter/material.dart';
import 'dart:async';
import 'package:tapjoy/tapjoy.dart';
import 'package:tapjoy/tJPlacementListener.dart';
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
  String _platformVersion = 'Unknown';
  String _tapjoyStatus = 'Initializing';
  var active;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    Tapjoy.setDebugEnabled(true);
    Tapjoy.connect(
      DotEnv().env['TAPJOY_KEY'],
      // "pqRbeIEcSHqjTgpN3zuVEwECKLDGUq3y0sIKMQmqH5mv0SKukE4DO9cmG6f9",
      tapjoyConnectSuccess,
      () => setState(
        () {
          _tapjoyStatus = 'failed';
        },
      ),
    );

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
  }

  void tapjoyConnectSuccess() {
    setState(() {
      _tapjoyStatus = 'connected';
    });
    // Tapjoy.isConnected();
    // Tapjoy.getPlacement("mohamed", new TJPlacementListener());
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
              onPressed: () => Tapjoy.isContentAvailable(),
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
