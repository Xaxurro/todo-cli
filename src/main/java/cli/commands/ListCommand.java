package cli.commands;

import cli.Preferences;
import cli.Utils;
import core.node.FileHandler;
import core.node.Node;
import core.node.NodeOperations;
import core.task.Frequency;
import core.task.Status;
import picocli.CommandLine.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Command(name = "l",
		description = "List TODOs",
		version = "0.9.0",
		mixinStandardHelpOptions = true,
		requiredOptionMarker = '*')
public class ListCommand implements Runnable {

	@Option(names = {"-f", "--file"},
			required = false,
			description = "TODO File to read",
			paramLabel = "TODO File")
	String todoFilePathStr = Preferences.getTodoConfigHome() + "/todo";

	@Option(names = {"-c", "--preferencesFile"},
			required = false,
			description = "Preferences File",
			paramLabel = "Preferences File")
	String preferencesFilePathStr = Preferences.getTodoConfigHome() + "/preferences";

	@Option(names = {"-t", "--tag"},
			required = false,
			description = "Tags to search on the TODO File",
			paramLabel = "Tags")
	String searchTag = "";

	@Option(names = {"-d", "--stopAtDeep"},
			required = false,
			description = "Deep where it should stop listing, it has be greater than 0",
			paramLabel = "Stop at Deep")
	int searchStopAtDeep = 0;

	@Option(names = {"-D", "--beginAtDeep"},
			required = false,
			description = "Deep where it should begin listing, it has be greater than 0",
			paramLabel = "Begin at Deep")
	int searchBeginAtDeep = 0;


	@Option(names = {"-P", "--maxPriority"},
			required = false,
			description = "Maximum priority it should list, includes 0",
			paramLabel = "Maximum Priority")
	int searchMaxPriority = 0;

	@Option(names = {"-p", "--minPriority"},
			required = false,
			description = "Minimum priority it should list, it has to be greater than 0",
			paramLabel = "Minimum Priority")
	int searchMinPriority = 0;

	@Option(names = {"-F", "--frequency"},
			required = false,
			description = "Frequency of the Task it should list",
			paramLabel = "Frequency")
	String searchFrequency = "";

	@Option(names = {"-s", "--status"},
			required = false,
			description = "Status of the Task it should list",
			paramLabel = "Status")
	String searchStatus = "";

	@Option(names = {"-u", "--updateFile"},
			required = false,
			description = "If present it will update the file",
			paramLabel = "Should update file")
	boolean shouldUpdateFile = false;

	@Override
	public void run() {
		try {
			Preferences.loadPreferences(preferencesFilePathStr);
			Node node = FileHandler.readFile(todoFilePathStr);

			node = search(node);
			if (shouldUpdateFile) {
//				TODO Code FileHandler.updateNodeFiles()
			}
			node.print();
		} catch (IOException e) {
			System.exit(Utils.STATUS_FAILURE);
		}
	}

	public Node search(Node node) {
		if (!searchTag.isEmpty()) {
			node = NodeOperations.withTag(searchTag, node);
		}
		if (searchStopAtDeep > 0) {
			node = NodeOperations.stopAtDeep(searchStopAtDeep, node);
		}
		if (searchBeginAtDeep > 0) {
			node = NodeOperations.beginAtDeep(searchBeginAtDeep, node);
		}
		if (searchMinPriority > 0) {
			node = NodeOperations.withMinPriority(searchMinPriority, node);
		}
		if (searchMaxPriority > 0) {
			node = NodeOperations.withMaxPriority(searchMaxPriority, node);
		}
		if (!searchFrequency.isEmpty()) {
			Frequency frequency = Frequency.fromString(searchFrequency);
			node = NodeOperations.withFrequency(frequency, node);
		}
		if (!searchStatus.isEmpty()) {
			Status status = Status.fromString(searchStatus);
			switch (status) {
				case NOT_DONE -> node = NodeOperations.whereIsNotDone(node);
				case ALMOST_DONE -> node = NodeOperations.whereIsAlmostDone(node);
				case DONE -> node = NodeOperations.whereIsDone(node);
			}
		}

		return node;
	}
}
