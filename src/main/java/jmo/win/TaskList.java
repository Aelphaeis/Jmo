package jmo.win;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A class that represents tasklist.exe
 * 
 * @author Aelphaeis
 *
 */
public class TaskList {
	public static final String PROCESS_NAME = "tasklist";
	private static final String LINE_FORMAT = "%-25s %14s %-16s %14s %15s\n";

	public static Process getTaskListProcess() throws IOException {
		return Runtime.getRuntime().exec(PROCESS_NAME);
	}

	List<TaskListEntry> taskListEntries;
	boolean hasRun;

	public TaskList() {
		taskListEntries = new ArrayList<>();
	}

	public void run() {
		if (hasRun) {
			// intentions of running this twice is not clear so throw exception
			String err = "TaskList can only be run once";
			throw new IllegalStateException(err);
		}

		try {
			Process proc = getProcess();
			List<String> lines = Processes.getAllLines(proc);

			String[] seperators = lines.get(2).split(" ");
			if (seperators.length < 5) {
				throw new IllegalStateException("Incorrect number of seperators parsed");
			}

			// Remove header and separators
			lines = lines.subList(4, lines.size() - 1);

			for (String line : lines) {
				taskListEntries.add(new TaskListEntry(seperators, line));
			}
		} catch (IOException e) {
			String err = "Error resolving process";
			throw new IllegalStateException(err, e);
		}
	}

	Process getProcess() throws IOException {
		return getTaskListProcess();
	}

	public List<TaskListEntry> getTaskListEntries() {
		return Collections.unmodifiableList(taskListEntries);
	}

	@Override
	public String toString() {
		String sep = "============================";
		String headers = String.format(LINE_FORMAT, "Image Name", "PID", "Session Name", "Session#", "Mem Usage");
		StringBuilder builder = new StringBuilder(headers);
		builder.append(sep).append(" ").append(sep.substring(17)).append(" ").append(sep.substring(9)).append(" ")
				.append(sep.substring(17)).append(" ").append(sep.substring(13)).append('\n');

		for (TaskListEntry entry : getTaskListEntries()) {
			builder.append(entry.toString());
		}
		return builder.toString();
	}

	public class TaskListEntry {
		private TaskListEntry(String[] seperators, String line) {
			try {
				int start = 0;
				int end = start + seperators[0].length() + 1;
				this.image = line.substring(start, end).trim();

				start = end;
				end = start + seperators[1].length() + 1;
				this.pid = Integer.parseInt(line.substring(start, end).trim());

				start = end;
				end = start + seperators[2].length() + 1;
				this.sessionName = line.substring(start, end).trim();

				start = end;
				end = start + seperators[3].length() + 1;
				this.sessionNum = Integer.parseInt(line.substring(start, end).trim());

				start = end;
				end = start + seperators[4].length();
				this.memUsage = line.substring(start, end).trim();
			} catch (NullPointerException e) {
				throw e;
			} catch (RuntimeException e) {
				String format = "Unable to parse line {%s} with seperator {%s}";
				String msg = String.format(format, line, Arrays.toString(seperators));
				throw new IllegalArgumentException(msg, e);
			}

		}

		private String image;
		private int pid;
		private String sessionName;
		private int sessionNum;
		private String memUsage;

		@Override
		public String toString() {
			return String.format(LINE_FORMAT, image, pid, sessionName, sessionNum, memUsage);
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public String getSessionName() {
			return sessionName;
		}

		public void setSessionName(String sessionName) {
			this.sessionName = sessionName;
		}

		public int getSessionNum() {
			return sessionNum;
		}

		public void setSessionNum(int sessionNum) {
			this.sessionNum = sessionNum;
		}

		public String getMemUsage() {
			return memUsage;
		}

		public void setMemUsage(String memUsage) {
			this.memUsage = memUsage;
		}
	}
}
