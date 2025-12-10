package org.example;

import java.util.Random;

public class Enemy extends Unit {
    private static final Random rand = new Random();
    private boolean isActive = true;
    private int visionRange = 3;

    public Enemy(int x, int y, Map map) {
        super(x, y, map);
        this.hp = 30 + rand.nextInt(20);
        this.strength = 5 + rand.nextInt(5);
    }

    @Override
    public void attack() {
        System.out.println("Враг нападает из засады! Сила: " + strength);
    }

    @Override
    public void move(int targetX, int targetY) {
        if (!isActive || hp <= 0) return;

        Hero hero = map.getHero();
        if (hero != null) {
            int dx = hero.getX() - x;
            int dy = hero.getY() - y;
            int distance = Math.abs(dx) + Math.abs(dy);

            if (distance <= visionRange && distance > 0) {
                int moveX = x;
                int moveY = y;

                if (Math.abs(dx) > Math.abs(dy)) {
                    moveX += Integer.signum(dx);
                } else {
                    moveY += Integer.signum(dy);
                }

                if (map.isWalkable(moveX, moveY) && !isEnemyAt(moveX, moveY)) {
                    setPosition(moveX, moveY);
                    System.out.println("Враг движется к герою: (" + x + "," + y + ")");

                    if (x == hero.getX() && y == hero.getY()) {
                        attack();
                        hero.addHp(-strength);
                        System.out.println("Враг атаковал героя! Нанесено " + strength + " урона");
                    }
                }
            } else {
                randomMove();
            }
        } else {
            randomMove();
        }
    }

    private void randomMove() {
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        int[] dir = directions[rand.nextInt(4)];
        int newX = x + dir[0];
        int newY = y + dir[1];

        if (map.isWalkable(newX, newY) && !isEnemyAt(newX, newY)) {
            setPosition(newX, newY);
        }
    }

    private boolean isEnemyAt(int x, int y) {
        for (Enemy enemy : map.getEnemies()) {
            if (enemy != this && enemy.getX() == x && enemy.getY() == y && enemy.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            isActive = false;
            System.out.println("Враг повержен!");
            Hero hero = map.getHero();
            if (hero != null) {
                hero.addScore(10);
                hero.addExperience(20);
            }
        } else {
            System.out.println("Врагу нанесено " + damage + " урона. Осталось HP: " + hp);
        }
    }

    public boolean isActive() {
        return isActive && hp > 0;
    }

    public int getVisionRange() {
        return visionRange;
    }
}