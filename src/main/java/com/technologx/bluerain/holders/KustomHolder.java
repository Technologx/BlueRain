package com.technologx.bluerain.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.io.File;

import com.technologx.bluerain.R;
import com.technologx.bluerain.utilities.Preferences;

public class KustomHolder extends RecyclerView.ViewHolder {
    private final ImageView widget;
    private final TextView sectionTitle;
    private final OnKustomItemClickListener listener;
    private int section = -1;
    private int position = -1;

    public KustomHolder(View itemView, Drawable wallpaper, OnKustomItemClickListener nListener) {
        super(itemView);
        ImageView background = (ImageView) itemView.findViewById(R.id.wall);
        widget = (ImageView) itemView.findViewById(R.id.preview);
        sectionTitle = (TextView) itemView.findViewById(R.id.kustom_section_title);
        listener = nListener;
        if (background != null)
            background.setImageDrawable(wallpaper);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onKustomItemClick(section, position);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setSectionTitle(int section) {
        switch (section) {
            case 0:
                sectionTitle.setText("Komponents");
                break;
            case 1:
                sectionTitle.setText("Widgets");
                break;
            default:
                sectionTitle.setText("Empty Assets");
                break;
        }
    }

    public void setItem(Context context, int section, String filePath, int relPosition) {
        this.section = section;
        this.position = relPosition;
        if (filePath != null) {
            Preferences mPrefs = new Preferences(context);
            if (mPrefs.getAnimationsEnabled()) {
                Glide.with(context)
                        .load(new File(filePath))
                        .priority(Priority.IMMEDIATE)
                        .into(widget);
            } else {
                Glide.with(context)
                        .load(new File(filePath))
                        .dontAnimate()
                        .priority(Priority.IMMEDIATE)
                        .into(widget);
            }
        }
    }

    public interface OnKustomItemClickListener {
        void onKustomItemClick(int section, int position);
    }

}