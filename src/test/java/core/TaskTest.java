package core;

import cli.Preferences;
import core.Node.FileHandler;
import core.Node.Node;
import core.Node.NodeOperations;
import core.Task.Frequency;
import core.Task.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class TaskTest {
	@BeforeAll
	static void setTestProperties() {
		System.setProperty("TODO_CONFIG_HOME", "src/test/resources");
//		System.setProperty("TODO_CONFIG_HOME", "/home/xaxurro/.todo");
	}
	@Test
	void test1SearchOperations() throws IOException {
		Preferences.loadPreferences("src/test/resources/preferences");

		String todoPathStr = "src/test/resources/test.txt";
		Node tree = FileHandler.readFile(todoPathStr);

		Assertions.assertEquals(10, NodeOperations.withTag("Daily", tree).size());
		Assertions.assertEquals(12, NodeOperations.whereIsDone(tree).size());
		Assertions.assertEquals(17, NodeOperations.whereIsAlmostDone(tree).size());
		Assertions.assertEquals(34, NodeOperations.whereIsNotDone(tree).size());
		Assertions.assertEquals(6, NodeOperations.stopAtDeep(1, tree).size());
		Assertions.assertEquals(5, NodeOperations.beginAtDeep(4, tree).size());
		Assertions.assertEquals(11, NodeOperations.withPriorityGreaterThan(2, tree).size());
		Assertions.assertEquals(21, NodeOperations.withPriorityLesserThan(2, tree).size());
		Assertions.assertEquals(10, NodeOperations.withFrequency(Frequency.DAILY, tree).size());
		Assertions.assertEquals(5, NodeOperations.withFrequency(Frequency.WEEKLY, tree).size());
	}

	@Test
	void test2() throws IOException {
		Preferences.loadPreferences("src/test/resources/preferences");

		String todoPathStr = "src/test/resources/test.txt";
		Node node = FileHandler.readFile(todoPathStr);
		node = NodeOperations.withTag("Daily", node);

		NodeOperations.updateStatus(node);
		Assertions.assertEquals(Status.ALMOST_DONE, node.getChild(0).getTask().getStatus());
	}
}
