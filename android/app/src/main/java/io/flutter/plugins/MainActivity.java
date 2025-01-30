package io.flutter.plugins;
import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import android.os.Bundle;
import android.widget.Toast;
import com.atom.sdk.android.AtomManager;
import com.atom.core.models.VPNProperties;
import com.atom.core.models.Country;
import com.atom.core.models.Protocol;
import java.util.List;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "vpn_channel";
    private AtomManager atomManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        atomManager = ((MainApplication) getApplication()).getAtomManager();

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
            result.error("ERROR", "ATOM SDK not initialized", null);
            return;
        }

        atomManager.getCountries(new AtomManager.CollectionCallback<Country>() {
            @Override
            public void onSuccess(List<Country> countries) {
                result.success(countries);
            }

            @Override
            public void onError(Exception exception) {
                result.error("ERROR", exception.getMessage(), null);
            }
        });
    }

    private void connectVPN(String username, String password, String countryCode, MethodChannel.Result result) {
        if (atomManager == null) {
            result.error("ERROR", "ATOM SDK not initialized", null);
            return;
        }

        VPNProperties.Builder vpnBuilder = new VPNProperties.Builder(new Country(countryCode), new Protocol("TCP"));
        atomManager.setVPNCredentials(username, password);
        atomManager.connect(this, vpnBuilder.build());
        result.success("Connecting...");
    }

    private void disconnectVPN(MethodChannel.Result result) {
        if (atomManager == null) {
            result.error("ERROR", "ATOM SDK not initialized", null);
            return;
        }

        atomManager.disconnect(this);
        result.success("Disconnected");
    }
}
