package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("ChessApp");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Zamknięcie okna kończy proces całego programu
        window.setResizable(false); // Nie można zmienić rozmiaru okna

        // Dodanie GamePanel do okna
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null); // Okno na środku ekranu
        window.setVisible(true);

        gp.launchGame();

    }   
}