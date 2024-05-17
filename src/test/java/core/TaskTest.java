package core;

import cli.Preferences;
import core.Task.Node;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


class TaskTest {
    @Test
    void test1Building() throws IOException {
        Preferences.loadPreferences("src/test/resources/preferences");

        Path path = Paths.get("src/test/resources/test.txt");
        List<String> linesList = Files.readAllLines(path);
        String linesStr = String.join("\n", linesList);
        Node tree = Node.build(linesStr);

        tree.whereIsDone().print();
    }
}