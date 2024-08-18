package core.node;

import cli.Preferences;
import core.task.Frequency;
import core.task.Status;
import core.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;

public class FileHandler {
	public static String TODO_CONFIG_HOME = Preferences.getTodoConfigHome();

	public static Node readFile(String pathStr) throws IOException {
		Path path = Paths.get(pathStr);
		List<String> lines = Files.readAllLines(path);

		Node root = Node.root();
		Node.createTaskTree(root, lines);
		NodeOperations.updateStatus(root);

		return root;
	}

	public static Node taskNode(Matcher matcher) {
		Task task = new Task();

		int priority = 0;
		if (matcher.group(1) != null) {
			priority = Integer.valueOf(matcher.group(1));
		}

		Frequency frequency = Frequency.NONE;
		if (matcher.group(2) != null) {
			frequency = Frequency.fromString(matcher.group(2));
		}

		Status status = Status.fromString(matcher.group(3));
		String content = matcher.group(4);

		task.setFrequency(frequency);
		task.setPriority(priority);
		task.setStatus(status);
		task.setContent(content);

		return new Node(task);
	}

	public static Node fileNode(String path, Matcher fileMatcher) throws IOException {
		if (fileMatcher.group(2) == null) {
			path = TODO_CONFIG_HOME + "/" + fileMatcher.group(1);
		}else if (fileMatcher.group(2).equals("~/")) {
			path = System.getenv("HOME") + "/" + fileMatcher.group(1).substring(2);
		}
		Path todoPath = Paths.get(path);
		List<String> linesList = Files.readAllLines(todoPath);

		Node root = Node.root();

		if (linesList.size() == 0) {
			return root;
		}

		Node.createTaskTree(root, linesList);

		return root;
	}
}
