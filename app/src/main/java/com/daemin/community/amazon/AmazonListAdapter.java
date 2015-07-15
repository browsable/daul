package com.daemin.community.amazon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.daemin.common.MyVolley;
import com.daemin.timetable.R;

import java.util.List;

/**
 * Created by hernia on 2015-06-20.
 */
public class AmazonListAdapter extends BaseAdapter {
    List<ShoppingItem> items;
    private ImageLoader loader;
    private LayoutInflater inflater;

    private int LAYOUT_LISTVIEW_ITEM_SHOPPING = R.layout.listitem;

    public AmazonListAdapter(Context context, List<ShoppingItem> items) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.items = items;

        loader = MyVolley.getImageLoader();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ShoppingItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflatedView();
            setContentToHolder(convertView, holder);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShoppingItem item = getItem(position);
        holder.title.setText(item.getTitle());
        holder.itemImage.setImageBitmap(null);
        loader.get(item.getImageUrl(),
                ImageLoader.getImageListener(holder.itemImage, 0, 0), 30, 30);
        return convertView;
    }

    protected void setContentToHolder(View convertView, ViewHolder holder) {
        holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
        holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
    }

    protected View getInflatedView() {
        return (View) inflater.inflate(getItemLayoutId(), null);
    }

    protected int getItemLayoutId() {
        return LAYOUT_LISTVIEW_ITEM_SHOPPING;
    }
}