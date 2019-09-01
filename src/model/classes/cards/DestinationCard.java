package model.classes.cards;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represent a Destination Ticket
 * @author elefcodes
 */
public class DestinationCard extends PointsCard {
    final private int uniqueId;
    final private String departureCity;
    final private String arrivalCity;
    private ArrayList<CardColor> colors = new ArrayList<>();

    /**
     * <h2>Constructor</h2>
     * @post Creates a Destination with a unique id, an from/to City, its points, an list of  colors, an name of its image
     * @param id unique ID number
     * @param departureCity city's name
     * @param arrivalCity city's name
     * @param points number of points
     * @param colors list of colors
     * @param imageName name of image
     */
    public DestinationCard(int id, String departureCity, String arrivalCity, int points, ArrayList<String> colors, String imageName){
        super(points,imageName);
        this.uniqueId = id;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.setColors(colors);
    }


    /**
     * <b>Transformer Method</b>
     * @post all the strings that to existing cardColors have been attached to the cards
     * @param c ArrayList of strings
     */
    private void setColors(ArrayList<String> c){
        c.forEach(s -> {
            Arrays.stream(CardColor.values()).forEach(cardColor -> {

                if(s.toLowerCase().equals(cardColor.toString().toLowerCase())){
                    this.colors.add(cardColor);
                }
            });
        });
    }
    /**
     * <b>Accessor Method</b>
     * @post the id of the card has been returned
     * @return the id of the card
     */
    public int getUniqueId() {
        return uniqueId;
    }

    /**
     * <b>Accessor Method</b>
     * @post the name of the arrival city has been returned
     * @return the name of city
     */
    public String getArrivalCity() {
        return arrivalCity;
    }

    /**
     * <b>Accessor Method</b>
     * @post the name of the departure city has been returned
     * @return the name of city
     */
    public String getDepartureCity() {
        return departureCity;
    }

    /**
     * <b>Accessor Method</b>
     * @post the list of the colors has been returned
     * @return the list of card's colors
     */
    public ArrayList<CardColor> getColors() {

        return colors;
    }
}
