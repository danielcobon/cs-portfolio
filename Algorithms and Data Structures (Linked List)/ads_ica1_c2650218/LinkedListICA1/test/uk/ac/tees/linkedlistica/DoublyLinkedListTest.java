package uk.ac.tees.linkedlistica;

// DO  NOT MODIFY THIS FILE - THIS IS NOT GOING TO HELP YOU WITH YOUR ICA


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A suite of unit tests for the {@link DoublyLinkedList} class.
 */
public class DoublyLinkedListTest {
    
    public DoublyLinkedListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
        
    /**
     * Converts a doubly linked list to an array.
     * 
     * @param list  the list to convert
     * @return      the converted list
     */
    private static int[] doublyLinkedListToArray(DoublyLinkedList list) {
        // Gauge size.
        int n = 0;
        DoublyLinkedListNode c = list.head;
        while (c != null) {
            n++;
            c = c.next;
        }
        // Copy to int array.
        int[] arr = new int[n];
        c = list.head;
        int i = 0;
        while (c != null) {
            arr[i++] = c.data;
            c = c.next;
        }
        return arr; // Return result.
    }

    /**
     * Test of getSize method, of class DoublyLinkedList.
     */
    @Test
    public void testGetSize() {
        // Create instance of size 3.
        DoublyLinkedList instance = new DoublyLinkedList(new int[] {2, 4, 6});
        // Compare to expected.
        assertEquals("List size should be returned correctly.", 3, 
                instance.getSize());
    }

    /**
     * Test of getLast method, of class DoublyLinkedList.
     */
    @Test
    public void testGetLast() {
        // Create instance.
        DoublyLinkedList instance = new DoublyLinkedList(new int[] {7, 3, 6});
        // Compare to expected.
        assertEquals("Last item should be retrieved.", 6, instance.getLast());
    }
     /**
     * Test of deleteAllNodesWithValue method, of class DoublyLinkedList.
     */
    @Test
    public void testDeleteAllNodesWithValue() {
        // Create instance, delete first node containing '3'.
        DoublyLinkedList instance = new DoublyLinkedList(new int[] {8, 3, 6, 3});
        int firstPass = instance.deleteAllNodesWithValue(3);
        assertEquals("Number of nodes deleted should be returned.", 2, 
                firstPass);
        int secondPass = instance.deleteAllNodesWithValue(6);
        assertEquals("Number of nodes deleted should be returned.", 1, 
                secondPass);
        int nullPass = instance.deleteAllNodesWithValue(99);
        assertEquals("Number of nodes deleted should be returned.", 0, 
                nullPass);
        // Compare to expected.
        assertArrayEquals("Items should be correctly deleted.", 
                new int[] {8}, doublyLinkedListToArray(instance));
    }

    /**
     * Test of deleteAtPos method, of class DoublyLinkedList.
     */
    @Test
    public void testDeleteAtPos() {
        // Create instance.
        DoublyLinkedList instance = new DoublyLinkedList(new int[] {7, 2, 6, 3});
        boolean success = instance.deleteAtPos(1);
        assertTrue("True should return on successful delete.", success);
        boolean failure = instance.deleteAtPos(99);
        assertFalse("False should return on unsuccessful delete.", failure);
        // Compare to expected.
        assertArrayEquals("List should be correctly reversed.", 
                new int[] {7, 6, 3}, doublyLinkedListToArray(instance));
    }


}