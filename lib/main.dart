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
  List<Map<String, String>> countries = [];
  String? selectedCountry;

  @override
  void initState() {
    super.initState();
    getCountries();
  }

  // ✅ Fetch countries
  Future<void> getCountries() async {
    try {
      final List<dynamic> result = await platform.invokeMethod('getCountries');
      setState(() {
        countries = List<Map<String, String>>.from(result.map((e) => Map<String, String>.from(e)));
        selectedCountry = countries.isNotEmpty ? countries[0]['code'] : null;
      });
    } on PlatformException catch (e) {
      print("Failed to get countries: ${e.message}");
    }
  }

  // ✅ Connect to VPN
  Future<void> connectVPN() async {
    if (selectedCountry == null) {
      print("No country selected");
      return;
    }
    try {
      final String result = await platform.invokeMethod('connectVPN', {
        "username": "partner9560s14063958",
        "password": "Loginamd@321",
        "country": selectedCountry
      });
      setState(() {
        vpnStatus = result;
      });
    } on PlatformException catch (e) {
      print("Failed to connect VPN: ${e.message}");
    }
  }

  // ✅ Disconnect VPN
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
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text("VPN Status: $vpnStatus", style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
              SizedBox(height: 20),

              // ✅ Country Selection Dropdown
              countries.isNotEmpty
                  ? DropdownButton<String>(
                value: selectedCountry,
                items: countries.map((country) {
                  return DropdownMenuItem<String>(
                    value: country['code'],
                    child: Text(country['name']!),
                  );
                }).toList(),
                onChanged: (value) {
                  setState(() {
                    selectedCountry = value;
                  });
                },
              )
                  : CircularProgressIndicator(),

              SizedBox(height: 20),

              ElevatedButton(onPressed: connectVPN, child: Text("Connect VPN")),
              SizedBox(height: 10),
              ElevatedButton(onPressed: disconnectVPN, child: Text("Disconnect VPN")),
            ],
          ),
        ),
      ),
    );
  }
}
