package uk.ac.tees.linkedlistica;

/**
 * Represents a singly linked list.
 * @author Daniel Cyril Obon (c2650218@tees.ac.uk)
 */
public class SinglyLinkedList {
    
    /**
     * Stores the first node in the list.
     */
    public ListNode head;
    
    /**
     * Creates a new singly linked list from an existing array.
     * @param data  the array to create the new linked list from
     */
    public SinglyLinkedList(int[] data) {
        
        // DO NOT MODIFY THIS CONSTRUCTOR.
        
        // Populate list.
        for (int i = data.length - 1; i >= 0; i--) {
            head = new ListNode(data[i], head);
        }
    }
    
    /**
     * Creates a new, empty singly linked list.
     */
    public SinglyLinkedList() {
        
        // DO NOT MODIFY THIS CONSTRUCTOR.
        
        this(new int[] {});
    }
    
    /**
     * Adds an item at the front of the list.
     * @param val   the item to add
     */
    public void addFirst(int val) {
      //ADD YOUR ANSWER HERE
      ListNode newNode = new ListNode(val);
        
      newNode.next = head;
      head = newNode;      
    }
    
    /**
     * Returns the first item in the list.
     * @return val  the first item in the list, or -999 if empty
     */
    public int getFirst() {
        //ADD YOUR ANSWER HERE      
        if(head == null) {
            System.out.println("List is empty, can't get first element...");
            return -999;
        }
        
        return head.data;        
    }
    
    /**
     * Gets the item at the specified index in the list.
     * @param index the index
     * @return      the item, or -999 if not found
     */
    public int getAtPos(int index) {
        //ADD YOUR ANSWER HERE        
        if(head == null) {
            System.out.println("List is empty, no element to retrieve...");
            return -1;
        }
        
        ListNode current = head;
        int counter = 0;

        while(current != null) {
            if(counter == index) {
                return current.data;
            }
            current = current.next;
            counter ++;
        }
        
        System.out.println("Index does not exist...");
        return -1;
    }
    
    /**
     * Adds an item to the end of the list.
     * @param obj   the item
     */
    public void addLast(int obj) {
        //ADD YOUR ANSWER HERE
      ListNode newNode = new ListNode(obj);
      
      ListNode current = head;
      
      if(head == null) {
        newNode.next = head;
        head = newNode;
        return;
      }
      
      while(current.next != null) {
          current = current.next;
      }
      
      newNode.next = null;
      current.next = newNode;           
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
        int counter = 0;
      
        while(current.next != null) {
            if(counter == index) {
                ListNode newNode =  new ListNode(obj, current.next);
                current.next = newNode;
            
                return true;
            }
            current = current.next;
            counter ++;
        }
      
        System.out.println("Index does not exist, cannot add element... ");
        return false;
    }
    
    /**
     * Deletes the first item in the list with the given value.
     * @param obj   the value to seek
     * @return      true if an item was deleted, otherwise false
     */
    public boolean deleteWithValue(int obj) {
        //ADD YOUR ANSWER HERE
        ListNode current = head;
        int count = 0;
        
        if(current.data == obj) {
            head = current.next;
            return true;
        }
        
        while(current.next != null) {
            if(current.next.data == obj) {
                ListNode connectNode = current.next.next;
                current.next = connectNode;

                return true;
            }
            current = current.next;
            count ++;
        }
        
        System.out.println("Value does not exist, nothing to delete...");
        return false;
    }   
    
    
    /**
     * Returns a copy of this linked list.
     * @return  the copy
     */
    public SinglyLinkedList cloneLinkedList() {
        //ADD YOUR ANSWER HERE
        SinglyLinkedList cloneList = new SinglyLinkedList(new int[] {});
        ListNode current = head;
        ListNode cloneCurrent = cloneList.head;
        
        if(current == null) {
            return cloneList;
        }
        
        if(current == head) {
            ListNode newNode = new ListNode(current.data);
        
            newNode.next = cloneList.head;
            cloneList.head = newNode;
            
            current = current.next;
            cloneCurrent = cloneList.head;
        }

        while(current != null) {
            ListNode newNode = new ListNode(current.data);  

            newNode.next = null;
            cloneCurrent.next = newNode;

            current = current.next;
            cloneCurrent = cloneCurrent.next;
            
        }
        
        return cloneList;
    }
    
    // ---------- SELF TOSTRING METHOD ----------
    // precondition: The last link node has a null link.
    // postcondition: Returns a string formed by concatenating the 
    //	data fields of all list nodes.
    @Override
    public String toString()
    {
        // YOUR CODE GOES HERE
        String output = "";
        ListNode current = head;
                
        if(head == null) {
            return "The list is empty...";            
        }
        else {            
            while(current != null) {
                output += current.data + "\n";
                current = current.next;
            }
            return output;
        }
    }    
}
