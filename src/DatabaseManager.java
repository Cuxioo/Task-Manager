package src;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager 
{
    private static final String URL = "jdbc:sqlite:tasks_db.sqlite";

    public DatabaseManager()
    {
        initializeDatabase();
    }

    private void initializeDatabase()
    {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "task_name TEXT NOT NULL," +
                     "completed INTEGER NOT NULL)";
        
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement())
            {
                stmt.execute(sql);
            }
        catch(SQLException e)
        {
            System.err.println("Error initializing DataBase: " + e.getMessage());
        }
    }

    public void addTask(Task task)
    {
        String sql = "INSERT INTO tasks(task_name, completed) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, task.getTaskName());
                pstmt.setInt(2, task.isCompleted() ? 1 : 0);
                pstmt.executeUpdate();
            }
        catch (SQLException e)
        {
            System.err.println("Error adding task: " + e.getMessage());
        }
    }

    public List<Task> getAllTasks()
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
            {
                while(rs.next())
                {
                    tasks.add(new Task(rs.getInt("id"), rs.getString("task_name"), rs.getInt("completed") == 1));
                }
            }
        catch (SQLException e)
        {
            System.err.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void updateTaskStatus(int id, boolean completed)
    {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setInt(1, completed ? 1 : 0);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }
        catch (SQLException e)
        {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }

    public void deleteTask(int id)
    {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        catch (SQLException e)
        {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }
}
