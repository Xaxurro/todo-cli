package core.Task;

import cli.Preferences;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;

public class FileHandler {
	public static String TODO_CONFIG_HOME = Preferences.getTodoConfigHome();

	public static Node fromString(String str, Matcher matcher) throws IOException {
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

	static Node fromFile(String path, Matcher matcher) throws IOException {
		if (matcher.group(2) == null) {
			path = Preferences.getTodoConfigHome() + "/" + matcher.group(1);
		}
		Path todoPath = Paths.get(path);
		List<String> linesList = Files.readAllLines(todoPath);
		return Node.build(linesList);
	}
}
