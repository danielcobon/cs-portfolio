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
public class ClassicGame extends GameTemplate{
    
    /**
     * New Classic Game mode, where some Beast Cards have card effects when they are played from hand.
     * A little more advance/strategic gameplay.
     */
    @Override
    protected void getRules() {
        // Implement rules for this game mode
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
        
        /**
         * New Rules added, now some Beast Cards have card effects when they are played from hand
         */
        System.out.println("Some cards also have effects under 'EFFECT/' ");
        System.out.println("Heal:1 makes your character gain 1 health when the Beast card is played");
        System.out.println("Mana:1 makes your character gain 1 max mana (cannot go over mana limit) when the Beast card is played");
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
    
    public static String getPrompt(String prompt, String[] validResponse){
        System.out.print(prompt);
        
        Scanner sc = new Scanner(System.in);
        String response = sc.nextLine();
        
        if (Arrays.stream(validResponse).anyMatch(response::equals)) {
            return response;
        }
        
        return getPrompt(prompt, validResponse);
    }
    
    @Override
    protected void playGame(String player1, String player2) {
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
                for (BeastCard card : player.getInPlay().getCards()) {
                    System.out.println(card.toString());

                    String attack = getPrompt(
                            player.getName() + " attack with " + card.getName() + "? (Yes/No): ", 
                            new String[]{"Yes", "yes", "y", "No", "no", "n"});
                    if (attack.equals("Yes") || attack.equals("yes") || attack.equals("y")) {
                        // Choose who to attack, player directly or a player's beast
                        int attackChoice = 2;
                        System.out.println("Who would you like to attack? ");
                        System.out.println("1. " + otherPlayer.getName());
                        for (BeastCard otherCard: otherPlayer.getInPlay().getCards()) {
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
                            BeastCard targetCard = otherPlayer.getInPlay().getCard(Integer.parseInt(target)-2);
                            targetCard.damage(card.getAttack());
                            card.damage(targetCard.getAttack());
                        }
                    }
                }
                
                if (!run) {
                    break;
                }
                
                // Cycle through cards in play remove "dead" cards (health <= 0)
                ArrayList<BeastCard> toRemove = new ArrayList<>();
                for (BeastCard card : player.getInPlay().getCards()) {
                    if (card.getHealth() <= 0) {
                        toRemove.add(card);
                        player.getGraveyard().add(card);
                    }
                }
                player.getInPlay().removeAll(toRemove);
                
                toRemove = new ArrayList<>();
                for (BeastCard card : otherPlayer.getInPlay().getCards()) {
                    if (card.getHealth() <= 0) {
                        toRemove.add(card);
                        otherPlayer.getGraveyard().add(card);
                    }
                }
                otherPlayer.getInPlay().removeAll(toRemove);

                // Play cards from hand
                toRemove = new ArrayList<>();
                for (BeastCard card : player.getHand().getCards()) {
                    if (card.getManaCost() <= player.getManaAvailable()) {
                        System.out.println(card.toString());

                        String play = getPrompt(
                                player.getName() + " play " + card.getName() + "? (Yes/No) ", 
                                new String[]{"Yes", "yes", "y", "No", "no", "n"});
                        if (play.equals("Yes") || play.equals("yes") || play.equals("y")) {
                            player.getInPlay().add(card);
                            player.useMana(card.getManaCost());
                            toRemove.add(card);
                            
                            /**
                             * On play effects (Strategy Pattern)
                             * New Gameplay loop
                             */ 
                            if(card.getTag().split(":")[0].equals("Mana")) {
                                card.setEffectStrategy(new ManaStrategy());
                                card.getEffect(player, card.getTag());
                                System.out.println("Mana gained!");
                            }
                            else if(card.getTag().split(":")[0].equals("Heal")) {
                                card.setEffectStrategy(new HealStrategy());
                                card.getEffect(player, card.getTag());
                                System.out.println("Health gained!");
                            }
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
