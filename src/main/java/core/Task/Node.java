package core.Task;

import cli.Preferences;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@NoArgsConstructor
@Getter
@Setter
public class Node {
    Node parent = null;
    Task task = null;
    List<Node> children = new ArrayList<>();

    public Node(Task task) {
        this.task = task;
    }

    public static Node root() {
        return new Node(Task.root());
    }

    public static void createTaskTree(Node root, List<String> taskStrList) throws IOException {
        int actualDeep = 0;
        Node previousNode = root;

        String actualTag = "";

        for (String taskStr : taskStrList) {
            if (taskStr.isEmpty()) {
                actualTag = "";
                continue;
            }

            if (taskStr.strip().startsWith(Preferences.getCommentStr())) {
                continue;
            }

            if (taskStr.strip().startsWith(Preferences.getTagStr())) {
                actualTag = taskStr.strip().substring(Preferences.getTagStr().length());
                continue;
            }

//          Finds parent for new Node, 1 gets added to deep because root has deep = 0
            int taskDeep = RegexHandler.getDeep(taskStr) + 1;
            Node parent;
            if (previousNode == root) {
                parent = root;
            } else {
                parent = findParent(previousNode, taskDeep, actualDeep);
            }

//          Creates new node
            Node actualNode;

//          If it's an appended file, add the children to the parent
            if (RegexHandler.fileRegex(taskStr).matches()) {
                Matcher fileMatcher = RegexHandler.fileRegex(taskStr);
                if (fileMatcher.matches()) {
                    actualNode = FileHandler.fileNode(taskStr, fileMatcher);
                    addTag(actualTag, actualNode);
                    parent.addChildren(actualNode.children);
                }
            }

//          If it's a task, add the new node to the parent
            if (RegexHandler.taskRegex(taskStr).matches()) {
                Matcher taskMatcher = RegexHandler.taskRegex(taskStr);
                if (taskMatcher.matches()) {
                    actualNode = FileHandler.taskNode(taskMatcher);
                    addTag(actualTag, actualNode);
                    parent.addChild(actualNode);
                    previousNode = actualNode;
                }
            }

            actualDeep = previousNode.getDeep();
        }

    }

    public static void addTag(String tag, Node node) {
        if (tag.isBlank()) return;

        node.getTask().addTag(tag);
        for (Node child : node.getChildren()) {
            addTag(tag, child);
        }
    }

    private static Node findParent(Node previousNode, int taskDeep, int actualDeep) {
        if (taskDeep > actualDeep) {
            return previousNode;

        } else if (taskDeep == actualDeep) {
            return previousNode.getParent();

        } else if (taskDeep < actualDeep) {
            Node ancestorNode = previousNode;
            while (taskDeep < actualDeep) {
                ancestorNode = ancestorNode.getParent();
                actualDeep--;
            }
            if (ancestorNode.isRoot()) {
                return ancestorNode;
            }
            return ancestorNode.getParent();
        }

        throw new RuntimeException("There is no suitable parent for task after: " + previousNode);
    }

    private boolean isRoot() {
        return parent == null && task.getContent().equals("ROOT");
    }

    public void setParent(Node newParent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = newParent;
    }

    public void addChild(Node child) {
        if (child == null) return;
        child.setParent(this);
        children.add(child);
    }

    public void addChildren(List<Node> foreingChildren) {
        children.addAll(foreingChildren);
        int amountOfForeingChildren = foreingChildren.size();
        for (int i = 0; i < amountOfForeingChildren; i++) {
            foreingChildren.get(0).setParent(this);
        }
    }

    public Node getChild(int i) {
        return children.get(i);
    }

    public int getDeep() {
        Node pointer = this;
        int deep = 0;

        while (!pointer.isRoot()) {
            pointer = pointer.getParent();
            deep++;
        }

        return deep;
    }

    @Override
    public String toString() {
        if (isRoot()) {
            return "ROOT";
        }
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

    public Node withTag(String tag) {
        Node newNode = new Node(task);

        if (!isRoot() && !task.hasTag(tag)) {
            return null;
        }

        for (Node child : children) {
            newNode.addChild(child.withTag(tag));
        }

        return newNode;
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
