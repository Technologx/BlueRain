package com.technologx.bluerain.tasks;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.technologx.bluerain.activities.ShowcaseActivity;
import com.technologx.bluerain.fragments.MainFragment;
import com.technologx.bluerain.holders.lists.FullListHolder;
import com.technologx.bluerain.models.KustomKomponent;
import com.technologx.bluerain.models.KustomWallpaper;
import com.technologx.bluerain.models.KustomWidget;
import com.technologx.bluerain.utilities.utils.IconUtils;
import com.technologx.bluerain.utilities.utils.Utils;
import timber.log.Timber;

public class LoadKustomFiles extends AsyncTask<Void, String, Boolean> {

    private final ArrayList<KustomKomponent> komponents = new ArrayList<>();
    private final ArrayList<KustomWallpaper> wallpapers = new ArrayList<>();
    private final ArrayList<KustomWidget> widgets = new ArrayList<>();
    private final WeakReference<Context> context;
    private long startTime, endTime;

    public LoadKustomFiles(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        startTime = System.currentTimeMillis();
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected Boolean doInBackground(Void... params) {
        boolean worked = false;
        try {
            AssetManager assetManager = context.get().getAssets();
            String[] kustomFolders = {"komponents", "wallpapers", "widgets"};
            for (String kustomFolder : kustomFolders) {
                worked = readKustomFiles(assetManager, kustomFolder);
            }
        } catch (Exception e) {
            //Do nothing
            worked = false;
        }
        endTime = System.currentTimeMillis();
        return worked;
    }

    @Override
    protected void onPostExecute(Boolean worked) {
        if (worked) {
            FullListHolder.get().kustomWidgets().createList(widgets);
            FullListHolder.get().komponents().createList(komponents);
            FullListHolder.get().kustomWalls().createList(wallpapers);
            if (context.get() instanceof ShowcaseActivity) {
                if (((ShowcaseActivity) context.get()).getCurrentFragment() instanceof
                        MainFragment) {
                    ((MainFragment) ((ShowcaseActivity) context.get()).getCurrentFragment())
                            .updateAppInfoData();
                }
            }
            Timber.d("Load of kustom files task completed successfully in: %d milliseconds",
                    (endTime - startTime));
        } else {
            Timber.d("Something went really wrong while loading Kustom files");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean readKustomFiles(AssetManager assetManager, String folder) {
        try {
            String[] kustomFiles = assetManager.list(folder);
            File previewsFolder = new File(context.get().getExternalCacheDir(), IconUtils
                    .capitalizeText(folder) + "Previews");
            if (kustomFiles != null && kustomFiles.length > 0) {
                Utils.clean(previewsFolder);
                previewsFolder.mkdirs();
                for (String template : kustomFiles) {
                    File widgetPreviewFile = new File(previewsFolder, template);
                    String widgetName = Utils.getFilenameWithoutExtension(template);
                    String[] previews = getWidgetPreviewPathFromZip(widgetName, folder,
                            assetManager.open(folder + "/" + template), previewsFolder,
                            widgetPreviewFile);
                    if (previews != null) {
                        switch (folder) {
                            case "komponents":
                                komponents.add(new KustomKomponent(previews[0]));
                                break;
                            case "wallpapers":
                                wallpapers.add(new KustomWallpaper(template, previews[0],
                                        previews[1]));
                                break;
                            case "widgets":
                                widgets.add(new KustomWidget(template, previews[0], previews[1]));
                                break;
                        }
                    }
                    widgetPreviewFile.delete();
                }
                switch (folder) {
                    case "komponents":
                        return komponents.size() == kustomFiles.length;
                    case "wallpapers":
                        return wallpapers.size() == kustomFiles.length;
                    case "widgets":
                        return widgets.size() == kustomFiles.length;
                    default:
                        return false;
                }
            } else {
                return false;
            }
        } catch (IOException ex) {
            return false;
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ThrowFromFinallyBlock"})
    private String[] getWidgetPreviewPathFromZip(String name, String folder, InputStream in, File
            previewsFolder, File widgetPreviewFile) {
        OutputStream out;

        name.replaceAll(".komp", "");
        name.replaceAll(".kwgt", "");
        name.replaceAll(".klwp", "");

        String[] thumbNames = {"", ""};
        switch (folder) {
            case "komponents":
                thumbNames[0] = "komponent_thumb";
                break;
            default:
                thumbNames[0] = "preset_thumb_portrait";
                thumbNames[1] = "preset_thumb_landscape";
                break;
        }

        File preview1 = new File(previewsFolder, name + "_port.jpg");
        File preview2 = new File(previewsFolder, name + "_land.jpg");

        try {
            out = new FileOutputStream(widgetPreviewFile);
            Utils.copyFiles(in, out);
            in.close();
            out.close();

            if (widgetPreviewFile.exists()) {
                ZipFile zipFile = new ZipFile(widgetPreviewFile);
                Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();
                ZipEntry entry;
                while ((entry = entryEnum.nextElement()) != null) {
                    if (entry.getName().endsWith(thumbNames[0] + ".jpg")) {
                        InputStream zipIn = null;
                        OutputStream zipOut = null;
                        try {
                            zipIn = zipFile.getInputStream(entry);
                            zipOut = new FileOutputStream(preview1);
                            Utils.copyFiles(zipIn, zipOut);
                        } finally {
                            if (zipIn != null) zipIn.close();
                            if (zipOut != null) zipOut.close();
                        }
                    }

                    if (!(thumbNames[1].isEmpty())) {
                        if (entry.getName().endsWith(thumbNames[1] + ".jpg")) {
                            InputStream zipIn = null;
                            OutputStream zipOut = null;
                            try {
                                zipIn = zipFile.getInputStream(entry);
                                zipOut = new FileOutputStream(preview2);
                                Utils.copyFiles(zipIn, zipOut);
                            } finally {
                                if (zipIn != null) zipIn.close();
                                if (zipOut != null) zipOut.close();
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            //Do nothing
        }

        return new String[]{preview1.getAbsolutePath(), preview2.getAbsolutePath()};
    }

}