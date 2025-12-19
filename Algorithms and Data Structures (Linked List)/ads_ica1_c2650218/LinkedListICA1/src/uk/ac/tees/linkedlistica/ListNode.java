package uk.ac.tees.linkedlistica;

// DO NOT MODIFY THIS FILE.

/**
 * Represents a node in a linked list.
 */
public class ListNode {
    
    /**
     * Stores the data for the node (an integer value).
     */
    int data;

    /**
     * Stores the link to the next node.
     */
    ListNode next;

    /**
     * Creates a new empty node.
     */
    public ListNode() {
        data = 0;
        next = null;
    }

    /**
     * Creates a new node with `data` stored in it.
     * @param data  the data to store in the new node
     */
    public ListNode(int data) {
        this.data = data;
        next = null;
    }

    /**
     * Creates a new node storing `data` and linked to `next`.
     * @param data  the data to store in the new node
     * @param next  the next node in the list
     */
    public ListNode(int data, ListNode next) {
        this.data = data;
        this.next = next;
    }
}