package tech.noticeboard.nbsdkconnector.nbUserActivity;

/**
 * Created by Priyansh Srivastava on 30-Oct-17.
 */

public interface NBUserActivity {
    void activityPresent(int count);
    void noActivity();
    void activityError(int errorCode, String errorMessage);
}
