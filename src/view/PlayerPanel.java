package view;



import model.classes.cards.TrainCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayerPanel extends JLayeredPane {


    private TrainCardsPanel trainCardPanel;
    private RailYardPanel railYardPanel ;
    private OnTheTrackPanel onTheTrackPanel ;
    private DestinationTicketPanel destinationTicketPanel ;
    private final InfoPanel playerInfoPanel ;


    PlayerPanel(String playerName){
        this.initComponents();
        playerInfoPanel = new InfoPanel(playerName);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        this.add(railYardPanel,c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        this.add(trainCardPanel,c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        this.add(onTheTrackPanel,c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        this.add(destinationTicketPanel,c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        c.weightx = 0.5;
        this.add(playerInfoPanel,c);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
        this.setOpaque(true);

    }

    private void initComponents(){
        trainCardPanel = new TrainCardsPanel("Train Cards on hands");
        railYardPanel = new RailYardPanel("RailYard");
        onTheTrackPanel = new OnTheTrackPanel("On-The-Track");
        destinationTicketPanel = new DestinationTicketPanel("Destination Tickets on hands");
    }

    public DestinationTicketPanel getDestinationTicketPanel() {
        return destinationTicketPanel;
    }

    public InfoPanel getPlayerInfoPanel() {
        return playerInfoPanel;
    }

    public OnTheTrackPanel getOnTheTrackPanel() {
        return onTheTrackPanel;
    }

    public RailYardPanel getRailYardPanel() {
        return railYardPanel;
    }

    public TrainCardsPanel getTrainCardPanel() {
        return trainCardPanel;
    }
}
