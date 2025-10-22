package org.example;

public class Hero extends Unit {
    public Hero(int x, int y, Map map) {
        super(x, y, map);
    }

    @Override
    public void attack() {
        System.out.println("Герой наносит удар мечом!");
    }
}
