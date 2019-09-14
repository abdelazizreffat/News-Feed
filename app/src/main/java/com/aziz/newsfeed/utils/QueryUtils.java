package com.aziz.newsfeed.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.aziz.newsfeed.models.NewsModel;

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

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String TAG = "QU MainActivity";

    private static URL createUrl(String requestUrl) {
        Log.d(TAG, "createUrl() called with: requestUrl = [" + requestUrl + "]");
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String readStreamResponse(InputStream inputStream) throws IOException {
        Log.d(TAG, "readStreamResponse() called with: inputStream = [" + inputStream + "]");

        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        Log.d(TAG, "makeHttpRequest() called with: url = [" + url + "]");

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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readStreamResponse(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
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

    private static ArrayList<NewsModel> extractResponseFromJson(String newsJsonResponse) {
        Log.d(TAG, "extractResponseFromJson() called with: newsJsonResponse = [" + newsJsonResponse + "]");

        if (newsJsonResponse.isEmpty()) {
            return null;
        }

        ArrayList<NewsModel> newsArrListData = new ArrayList<>();

        try {

            String section, title, trail, date, webUrl;
            String thumbnail = "";
            String author = "";

            JSONObject baseJsonResponse = new JSONObject(newsJsonResponse);

            JSONObject responseJsonObject = baseJsonResponse.getJSONObject("response");

            JSONArray resultsJsonArray = responseJsonObject.getJSONArray("results");

            for (int i = 0; i < resultsJsonArray.length(); i++) {

                JSONObject currentNewsJsonObject = resultsJsonArray.getJSONObject(i);

                section = currentNewsJsonObject.getString("sectionName");
                title = currentNewsJsonObject.getString("webTitle");
                date = currentNewsJsonObject.getString("webPublicationDate");
                webUrl = currentNewsJsonObject.getString("webUrl");

                JSONObject fieldsJsonObject = currentNewsJsonObject.getJSONObject("fields");

                String unCheckedTrail = fieldsJsonObject.getString("trailText");
                if (unCheckedTrail.contains("<")) {
                    trail = "";
                } else {
                    trail = fieldsJsonObject.getString("trailText");

                }

                try {
                    thumbnail = fieldsJsonObject.getString("thumbnail");

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Missing thumbnail JSONObject");
                }

                JSONArray tagsArray = currentNewsJsonObject.getJSONArray("tags");
                if (tagsArray.length() > 0) {
                    for (int j = 0; j < 1; j++) {
                        JSONObject authorObj = tagsArray.getJSONObject(j);
                        try {
                            author = authorObj.getString("webTitle");
                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Missing one or more author's name JSONObject");
                        }
                    }
                }
                NewsModel newsModelObject = new NewsModel(section, title, thumbnail, trail, date, author, webUrl);

                newsArrListData.add(newsModelObject);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return newsArrListData;
    }

    public static ArrayList<NewsModel> fetchNewsData(String requestUrl) {
        Log.d(TAG, "fetchNewsData() called with: requestUrl = [" + requestUrl + "]");

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        assert jsonResponse != null;

        return extractResponseFromJson(jsonResponse);
    }

    public static boolean isConnected(Context context) {
        Log.d(TAG, "isConnected() called with: context = [" + context + "]");

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
