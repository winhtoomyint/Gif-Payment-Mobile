package com.example.paybill.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigUtils {

    private static final String KEY_LOCALE = "KEY_LOCALE";

    private static ConfigUtils mObjInstance;

    private SharedPreferences mSharedPreferences;

    private ConfigUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences("ConfigUtils", Context.MODE_PRIVATE);
    }

    public static ConfigUtils getInstance(Context context) {
        if (mObjInstance == null) {
            mObjInstance = new ConfigUtils(context);
        }
        return mObjInstance;
    }

    public void saveLocale(String locale) {
        mSharedPreferences.edit().putString(KEY_LOCALE, locale).apply();
    }

    public String loadLocale() {
        return mSharedPreferences.getString(KEY_LOCALE,"en");
    }
}
