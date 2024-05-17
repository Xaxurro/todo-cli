package core.Task;

import cli.Preferences;

public enum Status {
    NOT_DONE(Preferences.getStatusNotDoneStr()),
    ALMOST_DONE(Preferences.getStatusAlmostDoneStr()),
    DONE(Preferences.getStatusDoneStr());

    private String str;

    Status(String str) {
        this.str = str;
    }

    public String toString() {
        return this.str;
    }

    public static Status fromString(String task) {
        task = task.trim();
        boolean notDone = task.equals(NOT_DONE.toString());
        boolean almostDone = task.equals(ALMOST_DONE.toString());
        boolean done = task.equals(DONE.toString());

////        TODO Verify only one status is valid
//        if ((notDone && almostDone) || (almostDone && done) || (notDone && done)) {
//            return NOT_DONE;
//        }

        if (done) return DONE;
        if (almostDone) return ALMOST_DONE;

        return Status.NOT_DONE;
    }
}
