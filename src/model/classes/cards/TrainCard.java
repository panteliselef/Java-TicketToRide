package model.classes.cards;


public class TrainCard extends Card {
    final private CardColor color;

    /**
     * <h2>Constructor</h2>
     * @post Creates an instance of a TrainCard with a color and an name for it's image
     * @param imageFileName the name of the card's image
     * @param color the color on the card
     */
    public TrainCard(String imageFileName,CardColor color){
        super(imageFileName);
        this.color = color;
    }

    /**
     * <b>Accessor Method</b>
     * @post the color of the card has been returned
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
    }

}
