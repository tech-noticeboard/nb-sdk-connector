package tech.noticeboard.nbsdkconnector;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Priyansh Srivastava on 30-Oct-17.
 */

class SharedPreferenceProvider {

    static void writeIfAppInstalled(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.APP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.APP_INSTALL_KEY, true);
        editor.apply();
    }


    static boolean readIfAppInstalled(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.APP_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.APP_INSTALL_KEY, false);
    }

}
