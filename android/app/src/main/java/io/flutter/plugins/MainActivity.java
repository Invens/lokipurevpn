package io.flutter.plugins;
import android.os.Bundle;
import android.util.Log;
import com.atom.core.exceptions.AtomException;
import com.atom.core.models.ConnectionDetails;
import com.atom.core.models.Country;
import com.atom.core.models.Protocol;
import com.atom.sdk.android.AtomManager;
import com.atom.sdk.android.VPNCredentials;
import com.atom.sdk.android.VPNProperties;
import com.atom.sdk.android.data.callbacks.CollectionCallback;
import com.atom.sdk.android.listeners.ConnectionCallback;
import com.atom.sdk.android.listeners.VPNStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity implements VPNStateListener {
    private static final String CHANNEL = "vpn_channel";
    private static final String TAG = "ATOM_VPN_DEBUG";
    private AtomManager atomManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        atomManager = MainApplication.getInstance().getAtomManager();
        if (atomManager != null) {
            atomManager.addVPNStateListener(this);
        }

        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler((call, result) -> {
                    switch (call.method) {
                        case "getCountries":
                            getCountries(result);
                            break;
                        case "connectVPN":
                            String username = call.argument("username");
                            String password = call.argument("password");
                            String countryCode = call.argument("country");
                            connectVPN(username, password, countryCode, result);
                            break;
                        case "disconnectVPN":
                            disconnectVPN(result);
                            break;
                        default:
                            result.notImplemented();
                    }
                });
    }

    private void getCountries(MethodChannel.Result result) {
        if (atomManager == null) {
            Log.e(TAG, "❌ ATOM SDK not initialized");
            result.error("ERROR", "ATOM SDK not initialized", null);
            return;
        }

        atomManager.getCountries(new CollectionCallback<Country>() {
            @Override
            public void onSuccess(List<Country> countries) {
                List<Map<String, String>> countryList = new ArrayList<>();
                for (Country country : countries) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", country.getName());
                    map.put("code", country.getCountry());
                    countryList.add(map);
                }
                Log.d(TAG, "✅ Countries fetched: " + countryList.toString());
                result.success(countryList);
            }

            @Override
            public void onError(AtomException exception) {
                Log.e(TAG, "❌ Error fetching countries: " + exception.getMessage());
                result.error("ERROR", exception.getMessage(), null);
            }

            @Override
            public void onNetworkError(AtomException exception) {
                Log.e(TAG, "❌ Network Error fetching countries: " + exception.getMessage());
                result.error("ERROR", exception.getMessage(), null);
            }
        });
    }

    private void connectVPN(String username, String password, String countryCode, MethodChannel.Result result) {
        if (atomManager == null) {
            Log.e(TAG, "❌ ATOM SDK not initialized");
            result.error("ERROR", "ATOM SDK not initialized", null);
            return;
        }

        Log.d(TAG, "⚡ Attempting VPN connection for user: " + username + " Country: " + countryCode);

        Country selectedCountry = new Country();
        selectedCountry.setCountry(countryCode);

        Protocol selectedProtocol = new Protocol();
        selectedProtocol.setProtocol("UDP");

        try {
            VPNProperties.Builder vpnBuilder = new VPNProperties.Builder(selectedCountry, selectedProtocol);
            atomManager.setVPNCredentials(new VPNCredentials(username, password));

            atomManager.connect(this, vpnBuilder.build(), new ConnectionCallback() {
                @Override
                public void onConnected(ConnectionDetails details) {
                    Log.d(TAG, "✅ VPN Connected Successfully!");
                    result.success("Connected");
                }

                @Override
                public void onError(AtomException e) {
                    Log.e(TAG, "❌ VPN Connection Failed: " + e.getMessage());
                    result.error("ERROR", e.getMessage(), null);
                }

                @Override
                public void onRedialing(AtomException e) {
                    Log.w(TAG, "⚠ VPN Redialing: " + e.getMessage());
                }
            });

        } catch (AtomException e) {
            Log.e(TAG, "❌ Validation Error: " + e.getMessage());
            result.error("ERROR", e.getMessage(), null);
        }
    }

    private void disconnectVPN(MethodChannel.Result result) {
        if (atomManager == null) {
            Log.e(TAG, "❌ ATOM SDK not initialized");
            result.error("ERROR", "ATOM SDK not initialized", null);
            return;
        }

        atomManager.disconnect(this);
        Log.d(TAG, "✅ VPN Disconnected Successfully");
        result.success("Disconnected");
    }

    // VPN State Callbacks (Required for VPNStateListener)
    @Override
    public void onConnected(ConnectionDetails connectionDetails) {
        Log.d(TAG, "✅ VPN Connected");
    }

    @Override
    public void onDisconnected(ConnectionDetails connectionDetails) {
        Log.d(TAG, "✅ VPN Disconnected");
    }

    @Override
    public void onConnecting() {
        Log.d(TAG, "🔄 Connecting to VPN...");
    }

    @Override
    public void onDisconnecting() {
        Log.d(TAG, "🔄 Disconnecting from VPN...");
    }

    @Override
    public void onRedialing(AtomException exception) {
        Log.w(TAG, "⚠ VPN Redialing: " + exception.getMessage());
    }

    @Override
    public void onDialError(AtomException exception) {
        Log.e(TAG, "❌ VPN Connection Error: " + exception.getMessage());
    }
}


