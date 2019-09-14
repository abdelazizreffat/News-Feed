package com.aziz.newsfeed.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aziz.newsfeed.R;
import com.aziz.newsfeed.adapters.NewsRecyclerViewAdapter;
import com.aziz.newsfeed.loaders.NewsAsyncTaskLoader;
import com.aziz.newsfeed.models.NewsModel;
import com.aziz.newsfeed.utils.QueryUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<NewsModel>> {

    private static final String TAG = "MainActivity";
    private ArrayList<NewsModel> mNewsArrListData;
    private NewsRecyclerViewAdapter mNewsRecyclerViewAdapter;
    private RecyclerView newsRecyclerView;
    private TextView mLoading = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        mLoading = findViewById(R.id.loading);
        newsRecyclerView = findViewById(R.id.recycler_view_news);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mNewsArrListData == null) mNewsArrListData = new ArrayList<>();
        mNewsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, mNewsArrListData);
        newsRecyclerView.setAdapter(mNewsRecyclerViewAdapter);

        mSwipeRefreshLayout = findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        loadData();
        refresh();
    }

    private void loadData() {
        Log.d(TAG, "loadData() called");
        if (QueryUtils.isConnected(this)) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
            mSwipeRefreshLayout.setRefreshing(false);

        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            mLoading.setText(getString(R.string.no_internet));
        }
    }

    private void refresh() {
        Log.d(TAG, "refresh() called");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsModel>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader() called with: id = [" + id + "], args = [" + args + "]");

        return new NewsAsyncTaskLoader(this, "https://content.guardianapis.com/search?order-by=newest&show-fields=thumbnail%2CtrailText&show-tags=contributor&page-size=40&api-key=8f952d58-d8b2-4247-a027-f9be5825ac24");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsModel>> loader, ArrayList<NewsModel> data) {
        Log.d(TAG, "onLoadFinished() called with: loader = [" + loader + "], data = [" + data + "]");
        mNewsRecyclerViewAdapter.clearAll();

        if ((data != null) && !data.isEmpty()) {

            mNewsRecyclerViewAdapter.addAll(data);
            mLoading.setVisibility(View.GONE);
            newsRecyclerView.setVisibility(View.VISIBLE);

        } else {
            mLoading.setText(getString(R.string.no_data));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsModel>> loader) {

        Log.d(TAG, "onLoaderReset() called with: loader = [" + loader + "]");
        mNewsRecyclerViewAdapter.clearAll();

    }
}
