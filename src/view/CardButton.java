package view;

import model.classes.cards.Card;

import javax.swing.*;
import java.awt.*;

public class CardButton extends JButton {
    enum Type {
        TRAIN,DESTINATION
    }
    private boolean shouldBeTaken = false;
    final private Card card;

    /**
     * <h2>Constructor</h2>
     * @post Creates a new CardButton based on the card given
     * @param card the card which the button will be resembling
     */
    public CardButton(Card card){
        this.card = card;
        this.setSize(50,95);
        this.setIcon(new ImageIcon(this.card.getImageFileName()));
        ImageIcon myImage = new ImageIcon(card.getImageFileName());
        Image img = myImage.getImage();
        Image newimg = img.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newimg);
        this.setIcon(image);

        this.addActionListener(e->{
            if(this.isSelected()){
                this.setSelected(false);
                this.setBounds(this.getX(),this.getY()+10);
            }else {
                this.setSelected(true);
                this.setBounds(this.getX(),this.getY()-10);
            }
        });

    }


    /**
     * <h2>Constructor</h2>
     * @post Creates a new CardButton based on the type(enum) given
     * @param t the type of the CardButton
     */
    CardButton(Type t){
        this.card = null;
        ImageIcon myImage;
        switch (t){
            case DESTINATION:
                myImage = new ImageIcon("./resources/images/destination_Tickets/desBackCard.jpg");
                break;
            default:
                myImage = new ImageIcon("./resources/images/trainCards/trainBackCard.jpg");
                break;
        }
        this.setSize(50,95);

        Image img = myImage.getImage();
        Image newImg = img.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        this.setIcon(image);
    }

    /**
     * <b>Observer Method</b>
     * @return whether or not a cardButton should be taken
     */
    public boolean isShouldBeTaken() {
        return shouldBeTaken;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new value for shouldBeTaken has been set
     * @param shouldBeTaken true/false
     */
    public void setShouldBeTaken(boolean shouldBeTaken) {
        this.shouldBeTaken = shouldBeTaken;
    }

    /**
     * <b>Accessor Method</b>
     * @post the card of the cardButton has been returned
     * @return the card of the cardButton
     */
    public Card getCard() {
        return card;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new position has been set for the cardButton
     * @param x pixels for x-axis
     * @param y pixels for y-axis
     */
    public void setBounds(int x, int y) {
        super.setBounds(x, y, 50, 95);
    }
}
