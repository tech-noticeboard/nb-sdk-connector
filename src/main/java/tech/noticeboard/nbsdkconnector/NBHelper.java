package tech.noticeboard.nbsdkconnector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import tech.noticeboard.nbsdkconnector.NBAppUpdate.AppUpdate;
import tech.noticeboard.nbsdkconnector.NBUserActivity.NBUserActivity;
import tech.noticeboard.nbsdkconnector.NBUserActivity.UserActivityAsyncTask;

/**
 * Created by Priyansh Srivastava on 27-Oct-17.
 */
public class NBHelper {

    public static boolean openNoticeboard(Context context) {

        boolean result;

        try {
            PackageManager pm = context.getPackageManager();
            boolean isInstalled = isPackageInstalled("tech.noticeboard", pm);

            if(isInstalled) {
                Intent i = new Intent();
                i.setAction("tech.noticeboard.NBStartActivity");
                ((Activity)context).startActivityForResult(i,0);
                result = true;
            } else {
                AppUpdate appUpdate = new AppUpdate(context);
                appUpdate.updateApp();
                result = true;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }

        return result;
    }


    private static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static void getActivity(NBUserActivity activityInterface) {
        UserActivityAsyncTask activityAsyncTask = new UserActivityAsyncTask();
        activityAsyncTask.setup(activityInterface);
        activityAsyncTask.execute();
    }
}
