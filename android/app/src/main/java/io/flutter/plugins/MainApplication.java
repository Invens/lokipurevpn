package io.flutter.plugins;
import android.util.Log;
import android.app.Application;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;
import com.atom.core.exceptions.AtomValidationException;
import com.atom.core.models.AtomConfiguration;
import com.atom.core.models.AtomNotification;
import com.atom.sdk.android.AtomManager;
import com.example.lokipurevpn.R;
public class MainApplication extends Application {
    private static final String TAG = "ATOM_SDK_DEBUG";
    private static MainApplication instance;
    private AtomManager atomManager;

    public static MainApplication getInstance() {
        return instance;
    }

    public AtomManager getAtomManager() {
        return atomManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        String ATOM_SECRET_KEY = "1736508054082f41de9884d351b1f1e0ee939ce7"; // Use valid key

        if (!TextUtils.isEmpty(ATOM_SECRET_KEY)) {
            AtomConfiguration.Builder atomConfigBuilder = new AtomConfiguration.Builder(ATOM_SECRET_KEY);
            atomConfigBuilder.setVpnInterfaceName("Atom VPN");

            int NOTIFICATION_ID = 1001;
            AtomNotification.Builder atomNotificationBuilder = new AtomNotification.Builder(
                    NOTIFICATION_ID,
                    "VPN Connected",
                    "You are now secured",
                    R.mipmap.ic_launcher,
                    Color.BLUE
            );

            atomConfigBuilder.setNotification(atomNotificationBuilder.build());
            AtomConfiguration atomConfiguration = atomConfigBuilder.build();

            try {
                AtomManager.initialize(this, atomConfiguration, new AtomManager.InitializeCallback() {
                    @Override
                    public void onInitialized(AtomManager manager) {
                        atomManager = manager;
                        Log.d(TAG, "ATOM SDK Initialized Successfully");
                    }
                });
            } catch (AtomValidationException e) {
                Log.e(TAG, "ATOM SDK Initialization Failed: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Secret Key is Required");
            Toast.makeText(this, "Secret Key is Required", Toast.LENGTH_SHORT).show();
        }
    }
}

//package io.flutter.plugins;
//
//import android.app.Application;
//import android.graphics.Color;
//import android.text.TextUtils;
//import android.widget.Toast;
//import com.atom.core.exceptions.AtomValidationException;
//import com.atom.core.models.AtomConfiguration;
//import com.atom.core.models.AtomNotification;
//import com.atom.sdk.android.AtomManager;
//import com.example.lokipurevpn.R;
//
//public class MainApplication extends Application {
//    private static MainApplication instance;
//    private AtomManager atomManager;
//
//    public static MainApplication getInstance() {
//        return instance;
//    }
//
//    public AtomManager getAtomManager() {
//        return atomManager;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        instance = this;
//
//        // Put your ATOM SDK Secret Key here
//        String ATOM_SECRET_KEY = "1736508054082f41de9884d351b1f1e0ee939ce7"; // Replace with actual key
//
//        if (!TextUtils.isEmpty(ATOM_SECRET_KEY)) {
//            AtomConfiguration.Builder atomConfigBuilder = new AtomConfiguration.Builder(ATOM_SECRET_KEY);
//            atomConfigBuilder.setVpnInterfaceName("Atom VPN");
//
//            int NOTIFICATION_ID = 1001; // Define a proper integer ID
//            AtomNotification.Builder atomNotificationBuilder = new AtomNotification.Builder(
//                    NOTIFICATION_ID,
//                    "VPN Connected",
//                    "You are now secured",
//                    R.mipmap.ic_launcher,
//                    Color.BLUE
//            );
//
//
//            atomConfigBuilder.setNotification(atomNotificationBuilder.build());
//
//            AtomConfiguration atomConfiguration = atomConfigBuilder.build();
//            try {
//                AtomManager.initialize(this, atomConfiguration, new AtomManager.InitializeCallback() {
//                    @Override
//                    public void onInitialized(AtomManager manager) {
//                        atomManager = manager;
//                    }
//                });
//            } catch (AtomValidationException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(this, "Secret Key is Required", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
