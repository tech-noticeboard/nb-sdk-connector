package tech.noticeboard.nbsdkconnector;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import tech.noticeboard.nbsdkconnector.nbAppInstaller.AppInstaller;
import tech.noticeboard.nbsdkconnector.nbAppInstaller.NBAppInstallerListener;

/**
 * Created by Priyansh Srivastava on 30-Oct-17.
 */

public class NbSdkConnectorActivity extends AppCompatActivity implements NBAppInstallerListener {

    AppInstaller appInstaller;

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
            boolean isAppInstalledFirstTime;

            //Check the override flag.
            //If the override flag is set to true, that means that, we need to ignore the flag
            //which maintains if app is installed first time.
            //This we have done because, the app update logic in the SDK APP was breaking.
            //Hence, we manually install it here again.
            //This is a one time operation.
            if(!SharedPreferenceProvider.readIfAppInstalledOverride(this)) {
                isAppInstalledFirstTime = false;
                SharedPreferenceProvider.writeIfAppInstalledOverride(this);
            } else {
                isAppInstalledFirstTime = SharedPreferenceProvider.readIfAppInstalled(this);
            }

            if (isInstalled && isAppInstalledFirstTime) {

                Intent i = new Intent();
                i.setAction(Constants.INSTALL_PACKAGE_ACTIVITY);
                i.putExtra(Constants.LOGIN_KEY, getLoginData());
                i.putExtra(Constants.SDK_KEY, NbSdkHelper.readSdkKey(this));
                startActivityForResult(i, 0);
                finish();

            } else {

                appInstaller = new AppInstaller(this);
                appInstaller.updateApp();
            }
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            Toast.makeText(this, Constants.INVALID_SDK_LOGIN_OR_KEY, Toast.LENGTH_LONG).show();
            finish();
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


    private String getLoginData() throws NullPointerException {

        //Fetch the login data
        String loginData = "";
        if(null != getIntent()) {
            loginData = getIntent().getStringExtra(Constants.LOGIN_KEY);
            String check = loginData;
            if(!NbSdkHelper.validateEmail(check) &&
                    (null == (loginData = (NbSdkHelper.formatPhone(this, check))))) {
                throw new NullPointerException(Constants.INVALID_SDK_LOGIN_OR_KEY);
            }
        } else {
            throw new NullPointerException(Constants.INVALID_SDK_LOGIN_OR_KEY);
        }

        return loginData;
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
