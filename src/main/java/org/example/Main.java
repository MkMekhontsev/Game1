package org.example;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        Hero hero = new Hero(0, 0, map);

        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame(hero, map);
            gameFrame.setVisible(true);
        });

        System.out.println("=== ПРИКЛЮЧЕНЧЕСКАЯ ИГРА С ПРОДВИНУТОЙ ГЕНЕРАЦИЕЙ КАРТЫ ===");
        System.out.println("Размер карты: 24x18 клеток");
        System.out.println("Генерация включает:");
        System.out.println("- Реки и озера (вода, код ~)");
        System.out.println("- Горы (^) с использованием клеточного автомата");
        System.out.println("- Леса (деревья, T)");
        System.out.println("- Замки (стены, #)");
        System.out.println("- Лаву (L) - наносит урон!");
        System.out.println("- 10 сокровищ ($) и 5 артефактов (*)");
        System.out.println("- 5-10 врагов");
        System.out.println("\nУправление:");
        System.out.println("- Введите координаты и нажмите 'Переместиться'");
        System.out.println("- 'Атаковать вокруг' - атака соседних врагов");
        System.out.println("- 'Ход врагов' - принудительный ход врагов");
        System.out.println("- 'Показать карту' - отображение карты в консоли");
    }
}