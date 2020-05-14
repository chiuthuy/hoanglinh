package com.dev.novel.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.novel.Models.Chapter;
import com.dev.novel.R;

public class ChapterAdapter extends ArrayAdapter<Chapter> {
    private final Activity context;
    private final Chapter[] chapters;

    public ChapterAdapter(Activity context, Chapter[] chapters) {
        super(context, R.layout.chapter_item, chapters);
        this.context = context;
        this.chapters = chapters;
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
        String chapter = "Chapter " + Integer.toString(position + 1);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View chapterItem = ((LayoutInflater) layoutInflater).inflate(R.layout.chapter_item, null, true);
        LinearLayout chapterItemLayout = (LinearLayout) chapterItem.findViewById(R.id.chapterItem);
        TextView chapterTitle = (TextView) chapterItem.findViewById(R.id.chapterTitle);

        chapterItemLayout.setBackgroundResource(color);
        chapterTitle.setText(chapter + ": " + chapters[position].getTitle());

        return chapterItem;
    }
}
