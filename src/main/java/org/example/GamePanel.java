package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
    private final org.example.Map map;
    private final Hero hero;
    private static final int TILE_SIZE = 40;

    private final Map<Integer, Image> tileImages = new HashMap<>();
    private boolean imagesLoaded = false;

    public GamePanel(org.example.Map map, Hero hero) {
        this.map = map;
        this.hero = hero;

        setPreferredSize(new Dimension(map.getLength() * TILE_SIZE, map.getHeight() * TILE_SIZE));

        loadTileImages();
    }

    private void loadTileImages() {
        try {
            // Загружаем изображения для каждого типа тайла
            tileImages.put(0, loadImage("grass.png"));     // Трава
            tileImages.put(1, loadImage("mountain.png"));  // Горы
            tileImages.put(2, loadImage("treasure.png"));  // Сокровищница
            tileImages.put(3, loadImage("artifact.png"));  // Артефакт
            tileImages.put(4, loadImage("water.png"));     // Вода
            tileImages.put(5, loadImage("wall.png"));      // Стена замка

            imagesLoaded = true;
            System.out.println("Изображения тайлов успешно загружены!");
        } catch (Exception e) {
            System.out.println("Не удалось загрузить изображения. Используются цветные заглушки.");
            System.out.println("Поместите изображения в папку 'tiles' внутри папки проекта:");
            System.out.println(" - grass.png, mountain.png, treasure.png");
            System.out.println(" - artifact.png, water.png, wall.png");
            imagesLoaded = false;
        }
    }

    private Image loadImage(String filename) {
        String path = "tiles/" + filename;
        ImageIcon icon = new ImageIcon(path);

        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            throw new RuntimeException("Не удалось загрузить изображение: " + path);
        }

        // Масштабируем изображение под размер тайла
        return icon.getImage().getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
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

                if (imagesLoaded && tileImages.containsKey(objectType)) {
                    // Рисуем изображение
                    g.drawImage(tileImages.get(objectType), drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                } else {
                    // Рисуем цветную заглушку
                    g.setColor(getColorForTile(objectType));
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Рисуем героя
        g.setColor(Color.RED);
        int heroDrawX = hero.getX() * TILE_SIZE;
        int heroDrawY = hero.getY() * TILE_SIZE;
        g.fillOval(heroDrawX + TILE_SIZE/4, heroDrawY + TILE_SIZE/4, TILE_SIZE/2, TILE_SIZE/2);

        // Рисуем координаты
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 9));
        for (int x = 0; x < map.getLength(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                g.drawString("(" + x + "," + y + ")", x * TILE_SIZE + 2, y * TILE_SIZE + 12);
            }
        }
    }

    // Геттер для проверки загрузки изображений
    public boolean areImagesLoaded() {
        return imagesLoaded;
    }
}