package view;

import model.classes.cards.DestinationCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DestinationTicketPanel extends JLayeredPane {
    private JLabel nameTag;
    private ArrayList<CardButton> destinationCardButtonsOnHands = new ArrayList<>();
    private final int stx = 10;
    private final int sty = 10;
    private final int offset = 40;

    DestinationTicketPanel(String nameTag){
        this.initComponents();
        this.nameTag.setText(nameTag);
        this.setLayout(null);
        this.setSize(400,400);
        this.setPreferredSize(new Dimension(300,125));
        this.nameTag.setBounds(10,110,200,10);
        this.add(this.nameTag);


        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setOpaque(true);
    }


    public void setDestinationCardButtonsOnHands(ArrayList<DestinationCard> destinationCards){
        this.removeAllCardButtons();

        destinationCards.forEach(destinationCard -> {
            CardButton tmp = new CardButton(destinationCard);
            tmp.setBounds(destinationCardButtonsOnHands.size() * offset + stx,sty);
            destinationCardButtonsOnHands.add(tmp);
            this.add(tmp);
        });
    }

    private void initComponents(){
        nameTag = new JLabel();
        destinationCardButtonsOnHands = new ArrayList<>();
    }
    public ArrayList<CardButton> getDestinationCardButtonsOnHands(){
        return this.destinationCardButtonsOnHands;
    }

    private void removeAllCardButtons(){
        destinationCardButtonsOnHands.forEach(this::remove);
        destinationCardButtonsOnHands.clear();
        this.repaint();
    }


}
