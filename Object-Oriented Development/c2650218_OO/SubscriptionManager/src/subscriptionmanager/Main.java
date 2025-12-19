/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Daniel Cyril Obon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    /** 
    * Methods
    */
    static void option() {
        /** 
        * Setting up scanner and displaying instructions for user
        */
        Scanner input = new Scanner(System.in);        
        System.out.println("1. Enter new Subscription\n" +
                           "2. Display Summary of subscriptions\n" +
                           "3. Display Summary of subscription for selected Month\n" +
                           "4. Find and display subscription\n" +
                           "0. Exit\n");
        
        // try and catch to validate input for option method
        /** 
        * try and catch to validate input for option method
        */
        try {
            System.out.print("Please select an option (Enter number between 0-4): ");
            /** 
            * Take input from user
            */
            int num = input.nextInt();
            
            /** 
            * Switch statement on the options provided on the main menu
            * Case 1: Enter Subscription
            * Case 2: Summary of subscriptions
            * Case 3: Summary of subscription for a selected MONTH
            * Case 4: Find and display subscriptions
            * Case 0: Program exit
            */
            switch(num) {
                case 1:
                    option1();
                    break;
                
                case 2:
                    option2();
                    break;
                
                case 3:
                    option3();
                    break;
                
                case 4:
                    option4();
                    break;
                        
                case 0:
                    /**
                     * Message when exiting program, with a controlled system exit
                     */
                    System.out.println("\nThank you for using the service.\n" +
                                       "Exiting program...");
                    System.exit(0);         
                
                /**
                 * Prints message when input is invalid 
                 */   
                default:
                    System.out.println("--- The value entered was invalid, please enter a valid option: ---");
                    input.nextLine();
            }          

        }
        catch(Exception e){
            /**
             * Prints message when input is invalid if an 'Exception' is caught when scanning input
             */
            System.out.println("--- The value entered was invalid, please enter a valid option: ---");
            input.nextLine();
        }
    }
    
    /**
     * Enter subscription method
     */
    static void option1() {
        /**
         * Setting up scanner input and
         * declaring some variables to be used within Option 1
         * which are to be used for a method in the "subs.java" class
         */
        Scanner input = new Scanner(System.in);
        String name;
        char pack;
        int duration;
        String trueCode;
        char term;
        
        /**
         * Entering and validating user's name
         */
        while(true) {
            
            try {
                System.out.println("Please enter your name (Only 25 characters long): ");
                name = input.nextLine();
                
                /**
                 * Runs if input is longer than 25 characters
                 */
                if(name.length() > 25) {
                    System.out.println("\n--- Name entered is too long, please enter a valid name: ---");
                }
                /**
                 * Runs if input is valid
                 */
                else {
                    System.out.println("\n--- Name entered is valid ---\n");
                    break;
                }
            }
            /**
             * Catches any invalid inputs which will cause an exception error
             */
            catch(Exception e) {
                System.out.println("--- Name entered is invalid, please enter a valid name: ---");
                input.nextLine();
            }
            
        }
        
        /**
         * Entering and validating user's selected package
         */
        while(true) {
            
            try {
                System.out.println("B. Broze\n" +
                                "S. Silver\n" +
                                "G. Gold\n");
                System.out.print("Please enter a package (Enter B, S or G according to package above): ");                
                pack = input.next(".").charAt(0);
                
                /**
                 * Runs if package selected(input) is valid
                 */
                if(pack == 'B' || pack == 'S' || pack == 'G'){
                    System.out.println("\n--- Package entered is valid ---\n");
                    break;
                }
                /**
                 * Runs if input is invalid
                 */
                else {
                    System.out.println("--- Package entered is invalid, please enter a valid package: ---");
                    input.nextLine();
                }
                    
            }
            /**
             * Catches any invalid inputs which will cause an exception error
             */            
            catch(Exception e) {
                System.out.println("--- Input entered is invalid, please enter a valid input ---");
                input.nextLine();
            }
        }
        
        /**
         * Entering and validating user's package duration
         */
        while(true) {
            
            try {
                System.out.println("1. One Month\n" + 
                                   "3. Three Months\n" + 
                                   "6. Six Months\n" + 
                                   "12. Twelve Months (1 Year)\n");
                System.out.print("Please enter package duration (Enter 1, 3, 6 or 12): ");
                duration = input.nextInt();
                
                /**
                 * Runs if input is valid
                 */
                if(duration == 1 || duration == 3 || duration == 6 || duration == 12) {
                    System.out.println("\n--- Package duration entered is valid ---\n");
                    input.nextLine();
                    break;
                }
                
                /**
                 * Runs if input is invalid
                 */
                else {
                    System.out.println("--- Package duration entered is invalid, please enter a valid duration: ---");
                    input.nextLine();
                }                                
                
            }
            /**
             * Catches any invalid inputs which will cause an exception error
             */              
            catch(Exception e) {
                System.out.println("--- Input entered is invalid, please enter a valid input: ---");
                input.nextLine();
            }
        }
        
        /**
         * Entering and validating discount code
         */
        while(true) {
            try {
                System.out.print("Please enter discount code (6 characters long), if discount code is not needed enter '-': ");
                String code = input.nextLine();
                
                /**
                 * Converts letters in code variable into all upper case
                 */
                trueCode = code.toUpperCase();                              
                
                /**
                 * Runs if '-' is entered meaning no discount code is used
                 */
                if(trueCode.equals("-")) {
                    System.out.println("\n--- No discount code is used ---\n");
                    break;
                }
                /**
                 * Runs if a discount code is used, also validates the input
                 * Checks used:
                 * 1. Checks if discount code is 6 characters long
                 * 2. Checks if the first two characters are letters
                 * 3. Checks if the third and fourth characters are digits
                 * 4. Checks if the fifth character is either 'E' or 'L'
                 * 5. Checks if the last character is a number between 1 to 9
                 */
                if(trueCode.length() == 6 &&
                   trueCode.substring(0,2).matches("[a-zA-Z]+") &&
                   trueCode.substring(2,4).matches("[0-9]+") &&
                  (trueCode.substring(4,5).contains("E") || trueCode.substring(4,5).contains("L")) &&
                   trueCode.substring(5,6).matches("[1-9]+")) {
                            System.out.println("\n--- Discount code used is valid ---\n");
                            break;
                }
                /**
                 * Runs when an invalid code is used
                 */
                else {
                    System.out.println("\n--- The discount code is invalid, please enter a valid discount code ---");
                }                                
            }
            /**
             * Catches any invalid inputs which will cause an exception error
             */
            catch(Exception e) {
                System.out.println("--- Input entered is invalid, please enter a valid input: ---");                
            }
        }
        
        /**
         * Entering and validating user's payment term
         */
        while(true) {
            
            try {
                System.out.println("O. One-off Subscription\n" +
                                   "M. Monthly Subscription\n");
                System.out.print("Please enter payment term (Enter O or M according to the terms above): ");                
                term = input.next(".").charAt(0);
                
                /**
                 * Runs when 'O' or 'M' is entered as options
                 */
                if(term == 'O' || term == 'M') {
                    System.out.println("\n--- Term entered is valid ---\n");
                    break;
                }
                else {
                    System.out.println("--- Payment term entered is invalid, please enter a valid term: ---");
                    input.nextLine();
                }
            }
            /**
             * Catches any invalid inputs which will cause an exception error
             */
            catch(Exception e) {
                System.out.println("--- Input entered is invalid, please enter a valid input: ---");
                input.nextLine();
            }
        }
        
        /**
         * Setting up user from subs class matching its constructors.
         * Also uses the 'print' method from main to display summary box,
         * using some getter methods and the calcSubs() method from subs.java class as arguments.
         */
        subs user = new subs(pack, duration, trueCode, term);
        // System.out.printf("The fee is: £%.2f\n", user.calcSubs());
        
        print(name, DateHelper.getDate(), user.getDiscount(), user.getPack(), user.getDuration(), user.getTerm(), user.calcSubs());
        
        /**
         * Displaying some useful messages for user, 
         * to let them know that the 'current.txt' file has been updated
         */
        System.out.println("\nUpdating current.txt file...");
        user.writeFile(user.calcSubs(), name);
        System.out.println("--- Data updated successfully ---\n");
    }
    
    /**
     * Summary of subscriptions method (Option 2)
     */
    static void option2() {
        /** 
        * Setting up scanner and declaring subFile variable which is to be used as file name
        * Also displays instructions for user
        */        
        subs user = new subs();
        Scanner input = new Scanner(System.in);
        String subFile = "";
        
        /**
         * Entering and validating input for option 2 using try/catch
         */
        while(true) {
            System.out.println("\n1. Current subscription\n" +
                               "2. Sample subscription");            
            try {
                System.out.print("Please select a subscription file (Enter 1 or 2 according to the options provided above): ");
                int num = input.nextInt();
                
                switch(num) {
                    /** 
                    * Switch statement on the options provided in option 2
                    * Case 1: Set subFile as "current.txt" and runs readFileSubs method from subs.java class
                    * Case 2: Set subFile as "sample.txt" and runs readFileSubs method from subs.java class
                    * Default runs if the option entered is invalid
                    */                    
                    case 1:
                        subFile = "current.txt";
                        System.out.println("current.txt file is selected... ");
                        user.readFileSubs(subFile);
                        break;
                    case 2:
                        subFile = "sample.txt";
                        System.out.println("sample.txt file is selected... \n");
                        user.readFileSubs(subFile);
                        break;
                    default:
                        System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                        input.nextLine();
                }
                break;
            }
            catch(Exception e) {
                System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                input.nextLine();
            } 
        }
    }

    /**
     * Summary of subscription for a selected MONTH method (Option 3)
     */
    static void option3() {
        /**
         * Setting up scanner and the variables subFile and month,
         * which are to be used for the readFileMonth method from subs.java
         */
        subs user = new subs();
        Scanner input = new Scanner(System.in);
        String subFile = "";
        String month = "";
        
        /**
         * Entering and validating user's input in Option 3 using try/catch
         */
        while(true) {
            System.out.println("\n1. Current subscription\n" +
                               "2. Sample subscription");            
            try {
                System.out.print("Please select a subscription file (Enter 1 or 2 according to the options provided above): ");
                int num1 = input.nextInt();
                
                /** 
                * Switch statement on the options provided in option 3
                * Case 1: Set subFile as "current.txt" and prints a useful message for the user
                * Case 2: Set subFile as "sample.txt" and prints a useful message for the user
                * Default runs if the option entered is invalid
                */                  
                switch(num1) {
                    case 1:
                        subFile = "current.txt";
                        System.out.println("current.txt file is selected... ");
                        break;
                    case 2:
                        subFile = "sample.txt";
                        System.out.println("sample.txt file is selected... \n");
                        break;
                    default:
                        System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                        input.nextLine();
                }
                break;
            }
            catch(Exception e) {
                System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                input.nextLine();
            } 
        }
        
        /**
         * Entering and validating the month entered (number between 1-12) in Option 3 using try/catch
         */
        while(true) {
            System.out.println("\n1. January\n" + "2. Febuary\n" + "3. March\n" + "4. April\n" + "5. May\n" + "6. June\n" +
                               "7. July\n" + "8. Augest\n" + "9. September\n" + "10. October\n" + "11. November\n" + "12. December\n");            
            try {
                System.out.print("Please select a Month (Enter a number between 1-12 according to the options provided above): ");
                int num2 = input.nextInt();
                
                /**
                 * Switch statement on the options(months) provided in Option 3.
                 * Each number in the case corresponds to their respective months.
                 * Example: 
                 * Case 1: Jan (January)
                 * Case 6: Jun (June)
                 * Case 12: Dec (December)
                 * Default runs if the option entered is invalid
                 */
                switch(num2) {
                    case 1:
                        month = "Jan";
                        break;
                    case 2:
                        month = "Feb";
                        break;
                    case 3:
                        month = "Mar";
                        break;
                    case 4:
                        month = "Apr"; 
                        break;
                    case 5:
                        month = "May"; 
                        break;
                    case 6:
                        month = "Jun";
                        break;
                    case 7:
                        month = "Jul";
                        break;
                    case 8:
                        month = "Aug";
                        break;
                    case 9:
                        month = "Sep";
                        break;
                    case 10:
                        month = "Oct";
                        break;
                    case 11:
                        month = "Nov";
                        break;
                    case 12:
                        month = "Dec";
                        break;
                    default:
                        System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                        input.nextLine();
                }
                break;
            }
            catch(Exception e) {
                System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                input.nextLine();
            } 
        }
        /**
         * Prints useful info for the user
         * to let them know which file and month they've selected.
         * Runs the readFileMonth method from subs.java class,
         * which prints the summary out on the console.
         */
        System.out.println("\nFile selected: " + subFile);
        System.out.println("Month selected: " + month);
        user.readFileMonth(subFile, month);
    }
    
    /**
     * Find(Search) and display subscriptions (Option 4)
     */
    static void option4() {
        /**
         * Setting up scanner and the variables subFile and search,
         * which are to be used for the readFileSearch method from subs.java
         */
        subs user = new subs();
        Scanner input = new Scanner(System.in);
        String subFile = "";
        String search = "";
        
        /**
         * Entering and validating the option entered (number 1 or 2) in Option 4 using try/catch
         */
        while(true) {
            System.out.println("\n1. Current subscription\n" +
                               "2. Sample subscription");            
            try {
                System.out.print("Please select a subscription file (Enter 1 or 2 according to the options provided above): ");
                int num1 = input.nextInt();

                /** 
                * Switch statement on the options provided in option 4
                * Case 1: Set subFile as "current.txt" and prints a useful message for the user
                * Case 2: Set subFile as "sample.txt" and prints a useful message for the user
                * Default runs if the option entered is invalid
                */                   
                switch(num1) {
                    case 1:
                        subFile = "current.txt";
                        System.out.println("current.txt file is selected... ");
                        break;
                    case 2:
                        subFile = "sample.txt";
                        System.out.println("sample.txt file is selected... \n");
                        break;
                    default:
                        System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                        input.nextLine();
                }
                break;
            }
            catch(Exception e) {
                System.out.println("\n--- The option entered is invalid, please enter a valid option: ---");
                input.nextLine();
            } 
        }
        
        /**
         * Entering and validating search term/word used using try/catch
         */
        while(true) {           
            try {
                System.out.print("\nPlease enter a name to search for: ");
                input.nextLine();
                search = input.nextLine();
                break;
            }
            catch(Exception e) {
                System.out.println("\n--- The info entered is invalid, please enter a valid option: ---");
                input.nextLine();
            } 
        }
        /**
         * Prints useful info for the user
         * to let them know which file and search term they've used.
         * Runs the readFileSearch method from subs.java class,
         * which prints the summary out on the console
         * for each user that includes the searched term.
         */
        System.out.println("\nFile selected: " + subFile);
        System.out.println("Search Term: " + search);
        user.readFileSearch(subFile, search);        
    }
    
    /**
     * Method to display summary output
     * @param name
     * @param date
     * @param code
     * @param pack
     * @param duration
     * @param term
     * @param cost 
     */
    static void print(String name, String date, String code, char pack, int duration, char term, double cost) {
        /**
         * Declaring a variable called printout
         * which is to be used to store/print the summary box 
         * line by line.
         */
        String printout;
        
        /**
         * For loop used to generate summary box,
         * clearing/resetting the printout variable when the loop runs again.
         */
        for(int i = 0; i < 11; i++) {
            printout = "";
            
            /** 
             * Runs for the top and bottom most rows
             * Prints the top and bottom most boarder
             */
            if(i == 0 || i == 10) {
                for(int j = 0; j < 50; j++) {
                    if(j == 0 || j == 49) {
                        printout += "+";
                    }
                    else {
                        printout += "=";
                    }
                }
                System.out.println(printout);
            }
            
            /**
             * Runs for the name row
             * Prints the name row
             */
            else if(i == 2) {
                for(int j = 0; j < 50; j++) {
                    if(printout.length() == 0 || printout.length() == 49 ) {
                        printout += "|";
                    }
                    else if(printout.length() == 3) {
                        printout += "Customer: " + name;
                    }
                    else {
                        printout += " ";
                    }
                }
                System.out.println(printout);
            }
            
            /**
             * Runs for the Date and discount code row
             * Prints the Date and discount code row
             */
            else if(i == 4) {
                for(int j = 0; j < 50; j++) {
                    if(printout.length() == 0 || printout.length() == 49) {
                        printout += "|";
                    }
                    else if(printout.length() == 6) {
                        printout += "Date: " + date + "   " + "Discount Code: " + code;
                    }
                    else {
                        printout += " ";
                    }
                }
                System.out.println(printout);
            }
            
            /**
             * Runs for the Package and duration row
             * Prints the Package and duration row
             */            
            else if(i == 5) {
                for(int j = 0; j < 50; j++) {
                    if(printout.length() == 0 || printout.length() == 49) {
                        printout += "|";
                    }
                    else if(printout.length() == 3) {
                        /**
                         * Declaring fullPack variable and using switch statement
                         * to change the char B, S or G to their full terms
                         * Bronze, Silver or Gold respectively.
                         * Then prints the Package side of the row
                         */
                        String fullPack = "";
                        switch(pack) {
                            case 'B':
                                fullPack = "Bronze";
                                break;
                            case 'S':
                                fullPack = "Silver";
                                break;
                            case 'G':
                                fullPack = "Gold";
                                break;
                        }
                        printout += "Package: " + fullPack;
                    }
                    else if(printout.length() == 31) {
                        /**
                         * Declaring fullDuration variable and using switch statement
                         * to change the int 1, 3, 6 or 12 to words
                         * One, Three, Six or Twelve respectively.
                         * Then prints the duration side of the row
                         */                        
                        String fullDuration = "";
                        switch(duration) {
                            case 1:
                                fullDuration = "One";
                                break;
                            case 3:
                                fullDuration = "Three";
                                break;
                            case 6:
                                fullDuration = "Six";
                                break;
                            case 12:
                                fullDuration = "Twelve";
                                break;
                        }
                        printout += "Duration: " + fullDuration;
                    }
                    else {
                        printout += " ";
                    }
                }
                System.out.println(printout);
            }
            
            /**
             * Runs for the Terms row
             * Prints the Terms row
             */
            else if(i == 6) {
                for(int j = 0; j < 50; j++) {
                    if(printout.length() == 0 || printout.length() == 49) {
                        printout += "|";
                    }
                    else if(printout.length() == 5) {
                        /**
                         * Declaring fullTerm variable and using switch statement
                         * to change the char 'O' OR 'M' to their full terms
                         * One-off or Monthly respectively.
                         * Then prints the Terms row
                         */                            
                        String fullTerm = "";
                        switch(term) {
                            case 'O':
                                fullTerm = "One-off";
                                break;
                            case 'M':
                                fullTerm = "Monthly";
                                break;
                        }
                        printout += "Terms: " + fullTerm;
                    }
                    else {
                        printout += " ";
                    }
                }
                System.out.println(printout);
            }

            /**
             * Runs for the Cost row
             * Prints the Cost row
             */            
            else if(i == 8) {
                for(int j = 0; j < 50; j++) {
                    if(printout.length() == 0 || printout.length() == 49) {
                        printout += "|";
                    }
                    else if(printout.length() == 11) {
                        /**
                         * Declaring fullTerm variable and using switch statement
                         * to change the char 'O' OR 'M' to their full terms
                         * One-off or Monthly respectively.
                         * Then prints the Terms of the subscription in the Subscription row
                         */                             
                        String fullTerm = "";
                        switch(term) {
                            case 'O':
                                fullTerm = "One-off";
                                break;
                            case 'M':
                                fullTerm = "Monthly";  
                                break;
                        }
                        printout += fullTerm + " Subscription: £" + String.format("%.2f", cost);
                    }    
                    else {
                        printout += " ";
                    }
                }
                System.out.println(printout);
            }
            
            /**
             * Runs for the Blank row
             * Prints the Blank row
             */
            else {
                for(int j = 0; j < 50; j++) {
                    /**
                     * if prints the "|" boarder for the line
                     * else statement prints spaces (" ") in the line
                     */
                    if(j == 0 || j == 49) {
                        printout += "|";
                    }
                    else {
                        printout += " ";
                    }
                }
                System.out.println(printout);
            }        
        }
    }
    
    public static void main(String[] args) {
        /**
         * Main method
         * Displays the current date to user
         * and also runs the option method,
         * which has other nested methods within to complete the program.
         */
        String currentDate = DateHelper.getDate();
        System.out.printf("The current date is %s \n", currentDate);
        System.out.println();
        
        /**
         * Option method called within while loop
         */
        while(true) {
            option();
        }
    }
    
}
