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

    public static void main(String[] args) {
        Duke changooseBot = new Duke("Changoose");
        CliParserService cliParserService = new CliParserService(changooseBot);
        OutputService outputService = new OutputService();
        String startMessage = String.format("Hello! I'm %s%nWhat can I do for you?", changooseBot.getBotName());
        String endMessage = "Bye! Hope to see you again soon!";

        outputService.echo(startMessage);
        cliParserService.parse();
        outputService.echo(endMessage);
    }

    public String getBotName() {
        return botName;
    }

    public boolean addTask(Task task) {
        return this.taskList.add(task);
    }

    public boolean deleteTask(int index) {
        if (index < 0 || index >= taskList.size()) {
            outputService.echo(String.format("Invalid Task Index: %s provided.", index));
            return false;
        }
        Task removedTask = taskList.remove(index);
        List<String> displayText = new ArrayList<>();
        displayText.add("Noted. I have removed this task:");
        displayText.add(outputService.indentLeft(removedTask.toString()));
        displayText.add(String.format("Now you have %s %s in the list.",
                taskList.size(),
                taskList.size() == 1 ? "task" : "tasks"));

        outputService.echo(displayText);
        return true;
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
        return taskList;
    }

    public int getNumberOfTasks() {
        return taskList.size();
    }
}
