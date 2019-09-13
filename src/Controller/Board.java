package Controller;

import java.awt.*;

public class Board {
    Cell[][] cells;     // Create an array of cells for the board


    /**
     * Fill the array elements with a cell object
     */
    public Board () {
        cells = new Cell[GameEngine.ROWS][GameEngine.COLS];
        for (int row = 0; row < GameEngine.ROWS; ++row)
            for (int col = 0; col < GameEngine.COLS; ++col)
                cells[row][col] = new Cell(row, col);
    }


    /**
     * Clear the pieces on the board
     */
    public void init() {
        for (int row = 0; row < GameEngine.ROWS; ++row)
            for (int col = 0; col < GameEngine.COLS; ++col)
                cells[row][col].clearPiece(); // clear the cell content
    }


    /**
     * Check if every cell has a value other than NONE
     * @return bool
     */
    public boolean drawCheck() {
        for (int row = 0; row < GameEngine.ROWS; ++row)
            for (int col = 0; col < GameEngine.COLS; ++col)
                if (cells[row][col].value == PlayerVal.NONE)
                    return false;
        return true;
    }


    /**
     * Checks for 4 adjacent pieces horizontally, vertically, and diagonally
     * after a player drops a piece
     */
    public boolean winCheck() {
        int HEIGHT = GameEngine.ROWS;
        int WIDTH = GameEngine.COLS;
        for (int r = 0; r < HEIGHT; r++) { // iterate rows, bottom to top
            for (int c = 0; c < WIDTH; c++) { // iterate columns, left to right
                PlayerVal player = cells[r][c].value;
                if (player == PlayerVal.NONE)
                    continue; // don't check empty slots

                if (c + 3 < WIDTH &&
                        player == cells[r][c+1].value && // look right
                        player == cells[r][c+2].value &&
                        player == cells[r][c+3].value)
                    return true;
                if (r + 3 < HEIGHT) {
                    if (player == cells[r+1][c].value && // look up
                        player == cells[r+2][c].value &&
                        player == cells[r+3][c].value)
                        return true;
                    if (c + 3 < WIDTH &&
                        player == cells[r+1][c+1].value && // look up & right
                        player == cells[r+2][c+2].value &&
                        player == cells[r+3][c+3].value)
                        return true;
                    if (c - 3 >= 0 &&
                        player == cells[r+1][c-1].value && // look up & left
                        player == cells[r+2][c-2].value &&
                        player == cells[r+3][c-3].value)
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * Set up the board's appearance
     * @param g - graphic context
     */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(Color.GRAY);
        for (int row = 1; row < GameEngine.ROWS; ++row) {
            g.fillRoundRect(0, GameEngine.CELL_SIZE * row - GameEngine.GRID_WIDTH_HALF,
                    GameEngine.CANVAS_WIDTH - 1, GameEngine.GRID_WIDTH,
                    GameEngine.GRID_WIDTH, GameEngine.GRID_WIDTH);
        }
        for (int col = 1; col < GameEngine.COLS; ++col) {
            g.fillRoundRect(GameEngine.CELL_SIZE * col - GameEngine.GRID_WIDTH_HALF, 0,
                    GameEngine.GRID_WIDTH, GameEngine.CANVAS_HEIGHT - 1,
                    GameEngine.GRID_WIDTH, GameEngine.GRID_WIDTH);
        }
        for (int row = 0; row < GameEngine.ROWS; ++row)
            for (int col = 0; col < GameEngine.COLS; ++col)
                cells[row][col].setColor(g);
    }
}
