package core;

import cli.Preferences;
import core.Task.Node;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


class TaskTest {
	@BeforeAll
	static void setTestProperties() {
		System.setProperty("TODO_CONFIG_HOME", "src/test/resources");
	}
	@Test
	void test1Building() throws IOException {
		Preferences.loadPreferences("src/test/resources/preferences");

		String todoPathStr = "src/test/resources/test.txt";
//        String todoPathStr = "/home/xaxurro/.todo/todo";
		Path todoPath = Paths.get(todoPathStr);
		List<String> linesList = Files.readAllLines(todoPath);
		Node tree = Node.build(linesList);

		tree.print();
	}
}
