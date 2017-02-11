package com.technologx.bluerain.holders.lists;

import com.technologx.bluerain.events.OnLoadEvent;
import com.technologx.bluerain.models.KustomKomponent;
import com.technologx.bluerain.models.KustomWallpaper;
import com.technologx.bluerain.models.KustomWidget;
import com.technologx.bluerain.models.ZooperWidget;

public class Holder {

    private final KustomWidgetsList mKustomWidgets = new KustomWidgetsList();
    private final KomponentsList mKomponents = new KomponentsList();
    private final KustomWallsList mKustomWalls = new KustomWallsList();
    private final ZooperList mZooperList = new ZooperList();


    public KustomWidgetsList kustomWidgets() {
        return mKustomWidgets;
    }

    public KomponentsList komponents() {
        return mKomponents;
    }

    public KustomWallsList kustomWalls() {
        return mKustomWalls;
    }

    public ZooperList zooperList() {
        return mZooperList;
    }


    public class KustomWidgetsList extends ListHolderFrame<KustomWidget> {

        @Override
        public OnLoadEvent.Type getEventType() {
            return OnLoadEvent.Type.KUSTOMWIDGETS;
        }
    }

    public class KomponentsList extends ListHolderFrame<KustomKomponent> {

        @Override
        public OnLoadEvent.Type getEventType() {
            return OnLoadEvent.Type.KOMPONENTS;
        }
    }

    public class KustomWallsList extends ListHolderFrame<KustomWallpaper> {

        @Override
        public OnLoadEvent.Type getEventType() {
            return OnLoadEvent.Type.KUSTOMWALLPAPERS;
        }
    }


    public class ZooperList extends ListHolderFrame<ZooperWidget> {

        @Override
        public OnLoadEvent.Type getEventType() {
            return OnLoadEvent.Type.ZOOPER;
        }
    }

}