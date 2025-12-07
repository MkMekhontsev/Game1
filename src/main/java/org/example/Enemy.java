package org.example;

public class Enemy extends Unit {
    public Enemy(int x, int y, Map map) {
        super(x, y, map);
    }

    @Override
    public void attack() {
        System.out.println("Враг нападает из засады!");
    }

    @Override
    public void move(int x, int y){
        // пока враг не двигается
    }
}