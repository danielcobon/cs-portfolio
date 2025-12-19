package uk.ac.tees.linkedlistica;

/**
 * Represents a circular linked list.
 * @author Daniel Cyril Obon (c2650218@tees.ac.uk)
 */
public class CircularLinkedList {
    
    /**
     * Stores the first node in the list. Should always be a sentinel node.
     */
    public ListNode head;
    
    /**
     * Creates a new circular linked list from an existing array.
     *
     * @param data  the array to create the new linked list from
     */
    public CircularLinkedList(int[] data) {
        
        // DO NOT MODIFY THIS CONSTRUCTOR.
        
        // The head is a sentinel node.
        head = new ListNode(-999, null);
        head.next = head; // Complete the circle.
        
        // Populate list.
        ListNode n = head; // The last item in the circle.
        for (int i = 0; i < data.length; i++) {
            n.next = new ListNode(data[i], head);
            n = n.next;
        }
    }

    /**
     * Gets whether or not the list is empty.
     * @return  true if list is empty, otherwise false
     */
    public boolean isEmpty() {
        //ADD YOUR ANSWER HERE
        if(head.next == head) {
            System.out.println("List is empty...");
            return true;
        }
        else {
            System.out.println("List is not empty.");
            return false;
        }
    }

    /**
     * Returns the list as a string of the form "{item1, item2, ...}"
     * @return  the resulting string
     */
    @Override  //you need the @Override instruction. Make sure you uncomment it when testing your methods.
    public String toString() {
        //ADD YOUR ANSWER HERE
        ListNode current = head.next;
        String output = "{";
        while(current != head){
            output = output + current.data;
            current = current.next;
            
            if(current != head) {
                output += ", ";
            }
        }
        output += "}";
        return output;        
    }
    
    /**
     * Deletes any node containing a value that is a multiple of three.
     * @return 
     */
    public int deleteMultiplesOfThree() {
        //ADD YOUR ANSWER HERE
        if(head.next == head) {
            System.out.println("List is empty, nothing to delete...");
            return 0;
        }
        
        ListNode current = head;
        int count = 0;

        while(current.next != head) {
            if(current.next.data % 3 == 0 && current.next.data != 0) {
                ListNode toDelete = current.next;
                current.next = toDelete.next;
                
                current = current.next;
                count ++;
            }
            current = current.next;
            
            if(current == head) {
                return count;
            }    
        }
        
        return count;
    }
    
    /**
     * Adds an item after a specified index in the list.
     * @param obj   the item to add
     * @param index the index
     * @return      true if successful, otherwise false
     */
    public boolean addAfterPos(int obj, int index) {
        //ADD YOUR ANSWER HERE
        ListNode current = head;
        int count = 0;
       
        while(count != index + 1) {
           current = current.next;
           count ++;
           
           if(current == head) {
               System.out.println("Index does not exist, cannot add element...");
               return false;
           }
        }
        
        ListNode newNode = new ListNode(obj);
        newNode.next = current.next;
        current.next = newNode;
        return true;
    }
    
    /**
     * Deletes the middle element from the list, rounding the index down in case
     * the list has an even number of items.
     * @return  true on successful delete, otherwise false
     */
    public boolean deleteMiddle() {
        //ADD YOUR ANSWER HERE
        ListNode current = head;
        int count = 0;
        int mid = 0;
        
        if(head.next == head) {
            System.out.println("List is empty, cannot delete middle...");
            return false;
        }
        
        while(current.next != head) {
            current = current.next;
            count ++;
        }

        mid = count/2;
        System.out.println(mid);
        
        current = head;
        int countMid = 0;

        while(current.next != head) {
            if(countMid == mid ) {
                ListNode toDelete = current.next;
                current.next = toDelete.next;
                
                current = current.next;
            }
            current = current.next;
            countMid ++;
            
            if(current == head) {
                return false;
            }    
        }
        
        return true;
        
        
    }
    
    /**
     * Adds a piece of data at the specified index.
     * @param obj   the data
     * @param index the index
     * @return      true on successful insert, otherwise false
     */
    public boolean addAtPos(int obj, int index) {
       //ADD YOUR ANSWER HERE
        ListNode current = head;
        int count = 0;
       
        while(count != index) {
           current = current.next;
           count ++;
           
           if(current == head) {
               System.out.println("Index does not exist, cannot add element...");
               return false;
           }
        }
        
        ListNode newNode = new ListNode(obj);
        newNode.next = current.next;
        current.next = newNode;
        return true;       
    }
    
    /**
     * Returns the sum of all integers in the list.
     * @return  the sum
     */
    public int sum() {
        //ADD YOUR ANSWER HERE
        ListNode current = head;
        int total = 0;
       
        if(head.next == head) {
           System.out.println("List is empty, there is no sum...");
           return total;
        }
       
        while(current.next != head) {
           total += current.next.data;
           
           current = current.next;
        }
        return total;
    }
    
}
