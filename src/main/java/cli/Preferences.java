package cli;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Preferences {
    static Map<String, String> preferencesMap = new HashMap<>();
//    TODO FIX RETURN VOID
    public static void loadPreferences(String preferencesFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(preferencesFilePath));
        for (String preferenceStr : lines) {
            int indexSeparator = preferenceStr.indexOf('=');
            String preferenceName = preferenceStr.substring(0, indexSeparator);
            String settingValue = preferenceStr.substring(indexSeparator + 1);

           preferencesMap.put(preferenceName, settingValue);
        }
    }

    public static String getStatusNotDoneStr() {
        return get("statusNotDoneStr", "[ ]");
    }

    public static String getStatusAlmostDoneStr() {
        return get("statusAlmostDoneStr", "[/]");
    }

    public static String getStatusDoneStr() {
        return get("statusDoneStr", "[x]");
    }

    public static String getFrequencyNoneStr() {
        return get("frequencyNoneStr", "-");
    }

    public static String getFrequencyDailyStr() {
        return get("frequencyDailyStr", "d");
    }

    public static String getFrequencyWeeklyStr() {
        return get("frequencyWeeklyStr", "w");
    }

    public static String getFrequencyMonthlyStr() {
        return get("frequencyMonthlyStr", "m");
    }

    public static String getFrequencyYearlyStr() {
        return get("frequencyYearlyStr", "y");
    }

    public static int getTaskDeepStrLength() {
        return Integer.valueOf(get("deepStrLength", "4"));
    }

    public static boolean isTabEqualsToSpace() {
        String value = get("isTabEqualsToSpace", "false");
        return value.equals("true");
    }

    private static String get(String preference, String defaultValue) {
        return preferencesMap.getOrDefault(preference, defaultValue);
    }
}
