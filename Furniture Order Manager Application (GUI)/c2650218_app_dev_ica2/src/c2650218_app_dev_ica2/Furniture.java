/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package c2650218_app_dev_ica2;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Daniel
 */
public abstract class Furniture {
    /**
     * Properties
     */
    protected int id;
    protected char wood;
    protected double price;
    protected int qty;
    protected Image icon;
    
    /**
     * Default Constructor
     */
    public Furniture() {
        id = 0;
        wood = 'w';
        qty = 1;        
    }
    
    /**
     * User entered constructor
     * @param id
     * @param wood
     * @param quantity 
     */
    public Furniture(int id, char wood, int quantity) {
        this.id = id;
        this.wood = wood;
        this.qty = quantity;
    }
    
    /**
     * Getter methods
     * @return 
     */
    public int getID() {
        return id;
    }
    public double getItemPrice() {
        return price;
    }
    
    public double getTotalPrice() {
        /**
         * This method is a placeholder
         */
        return price;
    }
    
    public char getTypeOfWood() {
        return wood;
    }
    
    public int getQuantity() {
        return qty;
    }
    
    public Image getImage() {
        return icon;
    }
    
    /**
     * Setter methods
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.qty = quantity;
    }
    
    /**
     * Abstract toString method
     * @return 
     */
    @Override
    public abstract String toString();
    
    /**
     * Abstract method for furniture
     * @return 
     */
    public abstract int calcUnits();
    
    public abstract double calcPrice();
    
    /**
     * Use to check if panels have images in them
     * @param panel
     * @return 
     */
    public static boolean hasImage(JPanel panel) {
        Component[] components = panel.getComponents();
        
        for (Component component : components) {
            if (component instanceof JLabel) {
                Icon icon = ((JLabel) component).getIcon();
                
                if (icon instanceof ImageIcon) {
                    return true;
                }
            }
        }
        
        /**
         * Returns false if there is no image
         */
        return false;    
    }
    
    /**
     * Reads data in temporary.txt
     */
    public void readFile() {
        File file = new File("Data/temporary.txt");

        try {
            Scanner scanner = new Scanner(file);
            double totalPrice = 0.0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                String priceStr = parts[10];
                double price = Double.parseDouble(priceStr.replace("n", ""));
                totalPrice += price;
            }

            System.out.println("Total Price: " + totalPrice);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File does not exist.");
        }
    }    
}
