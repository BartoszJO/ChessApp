package piece;

import main.GamePanel;

public class King extends Piece {
    public King(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-king");
        } else {
            image = getImage("/piece/b-king");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow)) {
            // Król może poruszać się tylko o jedno pole w dowolnym kierunku (zakłądając, ze pole jest wolne)
            // Odejmujemy od wybranego pola wspołrzędne poprzedniego, i sprawdzamy ich odległości
            // Pierwszy warunek sprawdza ruchy na górę, dół, prawo i lewo
            // Drugi warunek sprawdza ruchy po przekątnej

            // NIE ROZUMIEM jakie wartości przyjmują targetcol, preCol, targetRow i preRow, że wynikiem ma być 1 ???????????????????????????

            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1 ||
                    Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1) {
                return true;
            }
        }

        return false;
    }
}
