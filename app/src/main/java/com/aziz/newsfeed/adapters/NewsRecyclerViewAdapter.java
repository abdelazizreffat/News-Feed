package com.aziz.newsfeed.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.aziz.newsfeed.R;
import com.aziz.newsfeed.models.NewsModel;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class NewsRecyclerViewAdapter extends
        RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "NRVAdapter MainActivity";

    private Context mContext;
    private ArrayList<NewsModel> mNewsArrListData;

    public NewsRecyclerViewAdapter(Context mContext, ArrayList<NewsModel> newsArrListData) {
        Log.d(TAG, "NewsRecyclerViewAdapter() called with: mContext = [" + mContext + "], newsArrListData = [" + newsArrListData + "]");

        this.mContext = mContext;
        this.mNewsArrListData = newsArrListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() called with: parent = [" + parent + "], viewType = [" + viewType + "]");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");

        final NewsModel newsModel = mNewsArrListData.get(position);

        String itemNumber = String.valueOf(position + 1);
        holder.mTextItemNumber.setText(itemNumber);

        holder.mTextSection.setText(newsModel.getSection());
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(200), random.nextInt(200), random.nextInt(200));
        GradientDrawable drawable = (GradientDrawable) holder.mTextSection.getBackground().mutate();
        drawable.setStroke(3, color);

        holder.mTextTitle.setText(newsModel.getTitle());

        holder.mTextTrail.setText(newsModel.getTrail());

        final String thumbnailUrl = newsModel.getThumbnail();
        if (thumbnailUrl.equals("")) {
            holder.mImageThumbnail.setImageResource(R.drawable.shape_rounded_corner);
        } else {
            Glide.with(mContext).load(thumbnailUrl).into(holder.mImageThumbnail);
        }

        final String author = newsModel.getAuthor();
        if (author.equals("")) {
            holder.mTextAuthor.setText(mContext.getString(R.string.by_Anonymous));
        } else {
            holder.mTextAuthor.setText(author);
        }

        final String date = formatDate(newsModel.getDate());
        holder.mTextDate.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickWebUrl(newsModel.getWebUrl());

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() called");

        return mNewsArrListData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextItemNumber;
        private TextView mTextSection;
        private TextView mTextTitle;
        private ImageView mImageThumbnail;
        private TextView mTextTrail;
        private TextView mTextDate;
        private TextView mTextAuthor;

        ViewHolder(@NonNull View itemNews) {
            super(itemNews);
            mTextItemNumber = itemNews.findViewById(R.id.text_item_number);
            mTextSection = itemNews.findViewById(R.id.text_section);
            mTextTitle = itemNews.findViewById(R.id.text_title);
            mImageThumbnail = itemNews.findViewById(R.id.image_thumbnail);
            mTextTrail = itemNews.findViewById(R.id.text_trail);
            mTextDate = itemNews.findViewById(R.id.text_date);
            mTextAuthor = itemNews.findViewById(R.id.text_author);
        }
    }

    private String formatDate(String dateObj) {
        Log.d(TAG, "formatDate() called with: dateObj = [" + dateObj + "]");

        String dateFormatted = "";
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        SimpleDateFormat outputDate = new SimpleDateFormat("EEE dd/MM/yy , hh:mm a", Locale.getDefault());
        try {
            Date newDate = inputDate.parse(dateObj);
            assert newDate != null;
            return outputDate.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }

    private void onClickWebUrl(String openUrl) {
        Log.d(TAG, "onClickWebUrl() called with: openUrl = [" + openUrl + "]");
        Uri webPage = Uri.parse(openUrl);
        Intent i = new Intent(Intent.ACTION_VIEW, webPage);
        if (i.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(i);
        }
    }

    public void clearAll() {
        Log.d(TAG, "clearAll() called");
        mNewsArrListData.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<NewsModel> newsArrListData) {
        Log.d(TAG, "addAll() called with: newsArrListData = [" + newsArrListData + "]");

        mNewsArrListData.clear();
        mNewsArrListData.addAll(newsArrListData);
        notifyDataSetChanged();
    }
}
