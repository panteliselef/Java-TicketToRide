package model.classes.regions;

import model.classes.cards.BigCitiesCard;
import model.classes.cards.CardColor;
import model.classes.cards.DestinationCard;
import model.classes.cards.TrainCard;

import java.util.ArrayList;



/**
 * This class represents the green section in the middle of the game.
 * It contains 2 Decks of Cards, 5 train cards on table and the bonus cards
 *
 * @author elefcodes
 */
public class Deck {
    private ArrayList<TrainCard> trainCardsDeck;
    private ArrayList<DestinationCard> destinationCardsDeck;
    private TrainCard[] cardsOnTable = new TrainCard[5];
    private BigCitiesCard[] bigCitiesOnTable = new BigCitiesCard[6];


    /**
     * <h2>Constructor</h2>
     * @post Creates a Deck for the game
     */
    public Deck(){
        trainCardsDeck = new ArrayList<>();
        destinationCardsDeck = new ArrayList<>();
    }

    /**
     * <b>Transformer Method</b>
     * @post a new train card has been added to deck
     * @param cardColor color of the new cards
     */
    public void addTrainCardOnDeck(CardColor cardColor){
        this.getTrainCardsDeck().add(new TrainCard("./resources/images/trainCards/"+cardColor.toString().toLowerCase()+".jpg",cardColor));
    }

    /**
     * <b>Transformer Method</b>
     * @post the last card of the train card deck has been removed
     * @return the last card of the train card deck
     */
    public TrainCard removeTrainCardFromDeck(){
        TrainCard tmp = this.getTrainCardsDeck().get(this.getTrainCardsDeck().size() -1);
        this.getTrainCardsDeck().remove(this.getTrainCardsDeck().get(this.getTrainCardsDeck().size() -1));
        return tmp;
    }

    /**
     * <b>Transformer Method</b>
     * @post a train deck has been set
     * @param trainCardsDeck the deck of train cards
     */
    public void setTrainCardsDeck(ArrayList<TrainCard> trainCardsDeck) {
        this.trainCardsDeck = trainCardsDeck;
    }

    /**
     * <b>Accessor Method</b>
     * @post the deck of train cards has been returned
     * @return the deck of train cards
     */
    public ArrayList<TrainCard> getTrainCardsDeck() {
        return trainCardsDeck;
    }

    /**
     * <b>Transformer Method</b>
     * @post a destination ticket deck has been set
     * @param destinationCardsDeck the deck of destination tickets
     */
    public void setDestinationCardsDeck(ArrayList<DestinationCard> destinationCardsDeck) {
        this.destinationCardsDeck = destinationCardsDeck;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new destination card has been added to deck
     * @param card DestinationCard
     */
    public void addDestinationCardOnDeck(DestinationCard card){
        this.getDestinationCardsDeck().add(card);
    }

    /**
     * <b>Accessor Method</b>
     * @post the deck of destination tickets has been returned
     * @return the deck of destination tickets
     */
    public ArrayList<DestinationCard> getDestinationCardsDeck() {
        return destinationCardsDeck;
    }

    /**
     * <b>Transformer Method</b>
     * @post the last card of the destination card deck has been removed
     * @return the last card of the destination card deck
     */
    public DestinationCard removeDestCardFromDeck(){
        DestinationCard tmp = this.getDestinationCardsDeck().get(this.getDestinationCardsDeck().size() -1);
        this.getDestinationCardsDeck().remove(this.getDestinationCardsDeck().get(this.getDestinationCardsDeck().size() -1));
        return tmp;
    }


    /**
     * <b>Accessor Method</b>
     * @post the list of BigCities cards has been returned
     * @return the list of BigCities cards
     */
    public BigCitiesCard[] getBigCitiesOnTable() {
        return bigCitiesOnTable;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new list of big cities cards has been set
     * @param bigCitiesOnTable an array of big city cards
     */
    public void setBigCitiesOnTable(BigCitiesCard[] bigCitiesOnTable) {
        this.bigCitiesOnTable = bigCitiesOnTable;
    }


    /**
     * <b>Transformer Method</b>
     * @post five cards on table have been set
     * @param cardsOnTable 5 train cards
     */
    public void setCardsOnTable(TrainCard[] cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    /**
     * <b>Accessor Method</b>
     * @post an array of the cards on the table has been returned
     * @return an array of the cards on the table
     */
    public TrainCard[] getCardsOnTable() {
        return cardsOnTable;
    }
}
