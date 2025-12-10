package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameFrame extends JFrame {
    private final Hero hero;
    private final Map map;
    private final GamePanel gamePanel;
    private final JLabel hpLabel;
    private final JLabel scoreLabel;
    private final JLabel levelLabel;
    private final JLabel expLabel;
    private final JTextField xField;
    private final JTextField yField;
    private final JButton moveButton;
    private final JTextArea messageArea;
    private final JButton attackButton;
    private final JButton enemyTurnButton;
    private final JButton showMapButton;

    public GameFrame(Hero hero, Map map) {
        this.hero = hero;
        this.map = map;
        map.setHero(hero);

        setTitle("Приключенческая игра - Генерация карты");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        gamePanel = new GamePanel(map, hero);
        mainPanel.add(gamePanel);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout());

        hpLabel = new JLabel("HP: " + hero.getHp());
        scoreLabel = new JLabel("Очки: " + hero.getScore());
        levelLabel = new JLabel("Уровень: " + hero.getLevel());
        expLabel = new JLabel("Опыт: " + hero.getExperience() + "/" + hero.getExperienceToNextLevel());

        statsPanel.add(hpLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(levelLabel);
        statsPanel.add(expLabel);

        mainPanel.add(statsPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        controlPanel.add(new JLabel("X:"));
        xField = new JTextField(3);
        controlPanel.add(xField);

        controlPanel.add(new JLabel("Y:"));
        yField = new JTextField(3);
        controlPanel.add(yField);

        moveButton = new JButton("Переместиться");
        moveButton.addActionListener(new MoveActionListener());
        controlPanel.add(moveButton);

        attackButton = new JButton("Атаковать вокруг");
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attackNearbyEnemies();
                updateStats();
                gamePanel.repaint();
            }
        });
        controlPanel.add(attackButton);

        enemyTurnButton = new JButton("Ход врагов");
        enemyTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enemyTurn();
                updateStats();
                gamePanel.repaint();
            }
        });
        controlPanel.add(enemyTurnButton);

        showMapButton = new JButton("Показать карту");
        showMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.showObjects(hero.getX(), hero.getY());
                appendMessage("Карта отображена в консоли");
            }
        });
        controlPanel.add(showMapButton);

        mainPanel.add(controlPanel);

        messageArea = new JTextArea(5, 50);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);

        appendMessage("Добро пожаловать в игру с улучшенной генерацией карты!");
        appendMessage("Размер карты: 24x18 клеток");
        appendMessage("Новые объекты: деревья (T) и лава (L)");
        appendMessage("Лава наносит урон при попадании на нее!");
    }

    private class MoveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int targetX = Integer.parseInt(xField.getText());
                int targetY = Integer.parseInt(yField.getText());

                if (isValidCoordinate(targetX, targetY)) {
                    messageArea.setText("");

                    int oldLevel = hero.getLevel();

                    hero.move(targetX, targetY);

                    enemyTurn();

                    updateStats();
                    gamePanel.repaint();

                    if (hero.getLevel() > oldLevel) {
                        appendMessage("Поздравляем! Вы достигли " + hero.getLevel() + " уровня!");
                        appendMessage("Здоровье и сила увеличены!");
                    }

                    if (hero.getHp() <= 0) {
                        appendMessage("Вы проиграли! Финальный счёт: " + hero.getScore() + "\n");
                        moveButton.setEnabled(false);
                        attackButton.setEnabled(false);
                        enemyTurnButton.setEnabled(false);
                        showMapButton.setEnabled(false);
                    }
                } else {
                    messageArea.append("Неверные координаты. Диапазон: 0-" + (map.getLength() - 1) + ", 0-" + (map.getHeight() - 1) + "\n");
                }
            } catch (NumberFormatException ex) {
                messageArea.append("Ошибка ввода. Введите числа для координат.\n");
            }
            xField.setText("");
            yField.setText("");
        }
    }

    private void attackNearbyEnemies() {
        List<Enemy> enemies = map.getEnemies();
        int enemiesHit = 0;

        for (Enemy enemy : enemies) {
            int dx = Math.abs(hero.getX() - enemy.getX());
            int dy = Math.abs(hero.getY() - enemy.getY());

            if (dx <= 1 && dy <= 1 && (dx + dy) > 0 && enemy.isActive()) {
                hero.attack();
                enemy.takeDamage(hero.getStrength());
                enemiesHit++;

                if (!enemy.isActive()) {
                    appendMessage("Враг повержен! +10 очков, +20 опыта");
                }
            }
        }

        if (enemiesHit == 0) {
            appendMessage("Нет врагов рядом для атаки");
        } else {
            appendMessage("Атаковано врагов: " + enemiesHit);
        }

        map.updateEnemies();
    }

    private void enemyTurn() {
        appendMessage("--- Ход врагов ---");
        map.updateEnemies();
        appendMessage("Враги сделали ход");

        if (hero.getHp() <= 0) {
            appendMessage("Вы проиграли! Финальный счёт: " + hero.getScore() + "\n");
            moveButton.setEnabled(false);
            attackButton.setEnabled(false);
            enemyTurnButton.setEnabled(false);
            showMapButton.setEnabled(false);
        }
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < map.getLength() && y >= 0 && y < map.getHeight();
    }

    public void updateStats() {
        hpLabel.setText("HP: " + hero.getHp());
        scoreLabel.setText("Очки: " + hero.getScore());
        levelLabel.setText("Уровень: " + hero.getLevel());
        expLabel.setText("Опыт: " + hero.getExperience() + "/" + hero.getExperienceToNextLevel());
    }

    public void appendMessage(String message) {
        messageArea.append(message + "\n");
    }
}