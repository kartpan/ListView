package com.example.karthik.listview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsFeedAdaptor extends BaseAdapter {
    private Context mContext;
    private List<NewsArticles> listNewsFeed;

    public NewsFeedAdaptor(Context context, List<NewsArticles> list) {
        mContext = context;
        listNewsFeed = list;
    }

    @Override
    public int getCount() {
        return listNewsFeed.size();
    }

    @Override
    public Object getItem(int pos) {
        return listNewsFeed.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // get selected entry
        NewsArticles entry = listNewsFeed.get(pos);

        // inflating list view layout if null
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_content, null);
        }

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);

        Glide.with(mContext)
                .load(entry.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fitCenter()
                .into(ivAvatar);

//        if (ivAvatar != null) {
//            new DownloadImages(ivAvatar).execute(entry.getImageURL());
//        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(entry.getTitle());

        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        tvDescription.setText(entry.getSubTitle());


        return convertView;
    }

}