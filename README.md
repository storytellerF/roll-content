# roll content

## 描述

滚动内容。可以用来制作滚动的字幕或其他类似的功能。

## 使用

继承`RollContentAdapter`，然后整一个`recycle view`，然后剩下的就很一般了。

比如:

```java
public class TextAdapter extends RollContentAdapter<TextAdapter.TextViewHolder> {
    
    @Override
    protected int getEndTime(int position) {
        return position*1000;
    }
    //这个函数不太一样，current 标识当前是否高亮，具体如何高亮，自行实现
    @Override
    public void onBindViewHolder(@NonNull @NotNull TextAdapter.TextViewHolder holder, int position,boolean current) {
        if (current) {
            holder.textView.setTextColor(Color.BLUE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.bind(list.get(position));
    }
    //其他的构造函数，get item count之类的省略
}


```