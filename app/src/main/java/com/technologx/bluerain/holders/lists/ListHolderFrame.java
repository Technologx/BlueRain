package com.technologx.bluerain.holders.lists;

import android.support.annotation.NonNull;

import com.technologx.bluerain.events.OnLoadEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public abstract class ListHolderFrame<T> {

    private ArrayList<T> mList = new ArrayList<>();

    public abstract OnLoadEvent.Type getEventType();

    public void createList(@NonNull ArrayList<T> list) {
        mList = list;
        EventBus.getDefault().post(new OnLoadEvent(getEventType()));
    }

    public ArrayList<T> getList() {
        return mList;
    }

    public void clearList() {
        mList = null;
    }

    public boolean hasList() {
        return mList != null && !mList.isEmpty();
    }

    public boolean isEmpty() {
        return !hasList();
    }

    public boolean isNull() {
        return mList == null;
    }
}
