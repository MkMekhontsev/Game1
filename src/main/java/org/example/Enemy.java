package org.example;

public class Enemy extends Unit {
    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void move(int newX, int newY, Map map) {
        super.move(newX, newY, map);
    }

    @Override
    public void attack() {
        System.out.println("Враг нападает из засады!");
    }
}
