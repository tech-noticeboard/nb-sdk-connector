package tech.noticeboard.nbsdkconnector.nbAppInstaller.apkDownloader;

import android.content.Context;

/**
 * Created by Priyansh Srivastava on 09-Oct-17.
 */

public interface ApkDownloaderListener {
    void onDownloadSuccess();
    void onDownloadFailure();
    Context getContext();
}
