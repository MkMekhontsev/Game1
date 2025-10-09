package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        Hero hero = new Hero(0, 0);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в игру!");
        System.out.println("Управление: w - вверх, s - вниз, a - влево, d - вправо, q - выход");

        while (true) {
            System.out.println("\n===================================");
            map.showObjects(hero.getX(), hero.getY());
            System.out.println("Ваш HP: " + hero.getHp() + " | Очки: " + hero.getScore());
            System.out.print("Введите команду: ");
            String command = scanner.nextLine();

            int x = hero.getX();
            int y = hero.getY();

            switch (command.toLowerCase()) {
                case "w": hero.move(x - 1, y, map); break;
                case "s": hero.move(x + 1, y, map); break;
                case "a": hero.move(x, y - 1, map); break;
                case "d": hero.move(x, y + 1, map); break;
                case "q": System.out.println("Выход из игры. До свидания!"); return;
                default: System.out.println("Неизвестная команда!");
            }

            if (hero.getHp() <= 0) {
                System.out.println("Вы проиграли! Финальный счёт: " + hero.getScore());
                break;
            }
        }
    }
}
