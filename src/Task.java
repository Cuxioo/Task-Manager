package src;

public class Task
{   
    private int id;
    private String taskName;
    private boolean completed;
    
    public Task(String taskName, boolean completed)
    {
        this.taskName = taskName;
        this.completed = completed;
    }

    public Task(int id, String taskName, boolean completed)
    {
        this.id = id;
        this.taskName = taskName;
        this.completed = completed;
    }

    public int getId()
    {
        return id;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean finished)
    {
        completed = finished;
    }

    @Override
    public String toString()
    {
        return taskName + (completed && !taskName.startsWith("[X] ") ? " [completed]" : "");
    }
}
