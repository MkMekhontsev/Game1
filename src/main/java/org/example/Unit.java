package org.example;
import java.util.Random;

public class Unit {
    private int x, y, hp = 100, score = 0;
    public Unit(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void damage(){
        int damage = new Random().nextInt(30);
        this.hp = this.hp - damage;
        if(this.hp < 0){
            System.out.println("Поражение :(");
            return;
        }
        if(this.hp <50){
            System.out.println("Осталось меньше половины здоровья");
        }
    }

    public void move(int x, int y, Map map){
        if()
    }
}
