package com.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT= 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    Panel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start();
    }
    public void start() {
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if(running) {
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
             */
            g.setColor(Color.blue);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.white);
                    g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 10, 5);
                } else {
                    g.setColor(Color.lightGray);
//                  makes the body parts randomly change colours
//                  g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.blue);
            g.setFont(new Font("Helvetica", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    public void spawnFood() {
        foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move() {
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'W':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'S':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'A':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'D':
                x[0] = x[0] + UNIT_SIZE;
        }
    }
    public void checkFood() {
        if((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            spawnFood();
        }
    }
    public void checkCollisions() {
        // head collides with body
        for(int i=bodyParts;i>0;i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // head collides wit boarders
        if(x[0] < 0) {
            running = false;
        }
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if(y[0] < 0) {
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        g.setColor(Color.blue);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Helvetica", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if(direction != 'D') {
                        direction = 'A';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction != 'A') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction != 'S') {
                        direction = 'W';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction != 'W') {
                        direction = 'S';
                    }
                    break;
            }
        }
    }
}
