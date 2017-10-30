package tech.noticeboard.nbsdkconnector.NBAppUpdate;

import android.content.Context;

/**
 * Created by Priyansh Srivastava on 09-Oct-17.
 */

abstract class AppUpdateListener {
    abstract void onDownloadSuccess();
    abstract void onDownloadFailure();
    abstract Context getContext();
}
