package com.dev.novel.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.novel.Models.Novel;
import com.dev.novel.R;

public class NovelAdapter extends ArrayAdapter<Novel> {
    private final Activity context;
    private final Novel[] novels;

    public NovelAdapter(Activity context, Novel[] novels) {
        super(context, R.layout.novel_item, novels);
        this.context = context;
        this.novels = novels;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int color = position % 2 == 0 ?
                R.color.white :
                R.color.fakeTransparentPrimary;
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View novelItem = ((LayoutInflater) layoutInflater).inflate(R.layout.novel_item, null, true);
        LinearLayout novelItemLayout = (LinearLayout) novelItem.findViewById(R.id.novelItem);
        TextView novelTitle = (TextView) novelItem.findViewById(R.id.novelTitle);

        novelItemLayout.setBackgroundResource(color);
        novelTitle.setText(novels[position].getName());

        return novelItem;
    }
}
