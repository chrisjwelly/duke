import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;

public class Duke {
    private Storage storage;
    private TaskList tasks;
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private Image user = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image duke = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));
    // Empty constructor required for Launcher
    public Duke() {
    }

    public Duke(String filePath) {
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasksFromSaveFile());

            // Since we can successfully load it, we save all in TaskList
            storage.writeTasks(tasks);
        } catch (FileNotFoundException e) {
            // Load an empty one
            tasks = new TaskList();
            Ui.printLine();
            Ui.indent("It seems that there is no save file in: " + filePath);
            Ui.printLine();
        }
    }

    public void run() {
        Ui.greet();
        while (Parser.stillHasInputs()) {
            try {
                String input = Parser.readLine();
                if (Parser.commandEquals("bye", input)) {
                    Ui.sayGoodbye();
                    break;
                } else if (Parser.commandEquals("list", input)) {
                    Ui.displayList(tasks);
                } else if (Parser.commandEquals("done", input)) {
                    int taskNo = Parser.getTaskNo(input);
                    tasks.markTaskAsDone(taskNo);
                } else if (Parser.commandEquals("delete", input)) {
                    int taskNo = Parser.getTaskNo(input);
                    tasks.deleteTask(taskNo);
                } else if (Parser.commandEquals("deadline", input)) {
                    if (Parser.hasNoArgs(input)) throw new EmptyDescriptionException();
                    if (tasks.isFull()) throw new TooManyTasksException();

                    tasks.addDeadline(Parser.getArgs(input));
                } else if (Parser.commandEquals("event", input)) {
                    if (Parser.hasNoArgs(input)) throw new EmptyDescriptionException();
                    if (tasks.isFull()) throw new TooManyTasksException();

                    tasks.addEvent(Parser.getArgs(input));
                } else if (Parser.commandEquals("todo", input)) {
                    if (Parser.hasNoArgs(input)) throw new EmptyDescriptionException();
                    if (tasks.isFull()) throw new TooManyTasksException();

                    tasks.addTodo(Parser.getArgs(input));
                } else if (Parser.commandEquals("find", input)) {
                    if (Parser.hasNoArgs(input)) throw new EmptyDescriptionException();

                    tasks.find(Parser.getArgs(input));
                } else {
                    throw new UnknownCommandException();
                }

                // Rewrites the entire file for every update you make here
                // Probably O(n^2) time where n is the number of tasks but this is the simplest change we can make
                storage.writeTasks(tasks);
            } catch (DukeException e) {
                Ui.printLine();
                Ui.indent(e.toString());
                Ui.printLine();
            } catch (DateTimeParseException e) {
                Ui.printLine();
                Ui.indent("It seems that you have entered a format we don't understand. ");
                Ui.indent("Please use the YYYY-MM-DD format.");
                Ui.printLine();
            }
        }
    }


    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }


    /**
     * Iteration 1:
     * Creates a label with the specified text and adds it to the dialog container.
     * @param text String containing text to add
     * @return a label with the specified text that has word wrap enabled
     */
    private Label getDialogLabel(String text) {
        // need to import javafx.scene.control.label
        Label textToAdd = new Label(text);
        textToAdd.setWrapText(true);

        return textToAdd;
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    public String getResponse(String input) {
        return "Duke heard: " + input;
    }
}
