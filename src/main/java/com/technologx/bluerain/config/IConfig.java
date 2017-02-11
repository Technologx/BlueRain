package com.technologx.bluerain.config;

import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;


public interface IConfig {

    //General Functions

    boolean bool(@BoolRes int id);

    String string(@StringRes int id);

    String[] stringArray(@ArrayRes int id);

    int integer(@IntegerRes int id);

    boolean hasString(@StringRes int id);

    boolean hasArray(@ArrayRes int id);

    //Main Configs

    boolean allowDebugging();

    boolean hasDonations();

    boolean hasGoogleDonations();

    boolean hasPaypal();

    @NonNull
    String getPaypalCurrency();

    boolean devOptions();

}