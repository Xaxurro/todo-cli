package core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Task {
    private static String DEEP_STR = "    ";

    private TaskStatus status;
    private String content;

    private static boolean doesRegexMatch(String str) {
//        TODO FIX REGEX
        return str.matches(String.format("^(%s)*(%s|%s|%s) ?.+$", DEEP_STR, TaskStatus.NOT_DONE, TaskStatus.ALMOST_DONE, TaskStatus.DONE));
    }

//        String format
//        <STATUS><Content>
    public static Task build(String str) {
        if (!doesRegexMatch(str)) {
            throw new IllegalArgumentException("Incorrect format at line: " + str);
        }

        Task task = new Task();
        task.setStatus(TaskStatus.fromString(str));

        String content = str.trim().substring(task.getStatus().toString().length()).trim();
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
        return status.toString() + " " + content.trim();
    }
}
