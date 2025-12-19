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
    
    protected final ArrayList<BeastCard> cards;
    
    public CardHandler() {
        this.cards = new ArrayList<>();
    }
    
    public CardHandler(ArrayList<BeastCard> cards) {
        this.cards = cards;
    }
    
    public void add(BeastCard card) {
        this.cards.add(card);
    }
    
    public int count() {
        return this.cards.size();
    }
    
}
