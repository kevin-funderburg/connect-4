package View;
import Controller.GameEngine;
import javax.swing.*;

public class mainMenu extends JPanel {

    public static JFrame menu;

    public mainMenu() {
        init();
    }

    private void init() {

        menu = new JFrame();
        menu.setTitle("Connect 4");
        menu.setBounds(100, 100, 450, 300);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 414, 237);
        menu.getContentPane().add(panel);
        panel.setLayout(null);


        JButton btnvAI = new JButton("P V AI");
        btnvAI.setBounds(35, 130, 150, 30);
        panel.add(btnvAI);
        btnvAI.addActionListener((event) -> {

            GameEngine.singlePlayer = true;

            JButton btnRed = new JButton("Red");
            btnRed.setBounds(35, 60, 150, 30);
            panel.add(btnRed);
            btnRed.addActionListener((choice) -> {
                GameEngine.defaultColor = true;
                GameEngine.startBoard();
                menu.dispose();
                //default is player 1 == red
            });
            JButton btnBlack = new JButton("Black");
            btnBlack.setBounds(220, 60, 150, 30);
            panel.add(btnBlack);
            btnBlack.addActionListener((choice) -> {
                GameEngine.defaultColor = false;
                GameEngine.startBoard();
                menu.dispose();
            });
            panel.repaint();
        });

        //TODO

        JButton btnvP = new JButton("P V P");
        btnvP.setBounds(220, 130, 150, 30);
        panel.add(btnvP);
        btnvP.addActionListener((event) -> {
            GameEngine.startBoard();
            menu.setVisible(false);
        });


        JButton btnSave = new JButton("Toggle Sound");
        btnSave.setBounds(10, 203, 89, 23);
        panel.add(btnSave);
        //TODO

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(318, 214, 89, 23);
        panel.add(btnExit);
        btnExit.addActionListener((event) -> System.exit(0));
    }

    public static void newGame() {
        mainMenu window = new mainMenu();
        window.menu.setVisible(true);
    }

}



