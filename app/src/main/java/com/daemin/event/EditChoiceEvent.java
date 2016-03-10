package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class EditChoiceEvent {
    boolean singleEffect;
    int position;

    public EditChoiceEvent(int position, boolean singleEffect) {
        this.position = position;
        this.singleEffect = singleEffect;
    }

    public int getPosition() {
        return position;
    }

    public boolean isSingleEffect() {
        return singleEffect;
    }
}
