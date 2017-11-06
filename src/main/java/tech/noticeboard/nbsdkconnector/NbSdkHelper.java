package tech.noticeboard.nbsdkconnector;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Pattern;

/**
 * Created by Priyansh Srivastava on 31-Oct-17.
 */

class NbSdkHelper {

    static String getLoginToken(Context context) {

        Cursor c = null;
        String token = "";

        try {
            String URL = Constants.CONTENT_PROVIDER_URL;

            Uri uri = Uri.parse(URL);
            c = context.getContentResolver().query(uri,
                    null, null,null, Constants.CONTENT_PROVIDER_KEY);
            if (null != c && c.moveToFirst()) {
                do {
                    token = c.getString(c.getColumnIndex(Constants.CONTENT_PROVIDER_KEY));
                } while (c.moveToNext());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(null != c) {
                c.close();
            }
        }

        return token;
    }


    private static String getCountryCode(Context context) {

        String countryId = "";

        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (manager != null && manager.getSimCountryIso() != null) {
                //getNetworkCountryIso
                countryId = manager.getSimCountryIso().toUpperCase().trim();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(countryId.isEmpty()) {
                countryId = "+91";
            }
        }

        return countryId;
    }


    public static String formatPhone(Context context, String phone) {

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneUtil.parse(phone, getCountryCode(context));
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        if(phoneNumber != null && phoneUtil.isValidNumber(phoneNumber)){
            return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        }
        return null;
    }


    public static boolean validateEmail(String email){
        if(email == null)
            return false;
        return email.matches(Constants.EMAIL_REGEX);
    }

    public static boolean validatePhone(String phone) {
        if(phone == null)
            return false;
        return Pattern.matches(Constants.PHONE_REGEX, phone);
    }


    public static String readSdkKey(@NonNull Context context) throws NullPointerException {

        String TAG = "Noticeboard Helper";
        String sdkKey = "";

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            sdkKey = bundle.getString(Constants.MANIFEST_SDK_KEY);
            if(null == sdkKey || sdkKey.isEmpty()) {
                throw new NullPointerException();
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        }

        return sdkKey;
    }
}
