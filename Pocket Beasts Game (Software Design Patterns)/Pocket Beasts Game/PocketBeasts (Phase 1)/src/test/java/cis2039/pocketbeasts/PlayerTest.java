/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Player class.
 * This class contains test methods to verify the functionality of the Player class.
 * @author Daniel
 */
public class PlayerTest {
    
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
    
    
    public PlayerTest() {
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
     * Test of getName method, of class Player.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        
        // List of expected names
        List<String> expectedNames = Arrays.asList("Daniel", "Alice", "Bob", "Eva");
        
        Deck deck = new Deck(new ArrayList<Card>());
        
        // Iterating through each name in list to be used as the player's name for each instance
        for (String name : expectedNames) {
            Player instance = new Player(name, deck);
            
            // Verify if the getName() method returns the correct name for each expected name
            assertEquals("Testing GetName: ", name, instance.getName());
        }
    }

    /**
     * Test of getManaAvailable method, of class Player.
     */
    @Test
    public void testGetManaAvailable() {
        System.out.println("getManaAvailable");
        
        int manaAvailable = 0;
        Deck deck = new Deck(new ArrayList<Card>());
        Player instance = new Player("TestPlayer", deck);
        
        // Verify if the getManaAvailable() method returns the correct value, in this case it is 0 which is the default mana available at the start of the game
        assertEquals("Testing GetManaAvailable: ", manaAvailable, instance.getManaAvailable());
    }

    /**
     * Test of getHealth method, of class Player.
     */
    @Test
    public void testGetHealth() {
        System.out.println("getHealth");
        
        int health = 15;
        Deck deck = new Deck(new ArrayList<Card>());
        Player instance = new Player("TestPlayer", deck);
        
        // Verify if the getHealth() method returns the correct value, in this case it is 15 which is the default health at the start of the game
        assertEquals("Testing GetHealth: ", health, instance.getHealth());
    }

    /**
     * Test of getDeck method, of class Player.
     */
    @Test
    public void testGetDeck() {
        System.out.println("getDeck");
        
        // Create a deck with the default starter deck
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(STARTER_CARDS)));
        
        Player instance = new Player("TestPlayer", deck);
        
        // Verify if the getDeck() method returns the correct deck (default starter deck)
        assertEquals("Testing GetDeck: ", Arrays.asList(STARTER_CARDS), instance.getDeck().cards);
    }

    /**
     * Test of getHand method, of class Player.
     */
    @Test
    public void testGetHand() {
        System.out.println("getHand");

        // Create a deck with the default starter deck
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(STARTER_CARDS)));

        // Create a player instance with the deck
        Player instance = new Player("TestPlayer", deck);

        // Add some cards to the player's hand
        instance.getHand().add(new Card("BR", "Barn Rat", 1, 1, 1));
        instance.getHand().add(new Card("SP", "Scampering Pup", 2, 2, 1));

        // Expected hand cards as an ArrayList
        List<Card> expectedHandCards = new ArrayList<>(Arrays.asList(
            new Card("BR", "Barn Rat", 1, 1, 1),
            new Card("SP", "Scampering Pup", 2, 2, 1)
        ));

        // Convert both lists to their string representations
        String expectedHandString = expectedHandCards.toString();
        String actualHandString = instance.getHand().getCards().toString();

        // Debugging output
        System.out.println("Expected: " + expectedHandString);
        System.out.println("Actual: " + actualHandString);

        // Verify if the getHand() method returns the correct hand object with expected cards
        assertEquals("Testing GetHand: ", expectedHandString, actualHandString);
    }

    /**
     * Test of getInPlay method, of class Player.
     */
    @Test
    public void testGetInPlay() {
        System.out.println("getInPlay");

        // Create a deck with the default starter deck
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(STARTER_CARDS)));

        // Create a player instance with the deck
        Player instance = new Player("TestPlayer", deck);

        // Add some cards to the player's in-play area
        instance.getInPlay().add(new Card("BR", "Barn Rat", 1, 1, 1));
        instance.getInPlay().add(new Card("HT", "Highland Tiger", 5, 4, 4));

        // Expected in-play cards as an ArrayList
        List<Card> expectedInPlayCards = new ArrayList<>(Arrays.asList(
            new Card("BR", "Barn Rat", 1, 1, 1),
            new Card("HT", "Highland Tiger", 5, 4, 4)
        ));

        // Convert both lists to their string representations
        String expectedInPlayString = expectedInPlayCards.toString();
        String actualInPlayString = instance.getInPlay().getCards().toString();

        // Debugging output
        System.out.println("Expected: " + expectedInPlayString);
        System.out.println("Actual: " + actualInPlayString);

        // Verify if the getInPlay() method returns the correct in-play object with expected cards
        assertEquals("Testing GetInPlay: ", expectedInPlayString, actualInPlayString);
    }

    /**
     * Test of getGraveyard method, of class Player.
     */
    @Test
    public void testGetGraveyard() {
        System.out.println("getGraveyard");

        // Create a deck with the default starter deck
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(STARTER_CARDS)));

        // Create a player instance with the deck
        Player instance = new Player("TestPlayer", deck);

        // Add some cards to the player's graveyard
        instance.getGraveyard().add(new Card("BR", "Barn Rat", 1, 1, 1));
        instance.getGraveyard().add(new Card("MO", "Moor Owl", 4, 4, 2));

        // Expected graveyard cards as an ArrayList
        List<Card> expectedGraveyardCards = new ArrayList<>(Arrays.asList(
            new Card("BR", "Barn Rat", 1, 1, 1),
            new Card("MO", "Moor Owl", 4, 4, 2)
        ));

        // Convert both lists to their string representations
        String expectedGraveyardString = expectedGraveyardCards.toString();
        String actualGraveyardString = instance.getGraveyard().cards.toString();

        // Debugging output
        System.out.println("Expected: " + expectedGraveyardString);
        System.out.println("Actual: " + actualGraveyardString);

        // Verify if the getGraveyard() method returns the correct graveyard object with expected cards
        assertEquals("Testing GetGraveyard: ", expectedGraveyardString, actualGraveyardString);
    }
}


