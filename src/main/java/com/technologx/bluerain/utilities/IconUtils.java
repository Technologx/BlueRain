package com.technologx.bluerain.utilities;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.technologx.bluerain.R;

public class IconUtils {

    private static final int SPACE = 1;
    private static final int CAPS = 2;
    private static final int CAPS_LOCK = 3;

    public static Drawable getTintedDrawable(@NonNull Context context, String name) {
        return getTintedIcon(context, getIconResId(context.getResources(), context.getPackageName
                (), name), ThemeUtils.darkOrLight(context, R.color.drawable_tint_dark, R.color
                .drawable_tint_light));
    }

    public static Drawable getTintedIcon(@NonNull Context context, @DrawableRes int drawable,
                                         @ColorInt int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return getTintedIcon(ContextCompat.getDrawable(context, drawable), color);
            } else {
                Drawable icon = VectorDrawableCompat.create(context.getResources(), drawable, null);
                return getTintedIcon(icon, color);
            }
        } catch (Resources.NotFoundException ex) {
            return getTintedIcon(ContextCompat.getDrawable(context, R.drawable.ic_android), color);
        }
    }

    @CheckResult
    @Nullable
    public static Drawable getTintedIcon(Drawable drawable, int color) {
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (drawable instanceof VectorDrawable) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
                drawable = DrawableCompat.wrap(drawable.mutate());
            } else {
                drawable = DrawableCompat.wrap(drawable);
            }
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            DrawableCompat.setTint(drawable, color);
            return drawable;
        } else {
            return null;
        }
    }


    public static String formatName(String mName) {
        StringBuilder sb = new StringBuilder();
        int underscoreMode = 0;
        boolean foundFirstLetter = false;
        boolean lastWasLetter = false;

        for (int i = 0; i < mName.length(); i++) {
            final char c = mName.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                if (underscoreMode == SPACE) {
                    sb.append(' ');
                    underscoreMode = CAPS;
                }
                if (!foundFirstLetter && underscoreMode == CAPS)
                    sb.append(c);
                else sb.append(i == 0 || underscoreMode > 1 ? Character.toUpperCase(c) : c);
                if (underscoreMode < CAPS_LOCK)
                    underscoreMode = 0;
                foundFirstLetter = true;
                lastWasLetter = true;
            } else if (c == '_') {
                if (underscoreMode == CAPS_LOCK) {
                    if (lastWasLetter) {
                        underscoreMode = SPACE;
                    } else {
                        sb.append(c);
                        underscoreMode = 0;
                    }
                } else {
                    underscoreMode++;
                }
                lastWasLetter = false;
            }
        }
        return sb.toString();
    }

    public static String capitalizeText(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static int getIconResId(Resources r, String p, String name) {
        int res = r.getIdentifier(name, "drawable", p);
        if (res != 0) {
            return res;
        } else {
            return 0;
        }
    }

    public static Drawable getVectorDrawable(@NonNull Context context, @DrawableRes int drawable) {
        Drawable vectorDrawable;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vectorDrawable = ContextCompat.getDrawable(context, drawable);
            } else {
                vectorDrawable = VectorDrawableCompat.create(context.getResources(), drawable,
                        null);
                if (vectorDrawable != null) {
                    vectorDrawable = DrawableCompat.wrap(vectorDrawable);
                }
            }
        } catch (Resources.NotFoundException ex) {
            vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_android);
        }
        return vectorDrawable != null ? vectorDrawable : null;
    }

}