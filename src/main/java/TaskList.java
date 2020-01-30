import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private static List<Task> tasks;

    public TaskList(List<List<String>> savedTasks) {
        tasks = new ArrayList<>();
        for (List<String> savedTask : savedTasks) {
            String type = savedTask.get(0);
            Task taskObject;
            if (type.equals("D")) {
                taskObject = new Deadline(savedTask.get(2), savedTask.get(3));
            } else if (type.equals("E")) {
                taskObject = new Event(savedTask.get(2), savedTask.get(3));
            } else {
                // type equals ("T")
                taskObject = new Todo(savedTask.get(2));
            }
            tasks.add(taskObject);

            if (savedTask.get(1).equals("1")) {
                // That means task was initially done
                taskObject.markAsDone();
            }
        }
    }

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public static void addDeadline(String args) {
        String[] descAndBy = args.split(" /by ");
        Deadline deadline = new Deadline(descAndBy[0], descAndBy[1]);
        tasks.add(deadline);

        Ui.printLine();
        Ui.indent("Acknowledged. I have added: ");
        Ui.doubleIndent( deadline.toString());
        Ui.printTaskCount();
        Ui.printLine();
    }

    public static void addEvent(String args) {
        String[] descAndAt = args.split(" /at ");
        Event event = new Event(descAndAt[0], descAndAt[1]);
        tasks.add(event);

        Ui.printLine();
        Ui.indent("Acknowledged. I have added: ");
        Ui.doubleIndent(event.toString());
        Ui.printTaskCount();
        Ui.printLine();
    }

    public static void addTodo(String args) {
        Todo todo = new Todo(args);
        tasks.add(todo);
        Ui.printLine();
        Ui.indent("Acknowledged. I have added: ");
        Ui.doubleIndent(todo.toString());
        Ui.printTaskCount();
        Ui.printLine();
    }
}
