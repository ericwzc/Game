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
 
 public class ChessMan extends JPanel
 {
   JLabel label = new JLabel();
   public int iStatus = PublicResource.chessUnknow;
   private int x = 0;
   private int y = 0;
   public int chessNum = 32;
   public boolean dead = false;
 
   public ChessMan(int x, int y)
   {
     setLayout(null);
     setSize(44, 44);
 
     this.label.setSize(getSize().width, getSize().height);
     this.label.setLocation(0, 0);
     this.label.setHorizontalAlignment(0);
     this.label.setFont(new Font("隶书", 1, 30));
     add(this.label);
 
     setOpaque(false);
 
     this.x = x;
     this.y = y;
     this.chessNum = 32;
 
     this.dead = false;
   }
 
   public void reset() {
     this.chessNum = 32;
     this.iStatus = PublicResource.chessUnknow;
     this.dead = false;
   }
   public void paintComponent(Graphics g) {
     if (this.iStatus == PublicResource.chessUnknow) {
       g.drawImage(
         PublicResource.imageBox.imageBack, 
         0, 
         0, 
         getSize().width, 
         getSize().height, 
         this);
     }
     else if (this.iStatus == PublicResource.chessUnselected) {
       g.drawImage(
         PublicResource.imageBox.imageRed, 
         0, 
         0, 
         getSize().width, 
         getSize().height, 
         this);
     }
     else
       g.drawImage(
         PublicResource.imageBox.imageSelect, 
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
 
   public void paintChessMan()
   {
     setLocation(this.x * 50 + 8, this.y * 50 + 8);
 
     if (this.chessNum == 32)
     {
       setText("");
       setForeground(true);
     }
     else {
       setText(PublicResource.sAllChessmen[this.chessNum][0]);
       setForeground(PublicResource.sAllChessmen[this.chessNum][1].toLowerCase().equals("red"));
     }
   }
 
   public void paintShowChess(boolean red)
   {
     setLocation(3, 3);
     this.iStatus = PublicResource.chessUnselected;
     if (red) {
       setText("帥");
       setForeground(true);
     }
     else {
       setText("將");
       setForeground(false);
     }
   }
 
   public void paintRunChess() {
     setLocation(3, 53);
     this.iStatus = PublicResource.chessUnselected;
     setText("走");
     this.label.setForeground(new Color(175, 51, 37));
   }
 }

