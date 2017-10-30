package tech.noticeboard.nbsdkconnector;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import tech.noticeboard.nbsdkconnector.nbUserActivity.NBUserActivity;
import tech.noticeboard.nbsdkconnector.nbUserActivity.UserActivityAsyncTask;

/**
 * Created by Priyansh Srivastava on 27-Oct-17.
 */
public class NBHelper {

    public static void openNoticeboard(@NonNull Activity activity) {

        try {

            Intent i = new Intent(activity, NBHelperActivity.class);
            activity.startActivity(i);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void getActivity(@NonNull  NBUserActivity activityInterface) {

        try {
            UserActivityAsyncTask activityAsyncTask = new UserActivityAsyncTask();
            activityAsyncTask.setup(activityInterface);
            activityAsyncTask.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
