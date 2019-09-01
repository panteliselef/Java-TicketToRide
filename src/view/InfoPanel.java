package view;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JLayeredPane {
    private JLabel playerName ;
    private JLabel playerTurn ;
    private JLabel scoreLabel ;
    private JButton destTicketsButton;
    private JButton bonusCardsButton ;

    InfoPanel(String playerName){
        this.initComponents();
        this.playerName.setText(playerName + " Scoreboard");
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(this.playerName);
        this.add(playerTurn);
        this.add(scoreLabel);
        this.add(destTicketsButton);
        this.add(bonusCardsButton);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setOpaque(true);
    }

    private void initComponents(){
        playerName = new JLabel();
        playerTurn = new JLabel("Player Turn: ");
        scoreLabel = new JLabel("Score: ");
        destTicketsButton = new JButton("My Destination Tickets");
        bonusCardsButton= new JButton("My Big Cities Bonus Cards");

    }

    public void updateLabels(int score, boolean turn){
        String playerScoreText = "Score: ";
        String playerTurnText = "Player Turn: ";
        playerTurn.setText(playerTurnText + turn);
        scoreLabel.setText(playerScoreText + score);
    }

    public JButton getBonusCardsButton() {
        return bonusCardsButton;
    }

    public JButton getDestTicketsButton() {
        return destTicketsButton;
    }
}
