/*
 * Copyright (c) 2017 Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Special thanks to the project contributors and collaborators
 * 	https://github.com/jahirfiquitiva/IconShowcase#special-thanks
 */

package com.technologx.bluerain.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.capsule.library.event.CFabEvent;
import com.pitchedapps.capsule.library.fragments.CapsuleFragment;
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

import java.util.ArrayList;

import com.technologx.bluerain.R;
import com.technologx.bluerain.activities.ShowcaseActivity;
import com.technologx.bluerain.activities.base.DrawerActivity;
import com.technologx.bluerain.adapters.KustomAdapter;
import com.technologx.bluerain.config.Config;
import com.technologx.bluerain.dialogs.ISDialogs;
import com.technologx.bluerain.utilities.utils.Utils;
import com.technologx.bluerain.views.SectionedGridSpacingItemDecoration;

public class KustomFragment extends CapsuleFragment {

    private KustomAdapter kustomAdapter;
    private static final String KLWP_PKG = "org.kustom.wallpaper",
            KWGT_PKG = "org.kustom.widget",
            KOLORETTE_PKG = "com.arun.themeutil.kolorette";
    private Context context;
    private SectionedGridSpacingItemDecoration space;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        context = getActivity();

        View layout = inflater.inflate(R.layout.zooper_section, container, false);
        setupRV(layout);
        if (areAppsInstalled()) hideFab();

        return layout;
    }

    @Override
    public int getTitleId() {
        return DrawerActivity.DrawerItem.KUSTOM.getTitleID();
    }

    @Nullable
    @Override
    protected CFabEvent updateFab() {
        return new CFabEvent(R.drawable.ic_store_download, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> apps = new ArrayList<>();
                if ((Config.get().bool(R.bool.includes_kustom_wallpapers))
                        && !Utils.isAppInstalled(context, KLWP_PKG)) {
                    apps.add("Kustom Live Wallpaper");
                } else if ((Config.get().bool(R.bool.includes_kustom_widgets))
                        && !Utils.isAppInstalled(context, KWGT_PKG)) {
                    apps.add("Kustom Widget");
                } else if ((Config.get().bool(R.bool.kustom_requires_kolorette)) &&
                        !Utils.isAppInstalled(context, KOLORETTE_PKG)) {
                    apps.add(Config.get().string(R.string.kolorette_app));
                }
                if (apps.size() > 0) {
                    ISDialogs.showKustomAppsDownloadDialog(context, apps);
                } else {
                    hideFab();
                    //TODO: Show snackbar saying something like "Apps installed" although this
                    // shouldn't ever happen.
                }
            }
        });
    }

    private void setupRV(View layout) {
        int gridSpacing = getResources().getDimensionPixelSize(R.dimen.lists_padding);
        final int columnsNumber = getResources().getInteger(R.integer.zooper_kustom_grid_width);

        RecyclerView mRecyclerView = (RecyclerView) layout.findViewById(R.id.zooper_rv);

        if (space != null) {
            mRecyclerView.removeItemDecoration(space);
        }

        GridLayoutManager gridManager = new GridLayoutManager(context, columnsNumber);

        RecyclerFastScroller fastScroller = (RecyclerFastScroller) layout.findViewById(R.id
                .rvFastScroller);

        kustomAdapter = new KustomAdapter(context, ((ShowcaseActivity) context)
                .getWallpaperDrawable());

        space = new SectionedGridSpacingItemDecoration(columnsNumber, gridSpacing, true,
                kustomAdapter);

        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (kustomAdapter.isHeader(position)) {
                    return columnsNumber;
                } else {
                    return 1;
                }
            }
        });

        mRecyclerView.addItemDecoration(space);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridManager);
        kustomAdapter.setLayoutManager(gridManager);
        mRecyclerView.setAdapter(kustomAdapter);
        fastScroller.attachRecyclerView(mRecyclerView);
    }

    private boolean areAppsInstalled() {
        boolean installed = true;

        if ((context.getResources().getBoolean(R.bool.includes_kustom_wallpapers))) {
            installed = Utils.isAppInstalled(context, KLWP_PKG);
        }

        if ((context.getResources().getBoolean(R.bool.includes_kustom_widgets)) && installed) {
            installed = Utils.isAppInstalled(context, KWGT_PKG);
        }

        if ((context.getResources().getBoolean(R.bool.kustom_requires_kolorette)) && installed) {
            installed = Utils.isAppInstalled(context, KOLORETTE_PKG);
        }

        return installed;
    }

}