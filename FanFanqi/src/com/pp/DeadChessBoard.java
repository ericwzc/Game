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
 
 public class DeadChessBoard extends JPanel
 {
   public JLabel label1 = new JLabel();
   public JLabel label2 = new JLabel();
   public JLabel label3 = new JLabel();
   public ChessMan showChessColor = new ChessMan(0, 0);
   public ChessMan showChessRun = new ChessMan(0, 0);
 
   public int num = 0;
   DeadChessMan[] deadchessmen = new DeadChessMan[32];
 
   public DeadChessBoard() {
     setLayout(null);
     setLocation(0, 210);
     setSize(520, 111);
 
     this.label1.setLocation(50, 0);
     this.label2.setLocation(50, getSize().height / 3);
     this.label3.setLocation(50, getSize().height / 3 * 2);
 
     this.label1.setText("八");
     this.label2.setText("宝");
     this.label3.setText("山");
 
     this.label1.setFont(new Font("隶书", 1, 40));
     this.label2.setFont(new Font("隶书", 1, 40));
     this.label3.setFont(new Font("隶书", 1, 40));
 
     this.label1.setForeground(new Color(0, 0, 0));
     this.label2.setForeground(new Color(0, 0, 0));
     this.label3.setForeground(new Color(0, 0, 0));
 
     this.label1.setHorizontalAlignment(0);
     this.label2.setHorizontalAlignment(0);
     this.label3.setHorizontalAlignment(0);
 
     this.label1.setSize(50, getSize().height / 3);
     this.label2.setSize(50, getSize().height / 3);
     this.label3.setSize(50, getSize().height / 3);
 
     add(this.label1);
     add(this.label2);
     add(this.label3);
 
     this.showChessColor.paintShowChess(true);
     this.showChessColor.setVisible(false);
     this.showChessRun.paintRunChess();
     this.showChessRun.setVisible(false);
     add(this.showChessColor);
     add(this.showChessRun);
 
     for (int i = 0; i < 32; i++) {
       this.deadchessmen[i] = new DeadChessMan();
       int y = i / 11;
       int x = i % 11;
       this.deadchessmen[i].setLocation(x * 37 + 101, y * 37 + 1);
       this.deadchessmen[i].setVisible(false);
       add(this.deadchessmen[i]);
     }
   }
 
   public void paintComponent(Graphics g)
   {
     g.drawImage(
       PublicResource.imageBox.imageMenu, 
       0, 
       0, 
       getSize().width, 
       getSize().height, 
       this);
 
     g.drawLine(50, 0, 50, getSize().height);
     g.drawLine(0, 50, 50, 50);
     g.drawLine(0, 0, 520, 0);
     g.drawLine(0, 110, 520, 110);
     g.setColor(new Color(16, 91, 114));
     g.drawLine(100, 0, 100, getSize().height);
     g.drawLine(100, getSize().height / 3, getSize().width, getSize().height / 3);
     g.drawLine(100, getSize().height / 3 * 2, getSize().width, getSize().height / 3 * 2);
   }
 
   public void addDeadChess(int chessnum)
   {
     this.deadchessmen[this.num].paintChessMan(chessnum);
     this.deadchessmen[this.num].setVisible(true);
     this.num += 1;
   }
 
   public void clearDeadChess()
   {
     for (int i = 0; i < 32; i++)
       this.deadchessmen[i].setVisible(false);
     this.num = 0;
   }
 }

