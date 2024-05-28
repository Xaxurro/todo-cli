package core.Task;

import java.util.ArrayList;
import java.util.List;

public class NodeSearchOperations {

	public static Node withTag(Node node, String tag) {
		Node newNode = node.duplicate();

		if (!node.isRoot() && !node.getTask().hasTag(tag)) {
			return null;
		}

		for (Node child : node.getChildren()) {
			newNode.addChild(NodeSearchOperations.withTag(child, tag));
		}

		return newNode;
	}

	public static Node untilDeep(Node node, int actualDeep) {
		Node newNode = node.duplicate();

		if (actualDeep <= 0) {
			return newNode;
		}
		for (Node child : node.getChildren()) {
			newNode.addChild(NodeSearchOperations.untilDeep(child, actualDeep - 1));
		}

		return newNode;
	}

	public static Node whereIsDone(Node node) {
		if (!node.isRoot() && node.getTask().getStatus() == Status.NOT_DONE) {
			return null;
		}
		Node newNode = node.duplicate();
		for (Node child : node.getChildren()) {
			if (child.getTask().getStatus() != Status.NOT_DONE) {
				newNode.addChild(NodeSearchOperations.whereIsDone(child));
			}
		}

		return newNode;
	}

	public static Node whereIsNotDone(Node node) {
		if (node.getTask().getStatus() == Status.DONE) {
			return null;
		}
		Node newNode = node.duplicate();
		for (Node child : node.getChildren()) {
			if (child.getTask().getStatus() != Status.DONE) {
				newNode.addChild(NodeSearchOperations.whereIsNotDone(child));
			}
		}

		return newNode;
	}

    public static Node whereIsAlmostDone(Node node) {
        if (!node.isRoot() && node.getTask().getStatus() != Status.ALMOST_DONE) {
            return null;
        }
		Node newNode = node.duplicate();
		List<Node> newChildren = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getTask().getStatus() == Status.ALMOST_DONE) {
                newChildren.add(NodeSearchOperations.whereIsAlmostDone(child));
            } else if (!node.isRoot()) {
				newChildren.add(child.duplicate());
			}
        }

		newNode.addChildren(newChildren);

        return newNode;
    }
}
