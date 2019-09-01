package model.classes.cards;


/**
 * This class represents an abstract version of a Card
 * @version 1.0
 * @author elefcodes
 */
abstract public class Card {
    private String imageFileName;

    /**
     * <h2>Constructor</h2>
     * @post Creates a new Card with a name for its image
     * @param imageFileName the name of the image
     */
    public Card(String imageFileName){
        if(imageFileName == null|| imageFileName.equals("") || imageFileName.equals(" ")) throw new IllegalArgumentException();
        else this.imageFileName = imageFileName;
    }

    /**
     * <b>Accessor Method</b>
     * @post the name of the card's image has been returned
     * @return the name of card's image
     */
    public String getImageFileName() {
        return imageFileName;
    }

    /**
     * <b>Transformer Method</b>
     * @post the name of the card's image has been set
     * @param imageFileName the name of card's image
     */
    public void setImageFileName(String imageFileName) {
        if(imageFileName == null|| imageFileName.equals("") || imageFileName.equals(" ")) throw new IllegalArgumentException();
        else this.imageFileName = imageFileName;
    }
}
