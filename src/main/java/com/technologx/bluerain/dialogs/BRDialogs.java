package com.technologx.bluerain.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.technologx.bluerain.R;
import com.technologx.bluerain.utilities.Utils;


import java.util.ArrayList;

public final class BRDialogs {

    public static void showPermissionNotGrantedDialog(Context context) {
        String appName = Utils.getStringFromResources(context, R.string.app_name);
        new MaterialDialog.Builder(context)
                .title(R.string.md_error_label)
                .content(context.getResources().getString(R.string.md_storage_perm_error, appName))
                .positiveText(android.R.string.ok)
                .show();
    }

    public static void showZooperAppsDialog(final Context context, final ArrayList<String>
            appsNames) {
        final String storePrefix = "https://play.google.com/store/apps/details?id=",
                muLink = "com.batescorp.notificationmediacontrols.alpha",
                koloretteLink = "com.arun.themeutil.kolorette";
        new MaterialDialog.Builder(context)
                .title(R.string.install_apps)
                .content(R.string.install_apps_content)
                .items(appsNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which,
                                            CharSequence text) {
                        if (appsNames.get(which).equals("Zooper Widget Pro")) {
                            showZooperDownloadDialog(context);
                        } else if (appsNames.get(which).equals("Media Utilities")) {
                            Utils.openLinkInChromeCustomTab(context,
                                    storePrefix + muLink);
                        } else if (appsNames.get(which).equals("Kolorette")) {
                            Utils.openLinkInChromeCustomTab(context,
                                    storePrefix + koloretteLink);
                        }
                    }
                })
                .show();
    }

    public static void showKustomAppsDownloadDialog(final Context context, final
    ArrayList<String> appsNames) {
        final String storePrefix = "https://play.google.com/store/apps/details?id=",
                klwpLink = "org.kustom.wallpaper",
                kwgtLink = "org.kustom.widget",
                koloretteLink = "com.arun.themeutil.kolorette";
        new MaterialDialog.Builder(context)
                .title(R.string.install_apps)
                .content(R.string.install_kustom_apps_content)
                .items(appsNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which,
                                            CharSequence text) {
                        if (appsNames.get(which).equals("Kustom Live Wallpaper")) {
                            Utils.openLinkInChromeCustomTab(context,
                                    storePrefix + klwpLink);
                        } else if (appsNames.get(which).equals("Kustom Widget")) {
                            Utils.openLinkInChromeCustomTab(context,
                                    storePrefix + kwgtLink);
                        } else if (appsNames.get(which).equals("Kolorette")) {
                            Utils.openLinkInChromeCustomTab(context,
                                    storePrefix + koloretteLink);
                        }
                    }
                })
                .show();
    }

    private static void showZooperDownloadDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.zooper_download_dialog_title)
                .items(R.array.zooper_download_dialog_options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int selection,
                                            CharSequence text) {
                        switch (selection) {
                            case 0:
                                Utils.openLinkInChromeCustomTab(context,
                                        "https://play.google.com/store/apps/details?id=org.zooper" +
                                                ".zwpro");
                                break;
                            case 1:
                                if (Utils.isAppInstalled(context, "com.amazon.venezia")) {
                                    Utils.openLinkInChromeCustomTab(context,
                                            "amzn://apps/android?p=org.zooper.zwpro");
                                } else {
                                    Utils.openLinkInChromeCustomTab(context,
                                            "http://www.amazon.com/gp/mas/dl/android?p=org.zooper" +
                                                    ".zwpro");
                                }
                                break;
                        }
                    }
                })
                .show();
    }

}