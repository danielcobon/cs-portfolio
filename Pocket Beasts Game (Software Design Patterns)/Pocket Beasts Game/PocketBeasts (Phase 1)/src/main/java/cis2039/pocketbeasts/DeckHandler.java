/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */


public class DeckHandler {
    
    /***
     * Deck is built here, changes to cards and deck is edit here
     */
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
    
    /***
     * Builds the deck for both players according to starter cards
     * @return 
     */
    public static ArrayList<Card> getStarterDeck() {
        ArrayList<Card> starterDeck = new ArrayList<>();
        
        for (int i=0; i<2; i++) {
            for (Card card : STARTER_CARDS) {
                starterDeck.add(new Card(card));
            }
        }
        
        return starterDeck;
    }
}
