package com.example.karthik.listview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsFeedAdaptor extends BaseAdapter {
    private Context mContext;
    private List<NewsFeed> listNewsFeed;

    public NewsFeedAdaptor(Context context, List<NewsFeed> list) {
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
        NewsFeed entry = listNewsFeed.get(pos);

        // inflating list view layout if null
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_content, null);
        }

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);

        if (ivAvatar != null) {
            new DownloadImages(ivAvatar).execute(entry.getUrl());
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(entry.getuTitle());

        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        tvDescription.setText(entry.getDescription());


        return convertView;
    }

}