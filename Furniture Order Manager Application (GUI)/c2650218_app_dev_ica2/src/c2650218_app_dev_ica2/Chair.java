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
public class Chair extends Furniture{
    /**
     * Chair properties
     */
    private boolean armRest;
    
    /**
     * Chair Constructor
     * @param id
     * @param wood
     * @param qty
     * @param armRest
     */
    public Chair(int id, char wood, int qty, boolean armRest) {
        super(id, wood, qty);
        this.armRest = armRest;
    }
    
    /**
     * Chair getter methods
     * @return 
     */
    public boolean getArmRest() {
        return armRest;
    }
    
    /**
     * Abstract toString method for Chair
     * @return 
     */
    @Override
    public String toString() {
        return("Chair Price: " + price);
    }
    
    /**
     * Abstract chair method
     * @return 
     */
    @Override
    public int calcUnits() {
        int total = 1500;
        
        if(armRest == true) {
            total += 250;
        }
        
        return total;
    }
    
    @Override
    public double calcPrice() {
        double woodPrice = 0;
        
        if(wood == 'o') {
            woodPrice = 0.05;
        }
        
        if(wood == 'w') {
            woodPrice = 0.03;
        }
        
        return(calcUnits() * woodPrice);
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
            String furnitureType = "Chair";
            int furnitureID = id;
            char woodType = wood;
            int quantity = qty;
            boolean armrest = armRest;
            int diameter = 0;
            String base = "-";
            int width = 0;
            int depth = 0;
            int numDrawers = 0;
            
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
