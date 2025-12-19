/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

/**
 *
 * @author Daniel
 */
public class HealStrategy implements EffectStrategy{
    public void getEffect(Player player, String tag) {
        // Extract the digits part and convert it to an integer
        String[] parts = tag.split(": ");        
        int healthGain = Integer.parseInt(parts[1]); 
        
        // Gain the specified number of health
        int newHealth = player.getHealth() + healthGain;
        player.setHealth(newHealth);
        
        System.out.println(String.format("\nGain %d health, now %s has [HEALTH: %d]", healthGain, player.getName(), player.getHealth()));   
    }    
}
