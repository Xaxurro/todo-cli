package core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


class TaskTest {
    @Test
    void test1Building() throws IOException {
        Path path = Paths.get("src/test/resources/test.txt");
        List<String> linesList = Files.readAllLines(path);
        String linesStr = String.join("\n", linesList);
        TaskNode tree = TaskNode.build(linesStr);
    }
}