package org.ieszaidinvergeles.android.webizv.util;

// WIzv

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    final private SharedPreferences sp;
    final private SharedPreferences.Editor ed;

    public Preferences(Context c) {
        sp = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
        ed = this.sp.edit();
    }

    public String get(String name) {
        return this.sp.getString(name, "");
    }

    public String get(String name, String value) {
        return this.sp.getString(name, value);
    }

    public boolean set(String name, String value) {
        ed.putString(name, value);
        return ed.commit();
    }

    public boolean unSet(String name) {
        ed.remove(name);
        return ed.commit();
    }
}