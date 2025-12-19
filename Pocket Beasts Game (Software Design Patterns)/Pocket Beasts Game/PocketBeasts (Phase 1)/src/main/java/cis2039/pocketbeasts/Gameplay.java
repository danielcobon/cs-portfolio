/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cis2039.pocketbeasts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class Gameplay {
    
    public static String getPrompt(String prompt, String[] validResponse){
        System.out.print(prompt);
        
        Scanner sc = new Scanner(System.in);
        String response = sc.nextLine();
        
        if (Arrays.stream(validResponse).anyMatch(response::equals)) {
            return response;
        }
        
        return getPrompt(prompt, validResponse);
    }
    
    public static void getGameplay(String player1, String player2) {
        Player[] players = new Player[] {
            new Player(player1, new Deck(DeckHandler.getStarterDeck())),
            new Player(player2, new Deck(DeckHandler.getStarterDeck()))
        }; 
        
        for (Player player : players) {
            player.newGame();
            System.out.println(player);
        }
        
        String winningMessage = "";
        Boolean run = true;
        while(run) {
            for (Player player : players) {
                // Add mana and draw card
                player.addMana();
                player.drawCard();

                // Print initial play state
                System.out.println(player);

                // HACK assumes only one other player
                Player otherPlayer = null;
                for (Player iPlayer : players) {
                    if (iPlayer != player) {
                        otherPlayer = iPlayer;
                    }
                }
                if (otherPlayer == null){
                    winningMessage = "Something has gone terribly wrong...";
                    run = false;
                    break;
                }
                
                // Cycle through cards in play to attack
                for (Card card : player.getInPlay().getCards()) {
                    System.out.println(card.toString());

                    String attack = getPrompt(
                            player.getName() + " attack with " + card.getName() + "? (Yes/No): ", 
                            new String[]{"Yes", "yes", "y", "No", "no", "n"});
                    if (attack.equals("Yes") || attack.equals("yes") || attack.equals("y")) {
                        // Choose who to attack, player directly or a player's beast
                        int attackChoice = 2;
                        System.out.println("Who would you like to attack? ");
                        System.out.println("1. " + otherPlayer.getName());
                        for (Card otherCard: otherPlayer.getInPlay().getCards()) {
                            System.out.println(attackChoice + ". " + otherCard);
                            attackChoice++;
                        }
                        ArrayList<String> prompts = new ArrayList<>();
                        for (int i=1; i<attackChoice; i++) {
                            prompts.add(String.valueOf(i));
                        }
                        String target = getPrompt("Choose a number: ", prompts.toArray(new String[0]));
                        if (target.equals("1")) { // Player
                            if (otherPlayer.damage(card.getAttack())) {
                                // if true returned players health <= 0
                                winningMessage = player.getName() + " wins!";
                                run = false;
                                break;
                            }
                            System.out.println(otherPlayer.getName() + " is now at " + otherPlayer.getHealth());
                        }
                        else { // Beast, index is `target-2`
                            Card targetCard = otherPlayer.getInPlay().getCard(Integer.parseInt(target)-2);
                            targetCard.damage(card.getAttack());
                            card.damage(targetCard.getAttack());
                        }
                    }
                }
                
                if (!run) {
                    break;
                }
                
                // Cycle through cards in play remove "dead" cards (health <= 0)
                ArrayList<Card> toRemove = new ArrayList<>();
                for (Card card : player.getInPlay().getCards()) {
                    if (card.getHealth() <= 0) {
                        toRemove.add(card);
                        player.getGraveyard().add(card);
                    }
                }
                player.getInPlay().removeAll(toRemove);
                
                toRemove = new ArrayList<>();
                for (Card card : otherPlayer.getInPlay().getCards()) {
                    if (card.getHealth() <= 0) {
                        toRemove.add(card);
                        otherPlayer.getGraveyard().add(card);
                    }
                }
                otherPlayer.getInPlay().removeAll(toRemove);

                // Play cards from hand
                toRemove = new ArrayList<>();
                for (Card card : player.getHand().getCards()) {
                    if (card.getManaCost() <= player.getManaAvailable()) {
                        System.out.println(card.toString());

                        String play = getPrompt(
                                player.getName() + " play " + card.getName() + "? (Yes/No) ", 
                                new String[]{"Yes", "yes", "y", "No", "no", "n"});
                        if (play.equals("Yes") || play.equals("yes") || play.equals("y")) {
                            player.getInPlay().add(card);
                            player.useMana(card.getManaCost());
                            toRemove.add(card);
                        }
                    }
                }
                player.getHand().removeAll(toRemove);
                
                // Print final play state
                System.out.println("\n".repeat(16));
                System.out.println(player);
            }
        }
        
        System.out.println(winningMessage);
    }
    
}
