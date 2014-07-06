package baiya.flashlight.controler;

import android.content.Context;

public class PreferencesManager {

    public static final String CACHE = "share";

    public static final String SELECTED_COLOR_ARRAY_INDEX = "selected_color_array_index";
    public static final String SELECTED_COLOR_INDEX = "selected_color_index";
    public static final String SCREEN_COLOR = "screen_color";
    public static final String SCREEN_TIPS = "screen_tips";
    public static final String LIGHT_TIPS = "light_tips";
    public static final String LIGHT_ON_OPEN = "light_on_open";

    public static int getIntPreference(Context context, String key, int defValue) {
        return context.getSharedPreferences(CACHE, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static boolean getBooleanPreference(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(CACHE, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static void setIntPreference(Context context, String key, int value) {
        context.getSharedPreferences(CACHE, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .commit();
    }

    public static void setBooleanPreference(Context context, String key, boolean value) {
        context.getSharedPreferences(CACHE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .commit();
    }

}
