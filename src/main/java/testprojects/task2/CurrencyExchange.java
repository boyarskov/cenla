package testprojects.task2;

import java.util.Locale;
import java.util.Scanner;

public class CurrencyExchange {
        private static final double USD_TO_USD = 1.00;
        private static final double EUR_TO_USD = 1.08;
        private static final double RUB_TO_USD = 0.011;
        private static final double CNY_TO_USD = 0.14;
        private static final double KZT_TO_USD = 0.0022;

        public static void main(String[] args) {
            Locale.setDefault(Locale.US);

            Scanner sc = new Scanner(System.in);

            System.out.println("Курсы валют (фиксированные внутри программы). Базовая валюта: USD.");
            System.out.println("Доступные валюты: USD, EUR, RUB, CNY, KZT");
            System.out.print("Введите сумму: ");

            while (!sc.hasNextDouble()) {
                System.out.println("Ошибка: введите число. Пример: 1234.56");
                sc.next();
            }
            double amount = sc.nextDouble();

            System.out.print("Введите валюту суммы (USD/EUR/RUB/CNY/KZT): ");
            String code = sc.next().trim().toUpperCase(Locale.ROOT);

            Double inUsd = toUsd(amount, code);
            if (inUsd == null) {
                System.out.println("Неизвестный код валюты. Попробуйте снова.");
                sc.close();
                return;
            }

            printAll(inUsd);
            sc.close();
        }

        private static Double toUsd(double amount, String code) {
            return switch (code) {
                case "USD" -> amount * USD_TO_USD;
                case "EUR" -> amount * EUR_TO_USD;
                case "RUB" -> amount * RUB_TO_USD;
                case "CNY" -> amount * CNY_TO_USD;
                case "KZT" -> amount * KZT_TO_USD;
                default -> null;
            };
        }

        private static void printAll(double inUsd) {
            double usd = inUsd;
            double eur = inUsd / EUR_TO_USD;
            double rub = inUsd / RUB_TO_USD;
            double cny = inUsd / CNY_TO_USD;
            double kzt = inUsd / KZT_TO_USD;

            System.out.println("Результаты конвертации:");
            System.out.printf("USD: %.2f%n", usd);
            System.out.printf("EUR: %.2f%n", eur);
            System.out.printf("RUB: %.2f%n", rub);
            System.out.printf("CNY: %.2f%n", cny);
            System.out.printf("KZT: %.2f%n", kzt);
        }
    }