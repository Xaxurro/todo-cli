package core.Task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class Node {
    Node parent = null;
    Task task = null;
//    int deep = 0;
    List<Node> children = new ArrayList<>();

    public Node(Task task) {
        this.task = task;
    }

//
    private static void sortTasksTree(Node tree, List<String> taskStrList) {
        int actualDeep = 0;
        Node previousNode = tree;

        for (String taskStr : taskStrList) {
            int taskDeep = Task.getDeep(taskStr);

            Task actualTask = Task.build(taskStr);
            Node actualNode = new Node(actualTask);

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
                Node ancestorNode = previousNode;
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

    public static Node build(String fileContents) {
        List<String> lines = fileContents.lines().collect(Collectors.toList());

        Node tree = new Node();
        tree.setTask(Task.root());

        if (lines.size() == 0) {
            return tree;
        }

        sortTasksTree(tree, lines);

        return tree;
    }

    public void setParent(Node newParent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = newParent;
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChildren(List<Node> children) {
        for (Node child : children) {
            addChild(child);
        }
    }

    public Node getChild(int i) {
        return children.get(i);
    }

    @Override
    public String toString() {
        return task.toString();
    }

    private void print(int actualDeep) {
        if (isRoot()) {
            for (Node child : children) {
                child.print();
            }
            return;
        }
        String indent = "\t";
        String taskContent = task.toString();
        System.out.println(indent.repeat(actualDeep) + taskContent);
        for (Node child : children) {
            child.print(actualDeep + 1);
        }
    }

    public void print() {
        print(0);
    }

    public Node untilDeep(int actualDeep) {
        Node newNode = new Node(task);

        if (actualDeep <= 0) {
            return newNode;
        }
        for (Node child : children) {
            newNode.addChild(child.untilDeep(actualDeep - 1));
        }

        return newNode;
    }

    public Node whereIsDone() {
        if (!isRoot() && task.getStatus() == Status.NOT_DONE) {
            return null;
        }
        Node newNode = new Node(task);
        for (Node child : children) {
            if (child.getTask().getStatus() != Status.NOT_DONE) {
                newNode.addChild(child.whereIsDone());
            }
        }

        return newNode;
    }

    public Node whereIsNotDone() {
        if (task.getStatus() == Status.DONE) {
            return null;
        }
        Node newNode = new Node(task);
        for (Node child : children) {
            if (child.getTask().getStatus() != Status.DONE) {
                newNode.addChild(child.whereIsNotDone());
            }
        }

        return newNode;
    }

//    public TaskNode whereIsAlmostDone() {
//        if (task.getStatus() != TaskStatus.ALMOST_DONE) {
//            return null;
//        }
//        TaskNode newNode = new TaskNode(task);
//        for (TaskNode child : children) {
//            if (child.getTask().getStatus() == TaskStatus.ALMOST_DONE) {
//                newNode.addChild(child.whereIsNotDone());
//            }
//        }
//
//        return newNode;
//    }
}
