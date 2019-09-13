package Controller;
import java.awt.*;

public class Cell {
    int row, col;       // Position on board
    PlayerVal value;    // content of this cell (PlayerVal.NONE, PlayerVal.PLAYER_1, or PlayerVal.PLAYER_2)

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.value = PlayerVal.NONE;
    }

    // Set the value of the cell to NONE
    public void clearPiece() { this.value = PlayerVal.NONE; }

    //TODO - SDD we made says we use a string to set the color, decide if we want to stick to that

    /**
     * Color in the piece that's dropped
     * @param g
     */
    public void setColor(Graphics g) {
        Graphics2D piece = (Graphics2D)g;
        // Set stroke for the piece
        piece.setStroke(new BasicStroke(GameEngine.PIECE_STROKE_WIDTH,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int x1 = col * GameEngine.CELL_SIZE + GameEngine.CELL_PADDING;
        int y1 = row * GameEngine.CELL_SIZE + GameEngine.CELL_PADDING;
        // Draw the piece if it is not empty
        if (value != PlayerVal.NONE) {
            if (value == PlayerVal.PLAYER_1)
                if (GameEngine.defaultColor)
                    piece.setColor(Color.RED);
                else
                    piece.setColor(Color.BLACK);
            else if (value == PlayerVal.PLAYER_2)
                if (GameEngine.defaultColor)
                    piece.setColor(Color.BLACK);
                else
                    piece.setColor(Color.RED);
            piece.fillOval(x1, y1, GameEngine.PIECE_SIZE, GameEngine.PIECE_SIZE);
        }
    }
}
