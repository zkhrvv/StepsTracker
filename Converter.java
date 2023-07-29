
public class Converter {
    final static double lengthStep = 0.75d;
    final static double calorieStep = 50d;

    
    static void convert(int steps) {
        System.out.println("" + (steps * lengthStep / 1000));
        System.out.println("" + (steps * calorieStep / 1000));
    }
}