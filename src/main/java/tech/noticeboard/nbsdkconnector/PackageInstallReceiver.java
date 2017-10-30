package tech.noticeboard.nbsdkconnector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Priyansh Srivastava on 31-Oct-17.
 */

public class PackageInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // handle install event here
        if(null != intent) {
            if(null != intent.getData()) {
                if(intent.getData().toString().equals(Constants.INSTALL_PACKAGE_NAME_URI)) {
                    SharedPreferenceProvider.writeIfAppInstalled(context);
                }
            }
        }
    }
}
