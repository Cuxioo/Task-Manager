import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Function 
{
    static boolean running = true;
    public void Run() 
    {
        ArrayList<Task> taskList = new ArrayList<Task>();
        try(BufferedReader reader = new BufferedReader(new FileReader("task.txt")))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                if(line.substring(0, 2).equals("[X]"))
                {
                    taskList.add(new Task(line.substring(4), true));
                }
                else
                {
                    taskList.add(new Task(line.substring(4), false));
                }
            }
        }
        catch(FileNotFoundException a)
        {
            System.out.println("Cannot find file.");
        }
        catch (IOException a)
        {
            System.out.println("Something went wrong.");
        }
        Scanner input = new Scanner(System.in);
        while(running)
        {
        System.out.println("=== Task Manager ===");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Complete Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Exit");
        System.out.println("Enter Choice:");

        String choiceLine = input.nextLine();
        int Choice;
        try {
            Choice = Integer.parseInt(choiceLine.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a number.");
            continue;
        }

        switch (Choice)
        {
        
            case 1: 
            {
                System.out.println("Task to add: ");
                String option = input.nextLine();
                taskList.add(new Task(option, false));
                Task.saveTask(taskList);
                System.out.println("Task Added!");
                break;
            }
            case 2:
            {
                if (taskList.isEmpty()) 
                {
                    System.out.println("No tasks found.");
                } else 
                    {
                    for (int i = 0; i < taskList.size(); i++) 
                    {
                        System.out.println((i + 1) + ". " + taskList.get(i));
                    }
                    }
                break;
            }
            case 3:
            {
                if (taskList.isEmpty())
                {
                    System.out.println("No tasks to complete.");
                }
                else
                {
                    System.out.println("Which would you like to complete? ");
                    for (int i = 0; i < taskList.size(); i++) 
                    {
                        System.out.println((i + 1) + ". " + taskList.get(i));
                    }
                    choiceLine = input.nextLine();
                    int choice;
                    try {
                        choice = Integer.parseInt(choiceLine.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choice! Please enter a number.");
                        continue;
                    }
                    if (choice < 1 || choice > taskList.size()) {
                        System.out.println("Invalid task number.");
                    } else {
                        taskList.get(choice - 1).completeTask();
                        Task.saveTask(taskList);
                        System.out.println("Completed Task");
                    }
                }
                break;
            }
            case 4:
                {
                     if (taskList.isEmpty())
                    {
                        System.out.println("No tasks to Delete.");
                    }
                    else
                    {
                        System.out.println("Which would you like to Delete? ");
                        for (int i = 0; i < taskList.size(); i++) 
                        {
                            System.out.println((i + 1) + ". " + taskList.get(i));
                        }
                        choiceLine = input.nextLine();
                        int choice;
                        try {
                            choice = Integer.parseInt(choiceLine.trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice! Please enter a number.");
                            continue;
                        }
                        if (choice < 1 || choice > taskList.size()) {
                            System.out.println("Invalid task number.");
                        } else {
                            taskList.remove(choice - 1);
                            Task.saveTask(taskList);
                            System.out.println("Deleted Task");
                        }
                    }
                    break;
                }
            case 5:
            {
                System.out.println("Exiting now. Goodbye!");
                running = false;
                break;
            }
            default:
            {
                System.out.println("Invalid choice!");
                break;
            }
        }


        }
        input.close();
    }
}

