package com.technologx.bluerain.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.technologx.bluerain.utilities.Utils;

public class ZooperWidget {

    private final String previewPath;

    public ZooperWidget(String previewPath) {
        this.previewPath = previewPath;
    }

    public static Bitmap getTransparentBackgroundPreview(Bitmap original) {
        return Utils.getWidgetPreview(original, Color.parseColor("#ec6231"));
    }

    public String getPreviewPath() {
        return this.previewPath;
    }

}