package core.Task;

import cli.Preferences;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class Task {
    private static int DEEP_STR_LENGTH = Preferences.getTaskDeepStrLength();
    private static boolean IS_TAB_EQUALS_SPACE = false;

    private Status status;
    private Frequency frequency;
    private int priority = 0;
    private String content;

    private static String escapeString(String str) {
        String newStr = "";

        String mustEscapeCharacters = "\\.[]{}()<>*+-=!?^$|";

        for (char cStr : str.toCharArray()) {
            for (char cMustEscape : mustEscapeCharacters.toCharArray()) {
                if (cStr == cMustEscape) {
                    newStr += '\\';
                }
            }
            newStr += cStr;
        }

        return newStr;
    }

    private static String escapeString(Status status) {
        return escapeString(status.toString());
    }

    private static String escapeString(Frequency frequency) {
        return escapeString(frequency.toString());
    }

    private static Matcher getRegexMatcher(String str) {
        String taskDeepRegex = String.format(" {%s}", DEEP_STR_LENGTH);
        if (Preferences.isTabEqualsToSpace()) {
            taskDeepRegex += "|\t";
        }

        String priorityRegex = "[0-9]+";
        String frequencyRegex = String.format("%s%s%s%s%s", escapeString(Frequency.NONE), escapeString(Frequency.DAILY), escapeString(Frequency.WEEKLY), escapeString(Frequency.MONTHLY), escapeString(Frequency.YEARLY));
        String statusRegex = String.format("%s|%s|%s", escapeString(Status.NOT_DONE), escapeString(Status.ALMOST_DONE), escapeString(Status.DONE));

        String patternStr = String.format("^(?:%s)*((%s) ?[%s]?|[%s]?) ?(%s) ?(.+)$", taskDeepRegex, priorityRegex, frequencyRegex, frequencyRegex, statusRegex);

        Pattern pattern = Pattern.compile(patternStr);
        return pattern.matcher(str);

//        return str.matches(String.format("^(%s)*(%s|%s|%s) ?.+$", escapeString(DEEP_STR), escapeString(Status.NOT_DONE), escapeString(Status.ALMOST_DONE), escapeString(Status.DONE)));
    }

//        String format
//        <STATUS><Content>
    public static Task build(String str) {
        Matcher matcher = getRegexMatcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Incorrect format at line: " + str);
        }

        Task task = new Task();
        Frequency frequency;
        int priority = 0;
        if (matcher.group(2) != null) {
            frequency = Frequency.fromString(matcher.group(1).substring(matcher.group(2).length()));
            priority = Integer.valueOf(matcher.group(2));
        } else {
            frequency = Frequency.fromString(matcher.group(1));
        }
        task.setFrequency(frequency);
        task.setPriority(priority);
        task.setStatus(Status.fromString(matcher.group(3)));
        task.setContent(matcher.group(4));

        return task;
    }

    public static Task build(Status status, String content) {
        Task task = new Task();
        task.setStatus(status);
        task.setContent(content);

        return task;
    }

    public static Task root() {
        Task task = new Task();
        task.setStatus(Status.NOT_DONE);
        task.setContent("ROOT");
        return task;
    }

    public static int getDeep(String taskStr) {
        int deep = 0;
        String deepStr = " ".repeat(DEEP_STR_LENGTH);

        if (IS_TAB_EQUALS_SPACE) {
            while (!taskStr.equals(taskStr.stripLeading())); {
                if (taskStr.startsWith(deepStr)) {
                    taskStr = taskStr.substring(DEEP_STR_LENGTH);
                    deep++;
                } else if (taskStr.startsWith("\t")) {
                    taskStr = taskStr.substring(1);
                    deep++;
                }
            }
        } else {
            int originalLength = taskStr.length();
            int strippedLength = taskStr.stripLeading().length();
            deep = originalLength - strippedLength;
            if (taskStr.startsWith(deepStr)) {
                return deep / DEEP_STR_LENGTH;
            }
            return deep;
        }


        return deep;
    }

    @Override
    public String toString() {
        return frequency.toString() + " " + status.toString() + " " + content.trim();
    }
}
