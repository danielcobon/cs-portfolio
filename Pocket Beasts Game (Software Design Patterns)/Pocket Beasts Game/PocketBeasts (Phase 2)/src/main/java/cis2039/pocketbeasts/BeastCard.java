/*
 * This file is part of PocketBeasts.
 *
 * PocketBeasts is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PocketBeasts is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */
package cis2039.pocketbeasts;

/**
 *
 * @author Steven Mead
 * @author Chris Curry
 */
public class BeastCard implements Comparable<BeastCard> {

    private final String id;
    private final String name;
    private final int manaCost;
    private final int attack;
    private int health;
    
    private String tag;
    private EffectStrategy e;
    
    public BeastCard(String id, String name, int manaCost, int attack, int health, String tag) {
        this.id = id;
        this.name = name;
        this.manaCost = manaCost;
        this.attack = attack;
        this.health = health;
        this.tag = tag;
    }
    
    public BeastCard(BeastCard card) {
        this.id = card.id;
        this.name = card.name;
        this.manaCost = card.manaCost;
        this.attack = card.attack;
        this.health = card.health;
        this.tag = card.tag;
    }

    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public int getManaCost() {
        return this.manaCost;
    }
    
    public int getAttack() {
        return this.attack;
    }

    public int getHealth() {
        return this.health;
    }
    
    public void damage(int amount) {
        this.health -= amount;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    // Getter and setter methods
    public void setEffectStrategy(EffectStrategy e) {
        this.e = e;
    }
    
    // Abstract method to be overidden
    public void getEffect(Player player, String tag) {
        e.getEffect(player, tag);
    }

    // Updated to show the effect tag of Beast Cards
    @Override
    public String toString() {
        return this.name + " (" + this.id + ") Mana Cost/" + this.manaCost + 
                " Attack/" + this.attack + " Health/" + this.health + " Effect/" + this.tag;
    }

    @Override
    public int compareTo(BeastCard o) {
        return Integer.compare(this.getManaCost(), o.getManaCost());
    }
}
