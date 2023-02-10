package dev.pdf417censo.com;

import android.app.Application;

import com.microblink.blinkbarcode.MicroblinkSDK;
import com.microblink.blinkbarcode.intent.IntentDataTransferMode;

public final class CustomUIDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // obtain your licence at http://microblink.com/login or
        // contact us at http://help.microblink.com
        MicroblinkSDK.setLicenseFile("license.key", this);

        // use optimised way for transferring RecognizerBundle between activities, while ensuring
        // data does not get lost when Android restarts the scanning activity
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);
    }
}
