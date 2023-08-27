import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private final String botName;
    private final List<Task> taskList;
    private final OutputService outputService;

    public Duke(String botName) {
        this.botName = botName;
        this.taskList = new ArrayList<>();
        this.outputService = new OutputService();
    }

    public String getBotName() {
        return this.botName;
    }

    public boolean addTask(Task task) {
        return this.taskList.add(task);
    }

    public void markTask(int index) {
        if (index < 0 || index >= taskList.size()) {
            outputService.echo(String.format("Invalid Task Index: %s provided.", index));
            return;
        }
        Task task = taskList.get(index);
        task.markAsDone();
        List<String> displayText = new ArrayList<>();
        displayText.add("Nice! I've marked this task as done:");
        displayText.add(outputService.indentLeft(task.toString()));
        outputService.echo(displayText);
    }

    public void unmarkTask(int index) {
        if (index < 0 || index >= taskList.size()) {
            outputService.echo(String.format("Invalid Task Index: %s provided.", index));
            return;
        }
        Task task = taskList.get(index);
        task.markAsNotDone();
        List<String> displayText = new ArrayList<>();
        displayText.add("OK, I've marked this task as not done yet:");
        displayText.add(outputService.indentLeft(task.toString()));
        outputService.echo(displayText);
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }
}
