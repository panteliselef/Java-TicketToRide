package model.classes.regions;

import model.classes.cards.CardColor;
import model.classes.cards.TrainCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class represents a player's railyard
 * @author elefcodes
 */
public class RailYard{
    private final HashMap<CardColor, ArrayList<TrainCard>> railYardCollection = new HashMap<>();

    /**
     * <h2>Constructor</h2>
     * @post Creates a railYard AND puts all cardColor option to hashMap
     */
    public RailYard(){
        Arrays.stream(CardColor.values()).forEach(cardColor -> railYardCollection.put(cardColor, new ArrayList<>()));
    }


    /**
     * <b>Observer Method</b>
     * @return true if there are no card on the railyard, otherwise false
     */
    public boolean isEmpty() {
        boolean empty = true;
        for (CardColor cd:CardColor.values()
             ) {
            if(!railYardCollection.get(cd).isEmpty()) empty = false;
        }
        return empty;
    }

    /**
     * <b>Transformer Method</b>
     * @post clears all the arrayLists that have been attached to cardColors
     */
    public void empty(){
        railYardCollection.forEach((cardColor, trainCards) -> trainCards.clear());
    }


    /**
     * <b>Transformer Method</b>
     * @post the selected card has been add to the railyard
     * @param card a train card
     */
    public void addCard(TrainCard card) {
        railYardCollection.get(card.getColor()).add(card);
    }

    /**
     * <b>Transformer Method</b>
     * @post the selected card has been add to the railyard in the selected color
     * @param card a train card
     */
    public void addCard(TrainCard card,CardColor cd) {
        if(card.getColor() !=CardColor.LOCOMOTIVE){
            railYardCollection.get(cd).add(0,card);
        }else {
            railYardCollection.get(cd).add(card);
        }
    }


    /**
     * <b>Accessor Method</b>
     * @post a list of trainCard with a particular color has been returned
     * @param cardColor the selected card color
     * @return a list of trainCard that matches the color given as parameter
     */
    public ArrayList<TrainCard> getCardsByColor(CardColor cardColor){
        return railYardCollection.get(cardColor);
    }

    /**
     * <b>Accessor Method</b>
     * @return all the cards on the railYard as hashMap with their color as key
     * @post all the cards on the railYard have been returned as hashMap with their color as key
     */
    public HashMap<CardColor, ArrayList<TrainCard>> getCards() {
        return railYardCollection;
    }


}
