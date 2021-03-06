package tech.noticeboard.nbsdkconnector.nbAppInstaller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

import tech.noticeboard.nbsdkconnector.BuildConfig;
import tech.noticeboard.nbsdkconnector.Constants;
import tech.noticeboard.nbsdkconnector.R;
import tech.noticeboard.nbsdkconnector.nbAppInstaller.apkDownloader.ApkDownloader;
import tech.noticeboard.nbsdkconnector.nbAppInstaller.apkDownloader.ApkDownloaderListener;

import static tech.noticeboard.nbsdkconnector.Constants.PERMISSION_REQUEST_READ_STORAGE;


/**
 * Created by Priyansh Srivastava on 29-Oct-17.
 */
public class AppInstaller implements ApkDownloaderListener {

    private Context context;
    private NBAppInstallerListener listener;

    public AppInstaller(Context context) throws RuntimeException {
        this.context = context;
        if(context instanceof NBAppInstallerListener) {
            this.listener = (NBAppInstallerListener) context;
        } else {
            throw new RuntimeException("Argument should implement NBAppInstallerListener");
        }
    }


    @Override
     public void onDownloadSuccess() {

        try {

            File toInstall = new File(ApkDownloader.NEW_APK_PATH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider", toInstall);
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ((Activity)context).startActivityForResult(intent,
                        Constants.START_PACKAGE_INSTALLER_REQUEST_CODE);
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).startActivityForResult(intent,
                        Constants.START_PACKAGE_INSTALLER_REQUEST_CODE);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            listener.close();
        }
    }


    @Override
    public void onDownloadFailure() {
        listener.close();
    }


    @Override
    public Context getContext() {
        return context;
    }


    public void updateApp() {
        try {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) context,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ_STORAGE);
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
////                    PermissionDialog dialog =
////                            PermissionDialog.getInstance(Constants.PERMISSION_REQUEST_READ_STORAGE);
////                    dialog.show(getSupportFragmentManager(), "PermissionDialog");
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context,
//                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                            PERMISSION_REQUEST_READ_STORAGE);
//                }
            } else {
                downloadAPK();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            listener.close();
        }
    }


    private void downloadAPK() {
        try {

            ProgressDialog barProgressDialog = new ProgressDialog(context);
            barProgressDialog.setTitle(context.getString(R.string.app_install_title));
            barProgressDialog.setMessage(context.getString(R.string.app_install_message));
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(100);
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);

            ApkDownloader updateApp = new ApkDownloader();
            updateApp.setup(this, barProgressDialog);
            updateApp.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            listener.close();
        }
    }
}
