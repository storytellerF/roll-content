package com.storyteller_f.test;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storyteller_f.roll_content.RollContentAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author storyteller_f
 */
public class TextAdapter extends RollContentAdapter<TextAdapter.TextViewHolder> {
    public ArrayList<String> list;

    public TextAdapter() {
        list = new ArrayList<>();
        list.addAll(Arrays.asList("hello", "world", "android studio", "android debug bridge",
                "hello", "world", "android studio", "android debug bridge",
                "hello", "world", "android studio", "android debug bridge",
                "hello", "world", "android studio", "android debug bridge",
                "hello", "world", "android studio", "android debug bridge",
                "hello", "world", "android studio", "android debug bridge",
                "hello", "world", "android studio", "android debug bridge"));
    }

    @Override
    protected int getEndTime(int position) {
        return position*1000;
    }

    @NonNull
    @NotNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TextAdapter.TextViewHolder holder, int position,boolean current) {
        if (current) {
            holder.textView.setTextColor(Color.BLUE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text) {
            textView.setText(text);
        }
    }
}
