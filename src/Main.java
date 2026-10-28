import java.util.function.Predicate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private final DatabaseManager database = new DatabaseManager();
    private final ObservableList<Task> taskData = FXCollections.observableArrayList();
    private final FilteredList<Task> visibleTasks = new FilteredList<>(taskData);
    private final Label totalCount = new Label("0");
    private final Label remainingCount = new Label("0");
    private ListView<Task> taskList;
    private Predicate<Task> currentFilter = task -> true;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setTop(createHeader());
        root.setCenter(createTaskArea());
        root.setBottom(createFooter());

        Scene scene = new Scene(root, 900, 650);
        var css = getClass().getResource("/task-manager.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("Task Manager");
        stage.setMinWidth(700);
        stage.setMinHeight(520);
        stage.setScene(scene);
        refreshTasks();
        stage.show();
    }

    private VBox createHeader() {
        Label eyebrow = new Label("YOUR PERSONAL WORKSPACE");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Task Manager");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Make room for what matters.");
        subtitle.getStyleClass().add("subtitle");

        VBox heading = new VBox(7, eyebrow, title, subtitle);
        heading.getStyleClass().add("header-copy");

        VBox totalCard = createStatCard("TOTAL TASKS", totalCount);
        VBox remainingCard = createStatCard("TO DO", remainingCount);
        HBox stats = new HBox(12, totalCard, remainingCard);
        stats.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(20, heading, spacer(), stats);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header");
        return new VBox(header);
    }

    private VBox createStatCard(String caption, Label value) {
        Label label = new Label(caption);
        label.getStyleClass().add("stat-caption");
        value.getStyleClass().add("stat-value");
        VBox card = new VBox(4, label, value);
        card.getStyleClass().add("stat-card");
        card.setMinWidth(105);
        return card;
    }

    private VBox createTaskArea() {
        Label sectionTitle = new Label("Your tasks");
        sectionTitle.getStyleClass().add("section-title");

        Label sectionHint = new Label("Add a task and take it one step at a time.");
        sectionHint.getStyleClass().add("section-hint");
        VBox sectionCopy = new VBox(5, sectionTitle, sectionHint);

        TextField taskInput = new TextField();
        taskInput.setPromptText("What do you need to get done?");
        taskInput.getStyleClass().add("task-input");
        taskInput.setOnAction(event -> addTask(taskInput));

        Button addButton = new Button("＋  Add task");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(event -> addTask(taskInput));

        HBox inputRow = new HBox(10, taskInput, addButton);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(taskInput, Priority.ALWAYS);

        ToggleGroup filters = new ToggleGroup();
        ToggleButton allButton = createFilterButton("All", filters);
        ToggleButton activeButton = createFilterButton("To do", filters);
        ToggleButton completedButton = createFilterButton("Completed", filters);
        allButton.setSelected(true);

        allButton.setOnAction(event -> setFilter(task -> true));
        activeButton.setOnAction(event -> setFilter(task -> !task.isCompleted()));
        completedButton.setOnAction(event -> setFilter(Task::isCompleted));

        HBox filterRow = new HBox(7, allButton, activeButton, completedButton);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        filterRow.getStyleClass().add("filter-row");

        taskList = new ListView<>(visibleTasks);
        taskList.setCellFactory(list -> new TaskCell());
        taskList.setPlaceholder(new Label("No tasks here yet. Add one above to get started."));
        VBox.setVgrow(taskList, Priority.ALWAYS);

        VBox content = new VBox(20, sectionCopy, inputRow, filterRow, taskList);
        content.setPadding(new Insets(28, 40, 28, 40));
        content.getStyleClass().add("task-content");
        VBox.setVgrow(taskList, Priority.ALWAYS);
        return content;
    }

    private ToggleButton createFilterButton(String text, ToggleGroup group) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(group);
        button.getStyleClass().add("filter-button");
        return button;
    }

    private void setFilter(Predicate<Task> filter) {
        currentFilter = filter;
        visibleTasks.setPredicate(currentFilter);
    }

    private void addTask(TextField input) {
        String description = input.getText().trim();
        if (description.isEmpty()) {
            input.requestFocus();
            return;
        }

        if (!runDatabaseAction(() -> database.addTask(new Task(description, false)))) {
            return;
        }
        input.clear();
        refreshTasks();
    }

    private void refreshTasks() {
        try {
            taskData.setAll(database.getAllTasks());
            totalCount.setText(Integer.toString(taskData.size()));
            remainingCount.setText(Long.toString(
                    taskData.stream().filter(task -> !task.isCompleted()).count()));
            visibleTasks.setPredicate(currentFilter);
        } catch (RuntimeException exception) {
            showError("Could not load tasks", exception);
        }
    }

    private boolean runDatabaseAction(Runnable action) {
        try {
            action.run();
            return true;
        } catch (RuntimeException exception) {
            showError("Could not update tasks", exception);
            return false;
        }
    }

    private void showError(String title, RuntimeException exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Task Manager");
        alert.setHeaderText(title);
        alert.setContentText(exception == null || exception.getMessage() == null
                ? "An unexpected error occurred."
                : exception.getMessage());
        alert.showAndWait();
    }

    private HBox createFooter() {
        Label footerLabel = new Label("TASK MANAGER  /  RED EDITION");
        footerLabel.getStyleClass().add("footer-label");
        HBox footer = new HBox(footerLabel);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("footer");
        return footer;
    }

    private Region spacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    private final class TaskCell extends ListCell<Task> {
        private final CheckBox completed = new CheckBox();
        private final Label description = new Label();
        private final Button deleteButton = new Button("×");
        private final HBox row = new HBox(12, completed, description, spacer(), deleteButton);

        private TaskCell() {
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("task-row");
            description.getStyleClass().add("task-description");
            deleteButton.getStyleClass().add("delete-button");
            completed.setOnAction(event -> {
                Task task = getItem();
                if (task == null) {
                    return;
                }
                runDatabaseAction(() -> database.updateTaskStatus(
                        task.getId(), completed.isSelected()));
                refreshTasks();
            });
            deleteButton.setOnAction(event -> {
                Task task = getItem();
                if (task == null) {
                    return;
                }
                runDatabaseAction(() -> database.deleteTask(task.getId()));
                refreshTasks();
            });
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                completed.setSelected(false);
                completed.setDisable(true);
                deleteButton.setDisable(true);
                description.setText("");
                description.getStyleClass().remove("task-completed");
                setGraphic(null);
                setText(null);
                return;
            }

            completed.setDisable(false);
            deleteButton.setDisable(false);
            completed.setSelected(task.isCompleted());
            description.setText(task.getTaskName());
            description.getStyleClass().remove("task-completed");
            if (task.isCompleted()) {
                description.getStyleClass().add("task-completed");
            }
            setGraphic(row);
            setText(null);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}