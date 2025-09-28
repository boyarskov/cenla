package task1;

import java.util.Random;
import java.util.Scanner;

public class Hangman {

    private static final String[] WORDS = {
            "cat", "dog", "horse", "tiger", "lion", "elephant", "giraffe", "monkey", "zebra"
    };

    private static final int MAX_LIVES = 9;

    public static void main(String[] args) {
        Random random = new Random();
        String secretWord = WORDS[random.nextInt(WORDS.length)];
        StringBuilder maskedWord = new StringBuilder("_".repeat(secretWord.length()));
        int remainingLives = MAX_LIVES;

        boolean[] usedLetters = new boolean[26];

        Scanner scanner = new Scanner(System.in);

        System.out.println("Начало игры Виселица!");

        while (remainingLives > 0 && maskedWord.toString().contains("_")) {
            System.out.println("Загаданное слово: " + maskedWord);
            System.out.println("У вас осталось: " + remainingLives + " жизней");
            System.out.print("Введите букву (a-z): ");

            String token = scanner.next();
            if (token.isEmpty()) {
                System.out.println("Пустой ввод. Повторите попытку.");
                continue;
            }

            char guess = Character.toLowerCase(token.charAt(0));

            if (!Character.isLetter(guess) || guess < 'a' || guess > 'z') {
                System.out.println("Нужно ввести букву латинского алфавита (a-z).");
                continue;
            }

            int idx = guess - 'a';
            if (usedLetters[idx]) {
                System.out.println("Эта буква уже была введена.");
                continue;
            }
            usedLetters[idx] = true;

            if (secretWord.indexOf(guess) >= 0) {
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guess) {
                        maskedWord.setCharAt(i, guess);
                    }
                }
                System.out.println("Такая буква есть в слове");
            } else {
                remainingLives--;
                System.out.println("Ошибка. Такой буквы нет в слове");
            }
        }

        if (maskedWord.toString().equals(secretWord)) {
            System.out.println("Победа. Вы отгадали слово: " + secretWord);
        } else {
            System.out.println("Попробуйте снова. Правильный ответ: " + secretWord);
        }

        scanner.close();
    }
}