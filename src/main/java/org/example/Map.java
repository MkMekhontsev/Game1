package org.example;

import java.util.Random;

public class Map {
    private int length = 10;
    private int height = 10;
    private int[][] positions = new int[length][height];

    /*
     -1 — за пределами карты
      0 — трава
      1 — горы
      2 — сокровищница
      3 — артефакт
      4 — вода
      5 — стена замка
    */

    public Map() {
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                positions[i][j] = rand.nextInt(6);
            }
        }
    }

    public void showObjects(int playerX, int playerY) {
        System.out.println("КАРТА МИРА С КООРДИНАТАМИ");
        System.out.println("Легенда: .-трава, ^-горы, $-сокровищница, *-артефакт, ~-вода, #-стена замка, @-игрок");
        System.out.println();

        System.out.print("    ");
        for (int x = 0; x < length; x++) {
            System.out.printf("%2d ", x);
        }
        System.out.println();

        for (int y = 0; y < height; y++) {
            // --- вывод координаты Y ---
            System.out.printf("%2d | ", y);

            for (int x = 0; x < length; x++) {
                if (x == playerX && y == playerY) {
                    System.out.print("@  ");
                } else {
                    System.out.print(getSymbol(positions[x][y]) + "  ");
                }
            }
            System.out.println();
        }
    }


    private char getSymbol(int objectType) {
        switch (objectType) {
            case 0: return '.';
            case 1: return '^';
            case 2: return '$';
            case 3: return '*';
            case 4: return '~';
            case 5: return '#';
            default: return '?';
        }
    }

    public int getObject(int x, int y) {
        if (x < 0 || y < 0 || x >= length || y >= height) {
            return -1;
        }
        return positions[x][y];
    }

    public void showCellInfo(int x, int y) {
        int obj = getObject(x, y);
        System.out.println("Позиция [" + x + "," + y + "]: " + obj + " - " + getObjectName(obj));
    }

    private String getObjectName(int objectType) {
        switch (objectType) {
            case -1: return "за пределами карты";
            case 0: return "трава";
            case 1: return "горы";
            case 2: return "сокровищница";
            case 3: return "артефакт";
            case 4: return "вода";
            case 5: return "стена замка";
            default: return "неизвестный объект";
        }
    }

    public int getLenght() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWalkable(int x, int y) {
        int obj = getObject(x, y);
        return obj == 0 || obj == 2 || obj == 3;
    }

}
