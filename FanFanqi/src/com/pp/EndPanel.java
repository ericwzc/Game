 package com.pp;
 
 import java.awt.Component;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.FlowLayout;
 import java.awt.Graphics;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.DataOutputStream;
 import java.io.IOException;
 import javax.swing.AbstractButton;
 import javax.swing.ButtonGroup;
 import javax.swing.ButtonModel;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JRadioButton;
 
 public class EndPanel extends JPanel
 {
   JRadioButton RtoLbutton;
   JRadioButton LtoRbutton;
   public String FirstOne = "The first one";
   public String FirstDifferent = "The first different";
   public ButtonGroup group = new ButtonGroup();
 
   FlowLayout experimentLayout = new FlowLayout();
 
   public EndPanel() {
     setSize(520, 100);
     setLocation(0, 321);
     setLayout(this.experimentLayout);
 
     this.LtoRbutton = new JRadioButton("一子定色");
     this.LtoRbutton.setActionCommand(this.FirstOne);
     this.LtoRbutton.setSelected(true);
     this.RtoLbutton = new JRadioButton("颜色异同定色");
     this.RtoLbutton.setActionCommand(this.FirstDifferent);
 
     this.group.add(this.LtoRbutton);
     this.group.add(this.RtoLbutton);
 
     this.RtoLbutton.setOpaque(false);
     this.LtoRbutton.setOpaque(false);
 
     this.RtoLbutton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         String command = EndPanel.this.group.getSelection().getActionCommand();
         if (command.equals("The first different")) {
           String sInfo = "actionbutton,2";
           try {
             if (!PublicResource.bServer) {
               PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
               PublicResource.panelMenu.gameclient.dOut.flush();
             }
             else {
               PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
               PublicResource.panelMenu.gameserver.out.flush();
             }
           }
           catch (IOException e1) {
             e1.printStackTrace();
           }
         }
       }
     });
     this.LtoRbutton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         String command = EndPanel.this.group.getSelection().getActionCommand();
         if (command.equals("The first one")) {
           String sInfo = "actionbutton,1";
           try {
             if (!PublicResource.bServer) {
               PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
               PublicResource.panelMenu.gameclient.dOut.flush();
             }
             else {
               PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
               PublicResource.panelMenu.gameserver.out.flush();
             }
           }
           catch (IOException e1) {
             e1.printStackTrace();
           }
         }
       }
     });
     add(this.LtoRbutton);
     add(this.RtoLbutton);
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

