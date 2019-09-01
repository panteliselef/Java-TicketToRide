package model.classes.game;

import model.classes.cards.CardColor;
import model.classes.cards.TrainCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
    Player pl;

    @Before
    public void setUp() throws Exception {
        pl = new Player("myName");
        pl.setScore(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayer(){
        Player pl1 = new Player("");
    }

    @Test
    public void getName() {
        assertEquals("myName",pl.getName());
    }

    @Test
    public void getScore() {
        assertEquals(10,pl.getScore());
    }

    @Test
    public void setScore() {
        pl.setScore(11);
        assertEquals(11,pl.getScore());
    }

    @Test
    public void updateScore() {
        pl.updateScore(2);
        assertEquals(12,pl.getScore());
    }

    @Test
    public void hasTurn() {
        assertFalse(pl.hasTurn());
    }

    @Test
    public void setTurn() {
        pl.setTurn(!pl.hasTurn());
        assertTrue(pl.hasTurn());
    }

    @Test
    public void getCardsOnHands() {
        ArrayList<TrainCard> tcArr = pl.getCardsOnHands();
        TrainCard[] tcarr = new TrainCard[pl.getCardsOnHands().size()];
        tcArr.toArray(tcarr);
        TrainCard[] tctmp = new TrainCard[0];
        assertArrayEquals(tcarr,tctmp);
    }

    @Test
    public void addCardOnHands() {
        int cards = pl.getCardsOnHands().size();
        pl.addCardOnHands(new TrainCard("daw", CardColor.BLACK));
        assertEquals(cards+1,pl.getCardsOnHands().size());
    }

}