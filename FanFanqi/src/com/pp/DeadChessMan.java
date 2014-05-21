 package com.pp;
 
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.Font;
 import java.awt.Graphics;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 
 public class DeadChessMan extends JPanel
 {
   JLabel label = new JLabel();
   public int iStatus = PublicResource.chessUnknow;
 
   public int chessNum = 32;
   public boolean dead = false;
 
   public DeadChessMan() {
     setLayout(null);
     setSize(34, 34);
 
     this.label.setSize(getSize().width, getSize().height);
     this.label.setLocation(0, 0);
     this.label.setHorizontalAlignment(0);
     this.label.setFont(new Font("隶书", 1, 28));
     add(this.label);
 
     setOpaque(false);
 
     this.dead = false;
   }
 
   public void reset() {
     this.chessNum = 32;
     this.iStatus = PublicResource.chessUnknow;
     this.dead = false;
   }
   public void paintComponent(Graphics g) {
     g.drawImage(
       PublicResource.imageBox.imageRed, 
       0, 
       0, 
       getSize().width, 
       getSize().height, 
       this);
   }
 
   public void setText(String name) {
     this.label.setText(name);
   }
 
   public void setForeground(boolean color) {
     if (color) {
       this.label.setForeground(new Color(255, 0, 0));
     }
     else
       this.label.setForeground(new Color(0, 0, 0));
   }
 
   public void paintChessMan(int chessnum)
   {
     setText(PublicResource.sAllChessmen[chessnum][0]);
     setForeground(PublicResource.sAllChessmen[chessnum][1].toLowerCase().equals("red"));
   }
 }

