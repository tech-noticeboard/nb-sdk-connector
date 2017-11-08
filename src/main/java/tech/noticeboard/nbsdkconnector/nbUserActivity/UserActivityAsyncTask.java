package tech.noticeboard.nbsdkconnector.nbUserActivity;

/**
 * Created by Priyansh Srivastava on 09-Oct-17.
 */

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import tech.noticeboard.nbsdkconnector.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by Priyansh on 24-10-2017.
 */
public class UserActivityAsyncTask extends AsyncTask<String, Void, Integer> {

    private NBUserActivity userActivityInterface;

    public void setup(NBUserActivity callingActivity) {
        this.userActivityInterface = callingActivity;
    }

    @Override
    protected Integer doInBackground(String... arg0) {

        Integer result = -1;
        HttpsURLConnection urlConnection = null;

        try {

            URL url = new URL(String.format(Constants.ACTIVITY_URL, arg0[1]));
            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            //urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");

            String authorization = "Bearer " + arg0[0];
            urlConnection.addRequestProperty("Authorization", authorization);
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {

                Log.d(TAG, "NbSdkConnector:  connection failed: statusCode: " + statusCode);
                result = -1;

            } else {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader responseBodyReader = new InputStreamReader(in, "UTF-8");
                result = getActivityCountFromJson(responseBodyReader);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }


    private static int getActivityCountFromJson(InputStreamReader reader) {

        int activityCount = -1;

        try {

            String value = "";

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals(Constants.ACTIVITY_COUNT_KEY)) { // Check if desired key
                    // Fetch the value as a String
                    value = jsonReader.nextString();
                    break; // Break out of the loop
                } else {
                    jsonReader.skipValue(); // Skip values of other keys
                }
            }

            //Close the json reader
            jsonReader.close();

            //parse the value
            if(null != value && !value.isEmpty()) {
                activityCount = Integer.parseInt(value);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return activityCount;
    }


    @Override
    protected void onPostExecute(Integer activityCount) {
        switch (activityCount) {
            case -1:    userActivityInterface.activityError(0, "Error");
                        break;
            case 0:     userActivityInterface.noActivity();
                        break;
            default:
                userActivityInterface.activityPresent(activityCount);
                break;
        }
    }
}