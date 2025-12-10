package org.example;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Map {
    private int length = 24;
    private int height = 18;
    private int[][] positions;
    private List<Enemy> enemies = new ArrayList<>();
    private Hero hero;
    private Random rand = new Random();

    public Map() {
        positions = new int[length][height];
        rand = new Random();

        generateTerrain();
        addRiversAndLakes();
        addMountains();
        addForests();
        addStructures();
        placeEnemies();

        positions[0][0] = 0;
    }

    private void generateTerrain() {
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                positions[x][y] = 0;
            }
        }
    }

    private void addRiversAndLakes() {
        for (int i = 0; i < 2; i++) {
            int startX = rand.nextInt(length);
            int startY = rand.nextInt(height);

            int riverLength = 8 + rand.nextInt(10);
            int currentX = startX;
            int currentY = startY;

            for (int j = 0; j < riverLength; j++) {
                if (currentX >= 0 && currentX < length && currentY >= 0 && currentY < height) {
                    positions[currentX][currentY] = 4;

                    if (rand.nextDouble() < 0.3) {
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                int nx = currentX + dx;
                                int ny = currentY + dy;
                                if (nx >= 0 && nx < length && ny >= 0 && ny < height && rand.nextDouble() < 0.7) {
                                    positions[nx][ny] = 4;
                                }
                            }
                        }
                    }
                }

                int dir = rand.nextInt(4);
                switch (dir) {
                    case 0: currentX++; break;
                    case 1: currentX--; break;
                    case 2: currentY++; break;
                    case 3: currentY--; break;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            int centerX = 5 + rand.nextInt(length - 10);
            int centerY = 5 + rand.nextInt(height - 10);
            int lakeSize = 3 + rand.nextInt(3);

            for (int dx = -lakeSize; dx <= lakeSize; dx++) {
                for (int dy = -lakeSize; dy <= lakeSize; dy++) {
                    int x = centerX + dx;
                    int y = centerY + dy;

                    if (x >= 0 && x < length && y >= 0 && y < height) {
                        double distance = Math.sqrt(dx*dx + dy*dy);
                        if (distance <= lakeSize && rand.nextDouble() < 0.8) {
                            positions[x][y] = 4;
                        }
                    }
                }
            }
        }
    }

    private void addMountains() {
        int[][] mountainMask = new int[length][height];

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                if (positions[x][y] != 4 && rand.nextDouble() < 0.2) {
                    mountainMask[x][y] = 1;
                }
            }
        }

        for (int iteration = 0; iteration < 3; iteration++) {
            int[][] newMask = new int[length][height];

            for (int x = 0; x < length; x++) {
                for (int y = 0; y < height; y++) {
                    int mountainNeighbors = countNeighbors(mountainMask, x, y, 1);

                    if (mountainMask[x][y] == 1) {
                        newMask[x][y] = (mountainNeighbors >= 4) ? 1 : 0;
                    } else {
                        newMask[x][y] = (mountainNeighbors >= 5) ? 1 : 0;
                    }
                }
            }

            mountainMask = newMask;
        }

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                if (mountainMask[x][y] == 1 && positions[x][y] != 4) {
                    positions[x][y] = 1;
                }
            }
        }
    }

    private void addForests() {
        for (int i = 0; i < 4; i++) {
            int centerX = 3 + rand.nextInt(length - 6);
            int centerY = 3 + rand.nextInt(height - 6);
            int forestSize = 2 + rand.nextInt(3);

            for (int dx = -forestSize; dx <= forestSize; dx++) {
                for (int dy = -forestSize; dy <= forestSize; dy++) {
                    int x = centerX + dx;
                    int y = centerY + dy;

                    if (x >= 0 && x < length && y >= 0 && y < height) {
                        if (positions[x][y] == 0 && isAwayFromWater(x, y)) {
                            if (rand.nextDouble() < 0.7) {
                                positions[x][y] = 6;
                            }
                        }
                    }
                }
            }
        }
    }

    private void addStructures() {
        for (int i = 0; i < 2; i++) {
            int castleX = 5 + rand.nextInt(length - 10);
            int castleY = 5 + rand.nextInt(height - 10);

            if (positions[castleX][castleY] == 4) continue;

            int castleSize = 2 + rand.nextInt(2);

            for (int dx = -castleSize; dx <= castleSize; dx++) {
                for (int dy = -castleSize; dy <= castleSize; dy++) {
                    int x = castleX + dx;
                    int y = castleY + dy;

                    if (x >= 0 && x < length && y >= 0 && y < height) {
                        if (Math.abs(dx) == castleSize || Math.abs(dy) == castleSize || rand.nextDouble() < 0.3) {
                            positions[x][y] = 5;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            placeRandomObject(2, 0.1);
        }

        for (int i = 0; i < 5; i++) {
            placeRandomObject(3, 0.05);
        }

        for (int i = 0; i < 3; i++) {
            int lavaX = rand.nextInt(length);
            int lavaY = rand.nextInt(height);
            int lavaSize = 1 + rand.nextInt(2);

            for (int dx = -lavaSize; dx <= lavaSize; dx++) {
                for (int dy = -lavaSize; dy <= lavaSize; dy++) {
                    int x = lavaX + dx;
                    int y = lavaY + dy;

                    if (x >= 0 && x < length && y >= 0 && y < height) {
                        if (positions[x][y] == 0 && rand.nextDouble() < 0.8) {
                            positions[x][y] = 7;
                        }
                    }
                }
            }
        }
    }

    private void placeRandomObject(int objectType, double probability) {
        for (int attempts = 0; attempts < 100; attempts++) {
            int x = rand.nextInt(length);
            int y = rand.nextInt(height);

            if (positions[x][y] == 0 && rand.nextDouble() < probability) {
                positions[x][y] = objectType;
                return;
            }
        }
    }

    private void placeEnemies() {
        int enemyCount = 5 + rand.nextInt(6);

        for (int i = 0; i < enemyCount; i++) {
            int x, y;
            int attempts = 0;

            do {
                x = rand.nextInt(length);
                y = rand.nextInt(height);
                attempts++;
            } while ((positions[x][y] != 0 || (x == 0 && y == 0)) && attempts < 100);

            if (attempts < 100) {
                enemies.add(new Enemy(x, y, this));
            }
        }
    }

    private int countNeighbors(int[][] mask, int x, int y, int value) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < length && ny >= 0 && ny < height) {
                    if (mask[nx][ny] == value) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean isAwayFromWater(int x, int y) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < length && ny >= 0 && ny < height) {
                    if (positions[nx][ny] == 4) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public void updateEnemies() {
        enemies.removeIf(enemy -> !enemy.isActive());

        for (Enemy enemy : enemies) {
            if (hero != null) {
                enemy.move(hero.getX(), hero.getY());
            }
        }
    }

    public void showObjects(int playerX, int playerY) {
        System.out.println("КАРТА МИРА С КООРДИНАТАМИ (24x18)");
        System.out.println("Легенда: .-трава, ^-горы, $-сокровищница, *-артефакт, ~-вода, #-стена замка, T-деревья, L-лава, @-игрок, E-враг");
        System.out.println();

        System.out.print("    ");
        for (int x = 0; x < length; x++) {
            System.out.printf("%2d ", x);
        }
        System.out.println();

        for (int y = 0; y < height; y++) {
            System.out.printf("%2d | ", y);
            for (int x = 0; x < length; x++) {
                if (x == playerX && y == playerY) {
                    System.out.print("@  ");
                } else if (hasEnemyAt(x, y)) {
                    System.out.print("E  ");
                } else {
                    System.out.print(getSymbol(positions[x][y]) + "  ");
                }
            }
            System.out.println();
        }
    }

    private boolean hasEnemyAt(int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y && enemy.isActive()) {
                return true;
            }
        }
        return false;
    }

    private char getSymbol(int objectType) {
        switch (objectType) {
            case 0: return '.';
            case 1: return '^';
            case 2: return '$';
            case 3: return '*';
            case 4: return '~';
            case 5: return '#';
            case 6: return 'T';
            case 7: return 'L';
            default: return '?';
        }
    }

    public int getObject(int x, int y) {
        if (x < 0 || y < 0 || x >= length || y >= height) {
            return -1;
        }
        return positions[x][y];
    }

    public boolean isWalkable(int x, int y) {
        int obj = getObject(x, y);
        return obj == 0 || obj == 2 || obj == 3 || obj == 6;
    }

    public boolean isDangerous(int x, int y) {
        int obj = getObject(x, y);
        return obj == 7;
    }

    public void removeObject(int x, int y) {
        if (x >= 0 && x < length && y >= 0 && y < height) {
            if (positions[x][y] == 2 || positions[x][y] == 3) {
                positions[x][y] = 0;
            }
        }
    }

    public int getLength() { return length; }
    public int getHeight() { return height; }
}