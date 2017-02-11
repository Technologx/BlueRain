package com.technologx.bluerain.tasks;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class CopyFilesToStorage extends AsyncTask<Void, String, Boolean> {

    private final WeakReference<Context> context;
    private final MaterialDialog dialog;
    private final String folder;
    private final View layout;

    public CopyFilesToStorage(Context context, View layout, MaterialDialog dialog, String folder) {
        this.context = new WeakReference<>(context);
        this.layout = layout;
        this.dialog = dialog;
        this.folder = folder;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean worked;
        try {
            AssetManager assetManager = context.get().getAssets();
            String[] files = assetManager.list(folder);

            if (files != null) {
                for (String filename : files) {
                    InputStream in = null;
                    OutputStream out = null;
                    if (filename.contains(".")) {
                        try {
                            String fileToIgnore1 = "material-design-iconic-font-v2.2.0.ttf",
                                    fileToIgnore2 = "materialdrawerfont.ttf",
                                    fileToIgnore3 = "materialdrawerfont-font-v5.0.0.ttf",
                                    fileToIgnore4 = "google-material-font-v2.2.0.1.original.ttf";
                            if (!filename.equals(fileToIgnore1) && !filename.equals(fileToIgnore2)
                                    && !filename.equals(fileToIgnore3) && !filename.equals
                                    (fileToIgnore4)) {
                                in = assetManager.open(folder + "/" + filename);
                                out = new FileOutputStream(
                                        Environment.getExternalStorageDirectory().toString() +
                                                "/ZooperWidget/" + getFolderName(folder) + "/" +
                                                filename);
                                copyFiles(in, out);
                            }
                        } catch (Exception e) {
                            //Do nothing
                        }
                    }
                    if (in != null) in.close();
                    if (out != null) out.close();
                }
            }
            worked = true;
        } catch (Exception e2) {
            worked = false;
        }
        return worked;
    }

    private void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }

    private String getFolderName(String folder) {
        switch (folder) {
            case "fonts":
                return "Fonts";
            case "iconsets":
                return "IconSets";
            case "bitmaps":
                return "Bitmaps";
            default:
                return folder;
        }
    }

}