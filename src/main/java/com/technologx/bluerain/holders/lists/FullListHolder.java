package com.technologx.bluerain.holders.lists;

public class FullListHolder {

    private static final Holder sLists = new Holder();

    public static Holder get() {
        if (sLists == null) return new Holder();
        return sLists;
    }

}
