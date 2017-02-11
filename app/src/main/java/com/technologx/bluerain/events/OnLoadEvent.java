package com.technologx.bluerain.events;

public class OnLoadEvent {

    public final Type type;

    public OnLoadEvent(Type type) {
        this.type = type;
    }

    public enum Type {
        KUSTOMWIDGETS, KOMPONENTS, KUSTOMWALLPAPERS, ZOOPER
    }
}