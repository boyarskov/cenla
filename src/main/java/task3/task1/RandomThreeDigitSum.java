package task3.task1;
import java.util.Random;

public class RandomThreeDigitSum {
    public static int sumDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum = sum + number % 10;
            number = number / 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        int number = 100 + (new java.util.Random()).nextInt(900);

        System.out.println("Random three digit: " + number);
        System.out.println("Sum digit: " + sumDigits(number));
    }
}
