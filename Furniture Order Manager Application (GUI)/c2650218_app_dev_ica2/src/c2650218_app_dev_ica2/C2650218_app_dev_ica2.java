/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package c2650218_app_dev_ica2;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Daniel
 */
public class C2650218_app_dev_ica2 extends JFrame{
    
    /**
     * Methods
     */
    static void menu() {
        /**
         * Making main menu frame and adding panels and buttons
         */
        myFrame main = new myFrame(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        /**
         * Clears data in temporary.txt to make it a clean slate when STARTING program
         */
        String filePath = "Data/temporary.txt";
        
        try {
            FileWriter writer = new FileWriter(filePath, false);
            
            // Close the writer to clear the file
            writer.close();
            
            System.out.println("File cleared successfully.");
        }
        catch (IOException e) {
            System.out.println("An error occurred while clearing the file: " + e.getMessage());
        }          
        
        /**
         * Run main menu method (Including JFrame data)
         */
        menu();         
    }
    
}
