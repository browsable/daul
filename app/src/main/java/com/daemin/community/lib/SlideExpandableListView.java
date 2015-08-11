package com.daemin.community.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by hernia on 2015-07-07.
 */
class SlideExpandableListView extends ListView {
    private SlideExpandableListAdapter adapter;

    public SlideExpandableListView(Context context) {
        super(context);
    }

    public SlideExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Collapses the currently open view.
     *
     * @return true if a view was collapsed, false if there was no open view.
     */
    public boolean collapse() {
        if(adapter!=null) {
            return adapter.collapseLastOpen();
        }
        return false;
    }

    public void setAdapter(ListAdapter adapter) {
        this.adapter = new SlideExpandableListAdapter(adapter);
        super.setAdapter(this.adapter);
    }

    public void setAdapter(ListAdapter adapter, int toggle_button_id, int expandable_view_id) {
        this.adapter = new SlideExpandableListAdapter(adapter, toggle_button_id, expandable_view_id);
        super.setAdapter(this.adapter);
    }

    /**
     * Registers a OnItemClickListener for this listview which will
     * expand the item by default. Any other OnItemClickListener will be overriden.
     *
     * To undo call setOnItemClickListener(null)
     *
     * Important: This method call setOnItemClickListener, so the value will be reset
     */
    public void enableExpandOnItemClick() {
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SlideExpandableListAdapter adapter = (SlideExpandableListAdapter)getAdapter();
                adapter.getExpandToggleButton(view).performClick();
            }
        });
    }


   /* @Override
    public Parcelable onSaveInstanceState() {
        return adapter.onSaveInstanceState(super.onSaveInstanceState());
    }

    // Fragment add -> Activity 띄우는거
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof AbstractSlideExpandableListAdapter.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        AbstractSlideExpandableListAdapter.SavedState ss = (AbstractSlideExpandableListAdapter.SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        adapter.onRestoreInstanceState(ss);
    }*/
}