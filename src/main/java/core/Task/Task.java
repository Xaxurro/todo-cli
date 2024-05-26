package core.Task;

import cli.Preferences;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class Task {

    private Status status;
    private Frequency frequency;
    private int priority = 0;
    private String content;

//        String format
//        <STATUS><Content>

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

    @Override
    public String toString() {
        return frequency.toString() + " " + status.toString() + " " + content.trim();
    }
}
