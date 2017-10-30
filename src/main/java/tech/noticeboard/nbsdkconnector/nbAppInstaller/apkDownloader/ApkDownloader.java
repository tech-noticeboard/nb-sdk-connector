package tech.noticeboard.nbsdkconnector.nbAppInstaller.apkDownloader;

/**
 * Created by Priyansh Srivastava on 09-Oct-17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tech.noticeboard.nbsdkconnector.Constants;

/**
 * Created by Priyansh on 24-10-2017.
 */
public class ApkDownloader extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog progressDialog;
    private ApkDownloaderListener callingActivity;
    private PowerManager.WakeLock mWakeLock;

    public static final String DOWNLOAD_URL = Constants.DOWNLOAD_URL;
    public static final String APP_PATH = Environment.getExternalStorageDirectory().getPath() + Constants.LOCAL_DIR;
    public static final String FILE_NAME = Constants.LOCAL_APP;
    public static final String NEW_APK_PATH = APP_PATH + "/" + FILE_NAME;

    public void setup(ApkDownloaderListener callingActivity, ProgressDialog progressDialog) {
        this.callingActivity = callingActivity;
        this.progressDialog = progressDialog;
    }


    @Override
    protected Boolean doInBackground(Void... arg0) {

        InputStream input = null;
        FileOutputStream output = null;
        HttpURLConnection connection = null;

        try {
            File file = new File(APP_PATH);
            if (!file.exists()) {
                if(!file.mkdirs()) {
                    return false;
                }
            }
            File outputFile = new File(file, FILE_NAME);
            if (outputFile.exists()) {
                outputFile.delete();
            }

            URL url = new URL(DOWNLOAD_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            output = new FileOutputStream(outputFile);
            long fileLength = connection.getContentLength();
            input = connection.getInputStream();

            byte[] buffer = new byte[4096];
            int len1 = 0, total = 0;
            while ((len1 = input.read(buffer)) > 0) {
                output.write(buffer, 0, len1);
                total += len1;
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));

            }

            return true;
        } catch (Exception e) {
            //Crashlytics.logException(e);
            return false;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) callingActivity.getContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        progressDialog.show();
    }


    @Override
    protected void onPostExecute(Boolean isSuccess) {

        if(null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (isSuccess) {
            super.onPostExecute(true);
            callingActivity.onDownloadSuccess();
        } else {
            callingActivity.onDownloadFailure();
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }
}