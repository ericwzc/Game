 package com.pp;
 
 import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
 public class ChessMenu extends JPanel
 {
   public JButton createServer = new JButton("建主机");
   public JButton connectServer = new JButton("连接主机");
   public JButton newgame = new JButton("新游戏");
   public JButton askPeace = new JButton("求和");
   public JButton lose = new JButton("认输");
   public JCheckBox jcb = new JCheckBox("作弊"); 
   
   public boolean iswaiting = false;
 
   public Image imageMenu = null;
   public ImageIcon imageicon = null;
 
   public GameServer gameserver = new GameServer();
   public GameClient gameclient = new GameClient();
 
   public ChessMenu()
   {
     setLayout(null);
 
     setSize(110, 210);
     setLocation(0, 0);
     setButtons();
     try {
       this.imageicon = LoadImage.loadImage("Menu.JPG");
     }
     catch (IOException e) {
       e.printStackTrace();
     }
 
     this.imageMenu = LoadImage.getImageFromJar("Menu.JPG", getClass());
   }
 
   public void setButtons() {
     try {
       this.createServer.setLocation(6, 10);
       this.createServer.setSize(new Dimension(90, 18));
       this.createServer.setFont(new Font("Helvetica", 1, 12));
       this.createServer.addActionListener(
         new FanActionListener(this, "createServer"));
 
       this.connectServer.setLocation(6, 45);
       this.connectServer.setSize(new Dimension(90, 18));
       this.connectServer.setFont(new Font("Helvetica", 1, 12));
       this.connectServer.addActionListener(
         new FanActionListener(this, "connectServer"));
 
       this.newgame.setLocation(6, 80);
       this.newgame.setSize(new Dimension(90, 18));
       this.newgame.setFont(new Font("Helvetica", 1, 12));
       this.newgame.addActionListener(
         new FanActionListener(this, "newgame"));
 
       this.askPeace.setLocation(6, 115);
       this.askPeace.setSize(new Dimension(90, 18));
       this.askPeace.setFont(new Font("Helvetica", 1, 12));
       this.askPeace.addActionListener(
         new FanActionListener(this, "askPeace"));
 
       this.lose.setLocation(6, 150);
       this.lose.setSize(new Dimension(90, 18));
       this.lose.setFont(new Font("Helvetica", 1, 12));
       this.lose.addActionListener(
         new FanActionListener(this, "lose"));
       
       this.jcb.setLocation(6, 185);
       this.jcb.setSelected(false);
       this.jcb.setSize(new Dimension(90,18));
       this.jcb.setFont(new Font("Helvetica", 1, 12));
       
       add(this.createServer, null);
       add(this.connectServer, null);
       add(this.newgame, null);
       add(this.askPeace, null);
       add(this.lose, null);
       add(this.jcb, null);
       
       setButtonEnabled();
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
   }
 
   public void setButtonEnabled()
   {
     this.createServer.setEnabled((!PublicResource.bPlaying) && (!PublicResource.bConnected));
     this.connectServer.setEnabled((!PublicResource.bPlaying) && (!PublicResource.bConnected));
     this.newgame.setEnabled((!PublicResource.bPlaying) && (PublicResource.bConnected));
     this.askPeace.setEnabled((PublicResource.bPlaying) && (PublicResource.bConnected));
     this.lose.setEnabled((PublicResource.bPlaying) && (PublicResource.bConnected));
   }
 
   public void createServer(ActionEvent e)
   {
     if (!this.iswaiting) {
       this.gameserver.waitConnect();
       this.iswaiting = true;
     }
     else
     {
       System.out.println("A server has been created to wait connection.");
     }
   }
 
   public void connectServer(ActionEvent e) { String ip = JOptionPane.showInputDialog(PublicResource.panelBoard, "输入对方ip");
 
     this.gameclient.setIp(ip);
     this.gameclient.connect();
     PublicResource.panelEnd.LtoRbutton.setSelected(true);
   }
 
   public void newgame(ActionEvent e)
   {
     System.out.println("New game!");
     this.newgame.setEnabled(false);
 
     String sInfo = "newgameapply";
     try
     {
       if (!PublicResource.bServer) {
         PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
         PublicResource.panelMenu.gameclient.dOut.flush();
       }
       else
       {
         PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
         PublicResource.panelMenu.gameserver.out.flush();
       }
 
       PublicResource.panelEnd.LtoRbutton.setEnabled(false);
       PublicResource.panelEnd.RtoLbutton.setEnabled(false);
     }
     catch (IOException e1)
     {
       e1.printStackTrace();
     }
   }
 
   public void askPeace(ActionEvent e)
   {
     System.out.println("Ask for peace!");
     String sInfo = "peaceapply";
     try {
       if (!PublicResource.bServer) {
         PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
         PublicResource.panelMenu.gameclient.dOut.flush();
       }
       else
       {
         PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
         PublicResource.panelMenu.gameserver.out.flush();
       }
 
     }
     catch (IOException e1)
     {
       e1.printStackTrace();
     }
   }
 
   public void lose(ActionEvent e) {
     System.out.println("You Lose!");
 
     int option = JOptionPane.showConfirmDialog(PublicResource.panelBoard, 
       "确定降了？", "信息", 0);
 
     if (option == 0)
     {
       String sInfo = "lose";
 
       PublicResource.panelBoard.matchend();
       try
       {
         if (!PublicResource.bServer) {
           PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
           PublicResource.panelMenu.gameclient.dOut.flush();
         }
         else
         {
           PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
           PublicResource.panelMenu.gameserver.out.flush();
         }
 
       }
       catch (IOException e1)
       {
         e1.printStackTrace();
       }
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
   }

 }

