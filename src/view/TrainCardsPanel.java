package view;

import model.classes.cards.TrainCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrainCardsPanel extends JLayeredPane {
    private JLabel nameTag;
    private ArrayList<CardButton> trainCardButtonsOnHands;
    private JButton button;
    private final int stx = 10;
    private final int sty = 10;
    private final int offset = 40;


    TrainCardsPanel(String nameTag){
        this.initComponents();
        this.nameTag.setText(nameTag);

        this.setLayout(null);

        this.setSize(400,400);
        this.setPreferredSize(new Dimension(450,125));


        CardButton trainDeckCardButton = new CardButton(CardButton.Type.TRAIN);
        CardButton destinationDeckCardButton= new CardButton(CardButton.Type.DESTINATION);

        trainDeckCardButton.setBounds( stx,sty);
        destinationDeckCardButton.setBounds(stx + offset, sty);

        this.button.setBounds(350,105,90,18);
        this.nameTag.setBounds(10,110,150,10);
        this.add(this.nameTag);
        this.add(this.button);

        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setOpaque(true);

    }

    public void setTrainCardButtonsOnHands(ArrayList<TrainCard> trainCards){
        this.removeAllCardButtons();

        trainCards.forEach(trainCard -> {
            CardButton tmp = new CardButton(trainCard);
            tmp.setBounds(trainCardButtonsOnHands.size() * offset + stx,sty);
            trainCardButtonsOnHands.add(tmp);
            this.add(tmp);
        });

    }

    private void initComponents(){

        nameTag = new JLabel();
        trainCardButtonsOnHands = new ArrayList<CardButton>();
        button = new JButton("Play Card");
    }
    public ArrayList<CardButton> getTrainCardButtonsOnHands(){
        return this.trainCardButtonsOnHands;
    }

    private void removeAllCardButtons(){
        trainCardButtonsOnHands.forEach(this::remove);
        trainCardButtonsOnHands.clear();
        this.repaint();
    }


    public JButton getButton() {
        return button;
    }
}
