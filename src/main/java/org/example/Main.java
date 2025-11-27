package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        Hero hero = new Hero(0, 0, map);
        Scanner scanner = new Scanner(System.in);
        int x, y;

        System.out.println("Добро пожаловать в игру!");
        System.out.println("Команды: a - атака, q - выход");

        while (true) {
            System.out.println("\n===================================");
            map.showObjects(hero.getX(), hero.getY());
            System.out.println("Ваш HP: " + hero.getHp() + " | Очки: " + hero.getScore());

            System.out.println("Введите координаты точки в которую хотите переместиться: ");
            x = scanner.nextInt();
            y = scanner.nextInt();

            while (!isValidCoordinate(x, y, map)) {
                System.out.println("Вы ввели неправильные координаты, введите их еще раз: ");
                x = scanner.nextInt();
                y = scanner.nextInt();
            }

            hero.move(x, y);

            if (hero.getHp() <= 0) {
                System.out.println("Вы проиграли! Финальный счёт: " + hero.getScore());
                break;
            }
        }
    }

    private static boolean isValidCoordinate(int x, int y, Map map) {
        return x >= 0 && x < map.getLength() && y >= 0 && y < map.getHeight();
    }
}