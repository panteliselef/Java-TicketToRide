package model.classes.cards;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DestinationCardTest {

    DestinationCard dc;

    @Before
    public void setUp() throws Exception {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("locomotive");
        dc = new DestinationCard(111,"Mexico","Hawaii",19,colors,"./someFolder/someImage.jpg");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDestinationCard(){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("locomotive");
        colors.add("adwada");
        DestinationCard tmp = new DestinationCard(111,"Mexico","Hawaii",19,colors,"./someFolder/someImage.jpg");
        assertEquals(tmp.getColors().get(0),CardColor.LOCOMOTIVE);
        assertEquals(tmp.getColors().get(1),CardColor.LOCOMOTIVE);
    }

    @Test
    public void getUniqueId() {
        assertEquals(111,dc.getUniqueId());
    }

    @Test
    public void getArrivalCity() {
        assertEquals("Hawaii",dc.getArrivalCity());
    }

    @Test
    public void getDepartureCity() {
        assertEquals("Mexico",dc.getDepartureCity());
    }

    @Test
    public void getColors() {
        assertEquals(dc.getColors().get(0),CardColor.LOCOMOTIVE);
    }
}