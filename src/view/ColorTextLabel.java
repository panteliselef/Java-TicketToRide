package view;

import model.classes.cards.CardColor;

import javax.swing.*;

public class ColorTextLabel extends JLabel {

    private final CardColor cardColor;

    /**
     * <h2>Constructor</h2>
     * @post Creates a JLabel which has an extra Color field
     * @param text text of the label
     * @param cd color of the label
     */
    ColorTextLabel(String text, CardColor cd){
        this.setText(text);
        this.cardColor = cd;
    }


    /**
     * <b>Accessor Method</b>
     * @post the color of the colorTextLabel has been returned
     * @return the color of the colorTextLabel
     */
    public CardColor getCardColor() {
        return cardColor;
    }
}
