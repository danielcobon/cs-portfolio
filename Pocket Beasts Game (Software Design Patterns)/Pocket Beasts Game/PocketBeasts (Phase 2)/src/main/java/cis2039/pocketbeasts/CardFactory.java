/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

/**
 *
 * @author Daniel
 */
class CardFactory {
    /**
     * Factory method to create a new BeastCard instance
     * @param id BeastCard ID
     * @param name BeastCard name
     * @param cost BeastCard cost
     * @param attack BeastCard attack value
     * @param defense BeastCard defense value
     * @return a new BeastCard instance
     */
    public static BeastCard createBeastCard(String id, String name, int cost, int attack, int defense, String tag) {
        return new BeastCard(id, name, cost, attack, defense, tag);
    }

    /**
     * Factory method to create a new BeastCard instance by copying an existing card
     * @param card BeastCard to copy
     * @return a new BeastCard instance copied from the provided card
     */
    public static BeastCard createCard(BeastCard card) {
        return new BeastCard(card);
    }
}
