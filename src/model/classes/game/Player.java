package model.classes.game;

import model.classes.cards.BigCities;
import model.classes.cards.BigCitiesCard;
import model.classes.cards.DestinationCard;
import model.classes.cards.TrainCard;
import model.classes.regions.OnTheTrack;
import model.classes.regions.RailYard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class represents a player of the game.
 * The player owns cards, take new ones and throws others.
 * @author elefcodes
 */
public class Player {
    final private String name;
    private int score = 0;
    private boolean hasTurn = false;
    private ArrayList<TrainCard> trainCardsOnHands = new ArrayList<>();
    private ArrayList<DestinationCard> destinationCardsOnHands = new ArrayList<>();
    private ArrayList<BigCitiesCard> bonusCardsAcquired = new ArrayList<>();
    private ArrayList<DestinationCard> destinationCardsAcquired = new ArrayList<>();
    private HashMap<BigCities,Integer> bigCitiesTimesVisited = new HashMap<>();
    private RailYard myRailYard =  new RailYard();
    private OnTheTrack myOnTheTrack  = new OnTheTrack();

    /**
     * <h2>Constructor</h2>
     * @post Creates a player with a name
     * @param name name of the player
     * @throws IllegalArgumentException if the parameter is not valid
     */
    public Player(String name){
        if(name == null || name.equals("")){
            throw new IllegalArgumentException();
        }
        else {
            this.name = name;
            Arrays.stream(BigCities.values()).forEach(bigCities -> this.bigCitiesTimesVisited.put(bigCities,0));
        }
    }

    /**
     *<b>Accessor Method</b>
     * @post The name of the player has been returned
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * <b>Accessor Method</b>
     * @post the player's score has been returned
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }


    /**
     * <b>Transformer Method</b>
     * @post The value of points has been set
     * @param score number of points
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * <b>Transformer Method</b>
     * @post The value of points has been updated
     * @param score number of points
     */
    public void updateScore(int score){
        this.score +=score;
    }

