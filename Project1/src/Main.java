import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
public class Main {
 public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter your profile name:");
    String username = scanner.nextLine();
    StepsTracker tracker = new StepsTracker(username);
    int choice;
//ignat loh
    do {
        System.out.println("What would you like to do? Enter the number corresponding to the menu item.");
        System.out.println("1 - Enter steps for a today");
        System.out.println("2 - Print statistics for a specific month");
        System.out.println("3 - Change daily step goal");
        System.out.println("4 - Print overall statistics");
        System.out.println("5 - Enter steps for a specific date");
        System.out.println("0 - Return to profile selection");

        choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter steps for today:");
                int steps = scanner.nextInt();
                LocalDate today = LocalDate.now(); 
                tracker.insertSteps(today, steps);
                System.out.println("Steps inserted!");
                break;
            case 2:
                System.out.println("Enter the month:");
                int statMonth = scanner.nextInt();
                tracker.printMonthlyStats(statMonth);
                break;
            case 3:
                System.out.println("Enter new daily step goal:");
                int newGoal = scanner.nextInt();
                tracker.changeDailyStepGoal(newGoal);
                break;
            case 4:
                tracker.printTotalStats();
                break;
            case 5: {
                System.out.println("Enter date (YYYY-MM-DD):");
                String dateString = scanner.next();
            try {
                LocalDate date = LocalDate.parse(dateString);
                System.out.println("Enter steps:");
                steps = scanner.nextInt();
    
                tracker.insertSteps(date, steps);
                System.out.println("Steps inserted for " + dateString + "!");
}               catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date as 'YYYY-MM-DD'.");
}

            }
        }
    } while (choice != 0);
}

}
