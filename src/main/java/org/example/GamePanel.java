package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
    private final org.example.Map map; // Ваш класс карты
    private final Hero hero;
    private static final int TILE_SIZE = 40;

    private final Map<Integer, Image> tileImages = new HashMap<>();

    public GamePanel(org.example.Map map, Hero hero) {
        this.map = map;
        this.hero = hero;

        setPreferredSize(new Dimension(map.getLength() * TILE_SIZE, map.getHeight() * TILE_SIZE));

        loadTileImages();
    }

    private void loadTileImages() {
        System.out.println("Используются цветные заглушки вместо изображений.");
    }

    private Color getColorForTile(int objectType) {
        return switch (objectType) {
            case 0 -> new Color(124, 252, 0); // Трава
            case 1 -> new Color(139, 69, 19);  // Горы
            case 2 -> Color.YELLOW;            // Сокровищница
            case 3 -> Color.ORANGE;            // Артефакт
            case 4 -> Color.BLUE;              // Вода
            case 5 -> Color.DARK_GRAY;         // Стена замка
            default -> Color.PINK;
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < map.getLength(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int objectType = map.getObject(x, y);

                int drawX = x * TILE_SIZE;
                int drawY = y * TILE_SIZE;

                if (tileImages.containsKey(objectType)) {
                    g.drawImage(tileImages.get(objectType), drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                } else {
                    g.setColor(getColorForTile(objectType));
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE); // Сетка
                }
            }
        }

        g.setColor(Color.RED);
        int heroDrawX = hero.getX() * TILE_SIZE;
        int heroDrawY = hero.getY() * TILE_SIZE;
        g.fillOval(heroDrawX + TILE_SIZE/4, heroDrawY + TILE_SIZE/4, TILE_SIZE/2, TILE_SIZE/2);

        g.setColor(Color.BLACK);
        for (int x = 0; x < map.getLength(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                g.drawString("(" + x + "," + y + ")", x * TILE_SIZE + 5, y * TILE_SIZE + 15);
            }
        }
    }
}