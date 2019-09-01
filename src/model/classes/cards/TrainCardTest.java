package model.classes.cards;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrainCardTest {

    TrainCard cd;

    @Before
    public void setUp() throws Exception {
        cd = new TrainCard("./someFolder/someFile.jpg",CardColor.BLACK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrainCard(){
        TrainCard trc = new TrainCard("",CardColor.BLACK);
    }

    @Test
    public void testGetColor(){
        assertEquals(cd.getColor(),CardColor.BLACK);
    }

}