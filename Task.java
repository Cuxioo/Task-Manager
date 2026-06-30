public class Task
{
    String taskName;
    boolean completed = false;
    
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
