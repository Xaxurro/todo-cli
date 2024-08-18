package core;

import cli.Preferences;
import cli.commands.ListCommand;
import core.node.FileHandler;
import core.node.Node;
import core.node.NodeOperations;
import core.task.Frequency;
import core.task.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


class TaskTest {
	String preferencesPathStr = "src/test/resources/preferences";
	String todoPathStr = "src/test/resources/test.todo";

	@BeforeAll
	static void setTestProperties() {
		System.setProperty("TODO_CONFIG_HOME", "src/test/resources");
//		System.setProperty("TODO_CONFIG_HOME", "/home/xaxurro/.todo");
	}
	@Test
	void test1SearchOperations() throws IOException {
		Preferences.loadPreferences(preferencesPathStr);
		Node tree = FileHandler.readFile(todoPathStr);

		Assertions.assertEquals(12, NodeOperations.whereIsDone(tree).size());
		Assertions.assertEquals(17, NodeOperations.whereIsAlmostDone(tree).size());
		Assertions.assertEquals(34, NodeOperations.whereIsNotDone(tree).size());
		Assertions.assertEquals(6, NodeOperations.stopAtDeep(1, tree).size());
		Assertions.assertEquals(5, NodeOperations.beginAtDeep(4, tree).size());
		Assertions.assertEquals(11, NodeOperations.withMinPriority(2, tree).size());
		Assertions.assertEquals(21, NodeOperations.withMaxPriority(2, tree).size());
		Assertions.assertEquals(10, NodeOperations.withFrequency(Frequency.DAILY, tree).size());
		Assertions.assertEquals(5, NodeOperations.withFrequency(Frequency.WEEKLY, tree).size());
	}
}
