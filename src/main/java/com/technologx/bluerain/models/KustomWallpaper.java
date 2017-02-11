package com.technologx.bluerain.models;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class KustomWallpaper {

    private final String wallpaperName;
    private final String previewPath;
    private final String previewPathLand;

    public KustomWallpaper(String wallpaperName, String previewPath, String previewPathLand) {
        this.wallpaperName = wallpaperName;
        this.previewPath = previewPath;
        this.previewPathLand = previewPathLand;
    }

    public String getPreviewPath() {
        return previewPath;
    }

    public String getPreviewPathLand() {
        return previewPathLand;
    }

    public Intent getKLWPIntent(Context context) {
        Intent klwpIntent = new Intent();
        klwpIntent.setComponent(new ComponentName("org.kustom.wallpaper", "org.kustom.lib.editor" +
                ".WpAdvancedEditorActivity"));
        klwpIntent.setData(Uri.parse("kfile://" + context.getPackageName() + "/wallpapers/" +
                wallpaperName));
        return klwpIntent;
    }

}