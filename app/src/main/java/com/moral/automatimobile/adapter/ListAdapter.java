package com.moral.automatimobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    Context context;
    private List<String> topInfo;
    private List<String> bottomInfo;
    private List<String> imgSrc;

    public ListAdapter(Context context, List<String> topInfo, List<String> bottomInfo, List<String> imgSrc) {
        this.context = context;
        this.topInfo = topInfo;
        this.bottomInfo = bottomInfo;
        this.imgSrc = imgSrc;
    }

    @Override
    public int getCount() {
        return topInfo.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_list_item, parent, false);
            viewHolder.topInfoTextView = (TextView) convertView.findViewById(R.id.topInfoTextView);
            viewHolder.bottomInfoTextView = (TextView) convertView.findViewById(R.id.bottomInfoTextView);
            viewHolder.modelImgSrc = (ImageView) convertView.findViewById(R.id.modelImgSrc);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.topInfoTextView.setText(topInfo.get(position));
        viewHolder.bottomInfoTextView.setText(bottomInfo.get(position));
        Picasso.get().load(this.imgSrc.get(position)).into(viewHolder.modelImgSrc);
        //viewHolder.icon.

        return convertView;
    }

    private static class ViewHolder {

        TextView topInfoTextView;
        TextView bottomInfoTextView;
        ImageView modelImgSrc;

    }
}
