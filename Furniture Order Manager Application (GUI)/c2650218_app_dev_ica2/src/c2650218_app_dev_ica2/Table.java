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
public class Table extends Furniture{
    /**
     * Chair properties
     */
    private int diameter;
    private String base;
    
    /**
     * Table Constructor
     * @param id
     * @param wood
     * @param qty
     * @param diameter
     * @param base
     */
    public Table(int id, char wood, int qty, int diameter, String base) {
        super(id, wood, qty);
        this.diameter = diameter;
        this.base = base;
    }
    
    /**
     * Table getter methods
     * @return 
     */
    public int getDiameter() {
        return diameter;
    }
    
    public String getBaseType() {
        return base;
    }
    
    /**
     * Abstract toString method for Table
     * @return 
     */
    @Override
    public String toString() {
        return("Table Price: " + price);
    }
    
    /**
     * Abstract table method
     * @return 
     */
    @Override
    public int calcUnits() {
        return(diameter * diameter);
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
        
        if(base.equals("Wooden")) {
            total += 40;
        }
        else if(base.equals("Chrome")) {
            total += 50;
        }
        
        total = (calcUnits() * woodPrice) + total;
        
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
            String furnitureType = "Table";
            int furnitureID = id;
            char woodType = wood;
            int quantity = qty;
            boolean armrest = false;
            int diameter = this.diameter;
            String base = this.base;
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
