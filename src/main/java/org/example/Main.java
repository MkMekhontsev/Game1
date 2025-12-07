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

        System.out.println("Добро пожаловать в игру!");
    }
}