package core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Task {
    private static String STATUS_NOT_DONE = "-";
    private static String STATUS_ALMOST_DONE = "/";
    private static String STATUS_DONE = "v";
    private static String DEEP_STR = "    ";

    private TaskStatus status;
    private String content;

    private static boolean doesRegexMatch(String str) {
//        TODO FIX REGEX
        return str.matches(String.format("^(%s)*(%s|%s|%s) ?.+$", DEEP_STR, STATUS_NOT_DONE, STATUS_ALMOST_DONE, STATUS_DONE));
    }

    private static TaskStatus matchStatusStr(String task) {
        boolean notDone = task.startsWith(STATUS_NOT_DONE);
        boolean almostDone = task.startsWith(STATUS_ALMOST_DONE);
        boolean done = task.startsWith(STATUS_DONE);

//        TODO Verify only one status is valid
        if ((notDone ^ almostDone) || (almostDone ^ done) || (notDone ^ done)) {
            return TaskStatus.NOT_DONE;
        }

        if (almostDone) return TaskStatus.ALMOST_DONE;
        if (done) return TaskStatus.DONE;

        return TaskStatus.NOT_DONE;
    }

//        String format
//        <STATUS><Content>
    public static Task build(String str) {
        if (!doesRegexMatch(str)) {
            throw new IllegalArgumentException("Incorrect format at line: " + str);
        }

        Task task = new Task();

        task.setStatus(matchStatusStr(str));

        String content = "";
        switch (task.getStatus()) {
            case NOT_DONE -> content = str.substring(STATUS_NOT_DONE.length());
            case ALMOST_DONE -> content = str.substring(STATUS_ALMOST_DONE.length());
            case DONE -> content = str.substring(STATUS_DONE.length());
        }

        task.setContent(content);

        return task;
    }

    public static Task build(TaskStatus status, String content) {
        Task task = new Task();
        task.setStatus(status);
        task.setContent(content);

        return task;
    }

    public static Task root() {
        Task task = new Task();
        task.setStatus(TaskStatus.NOT_DONE);
        task.setContent("ROOT");
        return task;
    }

    public static String getDeepStr() {
        return DEEP_STR;
    }

    public static int getDeep(String taskStr) {
        int deep = 0;
        String deepStr = Task.getDeepStr();
        if (taskStr.startsWith(deepStr)) {
            do {
                taskStr = taskStr.substring(deepStr.length());
                deep++;

            } while (taskStr.startsWith(deepStr));
        }

        return deep;
    }

    @Override
    public String toString() {
        return status + " " + content.trim();
    }
}
