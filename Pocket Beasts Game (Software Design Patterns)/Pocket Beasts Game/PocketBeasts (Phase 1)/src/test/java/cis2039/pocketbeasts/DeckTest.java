/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/** * Test class for the Deck class.
 * This class contains test methods to verify the functionality of the Deck class.
 * @author Daniel
 */
public class DeckTest {
    
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
    
    public DeckTest() {
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
     * Testing all extended methods from CardHandler class, add() and count()
     */
    
    /**
     * Test of add method, of class Deck.
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
     * Test of count method, of class Deck.
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

    /**
     * Test of draw method, of class Deck.
     */
    @Test
    public void testDraw() {
        System.out.println("draw");
        
        // Create a deck with some initial cards
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("BR", "Barn Rat", 1, 1, 1));
        cards.add(new Card("SP", "Scampering Pup", 2, 2, 1));
        
        Deck instance = new Deck(cards);
        
        // Draw a card and check if it's the first card in the deck
        Card expResult = cards.get(0);
        Card result = instance.draw();
        assertEquals("Testing Draw: ", expResult, result);
    }

    /**
     * Test of shuffle method, of class Deck.
     */
    @Test
    public void testShuffle() {
        System.out.println("shuffle");
        
        // Create a deck with some initial cards
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("BR", "Barn Rat", 1, 1, 1));
        cards.add(new Card("SP", "Scampering Pup", 2, 2, 1));
        cards.add(new Card("HB", "Hardshell Beetle", 2, 1, 2));
        
        Deck instance = new Deck(cards);
        
        // Shuffle the deck
        instance.shuffle();
        
        // Since shuffling changes the order, we can't really assert anything specific,
        // but we can at least check that the number of cards remains the same
        assertEquals("Testing Shuffle: ", cards.size(), instance.count());
    }
}
