/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package subscriptionmanager;

/** 
* Modules Imported
*/
import java.util.*;
import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 *
 * @author Daniel
 */
public class subs {
    /** 
     * Properties of subs.java class
     */
    private char pack;
    private int duration;
    private String discount;
    private char term;
    
    /**
     * Constructors used for subs.java class
     * Also setting up the default values for it
     */
    public subs() {
        pack = 'B';
        duration = 1;
        discount = "-";
        term = 'O';
    }
    
    /**
     * Setting values entered by user
     * @param pack
     * @param duration
     * @param discount
     * @param term 
     */
    public subs(char pack, int duration, String discount, char term) {
        this.pack = pack;
        this.duration = duration;
        this.discount = discount;  
        this.term = term;
    }
    
    /**
     * Getter methods used for each property of the subs.java class
     */
    public char getPack() {
        return pack;
    }

    public int getDuration() {
        return duration;
    }

    public String getDiscount() {
        return discount;
    }

    public char getTerm() {
        return term;
    }

    /** Setter methods
     * Setter methods used for each property of the subs.java class
     */
    public void setPack(char pack) {
        this.pack = pack;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setTerm(char term) {
        this.term = term;
    }
    
    /**
     * Class Methods of subs.java
     */
    
    /**
     * calcSubs method is responsible for the calculation of the cost
     * of the subscription.
     * @return 
     */
    public double calcSubs() {
        /**
         * Set up a list of the prices 
         * corresponding to their package and month
         */
        double[] bronze = {6.00, 5.00, 4.00, 3.00};
        double[] silver = {8.00, 7.00, 6.00, 5.00};
        double[] gold = {9.99, 8.99, 7.99, 6.99};
        
        /**
         * Declaring the variables baseCost, cost and index
         * which are to be used in the calculation later
         * the index variables acts as a pointer for the list of prices previously
         */
        double baseCost = 0;
        double cost = 0;
        int index;
        
        /**
         * Switch statement to set up the index/pointer for list of prices, 
         * using duration as a guide
         * Case 1 (if 1 month): index set to 0
         * Case 3 (if 3 months): index set to 1
         * Case 6 (if 6 months): index set to 2
         * Case 12 (if 12 months/1 year): index set to 3
         * default value is index set to 0, which is the cheapest
         */
        switch(duration) {
            case 1:
                index = 0;
                break;
            case 3:
                index = 1;
                break;
            case 6:
                index = 2;
                break;
            case 12:
                index = 3;
                break;
            default:
                index = 0;
        }

        /**
         * Switch statement to point to the correct price in the list of prices 
         * using pack(package) as a guide
         * Case B (Bronze): use the bronze[index] list
         * Case S (Silver): use the silver[index] list
         * Case G (Gold): use the gold[index] list
         * default value is Bronze
         * Sets the cost to which element it points to in the list
         */        
        switch(pack) {
            case 'B':
                cost += bronze[index];
                break;
            case 'S':
                cost += silver[index];
                break;
            case 'G':
                cost += gold[index];
                break;
            default:
                cost += bronze[index];
        }
        
        /**
         * Set base cost as the cost,
         * this way the base cost is stored elsewhere
         * which later to be used to calculate the discount.
         * The discountAmount variable is also then declared
         * to store the amount to be discounted in percent(%).
         */
        baseCost += cost;
        double discountAmount = 0;
        
        /**
         * Gives an additional 5% discount if the term is a One-off payment ('O'),
         * where the duration cannot be for only 1 month as it doesn't make sense
         * giving a discount to One-off payments for only 1 month.
         * Since it's a One-off payment, 
         * the cost has to be multiplied by the duration to get the total cost,
         * which is added to the cost variable.
         */
        if(term == 'O' && duration != 1) {
            cost = baseCost*duration;
            discountAmount += 5.00;
        }
        /**
         * Runs if there is discount code used.
         * This parses the last character of the string from discount
         * and converts the string value into a double,
         * which then can used for calculation.
         * This parsed value is then added into the discountAmount variable 
         * to total up the discount.
         */
        if(!discount.equals("-")) {
            double discountCode = Double.parseDouble(discount.substring(5,6));
            discountAmount += (double)discountCode;
        }
        
        /**
         * Calculates the total cost for the payment
         * Then returns totalCost
         */
        double totalCost = (double)cost - (cost*(discountAmount/100));
        return totalCost;
    }
    
    /**
     * Method for JUST reading a txt file
     * without updating it.
     * @param fileName
     */
    public void readFile(String fileName) {
        Scanner input = null;
        
        /**
         * Try/catch is used to determine whether the file being read exist.
         * Throws FileNotFoundException if it doesn't exist and exits the program.
         */
        try {
            
            input = new Scanner(new File(fileName));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }
        
        /**
         * Formats the data being read into the file reader,
         * using a while loop to read each line in the file
         */
        while (input.hasNext()) {
            String dataDate = input.next();
            char dataPack = input.next(".").charAt(0);
            int dataDuration = input.nextInt();
            String dataCode = input.next();
            char dataTerm = input.next(".").charAt(0);
            int dataCost = input.nextInt();
            String dataName = input.nextLine();
            
            /**
             * Displays the data in the console accroding to this format
             */
            System.out.println(dataDate + "\t" + dataPack + "\t" + dataDuration + "\t" + dataCode + "\t" + dataTerm + "\t" + dataCost + "\t" + dataName);
        }
        
        /**
         * Closing the input scanner
         */
        input.close();        
    }
    
    /**
     * Method for file writing on current.txt
     * @param cost
     * @param name 
     */
    public void writeFile(double cost, String name) { 
        /**
         * Setting PrintWriter and scanner
         */
        PrintWriter output = null;
        Scanner scanner = null;
        
        File write = new File("current.txt");
        
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
            String dataDate = DateHelper.getDate();
            char dataPack = pack;
            int dataDuration = duration;
            String dataCode = discount;
            char dataTerm = term;
            int dataCost = (int)(cost*100);
            String dataName = name;            
            FileWriter fw = new FileWriter(write, true);
            
            
            fw.write(dataDate + "\t" + dataPack + "\t" + dataDuration + "\t" + dataCode + "\t" + dataTerm + "\t" + dataCost + "\t" + dataName + "\n");
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
    
    /**
     * Method for reading ALL data in a file
     * and displaying its summary
     * @param fileName 
     */
    public void readFileSubs(String fileName) {
        /**
         * Initializing variables/counters
         */
        int totalSubs = 0;
        double averageSubs = 0;
        double totalMonthlyFee = 0;
        double averageMonthlyFee = 0;
        double bronze = 0;
        double silver = 0;
        double gold = 0;
        
        /**
         * Initializing counter for each month
         */
        int jan = 0;
        int feb = 0;
        int mar = 0;
        int apr = 0;
        int may = 0;
        int jun = 0;
        int jul = 0;
        int aug = 0;
        int sep = 0;
        int oct = 0;
        int nov = 0;
        int dec = 0;        
        
        Scanner input = null;
        
        /**
         * Try/catch statement to determine whether the file being read exist
         * Throws FileNotFoundException if the file doesn't exist
         */
        try {
            
            input = new Scanner(new File(fileName));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }
        
        /**
         * While loop is used to iterate through the data in the file
         * Using the iterating format below.
         */
        while (input.hasNext()) {
            /** 
             * Reads the date String in the file,
             * also uses 'contain' to check whether a given month is within 
             * the dataDate string.
             * Checks which month is iterated through in the file
             * and increases the counter according to which month it is.
             */
            String dataDate = input.next();
            
            if(dataDate.contains("Jan")) {
                jan += 1;
            }
            else if(dataDate.contains("Feb")) {
                feb += 1;
            }
            else if(dataDate.contains("Mar")) {
                mar += 1;
            }
            else if(dataDate.contains("Apr")) {
               apr += 1;
            }            
            else if(dataDate.contains("May")) {
                may += 1;
            }
            else if(dataDate.contains("Jun")) {
                jun += 1;
            }
            else if(dataDate.contains("Jul")) {
                jul += 1;
            }
            else if(dataDate.contains("Aug")) {
                aug += 1;
            }
            else if(dataDate.contains("Sep")) {
                sep += 1;
            }
            else if(dataDate.contains("Oct")) {
                oct += 1;
            }
            else if(dataDate.contains("Nov")) {
                nov += 1;
            }
            else if(dataDate.contains("Dec")) {
                dec += 1;
            }            
            
            /** 
             * Reads the package char in the file,
             * Checks which package is iterated through in the file
             * and increases the counter according to which package it is.
             */            
            char dataPack = input.next(".").charAt(0);
            if(dataPack == 'B') {
                bronze += 1;
            }
            else if(dataPack == 'S') {
                silver += 1;
            }
            else if(dataPack == 'G') {
                gold += 1;
            }
            
            /**
             * Iterates through the 
             * duration, discount code and term within the file.
             * These data are read for formatting reasons.
             */
            int dataDuration = input.nextInt();
            String dataCode = input.next();
            char dataTerm = input.next(".").charAt(0);
            
            /** 
             * Reads the cost int in the file,
             * Then converts the int cost from the file
             * into a double that is to be printed later.
             */ 
            int dataCost = input.nextInt();
            totalMonthlyFee += (double)dataCost/100;
            
            /**
             * Iterates through the customer name within the file.
             * This data are read for formatting reasons.
             */            
            String dataName = input.nextLine();
            
            /**
             * totalSubs counter increases for each line iterated through from the file
             */
            totalSubs += 1;
        }
        
        /**
         * Closing the input scanner
         */
        input.close(); 
        
        /**
         * Creating a top and bottom boarder for the summary data being displayed
         * to make the display more clear and obvious.
         * Then a series of print statements are used 
         * to construct and display the data on the console.
         */
        String boader = "";
        for(int i = 0; i < 50; i++) {
            boader += "+";
        }
        System.out.println(boader);
        System.out.println("Total number of subscriptions: " + totalSubs);
        
        averageMonthlyFee = totalMonthlyFee/totalSubs;
        System.out.printf("Average monthly subscription fee: £%.2f\n", averageMonthlyFee);
        
        /**
         * The percentage of each type package in a given file
         * is then calculated according to their respective packages.
         */
        System.out.println("\nPercentage of subscriptions: ");
        double bronzePer = (bronze/totalSubs)*100;
        System.out.printf("Bronze: %.1f\n", bronzePer);
        double silverPer = (silver/totalSubs)*100;
        System.out.printf("Silver: %.1f\n", silverPer);
        double goldPer = (gold/totalSubs)*100;
        System.out.printf("Gold: %.1f\n", goldPer);
        
        System.out.println("\nSubscriptions by month: ");
        System.out.println("Jan: " + jan);
        System.out.println("Feb: " + feb);
        System.out.println("Mar: " + mar);
        System.out.println("Apr: " + apr);
        System.out.println("May: " + may);
        System.out.println("Jun: " + jun);
        System.out.println("Jul: " + jul);
        System.out.println("Aug: " + aug);
        System.out.println("Sep: " + sep);
        System.out.println("Oct: " + oct);
        System.out.println("Nov: " + nov);
        System.out.println("Dec: " + dec);
        System.out.println(boader);
        System.out.println();
    }
    
    /**
     * Method for reading data from a specific month in a file
     * and displaying its summary
     * @param fileName
     * @param month 
     */
    public void readFileMonth(String fileName, String month) {
        /**
         * Initializing variables/counters
         */
        int totalSubs = 0;
        double averageSubs = 0;
        double totalMonthlyFee = 0;
        double averageMonthlyFee = 0;
        double bronze = 0;
        double silver = 0;
        double gold = 0;
        
        /**
         * Initializing month counter
         */
        int monthCounter = 0;     
        
        Scanner input = null;
        
        /**
         * Try/catch statement to determine whether the file being read exist
         * Throws FileNotFoundException if the file doesn't exist
         */        
        try {
            
            input = new Scanner(new File(fileName));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        /**
         * While loop is used to iterate through the data in the file
         * Using the iterating format below.
         */        
        while (input.hasNext()) {
            /** 
             * Reads the date String in the file,
             * while using an if statement to check if the month matches.
             * Also uses 'contain' to check whether a given month is within 
             * the dataDate string.
             * Checks which month is iterated through in the file
             * and increases the counter accordingly.
             */          
            String dataDate = input.next();            

            if(dataDate.contains(month)) {
                monthCounter += 1;                
            }           

            /** 
             * Reads the package char in the file,
             * Checks which package is iterated through in the file
             * and increases the counter according to which package it is.
             */            
            char dataPack = input.next(".").charAt(0);
            if(dataDate.contains(month)) {
                if(dataPack == 'B') {
                    bronze += 1;
                }
                else if(dataPack == 'S') {
                    silver += 1;
                }
                else if(dataPack == 'G') {
                    gold += 1;
                }
            }

            /**
             * Iterates through the 
             * duration, discount code and term within the file.
             * These data are read for formatting reasons.
             */            
            int dataDuration = input.nextInt();
            String dataCode = input.next();
            char dataTerm = input.next(".").charAt(0);

            /** 
             * Reads the cost int in the file,
             * Then converts the int cost from the file
             * into a double that is to be printed later.
             */             
            int dataCost = input.nextInt();
            if(dataDate.contains(month)) {
                totalMonthlyFee += (double)dataCost/100;
            }

            /**
             * Iterates through the customer name within the file.
             * This data are read for formatting reasons.
             */                
            String dataName = input.nextLine();
        }
        /**
         * Closing the input scanner
         */
        input.close(); 

        /**
         * Creating a top and bottom boarder for the summary data being displayed
         * to make the display more clear and obvious.
         * Then a series of print statements are used 
         * to construct and display the data on the console.
         */        
        String boader = "";
        for(int i = 0; i < 50; i++) {
            boader += "+";
        }
        System.out.println(boader);
        System.out.println("Total number of subscriptions for " + month + ": " + monthCounter);
        
        averageMonthlyFee = totalMonthlyFee/monthCounter;
        System.out.printf("Average monthly subscription fee: £%.2f\n", averageMonthlyFee);

        /**
         * The percentage of each type package in a given file
         * is then calculated according to their respective packages.
         */        
        System.out.println("\nPercentage of subscriptions: ");
        double bronzePer = (bronze/monthCounter)*100;
        System.out.printf("Bronze: %.1f\n", bronzePer);
        double silverPer = (silver/monthCounter)*100;
        System.out.printf("Silver: %.1f\n", silverPer);
        double goldPer = (gold/monthCounter)*100;
        System.out.printf("Gold: %.1f\n", goldPer);
        
        System.out.println(boader);
        System.out.println();        
    }
    
    /**
     * Method for reading data in a file 
     * following a specific word used to search in a name
     * and displaying a summary for each customer with the included word
     * @param fileName
     * @param search 
     */
    public void readFileSearch(String fileName, String search) {
        Scanner input = null;
        
        /**
         * Try/catch statement to determine whether the file being read exist
         * Throws FileNotFoundException if the file doesn't exist
         */            
        try {
            
            input = new Scanner(new File(fileName));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        /**
         * Formats the data being read into the file reader,
         * using a while loop to read each line in the file
         */        
        while (input.hasNext()) {
            String dataDate = input.next();
            char dataPack = input.next(".").charAt(0);
            int dataDuration = input.nextInt();
            String dataCode = input.next();
            char dataTerm = input.next(".").charAt(0);
            int dataCost = input.nextInt(); 

            /**
             * Using an if statement to check whether the searched word 
             * matches the name in the file by iterating through each line.
             * 
             * The search term is also case insensitive,
             * this is done by converting both the search term and the iterated data to lower case
             * and seeing whether the iterated term 'contains' the search term.
             * 
             * Then the print method from the main class is used
             * to print out/display the summary of each matching searched customer.
             */            
            String dataName = input.nextLine();                        
            if(dataName.toLowerCase().contains(search.toLowerCase())) {
                Main.print(dataName.substring(1), dataDate, dataCode, dataPack, dataDuration, dataTerm, (double)(dataCost/100));
            }            
        }
        System.out.println();
        /**
         * Closing the input scanner
         */
        input.close();         
    }
}
