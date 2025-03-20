package main;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // Wymiary planszy
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); // wywołuje metodę run()
    }

    @Override
    public void run() {

        // Pętla gry
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {

    }

    // Metoda, która pozwala na rysowanie obiektów na panelu
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
