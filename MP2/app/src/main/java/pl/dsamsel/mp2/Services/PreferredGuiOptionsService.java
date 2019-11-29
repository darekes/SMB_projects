package pl.dsamsel.mp2.Services;

import android.content.SharedPreferences;
import android.widget.Button;

public class PreferredGuiOptionsService {

    private SharedPreferencesService sharedPreferencesService;

    public PreferredGuiOptionsService(SharedPreferences sharedPreferences) {
        init(sharedPreferences);
    }

    public void setPreferredColorForButton(Button button) {
        setPreferredBackgroundColorForButton(button);
        setPreferredFontColorForButton(button);
    }

    private void init(SharedPreferences sharedPreferences) {
        sharedPreferencesService = new SharedPreferencesService(sharedPreferences);
    }

    private void setPreferredBackgroundColorForButton(Button button) {
        button.setBackgroundColor(sharedPreferencesService
                .getInt(SharedPreferencesService.BUTTON_BACKGROUND_COLOR_KEY));
    }

    private void setPreferredFontColorForButton(Button button) {
        button.setTextColor(sharedPreferencesService
                .getInt(SharedPreferencesService.BUTTON_FONT_COLOR_KEY));
    }
}
