package com.technologx.bluerain.holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.io.File;

import com.technologx.bluerain.R;
import com.technologx.bluerain.models.ZooperWidget;
import com.technologx.bluerain.utilities.Preferences;

public class ZooperWidgetHolder extends RecyclerView.ViewHolder {

    private final ImageView background;
    private final ImageView widget;
    private final Drawable wallpaper;

    public ZooperWidgetHolder(View itemView, Drawable nWallpaper) {
        super(itemView);
        background = (ImageView) itemView.findViewById(R.id.wall);
        widget = (ImageView) itemView.findViewById(R.id.preview);
        wallpaper = nWallpaper;
    }

    public void setItem(Context context, ZooperWidget item) {
        if (background != null && wallpaper != null)
            background.setImageDrawable(wallpaper);
        if (widget != null) {
            Preferences mPrefs = new Preferences(context);
            if (mPrefs.getAnimationsEnabled()) {
                Glide.with(context)
                        .load(new File(item.getPreviewPath()))
                        .priority(Priority.IMMEDIATE)
                        .into(widget);
            } else {
                Glide.with(context)
                        .load(new File(item.getPreviewPath()))
                        .priority(Priority.IMMEDIATE)
                        .dontAnimate()
                        .into(widget);
            }
        }
    }

}