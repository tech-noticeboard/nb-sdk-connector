package tech.noticeboard.nbsdkconnector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tech.noticeboard.nbsdkconnector.nbAppInstall.AppInstall;
import tech.noticeboard.nbsdkconnector.nbAppInstall.NBAppInstallListener;

/**
 * Created by Priyansh Srivastava on 30-Oct-17.
 */

public class NBHelperActivity extends AppCompatActivity implements NBAppInstallListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        installOrOpenApp();
    }


    private void installOrOpenApp() {

        try {
            //Check if package is installed
            PackageManager pm = getPackageManager();
            boolean isInstalled = isPackageInstalled(Constants.INSTALL_PACKAGE_NAME, pm);
            boolean isAppInstalledFirstTime = SharedPreferenceProvider.readIfAppInstalled(this);

            if (isInstalled && isAppInstalledFirstTime) {
                Intent i = new Intent();
                i.setAction(Constants.INSTALL_PACKAGE_ACTIVITY);
                startActivityForResult(i, 0);
                finish();
            } else {
                AppInstall appInstall = new AppInstall(this);
                appInstall.updateApp();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }


    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   installOrOpenApp();
                } else {
                    finish();
                }
            }
            default:
                finish();
        }
    }


    @Override
    public void close() {
        if(!isFinishing()) {
            finish();
        }
    }
}
