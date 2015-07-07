package com.daemin.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.daemin.common.BasicFragment;
import com.daemin.community.lib.ActionSlideExpandableListView;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-07-07.
 */
public class CommunityFragment2 extends BasicFragment {

    View root;

    public CommunityFragment2() {
        super(R.layout.fragment_community2, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {

            ActionSlideExpandableListView list = (ActionSlideExpandableListView) root.findViewById(R.id.list);

            // fill the list with data
            list.setAdapter(buildDummyData());

            // listen for events in the two buttons for every list item.
            // the 'position' var will tell which list item is clicked
            list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

                @Override
                public void onClick(View listView, View buttonview, int position) {
                    /**
                     * Normally you would put a switch
                     * statement here, and depending on
                     * view.getId() you would perform a
                     * different action.
                     */
                    String actionName = "";
                    if (buttonview.getId() == R.id.btChildEdit) {
                        actionName = "btChildEdit";
                    } else {
                        actionName = "btChildRemove";
                    }
                    /**
                     * For testing sake we just show a toast
                     */
                    Toast.makeText(
                            getActivity(),
                            "버튼이름: " + actionName,
                            Toast.LENGTH_SHORT
                    ).show();
                }

                // note that we also add 1 or more ids to the setItemActionListener
                // this is needed in order for the listview to discover the buttons
            }, R.id.btChildEdit, R.id.btChildRemove);
        }
        return root;
    }
    public ListAdapter buildDummyData() {
        final int SIZE = 20;
        String[] values = new String[SIZE];
        for(int i=0;i<SIZE;i++) {
            values[i] = "Item "+i;
        }
        return new ArrayAdapter<String>(
                getActivity(),
                R.layout.listitem_expandable,
                R.id.tvGroupTitle,
                values
        );
    }

}
