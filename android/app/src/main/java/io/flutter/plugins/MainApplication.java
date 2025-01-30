package io.flutter.plugins;

import android.app.Application;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;
import com.atom.core.models.AtomConfiguration;
import com.atom.core.models.AtomNotification;
import com.atom.sdk.android.AtomManager;

public class MainApplication extends Application {

    private AtomManager atomManager;
    private final int NOTIFICATION_ID = 1001;

    @Override
    public void onCreate() {
        super.onCreate();
        String ATOM_SECRET_KEY = "1736508054082f41de9884d351b1f1e0ee939ce7"; // Replace with actual key

        if (!TextUtils.isEmpty(ATOM_SECRET_KEY)) {
            AtomConfiguration.Builder atomConfigBuilder = new AtomConfiguration.Builder(ATOM_SECRET_KEY);
            atomConfigBuilder.setVpnInterfaceName("Atom VPN");

            AtomNotification.Builder atomNotificationBuilder = new AtomNotification.Builder(
                    NOTIFICATION_ID, "VPN Connected", "You are now secured", R.drawable.ic_launcher, Color.BLUE);
            atomConfigBuilder.setNotification(atomNotificationBuilder.build());

            AtomConfiguration atomConfiguration = atomConfigBuilder.build();
            AtomManager.initialize(this, atomConfiguration, new AtomManager.InitializeCallback() {
                @Override
                public void onInitialized(AtomManager manager) {
                    atomManager = manager;
                }
            });
        } else {
            Toast.makeText(this, "Secret Key is Required", Toast.LENGTH_SHORT).show();
        }
    }

    public AtomManager getAtomManager() {
        return atomManager;
    }
}
