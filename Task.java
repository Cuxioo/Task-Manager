import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Task
{
    String taskName;
    boolean completed;
    
    public Task(String Name, boolean finished)
    {
        this.taskName = Name;
        this.completed = finished;

        if(!finished)
        {
            this.taskName = "[ ] " + Name;
        } else
        {
            this.taskName = "[X] " + Name;
        }
    }

    public static void saveTask(ArrayList<Task> list)
    {
        try(FileWriter writer = new FileWriter("task.txt"))
        {
            for(Task task : list)
            {
                writer.write(task.toString() + "\n");
            }
        }
        catch(IOException e)
        {
            System.out.println("Couldnt write file");
        }
    }

    public void completeTask()
    {
        this.completed = true;
        if (this.taskName.startsWith("[ ] ")) {
            this.taskName = "[X] " + this.taskName.substring(4);
        }
    }

    @Override
    public String toString()
    {
        return taskName + (completed && !taskName.startsWith("[X] ") ? " [completed]" : "");
    }
}
