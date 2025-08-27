/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Hotel1 extends JFrame implements ActionListener{

        JLabel l1;
        JButton b1;

        public Hotel1() {

                setSize(1920,1080);          // setContentPane(300,300,1366,390);   frame size
                setLayout(null);
                setLocation(30,30);

                l1 = new JLabel("");
                b1 = new JButton("Next");

                b1.setBackground(new Color(103, 86, 144));
                b1.setForeground(Color.WHITE);



                ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/photo.jpg"));
                Image i3 = i1.getImage().getScaledInstance(1600, 800,Image.SCALE_DEFAULT);
                ImageIcon i2 = new ImageIcon(i3);
                l1 = new JLabel(i2);

                JLabel lid=new JLabel("HOTEL MANAGEMENT SYSTEM");
                lid.setBounds(200,350,1920,1080);
                lid.setFont(new Font("serif",Font.PLAIN,40));
                lid.setForeground(Color.WHITE);
                l1.add(lid);
                b1.setBounds(1550,875,150,50);
                l1.setBounds(-200, -140, 1920, 1080);

                l1.add(b1);
                add(l1);

                b1.addActionListener(this);
                setVisible(true);

                while(true){
                        lid.setVisible(false); // lid =  j label
                        try{
                                Thread.sleep(500); //1000 = 1 second
                        }catch(Exception e){}
                        lid.setVisible(true);
                        try{
                                Thread.sleep(500);
                        }catch(Exception e){}
                }
        }

        public void actionPerformed(ActionEvent ae){
                new Login().setVisible(true);
                this.setVisible(false);

        }

        public static void main(String[] args) {
                Hotel1 window = new Hotel1();
                window.setVisible(true);
        }
}