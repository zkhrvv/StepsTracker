public class User {
    private int id;
    private String username;
    private int stepsGoal;

    public User(int id, String username, int stepsGoal) {
        this.id = id;
        this.username = username;
        this.stepsGoal = stepsGoal;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getStepsGoal() {
        return stepsGoal;
    }

    public void setStepsGoal(int stepsGoal) {
        this.stepsGoal = stepsGoal;
    }
}
