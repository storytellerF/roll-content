package com.storyteller_f.roll_content;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * @author storyteller_f
 */
public abstract class RollContentAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * 当前高亮的位置
     * 继承的类需要根据这个参数高亮，否则将会失去意义
     */
    public volatile int currentHighLightPosition;
    public volatile boolean scrolling = false;
    // TODO: 2021/7/6 添加对开始时间的判断
    final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
            scrolling = false;
            super.onScrolled(recyclerView, dx, dy);
            //滚动结束
            tryScroll(recyclerView);
        }
    };
    private RecyclerView recyclerView;

    private synchronized void tryScroll(@NotNull RecyclerView recyclerView) {
        scrolling = true;
        RecyclerView.ViewHolder viewHolder =
                recyclerView.findViewHolderForAdapterPosition(currentHighLightPosition);
        if (viewHolder != null) {
            int[] location = new int[2];
            viewHolder.itemView.getLocationInWindow(location);
            int height = recyclerView.getHeight();
            int split = height / 2;
            //检查位置是否在recycle view 的下半部分
            if (location[1] >= split) {
                int dy = location[1] - split;
                if (dy > 0) {
                    recyclerView.post(() -> recyclerView.smoothScrollBy(0, dy));
                    return;
                }
            }
        }
        scrolling = false;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        onBindViewHolder(holder, position, position == currentHighLightPosition);
    }

    public abstract void onBindViewHolder(@NonNull @NotNull VH holder, int position, boolean current);

    @Override
    public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
        this.recyclerView = null;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(onScrollListener);
        this.recyclerView = recyclerView;
    }

    /**
     * 在任务进行时调用，用来通知adapter 需要在合适的时机滚动内容
     */
    public synchronized void progress(int ms) {
        final int old = currentHighLightPosition;
        while (currentHighLightPosition >= 0 && currentHighLightPosition < getItemCount()) {
            int endTiming = getEndTime(currentHighLightPosition);
            if (ms > endTiming) {//当前播放的时间超过了当前内容的结束时间，需要前进下一个
                if (currentHighLightPosition < getItemCount())
                    currentHighLightPosition++;
                else
                    break;
            } else {
                break;
            }
        }
        //如果两次高亮不同
        if (currentHighLightPosition != old) {
            recyclerView.post(() -> {
                if (!recyclerView.isComputingLayout()) {
                    notifyItemChanged(currentHighLightPosition);
                    notifyItemChanged(old);
                }
            });
        }
        if (!scrolling)
            tryScroll(recyclerView);
    }

    /**
     * 根据current highlight 获取当前高亮的结束时间
     * 如果当前播放的时间超过了高亮的结束时间，需要跳转到下一个
     *
     * @param current 指定的位置
     * @return 返回毫秒值
     */
    protected abstract int getEndTime(int current);
}
