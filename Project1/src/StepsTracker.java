import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class StepsTracker {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/step_tracker"; 

    private static final String USER = "root";
    private static final String PASS = "bdoacalol";

    private Connection conn;
    private User user;

    public StepsTracker(String username) {
        try {
            Class.forName(JDBC_DRIVER);
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
            int profileId = getProfileId(username);
            int stepsGoal = getStepsGoal(profileId); 
            this.user = new User(profileId, username, stepsGoal);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getProfileId(String username) {
        int profileId = -1;
        try {
            // Prepare a statement to select the id of the profile with the given username
            PreparedStatement stmt = this.conn.prepareStatement("SELECT id FROM profiles WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                // If a profile with the given username exists, get its id
                profileId = rs.getInt("id");
            } else {
                // If no such profile exists create new profile
                stmt = this.conn.prepareStatement("INSERT INTO profiles (username) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, username);
                stmt.executeUpdate();
                // Get the id of the newly created profile
                rs = stmt.getGeneratedKeys();
                rs.next();
                profileId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileId;
    }
    

    

   public void insertSteps(LocalDate date, int steps) {
    try {
        PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO step_counts (idprofile, date, steps) VALUES (?, ?, ?)");
        stmt.setInt(1, this.user.getId());
        stmt.setDate(2, java.sql.Date.valueOf(date));
        stmt.setInt(3, steps);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void printMonthlyStats(int month) {
    try {
        
        PreparedStatement stmt = this.conn.prepareStatement("SELECT SUM(steps) AS totalSteps FROM step_counts WHERE idprofile = ? AND EXTRACT(MONTH FROM date) = ?");
        stmt.setInt(1, this.user.getId());
        stmt.setInt(2, month);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int totalSteps = rs.getInt("totalSteps");
            System.out.println("Total steps for " + this.user.getUsername() + " in month " + month + " is: " + totalSteps);
        }

        
        stmt = this.conn.prepareStatement("SELECT date, steps FROM step_counts WHERE idprofile = ? AND EXTRACT(MONTH FROM date) = ? ORDER BY date");
        stmt.setInt(1, this.user.getId()); 
        stmt.setInt(2, month); 
        rs = stmt.executeQuery();

        while (rs.next()) {
            java.sql.Date date = rs.getDate("date");
            int steps = rs.getInt("steps");
            System.out.println("On " + date + ", steps taken: " + steps);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    public void changeDailyStepGoal(int newGoal) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("UPDATE profiles SET stepsgoal = ? WHERE id = ?");
            stmt.setInt(1, newGoal);
            stmt.setInt(2, this.user.getId());
            stmt.executeUpdate();
            this.user.setStepsGoal(newGoal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printTotalStats() {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT SUM(steps) AS totalSteps FROM step_counts WHERE idprofile = ?");
            stmt.setInt(1, this.user.getId());
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                int totalSteps = rs.getInt("totalSteps");
                System.out.println("Total steps for " + this.user.getUsername() + " is: " + totalSteps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int getStepsGoal(int profileId) {
        int stepsGoal = 0;
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT stepsgoal FROM profiles WHERE id = ?");
            stmt.setInt(1, profileId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                stepsGoal = rs.getInt("stepsgoal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsGoal;
    }
        //Constructor for testing
    public StepsTracker(int profileId, String username, Connection conn) {
        this.user = new User(profileId, username, getStepsGoal(profileId)); 
        this.conn = conn;
    }

    }
    
    
    