    /**
     * <b>Observer Method</b>
     * @return true if it's the player's turn, else false
     */
    public boolean hasTurn() {
        return hasTurn;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new value as player's turn has been set
     * @param myTurn true/false
     */
    public void setTurn(boolean myTurn) {
        hasTurn = myTurn;
    }


    /**
     * <b>Accessor Method</b>
     * @post a list of cards on player's hands has been returned
     * @return a list of cards on player's hands
     */
    public ArrayList<TrainCard> getCardsOnHands() {
        return trainCardsOnHands;
    }

    /**
     * <b>Transformer Method</b>
     * @post a card has been added to the player's cards on his hand
     * @param card a card
     */
    public void addCardOnHands(TrainCard card){
        this.getCardsOnHands().add(card);
    }

    /**
     * <b>Transformer Method</b>
     * @post the selected card has been removed from player's hand
     * @param selectedCard a train cards
     */
    public void removeCardFromHands(TrainCard selectedCard){
        this.getCardsOnHands().remove(selectedCard);
    }

    /**
     * <b>Accessor Method</b>
     * @post a list of the player's destination tickets has been returned
     * @return a list of the player's destination tickets
     */
    public ArrayList<DestinationCard> getDestinationCards() {
        return destinationCardsOnHands;
    }

    /**
     * <b>Transformer Method</b>
     * @post a card has been added to the player's destination ticket collection
     * @param card a card
     */
    public void addDestinationCard(DestinationCard card) {
        this.getDestinationCards().add(card);
    }

    /**
     * <b>Transformer Method</b>
     * @post a card has been removed to the player's destination ticket collection
     * @param card a card
     */
    public void removeDestinationCard(DestinationCard card){this.getDestinationCards().remove(card);}

    /**
     * <b>Accessor Method</b>
     * @post a list of the bonus cards, the player has earned, has been returned
     * @return a list of the bonus cards the player has earned
     */
    public ArrayList<BigCitiesCard> getBonusCardsAcquired() {
        return bonusCardsAcquired;
    }


    /**
     * <b>Accessor Method</b>
     * @post a list of the destination cards, the player has bought off, has been returned
     * @return a list of the destination cards, the player has bought off
     */
    public ArrayList<DestinationCard> getDestinationCardsAcquired() {
        return destinationCardsAcquired;
    }

    /**
     * <b>Transformer Method</b>
     * @post a card has been added to the player's bonus card collection
     * @param card a card
     */
    public void addBonusCard(BigCitiesCard card) {
        this.updateScore(card.getPoints());
        this.bonusCardsAcquired.add(card);
    }

    /**
     * <b>Accessor Method</b>
     * @post the content of the player's railyard has been returned
     * @return the content of the player's railyard
     */
    public RailYard getRailYard() {
        return myRailYard;
    }

    /**
     * <b>Accessor Method</b>
     * @post the content of the player's track has been returned
     * @return the content of the player's track
     */
    public OnTheTrack getTrack() {
        return myOnTheTrack;
    }


    /**
     * <b>Observer Method</b>
     * @param tc a train card
     * @return whether or not a player is allowed to play the specific cards
     */
    public boolean canCardBePlayed(TrainCard tc){
        return this.getRailYard().getCardsByColor(tc.getColor()).size()>0;
    }

    /**
     * <b>Transformer Method</b>
     * @post the counter of the specific city has been updated
     * @param bc BigCities enum
     */
    public void addBigCitiesTimeVisited(BigCities bc){
        this.bigCitiesTimesVisited.replace(bc,this.bigCitiesTimesVisited.get(bc)+1);
    }

    /**
     * <b>Accessor Method</b>
     * @param bc BigCities enum
     * @return the number of visits the player has to a specific city
     */
    public int getBigCitiesTimeVisited(BigCities bc){
        return this.bigCitiesTimesVisited.get(bc);
    }

    /**
     * <b>Accessor Method</b>
     * @param op another player
     * @return the player with the highest score
     */
    public Player compareByScore(Player op){
        if(this.getScore() > op.getScore()) return this;
        else  if(this.getScore() < op.getScore()) return op;
        return null;
    }

    /**
     * <b>Accessor Method</b>
     * @param op another player
     * @return the player with the largest amount of acquired dest cards
     */
    public Player compareByDestTickets(Player op){
        if(this.getDestinationCardsAcquired().size() > op.getDestinationCardsAcquired().size()) return this;
        else  if(this.getDestinationCardsAcquired().size() < op.getDestinationCardsAcquired().size()) return op;
        return null;
    }

    /**
     * <b>Accessor Method</b>
     * @param op another player
     * @return the player with the largest amount of bonus cards
     */
    public Player compareByBonusCards(Player op){
        if(this.getBonusCardsAcquired().size() > op.getBonusCardsAcquired().size()) return this;
        else  if(this.getBonusCardsAcquired().size() < op.getBonusCardsAcquired().size()) return op;
        return null;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new list of traincards has been set as trainCardsOnHands
     * @param trainCardsOnHands a list of trainCards
     */
    public void setTrainCardsOnHands(ArrayList<TrainCard> trainCardsOnHands) {
        this.trainCardsOnHands.clear();
        this.trainCardsOnHands = trainCardsOnHands;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new RailYard has been set
     * @param myRailYard a new railYard
     */
    public void setRailYard(RailYard myRailYard) {
        this.myRailYard = myRailYard;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new track has been set
     * @param myOnTheTrack a new track
     */
    public void setTrack(OnTheTrack myOnTheTrack) {
        this.myOnTheTrack = myOnTheTrack;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new list of dest cards has been set
     * @param destinationCardsOnHands list of dest cards
     */
    public void setDestinationCardsOnHands(ArrayList<DestinationCard> destinationCardsOnHands) {
        this.destinationCardsOnHands = destinationCardsOnHands;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new list of acquired dest cards has been set
     * @param destinationCardsAcquired list of dest cards
     */
    public void setDestinationCardsAcquired(ArrayList<DestinationCard> destinationCardsAcquired) {
        this.destinationCardsAcquired = destinationCardsAcquired;
    }

    /**
     * <b>Transformer Method</b>
     * @post new values for times of visits per big city has been set
     * @param bigCitiesTimesVisited times of visits per big city
     */
    public void setBigCitiesTimesVisited(HashMap<BigCities, Integer> bigCitiesTimesVisited) {
        this.bigCitiesTimesVisited = bigCitiesTimesVisited;
    }

    /**
     * <b>Transformer Method</b>
     * @post a new list of acquired bonus cards has been set
     * @param bonusCardsAcquired list of bonus cards
     */
    public void setBonusCardsAcquired(ArrayList<BigCitiesCard> bonusCardsAcquired) {
        this.bonusCardsAcquired = bonusCardsAcquired;
    }
}
