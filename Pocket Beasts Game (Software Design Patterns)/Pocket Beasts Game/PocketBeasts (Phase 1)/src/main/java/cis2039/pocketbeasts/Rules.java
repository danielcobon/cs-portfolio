/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class Rules {
    
    public static void getRules() {
        System.out.println("");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("Welcome to PocketBeasts!");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("");
        System.out.println("This basic console application tests our underlying software design patterns.");
        System.out.println("");
        System.out.println("Here's a key for each card:");
        System.out.println("");
        System.out.println("                             +-------+ ");
        System.out.println("M  = Mana Cost               |      M| ");
        System.out.println("ID = Card identifier:        |  ID   | ");
        System.out.println("A  = Attack:                 |       | ");
        System.out.println("H  = Health:                 |A     H| ");
        System.out.println("                             +-------+ ");
        System.out.println("");
        System.out.println("New players each start with 15 Health and 1 Mana to spend on playing cards.");
        System.out.println("At the start of the game each player draws 4 cards from their deck to hand.");
        System.out.println("");
        System.out.println("Players each take turns. Each turn consists four phases:");
        System.out.println("1. Add mana (mana increases by one each turn and replenishes in full).");
        System.out.println("2. Draw a card.");
        System.out.println("3. Cycle through your cards in play (if any), choosing whether to attack.");
        System.out.println("   a. Attacking the other player directly with your card inflicts damage to their health.");
        System.out.println("      equal to the attack power of the card.");
        System.out.println("   b. Attacking another players beast will damage both cards (equal to their attack values).");
        System.out.println("   c. Any beast with <= 0 health is removed from the play field and placed into the graveyard.");
        System.out.println("4. Play cards from hand.");
        System.out.println("");
        
        System.out.println("Press ENTER to continue...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}
