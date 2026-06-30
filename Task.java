public class Task
{
    String taskName;
    boolean completed = false;
    
    public Task(String Name, boolean finished)
    {
        this.taskName = Name;
        this.completed = finished;
    }

    @Override
    public String toString()
    {
        return taskName + (completed ? " [completed]" : "");
    }
}
