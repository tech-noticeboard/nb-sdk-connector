package tech.noticeboard.nbsdkconnector.NBAppUpdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

import tech.noticeboard.nbsdkconnector.BuildConfig;

import static tech.noticeboard.nbsdkconnector.Constants.PERMISSION_REQUEST_READ_STORAGE;

/**
 * Created by Priyansh Srivastava on 29-Oct-17.
 */

public class AppUpdate extends AppUpdateListener {

    private Context context;

    public AppUpdate(Context context) {
        this.context = context;
    }


    @Override
     void onDownloadSuccess() {

        File apkFile = new File(ApkDownloader.NEW_APK_PATH);
        Uri outputFileUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(apkFile.getPath()));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(outputFileUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }


    @Override
     void onDownloadFailure() {
        ((Activity)context).finish();
    }

    @Override
     Context getContext() {
        return context;
    }


    public void updateApp() {
        try {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    PermissionDialog dialog =
//                            PermissionDialog.getInstance(Constants.PERMISSION_REQUEST_READ_STORAGE);
//                    dialog.show(getSupportFragmentManager(), "PermissionDialog");
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_READ_STORAGE);
                }
            } else {
                downloadAPK();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void downloadAPK() {
        // if (isInternetConnected()) {
        try {
            ProgressDialog barProgressDialog = new ProgressDialog(context);
            barProgressDialog.setTitle("Installing Noticeboard App!");
            barProgressDialog.setMessage("Downloading...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(100);
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);

            ApkDownloader updateApp = new ApkDownloader();
            updateApp.setup(this, barProgressDialog);
            updateApp.execute();
            //   }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
