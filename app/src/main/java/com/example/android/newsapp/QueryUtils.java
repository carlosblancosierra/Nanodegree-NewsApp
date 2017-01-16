package com.example.android.newsapp;

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

/**
 * Created by carlosblanco on 12/19/16.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<News> extractFeatureFromJson(String newsJson) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        ArrayList<News> news = new ArrayList<>();

        try {
            //gets the root JSON Response
            JSONObject root = new JSONObject(newsJson);

            //gets the response Object
            JSONObject response = root.getJSONObject("response");

            //gets the results array which contains the news
            JSONArray results = response.getJSONArray("results");

            //loops through each news object
            for (int i = 0; i < results.length(); i++) {

                //gets the current news object
                JSONObject currentNew = results.getJSONObject(i);

                //gets the currentNew's title of the news
                String title = currentNew.getString("webTitle");

                //gets the currentNew's author
                //String author = currentNew.getString("");

                //gets the currentNew's section
                String section = currentNew.getString("sectionName");

                String date;

                //gets the currentNew's date if there is one
                if (currentNew.has("webPublicationDate")) {
                    date = currentNew.getString("webPublicationDate");
                } else {
                    date = null;
                }

                //gets the CurrentNews's Url
                String webUrl = currentNew.getString("webUrl");

                news.add(new News(title, date, section, webUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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

    public static ArrayList<News> fetchNewsResponse(String requestUrl) {

        String jsonResponse = "";

        URL url = createUrl(requestUrl);

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }
}
