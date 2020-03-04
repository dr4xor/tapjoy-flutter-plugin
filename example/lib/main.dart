import 'package:flutter/material.dart';
import 'dart:async';
import 'package:tapjoy/tapjoy.dart';
import 'package:tapjoy/tj_placement.dart';
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
    Tapjoy.setActivity();
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
              onPressed: () async {
                TJPlacement tjPlacement = await Tapjoy.getPlacement('mohamed',
                    onRequestSuccess: (placement) => placement.showContent(),
                    onRequestFailure: (placement, error) => print(
                        error.errorCode.toString() +
                            ' - ' +
                            error.errorMessage));
                tjPlacement.requestContent();
              },
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
