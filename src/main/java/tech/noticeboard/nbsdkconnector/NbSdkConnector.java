package tech.noticeboard.nbsdkconnector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import tech.noticeboard.nbsdkconnector.nbUserActivity.NBUserActivity;
import tech.noticeboard.nbsdkconnector.nbUserActivity.UserActivityAsyncTask;

/**
 * Created by Priyansh Srivastava on 27-Oct-17.
 */
public class NbSdkConnector {

    public static void openNoticeboard(@NonNull Activity activity, String identity) {

        try {
            Intent i = new Intent(activity, NbSdkConnectorActivity.class);
            i.putExtra(Constants.LOGIN_KEY, identity);
            activity.startActivity(i);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void getActivity(@NonNull Context context, @NonNull  NBUserActivity activityInterface) {

        try {

            UserActivityAsyncTask activityAsyncTask = new UserActivityAsyncTask();
            activityAsyncTask.setup(activityInterface);
            activityAsyncTask.execute(NbSdkHelper.getLoginToken(context));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
