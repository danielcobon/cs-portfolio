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

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Steven Mead
 * @author Chris Curry
 */
public class Hand extends CardHandler{

    public Hand() {
        super();
    }

    public ArrayList<BeastCard> getCards() {
        return this.cards;
    }
    
    @Override
    public void add(BeastCard card) {
        this.cards.add(card);
        this.sort();
    }
    
    public void remove(BeastCard card) {
        this.cards.remove(card);
    }
    
    public void removeAll(ArrayList<BeastCard> cards) {
        this.cards.removeAll(cards);
    }
    
    public void sort() {
        Collections.sort(this.cards);
    }

    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        for (int i=0; i<this.count(); i++) {
            sb.append("+-------+ ");
        }
        sb.append("\n");
        
        for (BeastCard card : this.cards) {
            sb.append(String.format("|%7d| ", card.getManaCost()));
        }
        sb.append("\n");
        
        for (BeastCard card : this.cards) {
            sb.append(String.format("|  %-5s| ", card.getId()));
        }
        sb.append("\n");
        
        for (int i=0; i<this.count(); i++) {
            sb.append("|       | ");
        }
        sb.append("\n");
        
        for (BeastCard card : this.cards) {
            sb.append(String.format("|%-2d %4d| ", card.getAttack(), card.getHealth()));
        }
        sb.append("\n");
        
        for (int i=0; i<this.count(); i++) {
            sb.append("+-------+ ");
        }
        sb.append("\n");
        
        return sb.toString();
    }
}
