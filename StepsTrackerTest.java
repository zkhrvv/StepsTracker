import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import static org.junit.Assert.*;

public class StepsTrackerTest {
    private StepsTracker tracker;
    private User user;
    private Connection conn;
  

    @Before
    public void setup() {
        try {
            // Initialize the database connection...
            Class.forName("com.mysql.jdbc.Driver"); // Replace with your JDBC driver class
            String TEST_DB_URL = "jdbc:mysql://localhost:3306/step_tracker"; // Replace with your test database URL
            String USER = "root"; // Replace with your database username
            String PASS = "bdoacalol"; // Replace with your database password
            conn = DriverManager.getConnection(TEST_DB_URL, USER, PASS);
    
            // Setup your test database...
            setupTestDatabase();
    
            // Initialize the StepsTracker with the test constructor
            int profileId = 1; // Replace with the test profile ID
            String username = "testuser"; // Replace with the test username
            tracker = new StepsTracker(profileId, username, conn);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInsertSteps() throws SQLException {
        LocalDate date = LocalDate.of(2023, 7, 26);
        int steps = 10000;

        // Insert steps for a specific date.
        tracker.insertSteps(date, steps);
        
        // Check the state of the test database.
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT steps FROM step_counts WHERE idprofile = ? AND date = ?"
        );
        stmt.setInt(1, tracker.getUser().getId());
        stmt.setDate(2, java.sql.Date.valueOf(date));
        ResultSet rs = stmt.executeQuery();
        
        assertTrue("Step count not inserted", rs.next());
        assertEquals("Incorrect step count inserted", steps, rs.getInt("steps"));
    }
    private void setupTestDatabase() {
        try {
            Statement stmt = conn.createStatement();
            
            // Drop existing tables if they exist.
            stmt.executeUpdate("DROP TABLE IF EXISTS step_counts");
            stmt.executeUpdate("DROP TABLE IF EXISTS profiles");
            
            // Create new tables.
            stmt.executeUpdate(
                "CREATE TABLE profiles (" +
                "    id INT PRIMARY KEY AUTO_INCREMENT," +
                "    username VARCHAR(255) UNIQUE," +
                "    stepsGoal INT" +
                ")"
            );
            
            stmt.executeUpdate(
                "CREATE TABLE step_counts (" +
                "    id INT PRIMARY KEY AUTO_INCREMENT," +
                "    idprofile INT," +
                "    date DATE," +
                "    steps INT," +
                "    FOREIGN KEY (idprofile) REFERENCES profiles(id)" +
                ")"
            );
            
            // Insert a test user.
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO profiles (username, stepsGoal) VALUES (?, ?)"
            );
            pstmt.setString(1, "testuser");
            pstmt.setInt(2, 10000);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Add more tests...
}