package com.example.paybill.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Helper {

    public static void changeLocation(Context context, String languageCode) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode.toLowerCase())); // API 17+ only.
        res.updateConfiguration(conf, dm);
    }
}
