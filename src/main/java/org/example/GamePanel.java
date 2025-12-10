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
    private final Map<Integer, Color> tileColors = new HashMap<>();
    private boolean imagesLoaded = false;

    public GamePanel(org.example.Map map, Hero hero) {
        this.map = map;
        this.hero = hero;

        setPreferredSize(new Dimension(map.getLength() * TILE_SIZE, map.getHeight() * TILE_SIZE));

        initializeTileColors();
        loadTileImages();
    }

    private void initializeTileColors() {
        // ЯРКИЕ И КОНТРАСТНЫЕ ЦВЕТА ДЛЯ ЛУЧШЕЙ ВИДИМОСТИ

        // 0 - Трава (Ярко-зеленый)
        tileColors.put(0, new Color(76, 187, 23));

        // 1 - Горы (Темно-коричневый с контрастом)
        tileColors.put(1, new Color(101, 67, 33));

        // 2 - Сокровище (Неоново-желтый)
        tileColors.put(2, new Color(255, 255, 0));

        // 3 - Артефакт (Ярко-оранжевый)
        tileColors.put(3, new Color(255, 140, 0));

        // 4 - Вода (Ярко-синий/голубой)
        tileColors.put(4, new Color(0, 191, 255));

        // 5 - Стена замка (Черный с серым)
        tileColors.put(5, new Color(50, 50, 50));

        // 6 - Деревья (Темно-зеленый, контрастный с травой)
        tileColors.put(6, new Color(0, 100, 0));

        // 7 - Лава (Огненно-красный)
        tileColors.put(7, new Color(255, 69, 0));
    }

    private void loadTileImages() {
        try {
            String[] imageNames = {
                    "grass.png", "mountain.png", "treasure.png", "artifact.png",
                    "water.png", "wall.png", "tree.png", "lava.png"
            };

            for (int i = 0; i < imageNames.length; i++) {
                Image image = loadImage(imageNames[i]);
                tileImages.put(i, image);
            }

            imagesLoaded = true;
            System.out.println("Все изображения загружены успешно!");

        } catch (Exception e) {
            System.out.println("Ошибка загрузки изображений: " + e.getMessage());
            System.out.println("Используются цветные заглушки (яркие цвета).");
            imagesLoaded = false;
        }
    }

    private Image loadImage(String filename) {
        ImageIcon icon = new ImageIcon("tiles/" + filename);

        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            throw new RuntimeException("Не удалось загрузить изображение: tiles/" + filename);
        }

        return icon.getImage().getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Фон для лучшего контраста
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, getWidth(), getHeight());

        drawMap(g);
        drawEnemies(g);
        drawHero(g);
        drawCoordinates(g);
    }

    private void drawMap(Graphics g) {
        for (int x = 0; x < map.getLength(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int objectType = map.getObject(x, y);
                int drawX = x * TILE_SIZE;
                int drawY = y * TILE_SIZE;

                if (imagesLoaded && tileImages.containsKey(objectType)) {
                    g.drawImage(tileImages.get(objectType), drawX, drawY, this);
                } else {
                    Color tileColor = tileColors.getOrDefault(objectType, Color.MAGENTA);
                    g.setColor(tileColor);
                    g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);

                    // Особые эффекты для опасных клеток
                    if (objectType == 7) { // Лава
                        // Яркий оранжевый эффект с прозрачностью
                        g.setColor(new Color(255, 165, 0, 180));
                        g.fillRect(drawX + 5, drawY + 5, TILE_SIZE - 10, TILE_SIZE - 10);

                        // Эффект огня
                        g.setColor(new Color(255, 69, 0, 100));
                        for (int i = 0; i < 3; i++) {
                            int size = TILE_SIZE - 10 - i * 4;
                            g.fillOval(drawX + 5 + i*2, drawY + 5 + i*2, size, size);
                        }
                    }

                    // Эффект для сокровища
                    if (objectType == 2) {
                        g.setColor(new Color(255, 255, 200, 100));
                        g.fillOval(drawX + 10, drawY + 10, TILE_SIZE - 20, TILE_SIZE - 20);
                    }

                    // Эффект для артефакта
                    if (objectType == 3) {
                        g.setColor(new Color(255, 200, 100, 100));
                        g.fillOval(drawX + 8, drawY + 8, TILE_SIZE - 16, TILE_SIZE - 16);
                    }

                    // Эффект для воды
                    if (objectType == 4) {
                        g.setColor(new Color(100, 200, 255, 100));
                        for (int i = 0; i < 2; i++) {
                            int size = TILE_SIZE - 10 - i * 8;
                            g.fillOval(drawX + 5 + i*4, drawY + 5 + i*4, size, size);
                        }
                    }
                }

                // Контрастная черная обводка
                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);

                // Тонкая внутренняя обводка для лучшего контраста
                g.setColor(new Color(0, 0, 0, 50));
                g.drawRect(drawX + 1, drawY + 1, TILE_SIZE - 2, TILE_SIZE - 2);
            }
        }
    }

    private void drawEnemies(Graphics g) {
        for (Enemy enemy : map.getEnemies()) {
            if (enemy.isActive()) {
                int enemyX = enemy.getX() * TILE_SIZE;
                int enemyY = enemy.getY() * TILE_SIZE;

                // Враг - фиолетовый квадрат с черной обводкой
                g.setColor(new Color(128, 0, 128)); // Фиолетовый
                g.fillRect(enemyX + 4, enemyY + 4, TILE_SIZE - 8, TILE_SIZE - 8);

                // Черная обводка врага
                g.setColor(Color.BLACK);
                g.drawRect(enemyX + 4, enemyY + 4, TILE_SIZE - 8, TILE_SIZE - 8);

                // Глаза врага
                g.setColor(Color.RED);
                g.fillOval(enemyX + 10, enemyY + 10, 6, 6);
                g.fillOval(enemyX + TILE_SIZE - 16, enemyY + 10, 6, 6);

                drawHealthBar(g, enemy, enemyX, enemyY);
            }
        }
    }

    private void drawHealthBar(Graphics g, Enemy enemy, int x, int y) {
        int maxHealth = 50;
        int healthWidth = (int)((TILE_SIZE - 10) * ((double)enemy.getHp() / maxHealth));

        // Фон полоски здоровья (серый)
        g.setColor(new Color(100, 100, 100));
        g.fillRect(x + 5, y + 2, TILE_SIZE - 10, 4);

        // Сама полоска здоровья (зеленая для высокого HP, желтая для среднего, красная для низкого)
        Color healthColor;
        double healthPercent = (double)enemy.getHp() / maxHealth;
        if (healthPercent > 0.6) {
            healthColor = new Color(0, 255, 0); // Зеленый
        } else if (healthPercent > 0.3) {
            healthColor = new Color(255, 255, 0); // Желтый
        } else {
            healthColor = new Color(255, 0, 0); // Красный
        }

        g.setColor(healthColor);
        g.fillRect(x + 5, y + 2, Math.max(healthWidth, 2), 4);

        // Черная обводка полоски здоровья
        g.setColor(Color.BLACK);
        g.drawRect(x + 5, y + 2, TILE_SIZE - 10, 4);
    }

    private void drawHero(Graphics g) {
        int heroX = hero.getX() * TILE_SIZE;
        int heroY = hero.getY() * TILE_SIZE;

        // Тело героя - синий круг
        g.setColor(new Color(0, 0, 255)); // Ярко-синий
        g.fillOval(heroX + 5, heroY + 5, TILE_SIZE - 10, TILE_SIZE - 10);

        // Черная обводка героя
        g.setColor(Color.BLACK);
        g.drawOval(heroX + 5, heroY + 5, TILE_SIZE - 10, TILE_SIZE - 10);

        // Щит героя (белый круг слева)
        g.setColor(new Color(255, 255, 255, 200));
        g.fillOval(heroX + 8, heroY + 10, 12, 15);

        // Меч героя (серый треугольник справа)
        int[] xPoints = {heroX + TILE_SIZE - 8, heroX + TILE_SIZE - 8, heroX + TILE_SIZE - 3};
        int[] yPoints = {heroY + 12, heroY + TILE_SIZE - 12, heroY + TILE_SIZE/2};
        g.setColor(new Color(192, 192, 192));
        g.fillPolygon(xPoints, yPoints, 3);

        // Уровень героя в центре
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String levelText = String.valueOf(hero.getLevel());
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(levelText);
        g.drawString(levelText, heroX + TILE_SIZE/2 - textWidth/2, heroY + TILE_SIZE/2 + 5);

        // Контур уровня для читаемости
        g.setColor(Color.BLACK);
        g.drawString(levelText, heroX + TILE_SIZE/2 - textWidth/2 - 1, heroY + TILE_SIZE/2 + 4);
    }

    private void drawCoordinates(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 9));

        for (int x = 0; x < map.getLength(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                String coord = x + "," + y;

                // Фон для координат для читаемости
                g.setColor(new Color(255, 255, 255, 150));
                g.fillRect(x * TILE_SIZE + 1, y * TILE_SIZE + 1, 20, 11);

                // Текст координат
                g.setColor(Color.BLACK);
                g.drawString(coord, x * TILE_SIZE + 2, y * TILE_SIZE + 10);
            }
        }
    }

    public boolean areImagesLoaded() {
        return imagesLoaded;
    }
}