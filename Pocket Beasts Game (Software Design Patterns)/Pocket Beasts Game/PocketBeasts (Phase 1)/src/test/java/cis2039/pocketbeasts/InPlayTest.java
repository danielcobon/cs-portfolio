/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the InPlay class.
 * This class contains test methods to verify the functionality of the InPlay class.
 * @author Daniel
 */
public class InPlayTest {
    
    // Default Starter Deck used for Phase 1 (same as code)
    public static final Card[] STARTER_CARDS = new Card[] {
        new Card ("BR", "Barn Rat", 1, 1, 1),
        new Card ("SP", "Scampering Pup", 2, 2, 1),
        new Card ("HB", "Hardshell Beetle", 2, 1, 2),
        new Card ("VHC", "Vicious House Cat", 3, 3, 2),
        new Card ("GD", "Guard Dog", 3, 2, 3),
        new Card ("ARH", "All Round Hound", 3, 3, 3),
        new Card ("MO", "Moor Owl", 4, 4, 2),
        new Card ("HT", "Highland Tiger", 5, 4, 4)
    };
    
    public InPlayTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of add method, of class InPlay.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card expectedCard = STARTER_CARDS[i];
            
            // Adds the starter deck cards into an instance
            instance.add(expectedCard);
            
            // Get the added card from the instance
            Card addedCard = instance.getCard(i);

            // Assert that the expected card is equal to the actual card
            assertEquals("Testing Add: " + expectedCard, expectedCard, addedCard);
        }
    }

    /**
     * Test of getCards method, of class InPlay.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (Card card : STARTER_CARDS) {
            instance.add(card);
        }
        
        // Array to store the original starter deck cards
        ArrayList<Card> expResult = new ArrayList<>();
        
        for (Card card : STARTER_CARDS) {
            expResult.add(card);
        }
        
        // Assert that the expected card is equal to the actual card
        assertEquals("Testing GetCards: " + expResult, expResult, instance.getCards());
    }

    /**
     * Test of getCard method, of class InPlay.
     */
    @Test
    public void testGetCard() {
        System.out.println("getCard");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (Card card : STARTER_CARDS) {
            instance.add(card);
        }
        
        // Iterates through each starter deck card and assert that the expected card is equal to the actual card
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card expResult = STARTER_CARDS[i];
            Card result = instance.getCard(i);
            assertEquals("Testing GetCard : " + expResult, expResult, result);
        }
    }

    /**
     * Test of remove method, of class InPlay.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (Card card : STARTER_CARDS) {
            instance.add(card);
        }
        for (Card card : STARTER_CARDS) {
            instance.remove(card);
            assertFalse("Testing Remove: ", instance.getCards().contains(card));
        }
    }

    /**
     * Test of removeAll method, of class InPlay.
     */
    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (Card card : STARTER_CARDS) {
            instance.add(card);
        }
        
        ArrayList<Card> cardsToRemove = new ArrayList<>();
        
        for (Card card : STARTER_CARDS) {
            cardsToRemove.add(card);
        }
        instance.removeAll(cardsToRemove);
        assertTrue("Testing Remove All: ", instance.getCards().isEmpty());
    }

    /**
     * Test of count method, of class InPlay.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (Card card : STARTER_CARDS) {
            instance.add(card);
        }
        
        int expResult = STARTER_CARDS.length;
        int result = instance.count();
        
        // Assert that the expected card is equal to the result
        assertEquals("Testing Count: " + expResult, expResult, result);
    }
}
