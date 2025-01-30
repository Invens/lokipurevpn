import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(VPNApp());
}

class VPNApp extends StatefulWidget {
  @override
  _VPNAppState createState() => _VPNAppState();
}

class _VPNAppState extends State<VPNApp> {
  static const platform = MethodChannel('vpn_channel');
  String vpnStatus = "Disconnected";

  Future<void> connectVPN() async {
    try {
      final String result = await platform.invokeMethod('connectVPN', {
        "username": "partner9560s14063958",
        "password": "Loginamd@321",
        "country": "US"
      });
      setState(() {
        vpnStatus = result;
      });
    } on PlatformException catch (e) {
      print("Failed to connect VPN: ${e.message}");
    }
  }

  Future<void> disconnectVPN() async {
    try {
      final String result = await platform.invokeMethod('disconnectVPN');
      setState(() {
        vpnStatus = result;
      });
    } on PlatformException catch (e) {
      print("Failed to disconnect VPN: ${e.message}");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text("Flutter VPN")),
        body: Center(
          child: Column(
            children: [
              Text("VPN Status: $vpnStatus"),
              ElevatedButton(onPressed: connectVPN, child: Text("Connect VPN")),
              ElevatedButton(onPressed: disconnectVPN, child: Text("Disconnect VPN")),
            ],
          ),
        ),
      ),
    );
  }
}
