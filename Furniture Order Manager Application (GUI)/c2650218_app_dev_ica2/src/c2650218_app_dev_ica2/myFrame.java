/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package c2650218_app_dev_ica2;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*; 
import javax.swing.JFrame;
import java.util.*;
import java.io.*;

/**
 *
 * @author Daniel
 */
public class myFrame extends JFrame implements ActionListener{
    JButton buttonChair;
    JButton buttonTable;
    JButton buttonDesk;
    JButton buttonClear;
    JButton buttonTotal;
    JButton buttonSummary;
    JButton buttonOrder;
//    JButton buttonReview; (Unused)
    
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JLabel label5;
    JLabel label6;
    JLabel label7;
    JLabel label8;
    JLabel label9;
    
    myFrame() {
        /**
         * Adding panels for GUI (9 panels to display purchased furniture)
         */
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.white);
        panel1.setBounds(250,20,220,230);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.white);
        panel2.setBounds(500,20,220,230);

        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.white);
        panel3.setBounds(750,20,220,230);   

        JPanel panel4 = new JPanel();
        panel4.setBackground(Color.white);
        panel4.setBounds(250,270,220,230);
        
        JPanel panel5 = new JPanel();
        panel5.setBackground(Color.white);
        panel5.setBounds(500,270,220,230);         

        JPanel panel6 = new JPanel();
        panel6.setBackground(Color.white);
        panel6.setBounds(750,270,220,230); 
        
        JPanel panel7 = new JPanel();
        panel7.setBackground(Color.white);
        panel7.setBounds(250,520,220,230);
        
        JPanel panel8 = new JPanel();
        panel8.setBackground(Color.white);
        panel8.setBounds(500,520,220,230);
                
        JPanel panel9 = new JPanel();
        panel9.setBackground(Color.white);
        panel9.setBounds(750,520,220,230);  

        /**
         * Adding button options for GUI
         */
        
        /**
         * Chair Button
         */
        buttonChair = new JButton("Add Chair");
        buttonChair.setBounds(50,50,150,50);
        buttonChair.setFocusable(false);
        buttonChair.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Variable for order data
                 */
                int id = 0;
                char wood = 'o';
                int qty = 0;
                boolean armRest = false;
                
                /**
                 * User enter Furniture ID
                 */
                while(true) {
                    String input = JOptionPane.showInputDialog(null,"Enter Furniture ID: ");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                
                    System.out.println("Furniture ID entered: " + input);
                
                    try{
                        id = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Input entered was invalid!", "ID Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Input entered invalid...");
                    }
                }
                
                /**
                 * User Selects Type of Wood
                 */
                while(true) {
                    String[] choices = {"Oak 5p","Walnut 3p"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose Type of Wood: ","Wood", JOptionPane.QUESTION_MESSAGE, null, choices, "Oak");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Type of Wood: " + input);
                    
                    try {
                        if(input.equals("Oak 5p")) {
                            wood = 'o';
                        }
                        else if(input.equals("Walnut 3p")) {
                            wood = 'w';
                        }                        
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }
                }
                
                /**
                 * User selects quantity
                 */
                while(true) {
                    String[] choices = {"1","2","3","4","5","6","7","8","9"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose quantity: ","Quantity", JOptionPane.QUESTION_MESSAGE, null, choices, "1");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Quantity: " + input);
                    
                    try {
                        qty = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }                    
                }
                
                /**
                 * User selects optional armrest
                 */
                while(true) {
                    int input = JOptionPane.showOptionDialog(null, "Do you want to include armrest?", "Armrest", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    
                    if(input == JOptionPane.CANCEL_OPTION) {
                        System.exit(0);
                    }
                    
                    System.out.println("Armrest: " + input);
                    
                    try {
                        if(input == JOptionPane.YES_OPTION) {
                            armRest = true;
                        }
                        else if(input == JOptionPane.NO_OPTION) {
                            armRest = false;
                        }
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    } 
                }
                
                /**
                 * Creating new instance of Chair for data storage
                 */                
                Chair addChair = new Chair(id, wood, qty, armRest);
                
                /**
                 * ~~~~~~~~~~ Add Image to panel ~~~~~~~~~~
                 */
                for(int i = 0; i < 10; i++) {
                    /**
                     * ~~~~~ Check panel 1 if there is an image ~~~~~
                     */
                    if(i == 0) {
                        if(Furniture.hasImage(panel1) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                               
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();
                            }
                            addChair.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 2 if there is an image ~~~~~
                     */
                    else if(i == 1) {
                        if(Furniture.hasImage(panel2) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();
                            }
                            addChair.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 3 if there is an image ~~~~~
                     */
                    else if(i == 2) {
                        if(Furniture.hasImage(panel3) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();
                            }
                            addChair.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 4 if there is an image ~~~~~
                     */
                    else if(i == 3) {
                        if(Furniture.hasImage(panel4) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();  
                            }
                            addChair.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 5 if there is an image ~~~~~
                     */
                    else if(i == 4) {
                        if(Furniture.hasImage(panel5) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();
                            }
                            addChair.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 6 if there is an image ~~~~~
                     */
                    else if(i == 5) {
                        if(Furniture.hasImage(panel6) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();
                            }
                            addChair.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 7 if there is an image ~~~~~
                     */
                    else if(i == 6) {
                        if(Furniture.hasImage(panel7) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint(); 
                            }
                            addChair.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 8 if there is an image ~~~~~
                     */
                    else if(i == 7) {
                        if(Furniture.hasImage(panel8) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();  
                            }
                            addChair.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 9 if there is an image ~~~~~
                     */
                    else if(i == 8) {
                        if(Furniture.hasImage(panel9) == false) {
                            /**
                             * Walnut chair with no Armrest
                             */
                            if(wood == 'w' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();
                            }
                            /**
                             * Walnut Chair with Armrest
                             */
                            else if(wood == 'w' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                                
                            }
                            /**
                             * Oak chair without Armrest
                             */
                            else if(wood == 'o' && armRest == false) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChair.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                                
                            }
                            /**
                             * Oak chair with Armrest
                             */
                            else if(wood == 'o' && armRest == true) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakChairArmrest.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                               
                            }
                            addChair.writeFile();
                            break;
                        }
                    }
                                       
                    /**
                     * ~~~~~ Runs if all panels are full ~~~~~
                     */                    
                    if(i == 9) {
                        JOptionPane.showMessageDialog(null,"You have reached the limit of items to add!", "Panel Limit Reached Error",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    /**
                     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                     */
                }
            }
        });
        
        /**
         * Table Button
         */
        buttonTable = new JButton("Add Table");
        buttonTable.setBounds(50,150,150,50);
        buttonTable.setFocusable(false);
        buttonTable.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Variable for order data
                 */
                int id = 0;
                char wood = 'o';
                int qty = 0;
                int diameter = 0;
                String base = "Wooden";
                
                /**
                 * User enter Furniture ID
                 */
                while(true) {
                    String input = JOptionPane.showInputDialog(null,"Enter Furniture ID: ");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                
                    System.out.println("Furniture ID entered: " + input);
                
                    try{
                        id = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Input entered was invalid!", "ID Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Input entered invalid...");
                    }
                }
                
                /**
                 * User Selects Type of Wood
                 */
                while(true) {
                    String[] choices = {"Oak 5p","Walnut 3p"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose Type of Wood: ","Wood", JOptionPane.QUESTION_MESSAGE, null, choices, "Oak");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Type of Wood: " + input);
                    
                    try {
                        if(input.equals("Oak 5p")) {
                            wood = 'o';
                        }
                        else if(input.equals("Walnut 3p")) {
                            wood = 'w';
                        }                        
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }
                }
                
                /**
                 * User selects quantity
                 */
                while(true) {
                    String[] choices = {"1","2","3","4","5","6","7","8","9"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose quantity: ","Quantity", JOptionPane.QUESTION_MESSAGE, null, choices, "1");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Quantity: " + input);
                    
                    try {
                        qty = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }                    
                }
                
                /**
                 * User enters Diameter
                 */
                while(true) {
                    String input = JOptionPane.showInputDialog(null,"Enter Diameter (In CM, must be 50 or more): ");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                
                    System.out.println("Diameter: " + input);
                
                    try{
                        diameter = Integer.parseInt(input);
                        if(diameter >= 50) {
                            System.out.println("Input accepted.");
                            break;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Diameter must be 50 or more", "Diameter Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Invalid diameter entered...");
                        }
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Input entered was invalid!", "Diameter Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Input entered invalid...");
                    }
                }
                
                /**
                 * User selects base type
                 */
                while(true) {
                    String[] choices = {"Wooden £40.00","Chrome £50.00"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose Base Type: ","Base Type", JOptionPane.QUESTION_MESSAGE, null, choices, "Wooden");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Base: " + input);
                    
                    try {
                        if(input.equals("Wooden £40.00")) {
                            base = "Wooden";
                        }
                        else if(input.equals("Chrome £50.00")) {
                            base = "Chrome";
                        }                        
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }                                   
                }                
                
                /**
                 * Creating new instance of Table for data storage
                 */                
                Table addTable = new Table(id, wood, qty, diameter, base);
                
                /**
                 * ~~~~~~~~~~ Add Image to panel ~~~~~~~~~~
                 */
                for(int i = 0; i < 10; i++) {
                    /**
                     * ~~~~~ Check panel 1 if there is an image ~~~~~
                     */
                    if(i == 0) {
                        if(Furniture.hasImage(panel1) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 2 if there is an image ~~~~~
                     */
                    else if(i == 1) {
                        if(Furniture.hasImage(panel2) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 3 if there is an image ~~~~~
                     */
                    else if(i == 2) {
                        if(Furniture.hasImage(panel3) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 4 if there is an image ~~~~~
                     */
                    else if(i == 3) {
                        if(Furniture.hasImage(panel4) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 5 if there is an image ~~~~~
                     */
                    else if(i == 4) {
                        if(Furniture.hasImage(panel5) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 6 if there is an image ~~~~~
                     */
                    else if(i == 5) {
                        if(Furniture.hasImage(panel6) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 7 if there is an image ~~~~~
                     */
                    else if(i == 6) {
                        if(Furniture.hasImage(panel7) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 8 if there is an image ~~~~~
                     */
                    else if(i == 7) {
                        if(Furniture.hasImage(panel8) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 9 if there is an image ~~~~~
                     */
                    else if(i == 8) {
                        if(Furniture.hasImage(panel9) == false) {
                            /**
                             * Walnut table with Wooden base
                             */
                            if(wood == 'w' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                               
                            }
                            /**
                             * Walnut table with Chrome base
                             */
                            else if(wood == 'w' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                                
                            }
                            /**
                             * Oak table with Wooden base
                             */
                            else if(wood == 'o' && base.equals("Wooden")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTable.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                                
                            }
                            /**
                             * Oak table with Chrome Base
                             */
                            else if(wood == 'o' && base.equals("Chrome")) {
                               ImageIcon walnutChair = new ImageIcon("Images/oakTableChrome.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();
                            }
                            addTable.writeFile();
                            break;
                        }
                    }
                                       
                    /**
                     * ~~~~~ Runs if all panels are full ~~~~~
                     */                    
                    if(i == 9) {
                        JOptionPane.showMessageDialog(null,"You have reached the limit of items to add!", "Panel Limit Reached Error",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    /**
                     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                     */
                }                            
            }
        });
        
        /**
         * Desk Button
         */
        buttonDesk = new JButton("Add Desk");
        buttonDesk.setBounds(50,250,150,50);
        buttonDesk.setFocusable(false);
        buttonDesk.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Variable for order data
                 */
                int id = 0;
                char wood = 'o';
                int qty = 0;
                int width = 0;
                int depth = 0;
                int numDrawers = 1;
                
                /**
                 * User enter Furniture ID
                 */
                while(true) {
                    String input = JOptionPane.showInputDialog(null,"Enter Furniture ID: ");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                
                    System.out.println("Furniture ID entered: " + input);
                
                    try{
                        id = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Input entered was invalid!", "ID Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Input entered invalid...");
                    }
                }
                
                /**
                 * User Selects Type of Wood
                 */
                while(true) {
                    String[] choices = {"Oak 5p","Walnut 3p"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose Type of Wood: ","Wood", JOptionPane.QUESTION_MESSAGE, null, choices, "Oak");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Type of Wood: " + input);
                    
                    try {
                        if(input.equals("Oak 5p")) {
                            wood = 'o';
                        }
                        else if(input.equals("Walnut 3p")) {
                            wood = 'w';
                        }                        
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }
                }
                
                /**
                 * User selects quantity
                 */
                while(true) {
                    String[] choices = {"1","2","3","4","5","6","7","8","9"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose quantity: ","Quantity", JOptionPane.QUESTION_MESSAGE, null, choices, "1");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Quantity: " + input);
                    
                    try {
                        qty = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }                    
                }
                
                /**
                 * User enters Width
                 */
                while(true) {
                    String input = JOptionPane.showInputDialog(null,"Enter Width (In CM): ");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                
                    System.out.println("Width entered: " + input);
                
                    try{
                        width = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Input entered was invalid!", "Width Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Input entered invalid...");
                    }
                }
                
                /**
                 * User enters Depth
                 */
                while(true) {
                    String input = JOptionPane.showInputDialog(null,"Enter Depth (In CM): ");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                
                    System.out.println("Width entered: " + input);
                
                    try{
                        depth = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Input entered was invalid!", "Depth Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Input entered invalid...");
                    }
                }

                /**
                 * User selects Number of Drawers (Max 4, Min 1)
                 */
                while(true) {
                    String[] choices = {"1","2","3","4"};
                    String input = (String) JOptionPane.showInputDialog(null,"Choose Number of Drawers: ","Drawers", JOptionPane.QUESTION_MESSAGE, null, choices, "1");
                    
                    if(input.equals(JOptionPane.CANCEL_OPTION)) {
                        System.exit(0);
                    }
                    
                    System.out.println("Quantity: " + input);
                    
                    try {
                        numDrawers = Integer.parseInt(input);
                        System.out.println("Input accepted.");
                        break;
                    }
                    catch(Exception error) {
                        JOptionPane.showMessageDialog(null,"Option selected was invalid!", "Selection Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println("Option selected invalid...");                        
                    }                    
                }                
                
                /**
                 * Creating new instance of Desk for data storage
                 */                
                Desk addDesk = new Desk(id, wood, qty, width, depth, numDrawers);
                
                /**
                 * ~~~~~~~~~~ Add Image to panel ~~~~~~~~~~
                 */
                for(int i = 0; i < 10; i++) {
                    /**
                     * ~~~~~ Check panel 1 if there is an image ~~~~~
                     */
                    if(i == 0) {
                        if(Furniture.hasImage(panel1) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                               
                            }  
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel1.getWidth();
                               int panelHeight = panel1.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label1 = new JLabel(resizedIcon);
                               
                               panel1.add(label1);
                               panel1.revalidate();
                               panel1.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 2 if there is an image ~~~~~
                     */
                    else if(i == 1) {
                        if(Furniture.hasImage(panel2) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                               
                            }
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel2.getWidth();
                               int panelHeight = panel2.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label2 = new JLabel(resizedIcon);
                               
                               panel2.add(label2);
                               panel2.revalidate();
                               panel2.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 3 if there is an image ~~~~~
                     */
                    else if(i == 2) {
                        if(Furniture.hasImage(panel3) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                               
                            }
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel3.getWidth();
                               int panelHeight = panel3.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label3 = new JLabel(resizedIcon);
                               
                               panel3.add(label3);
                               panel3.revalidate();
                               panel3.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }
                    
                    /**
                     * ~~~~~ Check panel 4 if there is an image ~~~~~
                     */
                    else if(i == 3) {
                        if(Furniture.hasImage(panel4) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                               
                            }
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel4.getWidth();
                               int panelHeight = panel4.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label4 = new JLabel(resizedIcon);
                               
                               panel4.add(label4);
                               panel4.revalidate();
                               panel4.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 5 if there is an image ~~~~~
                     */
                    else if(i == 4) {
                        if(Furniture.hasImage(panel5) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                               
                            }  
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel5.getWidth();
                               int panelHeight = panel5.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label5 = new JLabel(resizedIcon);
                               
                               panel5.add(label5);
                               panel5.revalidate();
                               panel5.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 6 if there is an image ~~~~~
                     */
                    else if(i == 5) {
                        if(Furniture.hasImage(panel6) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                               
                            }  
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel6.getWidth();
                               int panelHeight = panel6.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label6 = new JLabel(resizedIcon);
                               
                               panel6.add(label6);
                               panel6.revalidate();
                               panel6.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 7 if there is an image ~~~~~
                     */
                    else if(i == 6) {
                        if(Furniture.hasImage(panel7) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                               
                            }  
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel7.getWidth();
                               int panelHeight = panel7.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label7 = new JLabel(resizedIcon);
                               
                               panel7.add(label7);
                               panel7.revalidate();
                               panel7.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 8 if there is an image ~~~~~
                     */
                    else if(i == 7) {
                        if(Furniture.hasImage(panel8) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                               
                            }  
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel8.getWidth();
                               int panelHeight = panel8.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label8 = new JLabel(resizedIcon);
                               
                               panel8.add(label8);
                               panel8.revalidate();
                               panel8.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }

                    /**
                     * ~~~~~ Check panel 9 if there is an image ~~~~~
                     */
                    else if(i == 8) {
                        if(Furniture.hasImage(panel9) == false) {
                            /**
                             * Walnut Desk
                             */
                            if(wood == 'w') {
                               ImageIcon walnutChair = new ImageIcon("Images/walnutDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                               
                            }  
                            /**
                             * Oak Desk
                             */
                            else if(wood == 'o') {
                               ImageIcon walnutChair = new ImageIcon("Images/oakDesk.png");
                               
                               Image walnutChairImage = walnutChair.getImage();
                               int panelWidth = panel9.getWidth();
                               int panelHeight = panel9.getHeight();
                               Image resizedImage = walnutChairImage.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                               
                               ImageIcon resizedIcon = new ImageIcon(resizedImage);
                               
                               label9 = new JLabel(resizedIcon);
                               
                               panel9.add(label9);
                               panel9.revalidate();
                               panel9.repaint();                                
                            }
                            addDesk.writeFile();
                            break;
                        }
                    }
                                       
                    /**
                     * ~~~~~ Runs if all panels are full ~~~~~
                     */                    
                    if(i == 9) {
                        JOptionPane.showMessageDialog(null,"You have reached the limit of items to add!", "Panel Limit Reached Error",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    /**
                     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                     */
                }                
            }
        });  

        /**
         * Clear All Button
         */
        buttonClear = new JButton("Clear All");
        buttonClear.setBounds(50,350,150,50);
        buttonClear.setFocusable(false);
        buttonClear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Clearing all images in panels
                 */
                JPanel[] panels = {panel1, panel2, panel3, panel4, panel5, panel6, panel7, panel8, panel9};

                for (JPanel panel : panels) {
                    panel.removeAll();
                    panel.revalidate();
                    panel.repaint();
                }
                
                /**
                 * Used to clear the temporary.txt file
                 */
                String filePath = "Data/temporary.txt";
        
                try {
                    FileWriter writer = new FileWriter(filePath, false);
            
                    writer.close();
            
                    System.out.println("File cleared successfully.");
                }
                catch (IOException ex) {
                    System.out.println("An error occurred while clearing the file: " + ex.getMessage());
                }
                
                JOptionPane.showMessageDialog(null, "Successfully cleared all current orders.","Clear All", JOptionPane.INFORMATION_MESSAGE);
            }
        }); 
        
        /**
         * Total Price Button
         */
        buttonTotal = new JButton("Total Price");
        buttonTotal.setBounds(50,450,150,50);
        buttonTotal.setFocusable(false);
        buttonTotal.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Iterating through temporary.txt file and only adding the ItemPrice,
                 * and adding them together to display the totalPrice with a MessageDialog box
                 */
                File file = new File("Data/temporary.txt");

                try {
                    Scanner scanner = new Scanner(file);
                    double totalPrice = 0;
                    
                    /**
                     * Iterates through list and skipping parts until it reaches the itemPrice
                     */
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split("\t");
                        String priceStr = parts[10];
                        double price = Double.parseDouble(priceStr.replace("n", ""));
                        totalPrice += price;
                    }
                    System.out.printf("\nTotal Price: £%.2f\n", totalPrice);
                    String totalPriceMessage = String.format("\nTotal Price of Order: £%.2f\n", totalPrice);
                    JOptionPane.showMessageDialog(null, totalPriceMessage,"Total Price", JOptionPane.INFORMATION_MESSAGE);
;
                } 
                catch (FileNotFoundException ex) {
                    System.out.println("Error: File does not exist.");
                }
            }
        });

        /**
         * Summary Button
         */
        buttonSummary = new JButton("Summary");
        buttonSummary.setBounds(50,550,150,50);
        buttonSummary.setFocusable(false);
        buttonSummary.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Check if there is an order made
                 */
                if (panel1.getComponentCount() == 0) {
                    JOptionPane.showMessageDialog(null, "There are currently no orders made.","Summary", JOptionPane.INFORMATION_MESSAGE);
                } 
                else {
                    /**
                     * Prints boarder for Summary
                     */
                    JOptionPane.showMessageDialog(null, "Summary printed, please look at console for the Order Summary.","Summary", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println();
                    for(int i = 0; i < 81; i++ ) {
                        if(i == 40) {
                            System.out.print(" Order Summary ");
                        }
                        else {
                            System.out.print("-");
                        }                        
                    }
                    System.out.println();
                    
                    /**
                     * Prints Summary Data
                     */
                    try {
                        /**
                         * Iterating through temporary.txt and saving itemPrice into array list
                         */
                        FileReader fileReader = new FileReader("Data/temporary.txt");
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        /**
                         * Creating array list to store itemPrice
                         */
                        ArrayList<Double> sortedPrice = new ArrayList<>();
                        double total = 0;

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            String[] parts = line.split("\t");
                            double itemPrice = Double.parseDouble(parts[10]);

                            sortedPrice.add(itemPrice);
                        }

                        /**
                         * Sorting itemPrice in the array list in ASECENDING order
                         */
                        Collections.sort(sortedPrice);

                        /**
                         * Searching through temporary.txt for matching itemPrice following the sorted list
                         */
                        for(double itemPrice : sortedPrice) {
                            bufferedReader = new BufferedReader(new FileReader("Data/temporary.txt"));
                            while ((line = bufferedReader.readLine()) != null) {
                                String[] parts = line.split("\t");
                                double currentPrice = Double.parseDouble(parts[10]);

                                if (currentPrice == itemPrice) {
                                    /**
                                     * Formats printing of data in Summary
                                     */
                                    System.out.println("Type of Furniture: " + parts[0]);
                                    System.out.println("Furniture ID: " + parts[1]);
                                    System.out.printf("Item Price: £%.2f\n", currentPrice);
                                    
                                    /**
                                     * Prints end boarder for Summary
                                     */
                                    for(int j = 0; j < 95; j++) {
                                        System.out.print("-");
                                    }
                                    System.out.println();
                                }                               
                            }
                            bufferedReader.close();
                        }
                        /**
                         * Calculates the total price of the order
                         */
                        for(int i = 0; i < sortedPrice.size(); i++) {
                            total += sortedPrice.get(i);
                        }
                        System.out.println("Total Price: £" + total);

                        /**
                         * Prints end boarder for Summary
                         */
                        for(int j = 0; j < 95; j++) {
                            System.out.print("-");
                        }                        
                    } 
                    catch (IOException ex) {
                        System.out.println("Error - problem reading the file! Program closing");
                        System.exit(0);
                    }                    
                }                                
            }
        });

        /**
         * Place Order Button
         */
        buttonOrder = new JButton("Place Order");
        buttonOrder.setBounds(50,650,150,50);
        buttonOrder.setFocusable(false);
        buttonOrder.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Check if there is an order made
                 */
                if (panel1.getComponentCount() == 0) {
                    JOptionPane.showMessageDialog(null, "There are currently no orders made.","Place Order", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    /**
                     * User enter Payment Detail
                     */
                    while(true) {
                        String input = JOptionPane.showInputDialog(null,"Enter Credit Card Number: ");

                        if(input.equals(JOptionPane.CANCEL_OPTION)) {
                            System.exit(0);
                        }                
                        try{
                            Integer.parseInt(input);
                            System.out.println("Payment accepted.");
                            break;
                        }
                        catch(Exception error) {
                            JOptionPane.showMessageDialog(null,"Payment entered was invalid!", "ID Error",JOptionPane.ERROR_MESSAGE);
                            System.out.println("Payment entered invalid...");
                        }
                    }
                    
                    /**
                     * Copies data from temporary.txt and transfers them to orderlist.txt
                     */
                    String tempFilePath = "Data/temporary.txt";
                    String copyFilePath = "Data/orderlist.txt";
                    String orderID = "0";

                    File tempFile = new File(tempFilePath);
                    File copyFile = new File(copyFilePath);

                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(tempFile));

                        BufferedWriter writer = new BufferedWriter(new FileWriter(copyFile,true));

                        String line;

                        /**
                         * Checks if the current orderlist.txt has any data in it, if so, +1 to the order ID
                         */
                        if(copyFile.length() > 0) {
                            try {
                                /**
                                 * Reads the first column of the last line (Which is latest order ID in orderlist.txt)
                                 */
                                BufferedReader idReader = new BufferedReader(new FileReader(copyFile));

                                String firstColumn = null;

                                String checkID;
                                while ((checkID = idReader.readLine()) != null) {
                                    String[] columns = checkID.split("\t");
                                    firstColumn = columns[0];
                                }
                                idReader.close();

                                /**
                                 * Parses the latest order ID which is a number string and adds 1 to its value to be used for the new order
                                 */
                                int value = Integer.parseInt(firstColumn);
                                int newValue = value + 1;

                                orderID = Integer.toString(newValue);

                                } catch (IOException ex) {
                                    System.out.println("Error reading the file: " + ex.getMessage());
                                }                
                        } 

                        /**
                         * Writes the new order ID along with copying the data from temporary.txt
                         */
                        while ((line = reader.readLine()) != null) {
                            writer.write(orderID + "\t");
                            writer.write(line);
                            writer.newLine();
                        }
                        
                        reader.close();
                        writer.close();

                        /**
                         * Clearing all images in panels
                         */
                        JPanel[] panels = {panel1, panel2, panel3, panel4, panel5, panel6, panel7, panel8, panel9};

                        for (JPanel panel : panels) {
                            panel.removeAll();
                            panel.revalidate();
                            panel.repaint();
                        }

                        /**
                         * Used to clear the temporary.txt file
                         */
                        String filePath = "Data/temporary.txt";

                        try {
                            FileWriter clear = new FileWriter(filePath, false);

                            clear.close();

                            System.out.println("File cleared successfully.");
                        }
                        catch (IOException ex) {
                            System.out.println("An error occurred while clearing the file: " + ex.getMessage());
                        }

                        JOptionPane.showMessageDialog(null, "Your order has been successfully placed. Thank you for shopping with us!","Placed Order", JOptionPane.INFORMATION_MESSAGE);

                    } 
                    catch (IOException ex) {
                        System.out.println("Error copying the file: " + ex.getMessage());
                    }
                }
            }
        });
//
//        /**
//         * Review  Button (Unused)
//         */
//        buttonReview = new JButton("Review Order");
//        buttonReview.setBounds(50,720,150,50);
//        buttonReview.setFocusable(false);
//        buttonReview.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Review Order");
//            }
//        });           
                
        
        /**
         * Setting up Frame for GUI
         */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000,820);
        this.setTitle("Main GUI");
        this.setResizable(false);
        this.setVisible(true);
        
        this.add(buttonChair);
        this.add(buttonTable);
        this.add(buttonDesk);
        this.add(buttonClear);
        this.add(buttonTotal);
        this.add(buttonSummary);
        this.add(buttonOrder); 
        
        this.add(panel1);
        this.add(panel2);
        this.add(panel3);
        this.add(panel4);
        this.add(panel5);
        this.add(panel6);
        this.add(panel7);
        this.add(panel8);
        this.add(panel9);        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("An error has occured."); 
    }
    
}
