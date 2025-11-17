package org.example;

import java.util.Random;

public abstract class Unit {
    private int x, y, hp = 100, score = 0;
    protected Map map;

    public Unit(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public abstract void attack();

    public abstract void move(int x, int y);

    public void damage() {
        int damage = new Random().nextInt(30);
        hp -= damage;
        System.out.println("Получен урон: " + damage + " HP осталось: " + hp);

        if (hp <= 0) {
            System.out.println("Поражение :(");
        } else if (hp < 50) {
            System.out.println("Осталось меньше половины здоровья");
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }
    public int getScore() { return score; }

    protected void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
