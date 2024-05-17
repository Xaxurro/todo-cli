package core.Task;

import cli.Preferences;

public enum Frequency {
    NONE(Preferences.getFrequencyNoneStr()),
    DAILY(Preferences.getFrequencyDailyStr()),
    WEEKLY(Preferences.getFrequencyWeeklyStr()),
    MONTHLY(Preferences.getFrequencyMonthlyStr()),
    YEARLY(Preferences.getFrequencyYearlyStr());

    private String str;

    Frequency(String str) {
        this.str = str;
    }

    public String toString() {
        return this.str;
    }

    public static Frequency fromString(String task) {
        task = task.trim();
        boolean daily = task.startsWith(DAILY.toString());
        boolean weekly = task.startsWith(WEEKLY.toString());
        boolean monthly = task.startsWith(MONTHLY.toString());
        boolean yearly = task.startsWith(YEARLY.toString());

////        TODO Verify only one status is valid
//        if ((notDone && almostDone) || (almostDone && done) || (notDone && done)) {
//            return NOT_DONE;
//        }

        if (daily) return DAILY;
        if (weekly) return WEEKLY;
        if (monthly) return MONTHLY;
        if (yearly) return YEARLY;

        return NONE;
    }
}
