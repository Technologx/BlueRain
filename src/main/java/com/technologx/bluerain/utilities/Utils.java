package com.technologx.bluerain.utilities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static String getStringFromResources(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static void openLink(Context context, String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Do nothing
        }
    }

    @SuppressWarnings("ResourceAsColor")
    public static void openLinkInChromeCustomTab(Context context, String link) {
        final CustomTabsClient[] mClient = new CustomTabsClient[1];
        final CustomTabsSession[] mCustomTabsSession = new CustomTabsSession[1];
        CustomTabsServiceConnection mCustomTabsServiceConnection;
        CustomTabsIntent customTabsIntent;

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName,
                                                     CustomTabsClient customTabsClient) {
                mClient[0] = customTabsClient;
                mClient[0].warmup(0L);
                mCustomTabsSession[0] = mClient[0].newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient[0] = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome",
                mCustomTabsServiceConnection);
        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession[0])
                .setToolbarColor(ThemeUtils.darkOrLight(context, R.color.dark_theme_primary, R
                        .color.light_theme_primary))
                .setShowTitle(true)
                .build();

        try {
            customTabsIntent.launchUrl(context, Uri.parse(link));
        } catch (Exception ex) {
            openLink(context, link);
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static String getFilenameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static int clean(File file) {
        if (!file.exists()) return 0;
        int count = 0;
        if (file.isDirectory()) {
            File[] folderContent = file.listFiles();
            if (folderContent != null && folderContent.length > 0) {
                for (File fileInFolder : folderContent) {
                    count += clean(fileInFolder);
                }
            }
        }
        file.delete();
        return count;
    }

    public static void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }

    @SuppressWarnings("ConstantConditions")
    public static Bitmap getWidgetPreview(@NonNull Bitmap bitmap, @ColorInt int colorToReplace) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int minX = width;
        int minY = height;
        int maxX = -1;
        int maxY = -1;

        Bitmap newBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        int pixel;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int index = y * width + x;
                pixel = pixels[index];
                if (pixel == colorToReplace) {
                    pixels[index] = android.graphics.Color.TRANSPARENT;
                }
                if (pixels[index] != android.graphics.Color.TRANSPARENT) {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return Bitmap.createBitmap(newBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

}