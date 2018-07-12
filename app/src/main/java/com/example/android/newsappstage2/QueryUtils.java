package com.example.android.newsappstage2;

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

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> Newss = extractFeatureFromJson(jsonResponse);

        return Newss;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
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

    private static List<News> extractFeatureFromJson(String NewsJSON) {

        if (TextUtils.isEmpty(NewsJSON)) {
            return null;
        }

        List<News> NewsList = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(NewsJSON);

            JSONObject jsonResults = baseJsonResponse.getJSONObject("response");


            JSONArray NewsArray = jsonResults.getJSONArray("results");



            for (int i = 0; i < NewsArray.length(); i++) {

                JSONObject currentNews = NewsArray.getJSONObject(i);


                String topic = currentNews.getString("sectionName");

                String webTitle = currentNews.getString("webTitle");

                String webPublicationDate;

                if (currentNews.has("webPublicationDate")) {
                    webPublicationDate = currentNews.getString("webPublicationDate");

                } else {
                    webPublicationDate = "No webPublicationDate";
                }
                String url = currentNews.getString("webUrl");
                String author = "N/A";

                List<String> authorsList = new ArrayList<>();

                JSONArray tagsArray = currentNews.getJSONArray("tags");

                for (int j = 0; j < tagsArray.length(); j++) {
                    JSONObject tagsObject = tagsArray.getJSONObject(j);
                    String firstName = tagsObject.optString("firstName");
                    String lastName = tagsObject.optString("lastName");
                    String authorName;
                    if (TextUtils.isEmpty(firstName)) {
                        authorName = lastName;
                    } else {
                        authorName = firstName + " " + lastName;
                    }
                    authorsList.add(authorName);
                }

                if (authorsList.size() == 0) {
                    author = "N/A";
                } else {
                    author = TextUtils.join(", ", authorsList);
                }


                News article = new News(webTitle, topic, webPublicationDate, url, author);
                NewsList.add(article);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }

        return NewsList;
    }
}

