package com.aziz.newsfeed.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.AsyncTaskLoader;

import com.aziz.newsfeed.models.NewsModel;
import com.aziz.newsfeed.utils.QueryUtils;

import java.util.ArrayList;

public class NewsAsyncTaskLoader extends
        AsyncTaskLoader<ArrayList<NewsModel>> {

    private static final String TAG = "NATLoader MainActivity";

    private String mRequestUrl;

    public NewsAsyncTaskLoader(@NonNull Context context, String requestUrl) {
        super(context);
        mRequestUrl = requestUrl;
        Log.d(TAG, "NewsAsyncTaskLoader() called with: context = [" + context + "], requestUrl = [" + requestUrl + "]");
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading() called");
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<NewsModel> loadInBackground() {
        Log.d(TAG, "loadInBackground() called");

        if (mRequestUrl == null) {
            return null;
        }
        ArrayList<NewsModel> newsArrListData;
        newsArrListData = QueryUtils.fetchNewsData(mRequestUrl);
        return newsArrListData;
    }
}
