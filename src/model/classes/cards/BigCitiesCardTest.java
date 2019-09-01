package model.classes.cards;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BigCitiesCardTest {

    BigCitiesCard bc;
    @Before
    public void setUp() throws Exception {
        bc = new BigCitiesCard(BigCities.Miami,10,"./someFolder/someFile.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBigCitiesCard(){
        BigCitiesCard bca = new BigCitiesCard(BigCities.Miami,10,null);
    }

    @Test
    public void getCity() {
        assertEquals(bc.getCity(),BigCities.Miami);
    }

    @Test
    public void getCityName() {
        assertEquals(bc.getCityName(),"Miami");
    }

    @Test
    public void isTaken() {
        assertFalse(bc.isTaken());
    }

    @Test
    public void setTaken() {
        bc.setTaken(true);
        assertTrue(bc.isTaken());
    }
}