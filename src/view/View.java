package view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {

    final private DeckPanel gameDeckPanel;
    final private PlayerPanel playerPanel1;
    final private PlayerPanel playerPanel2;
    final private ImagePane bg;
    final private JMenuBar menuBar;
    final private JMenu saveGame;
    final private JMenu loadGame;
    final private JMenu newGame;
    final private JMenu exitGame;


    public View(String[] playerNames) {
        menuBar = new JMenuBar();
        gameDeckPanel = new DeckPanel();
        playerPanel1 = new PlayerPanel(playerNames[0]);
        playerPanel2 = new PlayerPanel(playerNames[1]);
        ImageIcon myImage = new ImageIcon("./resources/images/background.jpg");
        Image newImg = myImage.getImage().getScaledInstance(1200,900,Image.SCALE_DEFAULT);
        saveGame = new JMenu("Save Game");
        loadGame = new JMenu("Load Game");
        newGame = new JMenu("New Game");
        exitGame = new JMenu("Exit Game");
        menuBar.add(newGame);
        menuBar.add(saveGame);
        menuBar.add(loadGame);
        menuBar.add(exitGame);
        this.setJMenuBar(menuBar);

        bg = new ImagePane(newImg);

        this.setVisible(true);
        bg.setLayout(new BorderLayout());
        bg.add(playerPanel1,BorderLayout.SOUTH);
        bg.add(playerPanel2,BorderLayout.NORTH);
        bg.add(gameDeckPanel,BorderLayout.CENTER);
        bg.setSize(1000,1000);
        this.setMaximumSize(new Dimension(1200, 900));
        this.setMinimumSize(new Dimension(1170, 800));
        this.getContentPane().add(bg);
        this.pack();
    }


    /**
     * <b>Accessor Method</b>
     * @post the panel of 1st player has been returned
     * @return the panel of 1st player
     */
    public PlayerPanel getPlayerPanel1() {
        return playerPanel1;
    }

    /**
     * <b>Accessor Method</b>
     * @post the panel of 2nd player has been returned
     * @return the panel of 2nd player
     */
    public PlayerPanel getPlayerPanel2() {
        return playerPanel2;
    }

    /**
     * <b>Accessor Method</b>
     * @post the panel of deck has been returned
     * @return the panel of deck
     */
    public DeckPanel getGameDeckPanel() {
        return gameDeckPanel;
    }

    /**
     * <b>Accessor Method</b>
     * @post the menu bar has been returned
     * @return the menu bar
     */
    public JMenuBar getJMenuBar() {
        return menuBar;
    }


    /**
     * <b>Accessor Method</b>
     * @post the menu that saves the game has been returned
     * @return the menu that saves the game
     */
    public JMenu getSaveGame() {
        return saveGame;
    }

    /**
     * <b>Accessor Method</b>
     * @post the menu that loads the game has been returned
     * @return the menu that loads the game
     */
    public JMenu getLoadGame() {
        return loadGame;
    }

    /**
     * <b>Accessor Method</b>
     * @post the menu that creates a new game has been returned
     * @return the menu that creates a new
     */
    public JMenu getNewGame() {
        return newGame;
    }

    /**
     * <b>Accessor Method</b>
     * @post the menu that exits the game has been returned
     * @return the menu that exits the game
     */
    public JMenu getExitGame() {
        return exitGame;
    }
}
