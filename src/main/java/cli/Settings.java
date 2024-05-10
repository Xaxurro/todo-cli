package cli;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Settings {
    Map<String, String> settingsMap = new HashMap<>();

//    TODO FIX RETURN VOID
    public static void loadPreferences(String preferencesFilePath) throws IOException {
        Settings settings = new Settings();

        String preferencesData = Utils.readFile(preferencesFilePath).trim();
        if (preferencesData.equals("")) {
            return;
        }

        for (String line : preferencesData.split("\n")) {
            String[] setting = line.split(":");
            if (setting.length != 2) {
                throw new IOException("Illegal settings found:" + line);
            }

            settings.getSettingsMap().put(setting[0], setting[1]);
        }

    }

}
