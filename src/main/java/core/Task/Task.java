package core.Task;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Task {

    private Status status;
    private Frequency frequency;
    private int priority = 0;
    private String content;
    private List<String> tags = new ArrayList<>();

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
        if (content.equals("ROOT") && priority == 0) {
            return "ROOT";
        }
        return priority + frequency.toString() + " " + status.toString() + " " + content.trim();
    }

    public void addTag(String newTag) {
        tags.add(newTag);
    }
    public boolean hasTag(String tag) {
        for (int i = 0; i < tags.size(); i++) {
            String actualTag = tags.get(i);
            if (actualTag.contains(tag)) {
                return true;
            }
        }
        return false;
    }
}
