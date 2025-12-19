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
 * Test class for the Hand class.
 * This class contains test methods to verify the functionality of the Hand class.
 * @author Daniel
 */
public class HandTest {
    
    // Default Starter Deck used for Phase 1 (same as code)
    public static final BeastCard[] STARTER_CARDS = new BeastCard[] {
        CardFactory.createBeastCard("BR", "Barn Rat", 1, 1, 1, "-"),
        CardFactory.createBeastCard("SP", "Scampering Pup", 2, 2, 1, "-"),
        CardFactory.createBeastCard("HB", "Hardshell Beetle", 2, 1, 2, "-"),
        CardFactory.createBeastCard("VHC", "Vicious House Cat", 3, 3, 2, "-"),
        CardFactory.createBeastCard("GD", "Guard Dog", 3, 2, 3, "Heal: 1"),
        CardFactory.createBeastCard("ARH", "All Round Hound", 3, 3, 3, "-"),
        CardFactory.createBeastCard("MO", "Moor Owl", 4, 4, 2, "-"),
        CardFactory.createBeastCard("HT", "Highland Tiger", 5, 4, 4, "Mana: 1")
    };
    
    public HandTest() {
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
     * Testing all extended methods from CardHandler class, which is count(). add() from CardHandler will be overridden by the Hand class.
     */
    
    /**
     * Test of count method, of class Hand.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        
        InPlay instance = new InPlay();
        
        // Iterate through the each card in the Starter Deck and adding them into the instance
        for (BeastCard card : STARTER_CARDS) {
            instance.add(card);
        }
        
        int expResult = STARTER_CARDS.length;
        int result = instance.count();
        
        // Assert that the expected card is equal to the result
        assertEquals("Testing Count: " + expResult, expResult, result);
    }
    

    /**
     * Test of add method, of class Hand.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        
        Hand instance = new Hand();
        
        BeastCard cardToAdd = new BeastCard("BR", "Barn Rat", 1, 1, 1, "-");
        
        // Add a card to the hand
        instance.add(cardToAdd);
        
        // Check if the card is in the hand
        assertTrue("Testing Add: ", instance.getCards().contains(cardToAdd));
    }

    /**
     * Test of remove method, of class Hand.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        
        Hand instance = new Hand();
        BeastCard cardToAdd = new BeastCard("BR", "Barn Rat", 1, 1, 1, "-");
        
        // Add a card to the hand
        instance.add(cardToAdd);
        
        // Remove the card from the hand
        instance.remove(cardToAdd);
        
        // Check if the card is not in the hand
        assertFalse("Testing Remove: ", instance.getCards().contains(cardToAdd));
    }

    /**
     * Test of removeAll method, of class Hand.
     */
    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        
        Hand instance = new Hand();
        ArrayList<BeastCard> cardsToAdd = new ArrayList<>();
        cardsToAdd.add(new BeastCard("BR", "Barn Rat", 1, 1, 1, "-"));
        cardsToAdd.add(new BeastCard("SP", "Scampering Pup", 2, 2, 1, "-"));
        
        // Add cards to the hand
        for (BeastCard card : cardsToAdd) {
            instance.add(card);
        }
        
        // Remove all cards from the hand
        instance.removeAll(cardsToAdd);
        
        // Check if the hand is empty
        assertTrue("Testing Remove All: ", instance.getCards().isEmpty());
    }

    /**
     * Test of sort method, of class Hand.
     */
    @Test
    public void testSort() {
        System.out.println("sort");
        
        Hand instance = new Hand();
        ArrayList<BeastCard> cardsToAdd = new ArrayList<>();
        cardsToAdd.add(new BeastCard("SP", "Scampering Pup", 2, 2, 1, "-"));
        cardsToAdd.add(new BeastCard("BR", "Barn Rat", 1, 1, 1, "-"));
        
        // Add cards to the hand in unsorted order
        for (BeastCard card : cardsToAdd) {
            instance.add(card);
        }
        
        // Sort the hand
        instance.sort();
        
        // Get the cards from the sorted hand
        ArrayList<BeastCard> sortedCards = instance.getCards();
        
        // Check if the cards are sorted in ascending order of mana cost
        for (int i = 0; i < sortedCards.size() - 1; i++) {
            assertTrue("Testing Sort: ", sortedCards.get(i).getManaCost() <= sortedCards.get(i + 1).getManaCost());
        }
    }
}

