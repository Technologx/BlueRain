package com.technologx.bluerain.tasks;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.technologx.bluerain.R;
import com.technologx.bluerain.MainActivity;
import com.technologx.bluerain.holders.lists.FullListHolder;
import com.technologx.bluerain.models.ZooperWidget;
import com.technologx.bluerain.utilities.Utils;
import timber.log.Timber;

import static android.R.attr.name;
import static java.lang.System.in;

public class LoadZooperWidgets extends AsyncTask<Void, String, Boolean> {

    private final ArrayList<ZooperWidget> widgets = new ArrayList<>();
    private final WeakReference<Context> context;
    private long startTime, endTime;

    public LoadZooperWidgets(Context context) {
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
            String[] templates = assetManager.list("templates");

            File previewsFolder = new File(context.get().getExternalCacheDir(),
                    "ZooperWidgetsPreviews");

            if (templates != null && templates.length > 0) {
                Utils.clean(previewsFolder);
                previewsFolder.mkdirs();
                for (String template : templates) {
                    File widgetPreviewFile = new File(previewsFolder, template);
                    String widgetName = Utils.getFilenameWithoutExtension(template);
                    String preview = getWidgetPreviewPathFromZip(context, widgetName,
                            assetManager.open("templates/" + template), previewsFolder,
                            widgetPreviewFile);
                    widgets.add(new ZooperWidget(preview));
                    widgetPreviewFile.delete();
                }
                worked = widgets.size() == templates.length;
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
            FullListHolder.get().zooperList().createList(widgets);
            if (context.get() instanceof MainActivity) {
                if (((MainActivity) context.get()).getCurrentActivity() instanceof
                        MainActivity) {
                    ((MainActivity) ((MainActivity) context.get()).getCurrentActivity())
                            .updateAppInfoData();
                }
            Timber.d("Load of widgets task completed successfully in: %d milliseconds", (endTime
                    - startTime));
        } else {
            Timber.d("Something went really wrong while loading zooper widgets.");
        }

    }

    /**
     * This code was created by Aidan Follestad. Complete credits to him.
     */
    @SuppressWarnings("ThrowFromFinallyBlock")
    private String getWidgetPreviewPathFromZip(WeakReference<Context> context, String name,
                                               InputStream in,
                                               File previewsFolder, File widgetPreviewFile) {
        OutputStream out;
        File preview = new File(previewsFolder, name + ".png");

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
                    if (entry.getName().endsWith("screen.png")) {
                        InputStream zipIn = null;
                        OutputStream zipOut = null;
                        try {
                            zipIn = zipFile.getInputStream(entry);
                            zipOut = new FileOutputStream(preview);
                            Utils.copyFiles(zipIn, zipOut);
                        } finally {
                            if (zipIn != null) zipIn.close();
                            if (zipOut != null) zipOut.close();
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            //Do nothing
        }

        if (context.get().getResources().getBoolean(R.bool.remove_zooper_previews_background)) {
            out = null;
            try {
                Bitmap bmp = ZooperWidget.getTransparentBackgroundPreview(
                        BitmapFactory.decodeFile(preview.getAbsolutePath()));
                out = new FileOutputStream(preview);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                Timber.d("ZooperIOException", e.getLocalizedMessage());
            } finally {
                try {
                    if (out != null) out.close();
                } catch (IOException e1) {
                    Timber.d("ZooperIOException2", e1.getLocalizedMessage());
                }
            }
        }

        return preview.getAbsolutePath();
    }

}