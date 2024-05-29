package core;

import cli.Preferences;
import core.Node.FileHandler;
import core.Node.Node;
import core.Node.SearchOperations;
import core.Task.Frequency;
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
	void test1Building() throws IOException {
		Preferences.loadPreferences("src/test/resources/preferences");

		String todoPathStr = "src/test/resources/test.txt";
//        String todoPathStr = "/home/xaxurro/.todo/todo";
		Node tree = FileHandler.readFile(todoPathStr);

		Assertions.assertEquals(10, SearchOperations.withTag("Daily", tree).size());
		Assertions.assertEquals(12, SearchOperations.whereIsDone(tree).size());
		Assertions.assertEquals(17, SearchOperations.whereIsAlmostDone(tree).size());
		Assertions.assertEquals(34, SearchOperations.whereIsNotDone(tree).size());
		Assertions.assertEquals(6, SearchOperations.stopAtDeep(1, tree).size());
		Assertions.assertEquals(5, SearchOperations.beginAtDeep(4, tree).size());
		Assertions.assertEquals(11, SearchOperations.withPriorityGreaterThan(2, tree).size());
		Assertions.assertEquals(21, SearchOperations.withPriorityLesserThan(2, tree).size());
		Assertions.assertEquals(10, SearchOperations.withFrequency(Frequency.DAILY, tree).size());
		Assertions.assertEquals(5, SearchOperations.withFrequency(Frequency.WEEKLY, tree).size());

		tree = SearchOperations.withFrequency(Frequency.DAILY, tree);
		tree.print();
	}
}
