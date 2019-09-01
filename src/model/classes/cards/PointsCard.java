package model.classes.cards;

/**
 * This class represents all cards that contain and provide points to a player
 */
public class PointsCard extends Card {
    private int points;

    /**
     * <h2>Constructor</h2>
     * @post Creates a card which has points
     * @param points number of card's points
     * @param imageFileName name of card's image
     */
    PointsCard(int points, String imageFileName){
        super(imageFileName);
        this.points = points;
    }

    /**
     * <b>Accessor Method</b>
     * @post card's points have been returned
     * @return the points of the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * <b>Transformer Method</b>
     * @post new value for points has been set
     * @param points the number of points
     */
    public void setPoints(int points) {
        this.points = points;
    }
}
