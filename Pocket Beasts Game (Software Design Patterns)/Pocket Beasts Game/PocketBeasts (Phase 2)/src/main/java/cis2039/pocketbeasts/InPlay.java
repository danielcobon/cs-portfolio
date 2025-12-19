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

/**
 *
 * @author Steven Mead
 * @author Chris Curry
 */
public class InPlay extends CardHandler{

    public InPlay() {
        super();
    }

    public ArrayList<BeastCard> getCards() {
        return cards;
    }
    
    public BeastCard getCard(int index) {
        return cards.get(index);
    }
    
    public void remove(BeastCard card) {
        this.cards.remove(card);
    }
    
    public void removeAll(ArrayList<BeastCard> cards) {
        this.cards.removeAll(cards);
    }
    
}
