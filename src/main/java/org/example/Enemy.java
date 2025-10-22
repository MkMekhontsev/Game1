package org.example;

public class Enemy extends Unit {
    public Enemy(int x, int y, Map map) {
        super(x, y, map);
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
