import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class DatabaseManager 
{
    private static final String DB_FOLDER =
            System.getProperty("user.home") + File.separator + ".taskmanager";

    private static final String DB_PATH =
            DB_FOLDER + File.separator + "tasks_db.sqlite";

    private static final String URL =
            "jdbc:sqlite:" + DB_PATH;

    public DatabaseManager()
    {
        createDatabaseFolder();
        initializeDatabase();
    }

    private void createDatabaseFolder()
    {
        File folder = new File(DB_FOLDER);

        if (!folder.exists())
        {
            if (!folder.mkdirs() && !folder.isDirectory())
            {
                throw new IllegalStateException(
                        "Could not create database folder: " + folder.getAbsolutePath());
            }
        }
    }

    private void initializeDatabase()
    {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "task_name TEXT NOT NULL," +
                     "completed INTEGER NOT NULL)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("Error initializing database: " + e.getMessage(), e);
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
            throw new IllegalStateException("Error adding task: " + e.getMessage(), e);
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
            while (rs.next())
            {
                tasks.add(new Task(
                    rs.getInt("id"),
                    rs.getString("task_name"),
                    rs.getInt("completed") == 1
                ));
            }
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("Error fetching tasks: " + e.getMessage(), e);
        }

        return tasks;
    }

    public void updateTaskStatus(int id, boolean completed)
    {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, completed ? 1 : 0);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new IllegalStateException("Error updating task: " + e.getMessage(), e);
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
            throw new IllegalStateException("Error deleting task: " + e.getMessage(), e);
        }
    }
}