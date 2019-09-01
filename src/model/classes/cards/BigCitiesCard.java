package model.classes.cards;

/**
 * This class represents a bonus card
 */
public class BigCitiesCard extends PointsCard {
    final private BigCities cityName;
    private boolean isTaken;

    /**
     * <h2>Creates a BigCitiesCard with a city name, number of points and a name for its image</h2>
     * @param cityName city name on card
     * @param points number of points
     * @param imageName name of image
     */
    public BigCitiesCard(BigCities cityName, int points, String imageName){
        super(points,imageName);
        this.cityName = cityName;
        isTaken = false;
    }

    /**
     * <b>Accessor Method</b>
     * @post the enum of the city has been returned
     * @return the enum of the city
     */
    public BigCities getCity() {
        return cityName;
    }

    /**
     * <b>Accessor Method</b>
     * @post the name of the city has been returned
     * @return the name of the city
     */
    public String getCityName() {
        return cityName.toString();
    }

    /**
     * <b>Observer Method</b>
     * @post the status of the card has been returned
     * @return true if card is taken, otherwise false
     */
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * <b>Transformer Method</b>
     * @post the status of the card has been updated
     * @param taken the new status of the card
     */
    public void setTaken(boolean taken) {
        isTaken = taken;
    }
}
