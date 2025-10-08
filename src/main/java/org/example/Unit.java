package org.example;

import java.util.Random;

public class Unit {
    private int x, y, hp = 100, score = 0;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    public void move(int newX, int newY, Map map) {
        if (newX < 0 || newY < 0 || newX >= map.getLenght() || newY >= map.getHeight()) {
            System.out.println("Нельзя выйти за пределы карты!");
            return;
        }

        int cellType = map.getObject(newX, newY);

        switch (cellType) {
            case 1:
                System.out.println("Вы прошли через горы. Это было тяжело...");
                damage();
                break;
            case 2:
                System.out.println("Вы нашли сокровищницу! +100 очков");
                score += 100;
                break;
            case 3:
                System.out.println("Вы нашли артефакт! +50 очков и +20 HP");
                score += 50;
                hp += 20;
                if (hp > 100) hp = 100;
                break;
            case 4:
                System.out.println("Вы вошли в воду и утонули :(");
                hp = 0;
                return;
            case 5:
                System.out.println("Вы врезались в стену замка. Двигаться туда нельзя.");
                return;
            case 0:
            default:
                System.out.println("Вы прошли по траве.");
        }

        this.x = newX;
        this.y = newY;
        System.out.println("Текущая позиция: [" + x + "," + y + "], HP: " + hp + ", Очки: " + score);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public int getScore() {
        return score;
    }
}
