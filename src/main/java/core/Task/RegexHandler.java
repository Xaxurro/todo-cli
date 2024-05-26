package core.Task;

import cli.Preferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHandler {
	private static int DEEP_STR_LENGTH = Preferences.getTaskDeepStrLength();
	private static boolean IS_TAB_EQUALS_SPACE = false;

	private static String escapeString(String str) {
		String newStr = "";

		String mustEscapeCharacters = "\\.[]{}()<>*+-=!?^$|";

		for (char cStr : str.toCharArray()) {
			for (char cMustEscape : mustEscapeCharacters.toCharArray()) {
				if (cStr == cMustEscape) {
					newStr += '\\';
				}
			}
			newStr += cStr;
		}

		return newStr;
	}

	private static String escapeString(Status status) {
		return escapeString(status.toString());
	}

	private static String escapeString(Frequency frequency) {
		return escapeString(frequency.toString());
	}

	public static int getDeep(String taskStr) {
		int deep = 0;
		String deepStr = " ".repeat(DEEP_STR_LENGTH);

		if (IS_TAB_EQUALS_SPACE) {
			while (!taskStr.equals(taskStr.stripLeading())); {
				if (taskStr.startsWith(deepStr)) {
					taskStr = taskStr.substring(DEEP_STR_LENGTH);
					deep++;
				} else if (taskStr.startsWith("\t")) {
					taskStr = taskStr.substring(1);
					deep++;
				}
			}
		} else {
			int originalLength = taskStr.length();
			int strippedLength = taskStr.stripLeading().length();
			deep = originalLength - strippedLength;
			if (taskStr.startsWith(deepStr)) {
				return deep / DEEP_STR_LENGTH;
			}
			return deep;
		}


		return deep;
	}

	public static String deepRegex() {
		String taskDeepRegex = String.format(" {%s}", DEEP_STR_LENGTH);
		if (Preferences.isTabEqualsToSpace()) {
			taskDeepRegex += "|\t";
		}

		return String.format("(?:%s)*", taskDeepRegex);
	}

	public static Matcher taskRegex(String str) {

		String priorityRegex = "[0-9]+";

		String frequencies = String.format("[%s%s%s%s]", escapeString(Frequency.DAILY), escapeString(Frequency.WEEKLY), escapeString(Frequency.MONTHLY), escapeString(Frequency.YEARLY));
		String noFrequency = escapeString(Frequency.NONE);
		String frequencyRegex = String.format("%s|%s%s|%s", noFrequency, noFrequency, frequencies, frequencies);

		String statusRegex = String.format("%s|%s|%s", escapeString(Status.NOT_DONE), escapeString(Status.ALMOST_DONE), escapeString(Status.DONE));

//        Capturing Groups
//        1: Priority
//        2: Frequency
//        3: Status
//        4: Content
		String taskRegex = String.format("^%s(%s)? ?(%s)? ?(%s) ?(.+)$", deepRegex(), priorityRegex, frequencyRegex, statusRegex);

		Pattern pattern = Pattern.compile(taskRegex);
		return pattern.matcher(str);

//        return str.matches(String.format("^(%s)*(%s|%s|%s) ?.+$", escapeString(DEEP_STR), escapeString(Status.NOT_DONE), escapeString(Status.ALMOST_DONE), escapeString(Status.DONE)));
	}

	public static Matcher fileRegex(String str) {
		String pathRegex = "(\\/)?(?:\\w+\\/)*\\w+";

		String fileRegex = String.format("^%s\\+ ?(%s)$", deepRegex(), pathRegex);

		Pattern pattern = Pattern.compile(fileRegex);
		return pattern.matcher(str);
	}
}
