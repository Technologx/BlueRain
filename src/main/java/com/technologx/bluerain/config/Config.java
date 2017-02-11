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

package com.technologx.bluerain.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.preference.Preference;
import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.technologx.bluerain.BuildConfig;
import com.technologx.bluerain.R;
import timber.log.Timber;

public class Config implements IConfig {

    public static final String MARKET_URL = "https://play.google.com/store/apps/details?id=",
            PLAY_STORE_INSTALLER = "com.google.android.feedback",
            PLAY_STORE_PACKAGE = "com.android.vending",

    @SuppressLint("StaticFieldLeak")
    private static Config mConfig;
    private Context mContext;
    private Resources mR;

    private Config(@Nullable Context context) {
        mR = null;
        mContext = context;
        if (context != null)
            mR = context.getResources();
    }

    public static void init(@NonNull Context context) {
        mConfig = new Config(context);
    }

    public static void setContext(Context context) {
        if (mConfig != null) {
            mConfig.mContext = context;
            if (context != null)
                mConfig.mR = context.getResources();
        }
    }

    public static void deinit() {
        if (mConfig != null) {
            mConfig.destroy();
            mConfig = null;
        }
    }

    @NonNull
    public static IConfig get() {
        if (mConfig == null)
            return new Config(null); // shouldn't ever happen, but avoid crashes
        return mConfig;
    }

    @NonNull
    public static IConfig get(@NonNull Context context) {
        if (mConfig == null)
            return new Config(context);
        return mConfig;
    }

    private void destroy() {
        mContext = null;
        mR = null;
    }

    // Getters

    private Preference prefs() {
        return new Preference(mContext);
    }

    @Override
    public boolean bool(@BoolRes int id) {
        return mR != null && mR.getBoolean(id);
    }

    @Override
    @Nullable
    public String string(@StringRes int id) {
        if (mR == null) return null;
        return mR.getString(id);
    }

    @Override
    @Nullable
    public String[] stringArray(@ArrayRes int id) {
        if (mR == null) return null;
        return mR.getStringArray(id);
    }

    @Override
    public int integer(@IntegerRes int id) {
        if (mR == null) return 0;
        return mR.getInteger(id);
    }

    @Override
    public boolean hasString(@StringRes int id) {
        String s = string(id);
        return (s != null && !s.isEmpty());
    }

    @Override
    public boolean hasArray(@ArrayRes int id) {
        String[] s = stringArray(id);
        return (s != null && s.length != 0);
    }

    @Override
    public boolean allowDebugging() {
        return BuildConfig.DEBUG || mR == null || mR.getBoolean(R.bool.debugging);
    }


    @Override
    public boolean hasDonations() {
        return hasGoogleDonations() || hasPaypal();
    }

    @Override
    public boolean hasGoogleDonations() { //Also check donation key from java
        return hasArray(R.array.google_donations_catalog) && hasArray(R.array
                .google_donations_items);
    }

    @Override
    public boolean hasPaypal() {
        return hasString(R.string.paypal_email);
    }

    @NonNull
    @Override
    public String getPaypalCurrency() {
        String s = string(R.string.paypal_currency_code);
        if (s == null || s.length() != 3) {
            Timber.d("Invalid currency $s; switching to USD", s);
            return "USD";
        }
        return s;
    }

    @Override
    public boolean devOptions() {
        return bool(R.bool.dev_options);
    }
}
