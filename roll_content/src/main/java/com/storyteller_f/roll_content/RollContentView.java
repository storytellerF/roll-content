package com.storyteller_f.roll_content;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by entalent on 2015/12/19.
 * https://github.com/entalent/LyricViewDemo
 */
public class RollContentView extends ScrollView {

    LinearLayout rootView;
    LinearLayout lyricList;
    ArrayList<TextView> lyricItems = new ArrayList<>();

    ArrayList<String> lyricTextList = new ArrayList<>();
    ArrayList<Long> lyricTimeList = new ArrayList<>();

    ArrayList<Integer> lyricItemHeights;

    int height;
    int width;

    int prevSelected = 0;

    OnLyricScrollChangeListener listener;

    public RollContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = new LinearLayout(getContext());
        rootView.setOrientation(LinearLayout.VERTICAL);
        final ViewTreeObserver vto = rootView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            height = getHeight();
            width = getWidth();
            refreshRootView();
        });
        addView(rootView);
    }

    void refreshRootView() {
        rootView.removeAllViews();
        LinearLayout blank1 = new LinearLayout(getContext()),
                blank2 = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height / 2);
        rootView.addView(blank1, params);
        if (lyricList != null)
            rootView.addView(lyricList);
        rootView.addView(blank2, params);
    }

    void refreshLyricList() {

        if (lyricList == null)
            lyricList = new LinearLayout(getContext());
        lyricList.setOrientation(LinearLayout.VERTICAL);
        lyricList.removeAllViews();
        lyricItems.clear();
        lyricItemHeights = new ArrayList<>();
        prevSelected = 0;

        for (int i = 0; i < lyricTextList.size(); i++) {
            final TextView textView = new TextView(getContext());
            textView.setText(lyricTextList.get(i));
            final ViewTreeObserver vto = textView.getViewTreeObserver();
            final int index = i;
            vto.addOnGlobalLayoutListener(() -> lyricItemHeights.add(index, textView.getHeight()));
            lyricList.addView(textView);
            lyricItems.add(index, textView);
        }

        refreshRootView();
    }

    void scrollToIndex(int index) {
        if (index < 0) {
            scrollTo(0, 0);
        }
        if (index < lyricTextList.size()) {
            int height = 0;
            for (int i = 0; i <= index - 1; i++) {
                height += lyricItemHeights.get(i);
            }
            height += lyricItemHeights.get(index) / 2;
            scrollTo(0, height);
        }
    }

    int getIndex(int length) {
        int index = 0;
        int sum = 0;
        while (sum <= length) {
            sum += lyricItemHeights.get(index);
            index++;
        }
        return index - 1;
    }

    void setSelected(int index) {
        if (index == prevSelected)
            return;
        for (int i = 0; i < lyricItems.size(); i++) {
            if (i == index)
                lyricItems.get(i).setTextColor(Color.RED);
            else
                lyricItems.get(i).setTextColor(Color.BLACK);
        }
        prevSelected = index;
    }

    public void setLyricText(ArrayList<String> textList, ArrayList<Long> timeList) {
        if (textList.size() != timeList.size()) {
            throw new IllegalArgumentException();
        }
        this.lyricTextList = textList;
        this.lyricTimeList = timeList;
        refreshLyricList();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setSelected(getIndex(t));
        if (listener != null) {
            listener.onLyricScrollChange(getIndex(t), getIndex(oldt));
        }
    }

    public void setOnLyricScrollChangeListener(OnLyricScrollChangeListener i) {
        listener = i;
    }

    public interface OnLyricScrollChangeListener {
        void onLyricScrollChange(int index, int oldindex);
    }
}