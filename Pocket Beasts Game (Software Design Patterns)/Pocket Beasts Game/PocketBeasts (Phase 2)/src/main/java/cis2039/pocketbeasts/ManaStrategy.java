/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

/**
 *
 * @author Daniel
 */
public class ManaStrategy implements EffectStrategy {
    @Override
    public void getEffect(Player player, String tag) {
        // Extract the digits part and convert it to an integer
        String[] parts = tag.split(": ");        
        int manaGain = Integer.parseInt(parts[1]); 
        
        // Gain the specified number of mana
        for (int i = 0; i < manaGain; i++) {
            // player.addMana();
            if (player.manaTicker < player.MAX_MANA) {
                player.manaTicker++;
            }
        }
        System.out.println(String.format("\nGain %d max mana, now %s has [MANA: %d/%d]", manaGain, player.getName(), player.getManaAvailable(), player.getManaTicker()));        
    }
}
