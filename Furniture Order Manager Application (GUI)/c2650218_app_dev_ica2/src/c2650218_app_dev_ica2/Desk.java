/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package c2650218_app_dev_ica2;
import java.util.*;
import java.io.*;

/**
 *
 * @author Daniel
 */
public class Desk extends Furniture{
    /**
     * Chair properties
     */
    private int width;
    private int depth;
    private int numDrawers;
    
    /**
     * Desk Constructor
     * @param id
     * @param wood
     * @param qty
     * @param width
     * @param depth
     * @param numDrawers 
     */
    public Desk(int id, char wood, int qty, int width, int depth, int numDrawers) {
        super(id, wood, qty);
        this.width = width;
        this.depth = depth;
        this.numDrawers = numDrawers;
    }
    
    /**
     * Desk getter methods
     * @return 
     */
    public int getWidth() {
        return width;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public int getNumDrawers () {
        return numDrawers;
    }
       
    /**
     * Abstract toString method for Desk
     * @return 
     */
    @Override
    public String toString() {
        return("Desk Price: " + price);
    }
    
    /**
     * Abstract desk method
     * calcUnits is not used for the calculation of desk
     * @return 
     */
    @Override
    public int calcUnits() {
        return 0;
    } 
    
    @Override
    public double calcPrice() {
        double total = 0;
        double woodPrice = 0;
        
        if(wood == 'o') {
            woodPrice = 0.05;
        }        
        else if(wood == 'w') {
            woodPrice = 0.03;
        }
        
        total = ((85 + width + depth) * 14) + ((depth * width) * woodPrice) + (this.numDrawers * 7.25);
                
        return total;
    }
    
    /**
     * Writes file into temporary.txt
     */    
    public void writeFile() { 
        /**
         * Setting PrintWriter and scanner
         */
        PrintWriter output = null;
        Scanner scanner = null;
        
        File write = new File("Data/temporary.txt");
        
        /**
         * Try/catch is used to determine whether the file being read exist.
         * Throws FileNotFoundException if it doesn't exist and exits the program.
         * Throws IOException  when there's a problem creating/writing the file.
         */
        try {
            /**
             * Formats the data that is to be written into the file,
             * using the arguments entered and get methods.
             */
            String furnitureType = "Desk";
            int furnitureID = id;
            char woodType = wood;
            int quantity = qty;
            boolean armrest = false;
            int diameter = 0;
            String base = "-";
            int width = this.width;
            int depth = this.depth;
            int numDrawers = this.numDrawers;
            
            String price = String.format("%.2f", calcPrice());
            FileWriter fw = new FileWriter(write, true);
            
            
            fw.write(furnitureType + "\t" + furnitureID + "\t" + woodType + "\t" + qty + "\t" + armrest + "\t" + diameter + "\t" + base + "\t" + width + "\t" + depth + "\t"  + numDrawers +"\t" + price + "\n");
            fw.close();
        }
        catch(FileNotFoundException e) {
                System.out.println("Error: File does not exist, closing program");
                System.exit(0);
        }        
        catch (IOException ex) {
            System.out.println("Error - problem creating the file! Program closing");
            System.exit(0);
        }
    }    
}
