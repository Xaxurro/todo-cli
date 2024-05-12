package core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class TaskNode {
    TaskNode parent = null;
    Task task = null;
//    int deep = 0;
    List<TaskNode> children = new ArrayList<>();

    public TaskNode(Task task) {
        this.task = task;
    }

//
    private static void sortTasksTree(TaskNode tree, List<String> taskStrList) {
        int actualDeep = 0;
        TaskNode previousNode = tree;

        for (String taskStr : taskStrList) {
            int taskDeep = Task.getDeep(taskStr);

            Task actualTask = Task.build(taskStr);
            TaskNode actualNode = new TaskNode(actualTask);

            if (taskDeep > actualDeep) {
                previousNode.addChild(actualNode);
                actualDeep++;

            } else if (taskDeep == actualDeep) {
                if (previousNode != tree) {
                    previousNode.getParent().addChild(actualNode);
                    continue;
                }
                tree.addChild(actualNode);

            } else if (taskDeep < actualDeep) {
                TaskNode ancestorNode = previousNode;
                while (taskDeep < actualDeep) {
                    ancestorNode = ancestorNode.getParent();
                    actualDeep--;
                }
                ancestorNode.getParent().addChild(actualNode);
            }

            previousNode = actualNode;
        }

    }

    private boolean isRoot() {
        return parent == null && task.getContent().equals("ROOT");
    }

    public static TaskNode build(String fileContents) {
        List<String> lines = fileContents.lines().collect(Collectors.toList());

        TaskNode tree = new TaskNode();
        tree.setTask(Task.root());

        if (lines.size() == 0) {
            return tree;
        }

        sortTasksTree(tree, lines);

        return tree;
    }

    public void setParent(TaskNode newParent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = newParent;
    }

    public void addChild(TaskNode child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChildren(List<TaskNode> children) {
        for (TaskNode child : children) {
            addChild(child);
        }
    }

    public TaskNode getChild(int i) {
        return children.get(i);
    }

    @Override
    public String toString() {
        return task.toString();
    }

    private void print(int actualDeep) {
        if (isRoot()) {
            for (TaskNode child : children) {
                child.print();
            }
            return;
        }
        String indent = "\t";
        String taskContent = task.toString();
        System.out.println(indent.repeat(actualDeep) + taskContent);
        for (TaskNode child : children) {
            child.print(actualDeep + 1);
        }
    }

    public void print() {
        print(0);
    }

    public TaskNode untilDeep(int actualDeep) {
        TaskNode newNode = new TaskNode(task);

        if (actualDeep <= 0) {
            return newNode;
        }
        for (TaskNode child : children) {
            newNode.addChild(child.untilDeep(actualDeep - 1));
        }

        return newNode;
    }

    public TaskNode whereIsDone() {
        if (!isRoot() && task.getStatus() == TaskStatus.NOT_DONE) {
            return null;
        }
        TaskNode newNode = new TaskNode(task);
        for (TaskNode child : children) {
            if (child.getTask().getStatus() != TaskStatus.NOT_DONE) {
                newNode.addChild(child.whereIsDone());
            }
        }

        return newNode;
    }

    public TaskNode whereIsNotDone() {
        if (task.getStatus() == TaskStatus.DONE) {
            return null;
        }
        TaskNode newNode = new TaskNode(task);
        for (TaskNode child : children) {
            if (child.getTask().getStatus() != TaskStatus.DONE) {
                newNode.addChild(child.whereIsNotDone());
            }
        }

        return newNode;
    }

    public TaskNode whereIsAlmostDone() {
        if (task.getStatus() != TaskStatus.ALMOST_DONE) {
            return null;
        }
        TaskNode newNode = new TaskNode(task);
        for (TaskNode child : children) {
            if (child.getTask().getStatus() == TaskStatus.ALMOST_DONE) {
                newNode.addChild(child.whereIsNotDone());
            }
        }

        return newNode;
    }
}
