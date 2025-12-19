/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package cis2039.pocketbeasts;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Card class.
 * This class contains test methods to verify the functionality of the Card class.
 * @author Steven Mead
 */
public class CardTest {
    
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
    
    public CardTest() {
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
     * Test of getId method, of class Card.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");

        // Expected IDs corresponding to the STARTER_CARDS array
        String[] expectedIds = {"BR", "SP", "HB", "VHC", "GD", "ARH", "MO", "HT"};

        // Iterate through the each card ID in the Starter Deck
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card instance = STARTER_CARDS[i];
            String expResult = expectedIds[i];
            String result = instance.getId();
            assertEquals("Testing card ID: " + expResult, expResult, result);
        }
    }

    /**
     * Test of getName method, of class Card.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");

        // Expected names corresponding to the STARTER_CARDS array
        String[] expectedNames = {
            "Barn Rat", "Scampering Pup", "Hardshell Beetle", "Vicious House Cat",
            "Guard Dog", "All Round Hound", "Moor Owl", "Highland Tiger"
        };

        // Iterate through the each card Name in the Starter Deck
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card instance = STARTER_CARDS[i];
            String expResult = expectedNames[i];
            String result = instance.getName();
            assertEquals("Testing card name: " + expResult, expResult, result);
        }
    }

    /**
     * Test of getManaCost method, of class Card.
     */
    @Test
    public void testGetManaCost() {
        System.out.println("getManaCost");

        // Expected mana costs corresponding to the STARTER_CARDS array
        int[] expectedManaCosts = {1, 2, 2, 3, 3, 3, 4, 5};

        // Iterate through the each card's Mana Cost in the Starter Deck
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card instance = STARTER_CARDS[i];
            int expResult = expectedManaCosts[i];
            int result = instance.getManaCost();
            assertEquals("Testing card mana cost: " + expResult, expResult, result);
        }
    }

    /**
     * Test of getAttack method, of class Card.
     */
    @Test
    public void testGetAttack() {
        System.out.println("getAttack");

        // Expected attack values corresponding to the STARTER_CARDS array
        int[] expectedAttackValues = {1, 2, 1, 3, 2, 3, 4, 4};

        // Iterate through the each card's Attack value in the Starter Deck
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card instance = STARTER_CARDS[i];
            int expResult = expectedAttackValues[i];
            int result = instance.getAttack();
            assertEquals("Testing card attack value: " + expResult, expResult, result);
        }
    }

    /**
     * Test of getHealth method, of class Card.
     */
    @Test
    public void testGetHealth() {
        System.out.println("getHealth");

        // Expected health values corresponding to the STARTER_CARDS array
        int[] expectedHealthValues = {1, 1, 2, 2, 3, 3, 2, 4};

        // Iterate through the each card's Health value in the Starter Deck
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card instance = STARTER_CARDS[i];
            int expResult = expectedHealthValues[i];
            int result = instance.getHealth();
            assertEquals("Testing card health value: " + expResult, expResult, result);
        }
    }

    /**
     * Test of damage method, of class Card.
     */
    @Test
    public void testDamage() {
        System.out.println("damage");

        // Test data: pairs of (index, damage amount) and their expected health values after damage
        int[][] testCases = {
            {0, 1, 0}, // Barn Rat (1/1) takes 1 damage
            {1, 1, 0}, // Scampering Pup (2/1) takes 1 damage
            {2, 1, 1}, // Hardshell Beetle (1/2) takes 1 damage
            {3, 2, 0}, // Vicious House Cat (3/2) takes 2 damage
            {4, 1, 2}, // Guard Dog (2/3) takes 1 damage
            {5, 3, 0}, // All Round Hound (3/3) takes 3 damage
            {6, 2, 0}, // Moor Owl (4/2) takes 2 damage
            {7, 4, 0}  // Highland Tiger (4/4) takes 4 damage
        };

        // Iterates through the Starter Deck cards and dealing damage to each card with different values
        for (int[] testCase : testCases) {
            int cardIndex = testCase[0];
            int damageAmount = testCase[1];
            int expectedHealth = testCase[2];

            Card instance = STARTER_CARDS[cardIndex];
            instance.damage(damageAmount);
            int result = instance.getHealth();
            assertEquals("Testing card health after damage for card at index: " + cardIndex, expectedHealth, result);
        }
    }

    /**
     * Test of toString method, of class Card.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        // Expected string representations corresponding to the STARTER_CARDS array
        String[] expectedStrings = {
            "Barn Rat (BR) Mana Cost/1 Attack/1 Health/1",
            "Scampering Pup (SP) Mana Cost/2 Attack/2 Health/1",
            "Hardshell Beetle (HB) Mana Cost/2 Attack/1 Health/2",
            "Vicious House Cat (VHC) Mana Cost/3 Attack/3 Health/2",
            "Guard Dog (GD) Mana Cost/3 Attack/2 Health/3",
            "All Round Hound (ARH) Mana Cost/3 Attack/3 Health/3",
            "Moor Owl (MO) Mana Cost/4 Attack/4 Health/2",
            "Highland Tiger (HT) Mana Cost/5 Attack/4 Health/4"
        };

        // Iterate through the each card's details in the Starter Deck with the correct formatting
        for (int i = 0; i < STARTER_CARDS.length; i++) {
            Card instance = STARTER_CARDS[i];
            String expResult = expectedStrings[i];
            String result = instance.toString();
            assertEquals("Testing card toString for card at index: " + i, expResult, result);
        }
    }

    /**
     * Test of compareTo method, of class Card.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");

        // Define a set of test cases with pairs of cards and expected compareTo results
        Object[][] testCases = {
            {STARTER_CARDS[0], STARTER_CARDS[1], -1},  // Barn Rat [Mana: 1] < Scampering Pup [Mana: 2]
            {STARTER_CARDS[1], STARTER_CARDS[0], 1},   // Scampering Pup [Mana: 2] > Barn Rat [Mana: 1]
            {STARTER_CARDS[1], STARTER_CARDS[2], 0},   // Scampering Pup [Mana: 2] == Hardshell Beetle [Mana: 2]
            {STARTER_CARDS[3], STARTER_CARDS[4], 0},   // Vicious House Cat [Mana: 3] == Guard Dog [Mana: 3]
            {STARTER_CARDS[5], STARTER_CARDS[6], -1},  // All Round Hound [Mana: 3] < Moor Owl [Mana: 4]
            {STARTER_CARDS[7], STARTER_CARDS[6], 1}    // Highland Tiger [Mana: 5] > Moor Owl [Mana: 4]
        };

        // Iterate through the each card's Mana Cost in the Starter Deck and comparing two cards mana value to find the difference
        for (Object[] testCase : testCases) {
            Card instance = (Card) testCase[0];
            Card otherCard = (Card) testCase[1];
            int expResult = (int) testCase[2];
            int result = instance.compareTo(otherCard);
            assertEquals("Comparing card: " + instance + " with card: " + otherCard, expResult, result);
        }

    }
    
}
