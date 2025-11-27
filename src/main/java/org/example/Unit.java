package org.example;

public abstract class Unit {
    protected int x;
    protected int y;
    protected Map map;

    protected int hp = 100;
    protected int score = 0;
    protected int strength = 10; // добавлено для атаки

    public Unit(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public abstract void attack();
    public abstract void move(int targetX, int targetY);

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }
    public int getScore() { return score; }

    public void addHp(int amount) { hp += amount; }
    public void addScore(int amount) { score += amount; }
}
