import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:tapjoy/tapjoy.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _tapjoyStatus = 'Not connected yet';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    String tapjoyStatus;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Tapjoy.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    try {
      // await Tapjoy.connect(context, () => tapjoyStatus = 'connnect',
      await Tapjoy.connect(TAPJOY_KEY, () => tapjoyStatus = 'connnect',
          () => tapjoyStatus = 'failed');
    } catch (e) {
      print(e.toString()+'//\******************************************************');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _tapjoyStatus = tapjoyStatus;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text(
              'Running on: $_platformVersion\n Tapjay Status = $_tapjoyStatus'),
        ),
      ),
    );
  }
}
