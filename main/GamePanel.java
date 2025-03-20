package main;

import java.awt.Dimension;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;

public class GamePanel extends JPanel implements Runnable {

    // Wymiary planszy
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    // Figury
    public static ArrayList<Piece> pieces = new ArrayList<>(); // Backup, na wypadek cofnięcia ruchu
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activeP; // Do obsługi figury trzymanej przez gracza

    // Kolor
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE; // Białe zawsze zaczynają partię  

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);

        // Pozwala programowi na wykrycie ruchu lub akcji myszki
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        setPieces();
        copyPieces(pieces, simPieces);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); // wywołuje metodę run()
    }

    public void setPieces() {
        // Drużyna białych
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));

        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // Drużyna czarnych
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));

        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
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

        // FIGURA WZIĘTA (PRZYCISK MYSZY WCIŚNIETY)
        if (mouse.pressed) {
            // Jeśli gracz nie trzyma figury, sprawdza, czy można wziąć jakąś figurę
            if (activeP == null) {
                // Pętla po figurach
                for (Piece piece : simPieces) {
                    // Jeśli figura ma ten sam kolor, co aktualnego gracza i odpowiednio współrzędne jak myszka
                    // Innymi słowy: Jeśli myszka jest na figurze gracza, podnieś ją jako activeP
                    if (piece.color == currentColor &&
                        piece.col == mouse.x/Board.SQUARE_SIZE && 
                        piece.row == mouse.y/Board.SQUARE_SIZE) {

                            // Ustawiamy daną figurę jako aktywną
                            activeP = piece;
                    }
                }
            // Gracz już trzyma jakąś figurę, symuluje ruch
            } else {
                simulate();
            }
        }

        // FIGURA OPUSZCZONA (PRZYCISK MYSZY PUSZCZONY)
        if (mouse.pressed == false) {
            // Puszczenie po trzymaniu figury 
            if (activeP != null) {
                activeP.updatePosition();
                activeP = null;
            }
        }
    }

    // Symulujemy, ponieważ nie znamy jeszcze przebiegu wydarzeń
    // Gracz może przesunąć figurę lub na przykład zbić figurę przeciwnika
    private void simulate() {
        // Jeśli figura jest trzymana, aktualizuj jej pozycję
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
    }

    // Metoda, która pozwala na rysowanie obiektów na panelu
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // Plansza
        board.draw(g2);

        // Figury
        for (Piece p : simPieces) {
            p.draw(g2);
        }

        if (activeP != null ) {
            // Koloruje kwadrat pod aktywną figurą (kursorem, który trzyma figurę) na biało (dynamicznie)
            g2.setColor(Color.white);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                    Board.SQUARE_SIZE);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // Rysujemy aktywną figurę na końcu, żeby nie została zakryta przez planszę albo pokolorowany kwardat
            activeP.draw(g2);
        }

    }

}
