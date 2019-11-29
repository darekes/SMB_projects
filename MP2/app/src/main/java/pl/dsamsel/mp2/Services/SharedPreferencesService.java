package pl.dsamsel.mp2.Services;

import android.content.SharedPreferences;
import android.graphics.Color;

public class SharedPreferencesService {

    public static final String COLOR_PREFERENCES = "color_preferences";
    public static final String BUTTON_BACKGROUND_COLOR_KEY = "background_color_key";
    public static final String BUTTON_FONT_COLOR_KEY = "font_color_key";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    int getInt(String key) {
        if (containsKey(key)) {
            return sharedPreferences.getInt(key, Color.YELLOW);
        }

        return Color.YELLOW;
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private boolean containsKey(String key) {
        return sharedPreferences.contains(key);
    }
}
