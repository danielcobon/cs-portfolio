package uk.ac.tees.linkedlistica;

/**
 * Represents a singly linked list.
 * @author Daniel Cyril Obon (c2650218@tees.ac.uk)
 */
public class DoublyLinkedList {
    
    /**
     * Stores the first node in the list.
     */
    public DoublyLinkedListNode head;
      
    /**
     * Creates a new doubly linked list from an existing array.
     * @param data  the array to create the new linked list from
     */
    public DoublyLinkedList(int[] data) {
        
        // DO NOT MODIFY THIS CONSTRUCTOR.
           
        for (int i = data.length - 1; i >= 0; i--) {
            DoublyLinkedListNode n = new DoublyLinkedListNode(data[i], head, 
                    null);
            if (head != null) {
                head.prev = n;
            }
            head = n;
        }
    }
    
    /**
     * Gets the length of the doubly linked list.
     * @return  the length
     */
    public int getSize() {
        //ADD YOUR ANSWER HERE
        DoublyLinkedListNode current = head;
        int count = 0;

        if(current != null) {
            while(current != null) {
                current = current.next;
                count ++;
            }
        }
        return count;
    }
    
    /**
     * Gets the last item in the doubly linked list, or -999 if not found.
     * @return  the last item, or -999 if empty
     */
    public int getLast() {
        //ADD YOUR ANSWER HERE
        if(head != null) {
            DoublyLinkedListNode current = head;
            while(current.next != null) {
                current = current.next;
            }
            
            return(current.data);
        }
        // Check if List is empty
        else {
            System.out.println("Doubly Linked List is empty, cannot get last element...");
            return -999;   
        }    
    }
    
    /**
     * Deletes all nodes with the given value, returning the number of nodes
     * deleted.
     * @param obj   the value
     * @return      the number of nodes deleted
     */
    public int deleteAllNodesWithValue(int obj) {
        //ADD YOUR ANSWER HERE
        DoublyLinkedListNode current = head;
        int count = 0;
        
        // Check if List is empty
        if(head == null) {
            System.out.println("Doubly Linked List is empty, nothing to delete...");
            return count;
        }
        
        while(current != null) {
            if(current.data == obj) {
                // Deletion for first node
                if(current.next != null && current.prev == null) {
                    head = current.next;
                    current.next = null;
                    head.prev = null;
                }
                
                // Deletion for last node
                if(current.next == null && current.prev != null) {
                    current.prev.next = null;
                }
                
                // Standard deletion for all nodes
                if(current.next != null) {
                    current.next.prev = current.prev;
                }
                
                if(current.prev != null) {
                    current.prev.next = current.next;
                }
                
                current = current.next;                
                count ++;
            }
            if(current != null) {
                current = current.next;
            }
        }
        
        return count;        
    }
    
    /**
     * Deletes the node in the list at the specified index.
     * @param index the index
     * @return      true if successful, otherwise false
     */
    public boolean deleteAtPos(int index) {
        //ADD YOUR ANSWER HERE
        // Delete first node
        if(index == 0) {
            DoublyLinkedListNode toDelete = head;
            head = toDelete.next;
            toDelete.next = null;
            head.prev = null;
            toDelete = null;
            
            return true;
        }
        
        DoublyLinkedListNode current = head;
        int count = 0;
        
        // Stop at node before toDelete
        while(count != index-1 && current != null) {
            current = current.next;
            count ++;
        }
        
        if(current == null) {
            System.out.println("Index does not exist, nothing to delete...");
            return false;
        }
        // Standard deletion
        else {
            DoublyLinkedListNode toDelete = current.next;
            DoublyLinkedListNode success = toDelete.next;
            
            current.next = toDelete.next;
            
            toDelete.prev = null;
            toDelete.next = null;
            
            if(success != null) {
                success.prev = current;
            }
            
            toDelete = null;            
            return true;
        }
    }
    
    /**
     * Reverses the order of this doubly linked list.
     */
    public void reverseOrderLinkedList() {
        //ADD YOUR ANSWER HERE
        if(head == null) {
            System.out.println("List is empty, list cannot be reverse...");
        }        
        
        DoublyLinkedListNode current = head;
        DoublyLinkedListNode success = null;
        
        // Iterate through list
        while(current != null) {
            success = current.prev;
            
            // Reverse addresses
            current.prev = current.next;
            current.next = success;
            
            // Move pointer to the next data to be reversed
            current = current.prev;
        }
        
        // Set new head
        if(success != null) {
            head = success.prev;
        }
         
    }
    
    /**
     * Appends a singly linked list to this doubly linked list.
     * @param list  the singly linked list to append
     */
    public void appendSinglyLinkedList(SinglyLinkedList list) {
       //ADD YOUR ANSWER HERE
       DoublyLinkedList combine = new DoublyLinkedList(new int[] {});
       
       ListNode singleCurrent = list.head;
       DoublyLinkedListNode combineCurrent = combine.head;
       
    }
    
    // ---------- SELF TOSTRING METHOD ----------
    @Override
    public String toString()
    {
        // YOUR CODE GOES HERE
        String output = "";
        DoublyLinkedListNode current = head;
        if(current != null) {
            while(current.prev != null)
                current = current.prev;
            while(current != null){
                output = output + " " + current.data;
                current = current.next;
            }
        }
        return output;
    }    
}
