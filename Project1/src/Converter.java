public class Converter {
    final static double lengthStep = 0.75d;
    final static double calorieStep = 50d;

    static void convert(int steps) {
        System.out.println("Distance covered: " + (steps * lengthStep / 1000) + " km");
        System.out.println("Calories burned: " + (steps * calorieStep / 1000) + " kcal");
    }
}