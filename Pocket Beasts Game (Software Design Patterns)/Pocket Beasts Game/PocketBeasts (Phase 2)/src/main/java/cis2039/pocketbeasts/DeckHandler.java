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
     * Deck is built here, changes to cards and deck is edited here
     */
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

    /***
     * Builds the deck for both players according to starter cards
     * @return ArrayList<Card> containing the starter deck
     */
    public static ArrayList<BeastCard> getStarterDeck() {
        ArrayList<BeastCard> starterDeck = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (BeastCard card : STARTER_CARDS) {
                starterDeck.add(CardFactory.createCard(card));
            }
        }

        return starterDeck;
    }
}
