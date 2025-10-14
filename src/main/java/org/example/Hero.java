package org.example;

public class Hero extends Unit {
    public Hero(int x, int y) {
        super(x, y);
    }

    @Override
    public void attack() {
        System.out.println("Герой наносит удар мечом!");
    }
}
