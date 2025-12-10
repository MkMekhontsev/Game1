package org.example;

import java.util.*;

public class Hero extends Unit {
    private int level = 1;
    private int experience = 0;
    private int experienceToNextLevel = 100;

    public Hero(int x, int y, Map map) {
        super(x, y, map);
    }

    @Override
    public void attack() {
        System.out.println("Герой наносит удар мечом! (Сила: " + strength + ", Уровень: " + level + ")");
    }

    @Override
    public void move(int targetX, int targetY) {
        List<int[]> path = bfs(getX(), getY(), targetX, targetY);

        if (path == null) {
            System.out.println("Путь не найден!");
            return;
        }

        StringBuilder pathMessage = new StringBuilder("Двигаюсь по маршруту:");
        for (int[] p : path) {
            pathMessage.append(" -> (").append(p[0]).append(",").append(p[1]).append(")");
            setPosition(p[0], p[1]);

            int objectType = map.getObject(p[0], p[1]);
            handleCellObject(objectType, p[0], p[1]);

            checkEnemyCollision(p[0], p[1]);

            checkDangerousCell(p[0], p[1]);
        }
        System.out.println(pathMessage.toString());
    }

    private void handleCellObject(int objectType, int x, int y) {
        switch (objectType) {
            case 2:
                int treasureScore = 20 + (level * 5);
                addScore(treasureScore);
                experience += 15;
                map.removeObject(x, y);
                System.out.println("Найдено сокровище! +" + treasureScore + " очков, +15 опыта");
                checkLevelUp();
                break;

            case 3:
                int artifactScore = 50 + (level * 10);
                addScore(artifactScore);
                experience += 30;
                map.removeObject(x, y);
                System.out.println("Найден артефакт! +" + artifactScore + " очков, +30 опыта");
                checkLevelUp();
                break;

            case 1:
                System.out.println("Преодолены горы! +5 опыта");
                experience += 5;
                checkLevelUp();
                break;

            case 6:
                System.out.println("Пробираетесь через лес...");
                break;
        }
    }

    private void checkDangerousCell(int x, int y) {
        if (map.isDangerous(x, y)) {
            int lavaDamage = 10 + level * 2;
            addHp(-lavaDamage);
            System.out.println("Ой! Лава наносит " + lavaDamage + " урона! HP: " + hp);

            if (hp <= 0) {
                System.out.println("Герой сгорел в лаве!");
            }
        }
    }

    private void checkEnemyCollision(int x, int y) {
        for (Enemy enemy : map.getEnemies()) {
            if (enemy.getX() == x && enemy.getY() == y && enemy.isActive()) {
                System.out.println("Столкновение с врагом!");
                enemy.attack();

                int damage = enemy.getStrength();
                addHp(-damage);
                System.out.println("Враг нанес " + damage + " урона! HP: " + hp);

                attack();
                enemy.takeDamage(strength);

                if (hp <= 0) {
                    System.out.println("Герой повержен!");
                }
                break;
            }
        }
    }

    private void checkLevelUp() {
        while (experience >= experienceToNextLevel) {
            level++;
            experience -= experienceToNextLevel;
            experienceToNextLevel = (int)(experienceToNextLevel * 1.5);

            int oldHp = hp;
            hp += 20;
            strength += 5;

            System.out.println("Уровень повышен! Новый уровень: " + level);
            System.out.println("Здоровье: " + oldHp + " -> " + hp);
            System.out.println("Сила: " + (strength - 5) + " -> " + strength);
        }
    }

    public void addExperience(int amount) {
        experience += amount;
        checkLevelUp();
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    private List<int[]> bfs(int startX, int startY, int endX, int endY) {
        int w = map.getLength();
        int h = map.getHeight();

        boolean[][] visited = new boolean[w][h];
        int[][][] parent = new int[w][h][2];

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];

            if (x == endX && y == endY)
                break;

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx < 0 || ny < 0 || nx >= w || ny >= h)
                    continue;

                if (!map.isWalkable(nx, ny))
                    continue;

                if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parent[nx][ny][0] = x;
                    parent[nx][ny][1] = y;
                    q.add(new int[]{nx, ny});
                }
            }
        }

        if (!visited[endX][endY])
            return null;

        List<int[]> path = new ArrayList<>();
        int cx = endX, cy = endY;

        while (!(cx == startX && cy == startY)) {
            path.add(new int[]{cx, cy});
            int px = parent[cx][cy][0];
            int py = parent[cx][cy][1];
            cx = px;
            cy = py;
        }

        Collections.reverse(path);
        return path;
    }
}