//
//public class MainActivity extends FlutterActivity {
//    private static final String CHANNEL = "vpn_channel";
//    private static final int VPN_PERMISSION_REQUEST_CODE = 100;
//
//    private AtomManager atomManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Correctly get the AtomManager instance
//        atomManager = MainApplication.getInstance().getAtomManager();
//
//        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL)
//                .setMethodCallHandler((call, result) -> {
//                    switch (call.method) {
//                        case "getCountries":
//                            getCountries(result);
//                            break;
//                        case "connectVPN":
//                            String username = call.argument("username");
//                            String password = call.argument("password");
//                            String countryCode = call.argument("country");
//                            connectVPN(username, password, countryCode, result);
//                            break;
//                        case "disconnectVPN":
//                            disconnectVPN(result);
//                            break;
//                        default:
//                            result.notImplemented();
//                    }
//                });
//    }
//
//    private void getCountries(MethodChannel.Result result) {
//        if (atomManager == null) {
//            result.error("ERROR", "ATOM SDK not initialized", null);
//            return;
//        }
//
//
//        atomManager.getCountries(new CollectionCallback<Country>() {
//            @Override
//            public void onSuccess(List<Country> countries) {
//                List<Map<String, String>> countryList = new ArrayList<>();
//                for (Country country : countries) {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("name", country.getName());
//                    map.put("code", country.getCountry());
//                    countryList.add(map);
//                }
//                result.success(countryList);
//            }
//
//            @Override
//            public void onError(AtomException exception) {
//                result.error("ERROR", exception.getMessage(), null);
//            }
//
//            @Override
//            public void onNetworkError(AtomException exception) {
//                result.error("ERROR", exception.getMessage(), null);
//            }
//        });
//    }
//
//    private void connectVPN(String username, String password, String countryCode, MethodChannel.Result result) {
//        if (atomManager == null) {
//            result.error("ERROR", "ATOM SDK not initialized", null);
//            return;
//        }
//
//        // Request VPN permission before connecting
//        Intent intent = VpnService.prepare(this);
//        if (intent != null) {
//            startActivityForResult(intent, VPN_PERMISSION_REQUEST_CODE);
//            result.success("VPN Permission Required. Please Allow.");
//            return;
//        }
//
//        Country selectedCountry = new Country();
//        selectedCountry.setCountry(countryCode);
//
//        Protocol selectedProtocol = new Protocol();
//        selectedProtocol.setProtocol("IKEV"); // Use IKEV instead of UDP for better compatibility
//
//        try {
//            VPNProperties.Builder vpnBuilder = new VPNProperties.Builder(selectedCountry, selectedProtocol);
//            atomManager.setVPNCredentials(new VPNCredentials(username, password));
//
//            // Bind IKEV State Service (IMPORTANT)
//            atomManager.bindIKEVStateService(this);
//
//            // Start VPN Connection
//            atomManager.connect(this, vpnBuilder.build());
//            result.success("Connecting...");
//        } catch (AtomValidationException e) {
//            result.error("ERROR", e.getMessage(), null);
//        }
//    }
//
//    // Handle VPN permission response
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == VPN_PERMISSION_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // Permission granted, try reconnecting
//                vpnChannel.invokeMethod("vpnPermissionGranted", true);
//            } else {
//                vpnChannel.invokeMethod("vpnPermissionGranted", false);
//                Toast.makeText(this, "VPN permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }