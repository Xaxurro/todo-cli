package core.node;

import core.task.Frequency;
import core.task.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NodeOperations {
	private static Consumer<Node> childConsumer(Node root) {
		Consumer<Node> childConsumer = child -> root.addChild(child.duplicateWithChildren());
		return childConsumer;
	}

	public static Node stopAtDeep(int actualDeep, Node node) {
		Node newNode = node.duplicate();

		if (actualDeep <= 0) {
			return newNode;
		}
		for (Node child : node.getChildren()) {
			newNode.addChild(NodeOperations.stopAtDeep(actualDeep - 1, child));
		}

		return newNode;
	}

	public static Node beginAtDeep(int targetDeep, Node node) {
		Node root = Node.root();

		node.forEachChild(childConsumer(root), child -> child.getDeep() >= targetDeep);
		return root;
	}

	public static Node withMaxPriority(int priority, Node node) {
		Node root = Node.root();

		node.forEachChild(childConsumer(root), child -> child.getTask().getPriority() <= priority && child.getTask().getPriority() != 0);
		return root;
	}

	public static Node withMinPriority(int priority, Node node) {
		Node root = Node.root();

		node.forEachChild(childConsumer(root), child -> child.getTask().getPriority() >= priority);
		return root;
	}

	public static Node withFrequency(Frequency frequency, Node node) {
		Node root = Node.root();

		node.forEachChild(childConsumer(root), child -> child.getTask().getFrequency() == frequency);
		return root;
	}

	public static Node whereIsDone(Node node) {
		if (!node.isRoot() && node.getTask().getStatus() == Status.NOT_DONE) {
			return null;
		}
		Node newNode = node.duplicate();
		for (Node child : node.getChildren()) {
			if (child.getTask().getStatus() == Status.NOT_DONE) {
				continue;
			}
			newNode.addChild(NodeOperations.whereIsDone(child));
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
				newNode.addChild(NodeOperations.whereIsNotDone(child));
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
                newChildren.add(NodeOperations.whereIsAlmostDone(child));
            } else if (!node.isRoot()) {
				newChildren.add(child.duplicate());
			}
        }

		newNode.addChildren(newChildren);

        return newNode;
    }

	public static Status updateStatus(Node node) {
		Status nodeStatus = node.getTask().getStatus();
		if (node.getChildren().isEmpty()) {
			return nodeStatus;
		}

		boolean hasDifferentStatus = false;
		Status previousChildStatus = null;
		for (Node child : node.getChildren()) {
			Status actualChildStatus = updateStatus(child);
			if (previousChildStatus != null && previousChildStatus != actualChildStatus) {
				hasDifferentStatus = true;
				break;
			}

			previousChildStatus = actualChildStatus;
		}

		if (hasDifferentStatus) {
			node.getTask().setStatus(Status.ALMOST_DONE);
		} else {
			node.getTask().setStatus(previousChildStatus);
		}
		return node.getTask().getStatus();
	}
}
