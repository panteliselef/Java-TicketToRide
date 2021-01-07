package view;


import model.classes.cards.BigCitiesCard;
import model.classes.cards.TrainCard;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

public class DeckPanel extends JLayeredPane {
    private final CardButton trainDeckCardButton;
    private final CardButton destinationDeckCardButton;
    private CardButton[] cardButtonsOnTable;
    private CardLabel[] bonusCardLabels;
    private final JLabel trainDeckLabel;
    private final JLabel destinationDeckLabel;

    private int stx = 50;
    private final int sty = 10,offset = 60;
    private final String trainDeckText = "Train Deck: ";
    private final String destinationDeckText = "Dest Deck: ";


    /**
     * <h2>Constructor</h2>
     * @post Creates a Deck panel which is the area where all the stack of cards are.
     */
    DeckPanel(){
        trainDeckCardButton = new CardButton(CardButton.Type.TRAIN);
        destinationDeckCardButton= new CardButton(CardButton.Type.DESTINATION);
        trainDeckLabel = new JLabel();
        destinationDeckLabel= new JLabel();
        JLabel bonusCardsLabel = new JLabel("Available Big Cities Bonus Cards");
        bonusCardsLabel.setBounds(850,sty+100,220,20);
        cardButtonsOnTable = new CardButton[5];
        bonusCardLabels = new CardLabel[6];
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1000,140));
        trainDeckCardButton.setBounds(stx,sty);
        destinationDeckCardButton.setBounds(2*stx + offset,sty);
        trainDeckLabel.setText(trainDeckText+"0");
        destinationDeckLabel.setText(destinationDeckText+"0");
        trainDeckLabel.setBounds(stx,sty+100,100,20);
        destinationDeckLabel.setBounds(2*stx +offset,sty+100,130,20);
        this.add(trainDeckCardButton);
        this.add(destinationDeckCardButton);
        this.add(trainDeckLabel);
        this.add(destinationDeckLabel);
        this.add(bonusCardsLabel);
    }

    /**
     * <b>Accessor Method</b>
     * @post the button for the stack of destination tickets has been returned
     * @return the button for the stack of destination tickets
     */
    public CardButton getDestinationDeckCardButton() {
        return destinationDeckCardButton;
    }

    /**
     * <b>Accessor Method</b>
     * @post the button for the stack of train cards has been returned
     * @return the button for the stack of train cards
     */
    public CardButton getTrainDeckCardButton() {
        return trainDeckCardButton;
    }

    /**
     * <b>Accessor Method</b>
     * @post the list for train cards that are lying on the table has been returned
     * @return the list for train cards that are lying on the table
     */
    public CardButton[] getCardButtonsOnTable() {
        return cardButtonsOnTable;
    }

    /**
     * <b>Accessor Method</b>
     * @post the list for bonus cards that are lying on the table has been returned
     * @return the list for bonus cards that are lying on the table
     */
    public CardLabel[] getBonusCardLabels() {
        return bonusCardLabels;
    }

    /**
     * <b>Transformer Method</b>
     * @param bonusCardLabels list of bigCities Cards
     */
    public void setBonusCardLabels(BigCitiesCard[] bonusCardLabels) {
        stx = 800;
        this.removeAllBonusCardsLabels();
        for (int i = 0; i < bonusCardLabels.length; i++) {
            CardLabel cl = new CardLabel(bonusCardLabels[i],55,90);
            cl.makeItColored();
            if(bonusCardLabels[i].isTaken()){
                System.out.println("It's taken");
                cl.makeItGray();
            }
            cl.setBounds(i*offset+stx,this.sty,cl.getWidth(),cl.getHeight());
            this.bonusCardLabels[i] = cl;
            this.add(this.bonusCardLabels[i]);
            this.repaint();
        }

    }

    /**
     *<b>Transformer Method</b>
     * @post the labels for the decks of cards have been updated
     * @param trainCardsLeft amount of cards left in train deck
     * @param destCardsLeft amount of cards left in destination deck
     */
    public void updateLabel(int trainCardsLeft, int destCardsLeft){
        this.trainDeckLabel.setText(trainDeckText + trainCardsLeft);
        this.destinationDeckLabel.setText(destinationDeckText+ destCardsLeft);
    }

    /**
     * <b>Transformer Method</b>
     * @param cardButtons list of train Cards
     */
    public void setCardButtonsOnTable(TrainCard[] cardButtons) {
        stx = 400;
        this.removeAllTrainButtons();
        for (int i = 0; i < cardButtons.length; i++) {
            if(cardButtons[i]!=null){
                CardButton tmp = new CardButton(cardButtons[i]);
                tmp.setBounds(i * offset + stx,sty);
                this.cardButtonsOnTable[i] = tmp;
                this.add(cardButtonsOnTable[i]);
                this.repaint();
            }
        }
        this.repaint();
    }

    /**
     * <b>Transformer Method</b>
     * @post all train cardButton have been removed from the table
     */
    public void removeAllTrainButtons(){
        IntStream.range(0, cardButtonsOnTable.length).filter(i -> cardButtonsOnTable[i] != null).mapToObj(i -> cardButtonsOnTable[i]).forEachOrdered(this::remove);
        cardButtonsOnTable = new CardButton[5];
        this.repaint();
    }
    /**
     * <b>Transformer Method</b>
     * @post all bonus cardButton have been removed from the table
     */
    private void removeAllBonusCardsLabels(){
        IntStream.range(0, this.bonusCardLabels.length).filter(i -> this.bonusCardLabels[i] != null).mapToObj(i -> this.bonusCardLabels[i]).forEachOrdered(this::remove);
        this.bonusCardLabels = new CardLabel[6];
        this.repaint();
    }

}
