package by.zubchenok.earthporn;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* The class contains helper methods to work with HTTP and JSON.
* */
public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /* The method makes http connection, handles the response and returns List of image URLs.
    *
    * @param requestUrl the URL to make request
    * @return the List that contains URLs of images to display
    * */
    public static List<String> fetchImageUrls(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<String> imageUrls = extractFeaturesFromJson(jsonResponse);
        return imageUrls;
    }

    /* The method makes URL from String.
    *
    * @param urlString the String URL
    * @return the URL object
    * */
    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /* The method makes http connection and gets the JSON response.
    *
    * @param URL the URL to connect
    * @return the String JSON response
    * @throws IOExceptions if problems with connection were occurred
    * */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /* The method reads information from InputStream.
    *
    * @param inputStream InputStream to read from
    * @return the String have been read
    * @throws IOExceptions if problems with InputStream reading were occurred
    * */
    private static final String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /* The method parses JSON to List of Strings with image URLs.
    *
    * @param jsonString JSON String to parse
    * @return the List of Strings with image URLs
    * */
    private static List<String> extractFeaturesFromJson(String jsonString) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding images to
        ArrayList<String> imageUrls = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(jsonString);
            JSONObject data = response.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");
            for (int i = 0; i < children.length(); i++) {
                JSONObject item = children.getJSONObject(i);
                JSONObject itemData = item.getJSONObject("data");
                String postHint = itemData.getString("post_hint");
                if (postHint.equals("image")) {
                    String url = itemData.getString("url");
                    imageUrls.add(url);
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return imageUrls;
    }
}
