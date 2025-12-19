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
public abstract class CardHandler {
    
    protected final ArrayList<Card> cards;
    
    public CardHandler() {
        this.cards = new ArrayList<>();
    }
    
    public CardHandler(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
    public void add(Card card) {
        this.cards.add(card);
    }
    
    public int count() {
        return this.cards.size();
    }
    
}
