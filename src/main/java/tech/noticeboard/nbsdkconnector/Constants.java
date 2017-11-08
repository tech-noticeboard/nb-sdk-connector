package tech.noticeboard.nbsdkconnector;

/**
 * Created by Priyansh Srivastava on 13-Oct-17.
 */

public class Constants {

    public static final String DOWNLOAD_URL = "https://www.noticeboard.tech/dump/nb-app.apk";
    public static final String LOCAL_DIR = "/nb-app";
    public static final String LOCAL_APP = "nb-app.apk";
    static final String APP_NAME = "tech.noticeboard.nbsdkconnector";
    static final String APP_INSTALL_KEY = "NB_APP_INSTALL";
    public static final String ACTIVITY_URL = "https://devapi.noticeboard.tech/noticeboard-service/community/%s/login";
    public static final String ACTIVITY_COUNT_KEY = "activityCount";
    static final String CONTENT_PROVIDER_URL = "content://tech.noticeboard.TokenProvider/token";
    static final String CONTENT_PROVIDER_KEY = "_id";
    static final String INSTALL_PACKAGE_NAME = "tech.noticeboard";
    static final String INSTALL_PACKAGE_NAME_URI = "package:tech.noticeboard";
    static final String INSTALL_PACKAGE_ACTIVITY = "tech.noticeboard.NBStartActivity";
    static final String MANIFEST_SDK_KEY = "tech.noticeboard.sdkKey";
    static final String INVALID_SDK_LOGIN_OR_KEY = "Invalid key or login credentials.";
    static final String SDK_KEY = "NB_SDK_KEY";
    static final String LOGIN_KEY = "NB_LOGIN_KEY";
    static final String SDK_SPLIT_KEY = ":";

    static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    static final String PHONE_REGEX = "([1-9][0-9]{9})";

    public static final int PERMISSION_REQUEST_READ_STORAGE = 306;

    public static final int START_PACKAGE_INSTALLER_REQUEST_CODE = 1001;
}
