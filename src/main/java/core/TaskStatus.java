package core;

public enum TaskStatus {
    NOT_DONE("-"),
    ALMOST_DONE("/"),
    DONE("v");

    private String str;

    TaskStatus(String str) {
        this.str = str;
    }

    public String toString() {
        return this.str;
    }

    public static TaskStatus fromString(String task) {
        task = task.trim();
        boolean notDone = task.startsWith(NOT_DONE.toString());
        boolean almostDone = task.startsWith(ALMOST_DONE.toString());
        boolean done = task.startsWith(DONE.toString());

//        TODO Verify only one status is valid
        if ((notDone && almostDone) || (almostDone && done) || (notDone && done)) {
            return NOT_DONE;
        }

        if (done) return DONE;
        if (almostDone) return ALMOST_DONE;

        return TaskStatus.NOT_DONE;
    }
}
