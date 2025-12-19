package uk.ac.tees.linkedlistica;

// DO  NOT MODIFY THIS FILE - THIS IS NOT GOING TO HELP YOU WITH YOUR ICA


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 * A suite of unit tests for the {@link CircularLinkedList} class.
 */
public class CircularLinkedListTest {
    
    public CircularLinkedListTest() {
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
     * Converts a circular linked list to an array.
     * 
     * @param list  the list to convert
     * @return      the converted list
     */
    private static int[] circularLinkedListToArray(CircularLinkedList list) {
        // Gauge size.
        int n = 0;
        ListNode c = list.head.next;
        while (c != list.head) {
            n++;
            c = c.next;
        }
        // Copy to int array.
        int[] arr = new int[n];
        c = list.head.next;
        int i = 0;
        while (c != list.head) {
            arr[i++] = c.data;
            c = c.next;
        }
        return arr; // Return result.
    }

    /**
     * Test of isEmpty method, of class CircularLinkedList.
     */
    
    @Test
    public void testIsEmpty() {
        // Create instance.
        CircularLinkedList instance = new CircularLinkedList(
                new int[] {2, 4, 6, 7, 8});
        boolean empty = new CircularLinkedList(new int[] {}).isEmpty();
        assertTrue("True should return for empty lists.", empty);
        boolean notEmpty = instance.isEmpty();
        assertFalse("False should return for non-empty lists.", notEmpty);
    }

    /**
     * Test of toString method, of class CircularLinkedList.
     */
    
    @Test
    public void testToString() {
        // Create instance.
        CircularLinkedList instance = new CircularLinkedList(
                new int[] {3, 7, 9, 13, 12});
        assertEquals("List should be correctly turned into a string.", 
                "{3, 7, 9, 13, 12}", instance.toString());
    }
    /**
     * Test of deleteMultiplesOfThree method, of class CircularLinkedList.
     */
    
    @Test
    public void testDeleteMultiplesOfThree() {
        // Create instance.
        CircularLinkedList instance = new CircularLinkedList(
                new int[] {3, 7, 9, 13, 12});
        int deleted = instance.deleteMultiplesOfThree();
        assertEquals("Number of items deleted should be returned.", 3, deleted);
        // Compare to expected.
        assertArrayEquals("Middle node should be correctly deleted.", 
                new int[] {7, 13}, circularLinkedListToArray(instance));
    }

    /**
     * Test of addAfterPos method, of class CircularLinkedList.
     */
    
    @Test
    public void testAddAfterPos() {
        // Create instance, insert 3 and 6 after first and second index.
        CircularLinkedList instance = new CircularLinkedList(
                new int[] {2, 4, 6});
        instance.addAfterPos(3, 1);
        instance.addAfterPos(6, 2);
        // Compare to expected.
        assertArrayEquals("Items should be correctly added.", 
                new int[] {2, 4, 3, 6, 6}, circularLinkedListToArray(instance));
    }
    /**
     * Test of deleteMiddle method, of class CircularLinkedList.
     */
    
    @Test
    public void testDeleteMiddle() {
        // Create instance.
        CircularLinkedList instance = new CircularLinkedList(
                new int[] {2, 4, 6, 7, 8});
        boolean success = instance.deleteMiddle();
        assertTrue("True should return on successful delete.", success);
        boolean failure = new CircularLinkedList(new int[] {}).deleteMiddle();
        assertFalse("False should return on unsuccessful delete.", failure);
        // Compare to expected.
        assertArrayEquals("Middle node should be correctly deleted.", 
                new int[] {2, 4, 7, 8}, circularLinkedListToArray(instance));
    }

    
}

    