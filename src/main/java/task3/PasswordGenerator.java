package task3;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PasswordGenerator {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%&*()-_=+[]{};:,.?/";

    private static final SecureRandom RND = new SecureRandom();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Генератор паролей (8–12 символов), введите длину:");

        int length;
        while (true) {
            System.out.print("Введите длинну пароля (8–12): ");
            if (sc.hasNextInt()) {
                length = sc.nextInt();
                if (length >= 8 && length <= 12) break;
                System.out.println("Ошибка: длина должна быть от 8 до 12.");
            } else {
                System.out.println("Ошибка: укажите целое число.");
                sc.next();
            }
        }

        String password = generate(length);
        System.out.println("Пароль (" + length + "): " + password);
        sc.close();
    }

    private static String generate(int length) {
        List<Character> chars = new ArrayList<>(length);

        chars.add(LOWER.charAt(RND.nextInt(LOWER.length())));
        chars.add(UPPER.charAt(RND.nextInt(UPPER.length())));
        chars.add(DIGITS.charAt(RND.nextInt(DIGITS.length())));
        chars.add(SPECIAL.charAt(RND.nextInt(SPECIAL.length())));

        String all = LOWER + UPPER + DIGITS + SPECIAL;
        while (chars.size() < length) {
            chars.add(all.charAt(RND.nextInt(all.length())));
        }

        Collections.shuffle(chars, RND);

        StringBuilder sb = new StringBuilder(length);
        for (char c : chars) sb.append(c);
        return sb.toString();
    }
}
