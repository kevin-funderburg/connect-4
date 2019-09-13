package Controller;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

import View.mainMenu;

import static Controller.GameState.*;


public class GameEngine extends JPanel{
    // Variables to draw the board
    public static final int ROWS = 6;  // ROWS by COLS cells
    public static final int COLS = 7;
    public static final String TITLE = "Connect 4";

    // Variables to draw the cells
    public static final int CELL_SIZE = 100;
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;

    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int PIECE_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int PIECE_STROKE_WIDTH = 8;

    private Board board;                // the game board
    private GameState currentState;     // the current state of the game
    private PlayerVal currentPlayer;    // the current player
    private JLabel statusBar;           // for displaying status message
    private JMenuBar menubar;           // holds different game functions

    // win counts of each player
    public static int player1_winCount;
    public static int player2_winCount;

    public static boolean soundsOn;
    public static boolean defaultColor = true;
    public static boolean singlePlayer;

    private static JFrame frame = new JFrame(TITLE);

    // Constructor
    public GameEngine() {

        // This JPanel fires MouseEvent
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int rowSelected = 0;
                int colSelected = mouseX / CELL_SIZE;
                playSound("click.wav");

                //look and select the bottom-most cell
                for (int row = 0; row <= ROWS - 1; ++row){
                    if (board.cells[row][colSelected].value == PlayerVal.NONE){
                        rowSelected = row;
                    }
                }
                if (currentState == PLAYING)
                {
                    if (rowSelected >= 0 && rowSelected < ROWS &&
                            colSelected >= 0 && colSelected < COLS &&
                            board.cells[rowSelected][colSelected].value == PlayerVal.NONE) {

                        board.cells[rowSelected][colSelected].value = currentPlayer; // Drop a piece
                        refresh(currentPlayer); // Check game status
                        // Switch player
                        if (currentPlayer == PlayerVal.PLAYER_1)
                            currentPlayer = PlayerVal.PLAYER_2;
                        else
                            currentPlayer = PlayerVal.PLAYER_1;

                        if (singlePlayer) {
                            int randCol;
                            rowSelected = -1;
                            do {//do while to keep trying if random column is full
                                Random rand = new Random();
                                randCol = rand.nextInt(COLS);
                                // Get bottom-most empty cell of randCol
                                for (int row = 0; row <= ROWS - 1; ++row)
                                    if (board.cells[row][randCol].value == PlayerVal.NONE)
                                        rowSelected = row;
                            } while (rowSelected == -1);

                            board.cells[rowSelected][randCol].value = currentPlayer; // Drop a piece
                            refresh(currentPlayer);
                            // Switch player
                            if (currentPlayer == PlayerVal.PLAYER_1)
                                currentPlayer = PlayerVal.PLAYER_2;
                            else
                                currentPlayer = PlayerVal.PLAYER_1;
                        }
                    }
                } else {        // game over
                    newGame();  // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Call-back paintComponent().
            }
        });

        // TODO - Add an action listner class to handle the actions of the menubar
        // Setup the menubar
        JMenuItem newGameMenu = new JMenu("New Game");
        newGameMenu.setToolTipText("Exit application");
        newGameMenu.setMnemonic(KeyEvent.VK_N);
        newGameMenu.addActionListener((event) -> mainMenu.newGame());

        JButton forfeit = new JButton("Forfeit");
        forfeit.setToolTipText("Stop the current game");
        forfeit.setMnemonic(KeyEvent.VK_Q);
        forfeit.addActionListener((event) -> {
            mainMenu.newGame();
            frame.dispose();
        });

        JToggleButton sound = new JToggleButton("Sound",soundsOn);
        sound.setToolTipText("Toggle sounds");
        sound.setMnemonic(KeyEvent.VK_S);
        sound.addActionListener((event) -> toggleSound());

        JMenuItem quit = new JMenuItem("Quit");
        quit.setToolTipText("Exit application");
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.addActionListener((event) -> System.exit(0));

        menubar = new JMenuBar();
        menubar.add(newGameMenu);
        menubar.add(forfeit);
        menubar.add(sound);
        menubar.add(quit);

        // Setup the status bar
        statusBar = new JLabel("         ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);

        setLayout(new BorderLayout());
        add(menubar, BorderLayout.PAGE_START);
        add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT + 30));
        // account for statusBar in height

        board = new Board();   // allocate the game-board
        newGame();  // Initialize the game variables
    }

    public static void startBoard(){
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(() -> {
            // Set the content-pane of the JFrame to an instance of main JPanel
            frame.setContentPane(new GameEngine());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // center the application window
            frame.setVisible(true);            // show it
            });

    }
    /**
     * Clear the board and set the initial variables
     */
    public void newGame() {
        currentState = PLAYING;
        currentPlayer = PlayerVal.PLAYER_1;
        player1_winCount = player2_winCount = 0;
        soundsOn = true;

        for (int row = 0; row < ROWS; ++row)
            for (int col = 0; col < COLS; ++col)
                board.cells[row][col].value = PlayerVal.NONE; // all cells empty
    }


    /**
     * Check for a win or a draw after a piece is dropped
     * @param player - the player who dropped the piece
     */
    public void refresh(PlayerVal player) {
        if (board.winCheck())
            if (player == PlayerVal.PLAYER_1) {
                currentState = PLAYER1_WON;
                player1_winCount++;
                alertWinner(1);
            } else {
                currentState = PLAYER2_WON;
                player2_winCount++;
                alertWinner(2);
            }
        else if (board.drawCheck()) {
            currentState = DRAW;
            alertWinner(3);
        }
    }


    /**
     * Stop the current game and increase the other players win count
     */
    public void forfeit(PlayerVal player) {
        for (int row = 0; row < ROWS; ++row)
            for (int col = 0; col < COLS; ++col)
                board.cells[row][col].value = PlayerVal.NONE; // all cells empty

        if (player == PlayerVal.PLAYER_1) {
            player2_winCount++;
            alertWinner(2);
        }
        else {
            player1_winCount++;
            alertWinner(1);
        }
    }
    
    public void playSound(String soundName)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
        }
        catch(Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace( );
        }
    }


    /**
     * Toggle game sounds
     */
    public void toggleSound() {
        if (soundsOn) {
            // TODO - Action to turn sound off
            soundsOn = false;
        } else {
            // TODO - Action to turn sound on
            soundsOn = true;
        }
    }

    /**
     * Alert winner window
     */
    public void alertWinner(int winner) {
        if (singlePlayer && (PlayerVal.PLAYER_2 == currentPlayer)){
            return;
        }
        if (winner == 1) {
            //TODO - Rematch button
            JFrame frameAlertWinner = new JFrame();
            String[] options = {"Start New Game", "Rematch", "End Session"};
            int x = JOptionPane.showOptionDialog(frameAlertWinner,"Player 1 Wins!",
                    "End of Match", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            choice(x);
        }
        if (winner == 2) {
            JFrame frameAlertWinner = new JFrame("Alert");
            String[] options = {"Start New Game", "Rematch", "End Session"};
            int x = JOptionPane.showOptionDialog(frameAlertWinner,"Player 2 Wins!",
                    "End of Match", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            choice(x);
        }
        if (winner == 3) {
            JFrame frameAlertWinner = new JFrame("Alert");
            String[] options = {"Start New Game", "Rematch", "End Session"};
            int x = JOptionPane.showOptionDialog(frameAlertWinner,"Draw",
                    "End of Match", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            choice(x);
        }
    }

    private void choice(int x) {
        if (x == 0) {
            mainMenu window = new mainMenu();
            window.menu.setVisible(true);
            frame.dispose();
        }
        if (x == 1) { }//TODO: Rematch
        if (x == 2) { System.exit(0); }
    }

    /**
     * Custom painting codes on this JPanel
     */
    @Override
    public void paintComponent(Graphics g) {  // invoke via repaint()
        super.paintComponent(g);    // fill background
        setBackground(Color.WHITE); // set its background color

        board.paint(g);  // ask the game board to paint itself

        // Print status-bar message
        if (currentState == PLAYING) {
            statusBar.setForeground(Color.BLACK);
            if (currentPlayer == PlayerVal.PLAYER_1)
                statusBar.setText("Player 1's Turn");
            else
                statusBar.setText("Player 2's Turn");
        } else if (currentState == DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == PLAYER1_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Player 1 Won! Click to play again.");
        } else if (currentState == PLAYER2_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Player 2 Won! Click to play again.");
        }
    }

}
