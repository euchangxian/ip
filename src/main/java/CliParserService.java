import exception.TaskParseException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CliParserService {
    private final Duke dukeBot;
    private final OutputService outputService;
    private final TaskFactory taskFactory;

    public CliParserService(Duke dukeBot, OutputService outputService, TaskFactory taskFactory) {
        this.dukeBot = dukeBot;
        this.outputService = outputService;
        this.taskFactory = taskFactory;
    }

    public void parse() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            String[] input = line.split(" ");
            String command = input[0].toLowerCase();

            switch (command) {
                case "bye":
                    return;
                case "list":
                    outputService.printTasks(dukeBot.getTaskList());
                    break;
                case "mark":
                    handleTaskAction(input, dukeBot::markTask, "Nice! I've marked this task as done:");
                    break;
                case "unmark":
                    handleTaskAction(input, dukeBot::unmarkTask, "OK, I've unmarked this task:");
                    break;
                case "delete":
                    handleDelete(input);
                    break;
                case "todo":
                case "deadline":
                case "event":
                    parseTaskCommand(line);
                    break;
                default:
                    outputService.echo(String.format(":< Command: %s not recognised!", input[0]));
            }
        }
    }

    private void parseTaskCommand(String line) {
        String[] parsedInput = line.split("/");
        String[] temp = parsedInput[0].split(" ");
        String taskType = temp[0];
        String taskName = String.join(" ", Arrays.copyOfRange(temp, 1, temp.length));

        String[] taskArgs = Arrays.copyOfRange(parsedInput,1, parsedInput.length);
        try {
            Task task = taskFactory.createTask(taskType, taskName, taskArgs);
            dukeBot.addTask(task);
            List<String> displayText = new ArrayList<>();
            displayText.add("Got it. I've added this task:");
            displayText.add(outputService.indentLeft(task.toString()));
            displayText.add(String.format("Now you have %s %s in the list.",
                    dukeBot.getNumberOfTasks(),
                    dukeBot.getNumberOfTasks() == 1 ? "task" : "tasks"));
            outputService.echo(displayText);
        } catch (TaskParseException e) {
            outputService.echo(e.getMessage());
        }
    }

    private void handleTaskAction(String[] input, Function<Integer, Optional<Task>> action, String successMessage) {
        if (!isValidTaskNumberArgument(input)) {
            return;
        }

        int taskNumber = Integer.parseInt(input[1]);
        Optional<Task> optionalTask = action.apply(taskNumber - 1);

        optionalTask.ifPresentOrElse(task -> {
            List<String> displayText = new ArrayList<>();
            displayText.add(successMessage);
            displayText.add(outputService.indentLeft(task.toString()));
            outputService.echo(displayText);
        }, () ->
            outputService.echo(String.format("Invalid Task index: %s provided.%n" +
                    "Specify a number between %s - %s", taskNumber, 1, dukeBot.getNumberOfTasks() + 1))
        );
    }

    private void handleDelete(String[] input) {
        if (!isValidTaskNumberArgument(input)) {
            return;
        }

        int taskNumber = Integer.parseInt(input[1]);
        Optional<Task> optionalTask = dukeBot.deleteTask(taskNumber - 1);

        optionalTask.ifPresentOrElse(task -> {
            List<String> displayText = new ArrayList<>();
            displayText.add("Noted. I have removed this task:");
            displayText.add(outputService.indentLeft(task.toString()));
            displayText.add(String.format("Now you have %s %s in the list.",
                    dukeBot.getNumberOfTasks(),
                    dukeBot.getNumberOfTasks() == 1 ? "task" : "tasks"));
            outputService.echo(displayText);
        }, () ->
            outputService.echo(String.format("Invalid Task index: %s provided.%n" +
                    "Specify a number between %s - %s", taskNumber, 1, dukeBot.getNumberOfTasks() + 1))
        );
    }

    // Utility method to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidTaskNumberArgument(String[] input) {
        if (input.length <= 1) {
            outputService.echo("An argument is required.");
            return false;
        }
        if (!isNumeric(input[1])) {
            outputService.echo("A numeric argument should be provided.");
            return false;
        }
        return true;
    }
}
