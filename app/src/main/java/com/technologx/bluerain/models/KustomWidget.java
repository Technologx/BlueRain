package com.technologx.bluerain.models;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class KustomWidget {

    private final String widgetName;
    private final String previewPath;
    private final String previewPathLand;

    public KustomWidget(String widgetName, String previewPath, String previewPathLand) {
        this.widgetName = widgetName;
        this.previewPath = previewPath;
        this.previewPathLand = previewPathLand;
    }

    public String getPreviewPath() {
        return previewPath;
    }

    public String getPreviewPathLand() {
        return previewPathLand;
    }

    public Intent getKWGTIntent(Context context) {
        Intent kwgtIntent = new Intent();
        kwgtIntent.setComponent(new ComponentName("org.kustom.widget", "org.kustom.widget.picker" +
                ".WidgetPicker"));
        kwgtIntent.setData(Uri.parse("kfile://" + context.getPackageName() + "/widgets/" +
                widgetName));
        return kwgtIntent;
    }

}