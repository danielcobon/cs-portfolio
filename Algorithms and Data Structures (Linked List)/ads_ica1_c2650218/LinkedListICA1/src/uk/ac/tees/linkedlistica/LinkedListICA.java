package uk.ac.tees.linkedlistica;

/**
 * The project main class (intentionally empty).
 * @author Daniel Cyril Obon (C2650218@tees.ac.uk)
 */
public class LinkedListICA {

    /**
     * The entry point for your program.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* This main method is intentionally left blank. You should test your
         * code using the unit tests provided, and/or write your own if you're
         * feeling up to it. You may also write code here to test your ADTs.
         */
        
        // SinglyLinkedList debug = new SinglyLinkedList(new int[] {0,1,2,3,4,5});
        DoublyLinkedList debug = new DoublyLinkedList(new int[] {0,1,2,3,4,5});
        // CircularLinkedList debug = new CircularLinkedList(new int[] {0,1,2,3,4,5});
        System.out.println(debug.toString());
         
        // ~~~~~~~~~~~~~~~~~~~~ Singly Linked List ~~~~~~~~~~~~~~~~~~~~
        
//        debug.addFirst(0);
//        System.out.println(debug.toString());
        
//        System.out.println(debug.getFirst());
//        System.out.println();
//        System.out.println(debug.getAtPos(1));
//        System.out.println();
//        
//        debug.addLast(100);
//        System.out.println(debug.toString());
//
//        debug.addAfterPos(69, 0);
//        System.out.println(debug.toString());        

//        debug.deleteWithValue(100);
//        System.out.println(debug.toString());
        
//         System.out.println(debug.cloneLinkedList());

        // ~~~~~~~~~~~~~~~~~~~~ Doubly Linked List ~~~~~~~~~~~~~~~~~~~~
//        System.out.println(debug.getSize());
        
//        System.out.println(debug.getLast());
          // Test Again!!
//        System.out.println(debug.deleteAllNodesWithValue(3));
//        System.out.println(debug.toString());
        
//        System.out.println(debug.deleteAtPos(0));
//        System.out.println(debug.toString());
        
//        debug.reverseOrderLinkedList();
//        System.out.println(debug.toString());

        // ~~~~~~~~~~~~~~~~~~~~ Circular Linked List ~~~~~~~~~~~~~~~~~~~~  
//        debug.deleteMultiplesOfThree();
//        System.out.println(debug.toString());
        
//        debug.addAfterPos(100,0);
//        System.out.println(debug.toString());

//        debug.addAtPos(100,0);
//        System.out.println(debug.toString());
        
//        debug.deleteMiddle();
//        System.out.println(debug.toString());

//        System.out.println(debug.sum());
        
    }
    
}
