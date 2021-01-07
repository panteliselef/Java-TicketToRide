package model.classes.regions;

import model.classes.cards.CardColor;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class represents the on-the-track region which every player has
 *
 * @author elefcodes
 */
public class OnTheTrack {
    private final HashMap<CardColor, Integer> onTheTrackCollection = new HashMap<>();

    /**
     * <h2>Constructor</h2>
     * @post Creates a "on-the-track" region and initializes the keys for the hashMap
     */
    public OnTheTrack(){
        Arrays.stream(CardColor.values()).forEach(cardColor -> onTheTrackCollection.put(cardColor,0));
    }

    public void empty(){
        onTheTrackCollection.forEach((cardColor, integer) -> integer = 0);
    }


    /**
     * <b>Observer Method</b>
     * @return true if there are no card on the track, otherwise false
     */
    boolean IsEmpty() {
        boolean empty = true;
        for (CardColor cd:CardColor.values()
        ) {
            if(!(onTheTrackCollection.get(cd) == 0)) empty = false;
        }
        return empty;
    }

    /**
     * <b>Transformer Method</b>
     * @post the selected card has been removed from the track
     * @param cardColor color of the card
     */
    public void removeCard(CardColor cardColor) {
        onTheTrackCollection.replace(cardColor,onTheTrackCollection.get(cardColor) - 1);
    }

    /**
     * <b>Transformer Method</b>
     * @post the selected card has been add to the track
     * @param cardColor color of the card
     */
    public void addCard(CardColor cardColor) {
        onTheTrackCollection.replace(cardColor,onTheTrackCollection.get(cardColor) + 1);
    }

    /**
     * <b>Accessor Method</b>
     * @post the total number of card that matches the selected color has been returned
     * @param cardColor the selected card color
     * @return the total number of card that matches the selected color
     */
    public int getNumberOfCardsByColor(CardColor cardColor){
        return onTheTrackCollection.get(cardColor);
    }

    /**
     * <b>Accessor Method</b>
     * @return all the cards on the track as hashmap with their color as key
     * @post all the cards on the track have been returned as hashmap with their color as key
     */
    public HashMap<CardColor, Integer> getCards() {
        return onTheTrackCollection;
    }

}
