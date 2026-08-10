package src;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) 
    {
        DatabaseManager db = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Task Manager ===");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Complete/Undo Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Task description: ");
                    String desc = scanner.nextLine();
                    db.addTask(new Task(desc, false));
                    break;

                case "2":
                    List<Task> tasks = db.getAllTasks();
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks found.");
                    } else {
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + ". " + tasks.get(i));
                        }
                    }
                    break;

                case "3":
                    System.out.print("Enter task number to toggle: ");
                    int toggleIdx = Integer.parseInt(scanner.nextLine()) - 1;
                    List<Task> currentTasks = db.getAllTasks();
                    if (toggleIdx >= 0 && toggleIdx < currentTasks.size()) {
                        Task t = currentTasks.get(toggleIdx);
                        db.updateTaskStatus(t.getId(), !t.isCompleted());
                    }
                    break;

                case "4":
                    System.out.print("Enter task number to delete: ");
                    int delIdx = Integer.parseInt(scanner.nextLine()) - 1;
                    List<Task> toDel = db.getAllTasks();
                    if (delIdx >= 0 && delIdx < toDel.size()) {
                        db.deleteTask(toDel.get(delIdx).getId());
                    }
                    break;

                case "5":
                    System.out.println("Exiting Now. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }
}
