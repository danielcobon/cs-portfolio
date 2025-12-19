/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

/**
 *
 * @author Daniel
 */
public abstract class GameTemplate {
    
    // Template method for getting rules
    protected abstract void getRules();
    
    // Template method for gameplay
    protected abstract void playGame(String player1, String player2);
      
}
