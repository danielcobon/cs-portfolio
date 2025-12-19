/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cis2039.pocketbeasts;

/**
 *
 * @author Daniel
 */
public interface EffectStrategy {
    /**
     * Strategy pattern used for the effects that card have on their Tag
     * @param player
     * @param tag 
     */
    public void getEffect(Player player, String tag);
}
