package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public GameFrame(Hero hero, Map map) {
        this.hero = hero;
        this.map = map;

        setTitle("Приключенческая игра");
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

        JButton attackButton = new JButton("Атака");
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hero.attack();
                updateStats();
                appendMessage("Герой атакует! Уровень: " + hero.getLevel() + ", Сила: " + (hero.getLevel() * 10 + 10));
            }
        });
        controlPanel.add(attackButton);

        mainPanel.add(controlPanel);

        messageArea = new JTextArea(5, 40);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
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
                    updateStats();
                    gamePanel.repaint();

                    if (hero.getLevel() > oldLevel) {
                        appendMessage("Поздравляем! Вы достигли " + hero.getLevel() + " уровня!");
                        appendMessage("Здоровье и сила увеличены!");
                    }

                    if (hero.getHp() <= 0) {
                        appendMessage("Вы проиграли! Финальный счёт: " + hero.getScore() + "\n");
                        moveButton.setEnabled(false);
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