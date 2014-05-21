 package com.pp;
 
 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.FilterInputStream;
 import java.io.FilterOutputStream;
 import java.io.IOException;
 import java.io.PrintStream;
 import java.net.Socket;
 import java.net.UnknownHostException;
 import javax.swing.AbstractButton;
 import javax.swing.ButtonGroup;
 import javax.swing.ButtonModel;
 import javax.swing.JComponent;
 import javax.swing.JOptionPane;
 
 public class GameClient
 {
   public Socket client = null;
   public String ip = "192.168.1.102";
   public DataInputStream dIn = null;
   public DataOutputStream dOut = null;
 
   public void setIp(String ip)
   {
     this.ip = ip;
   }
   public void connect() {
     new Thread()
     {
       public void run() {
         try {
           PublicResource.panelMenu.createServer.setEnabled(false);
           GameClient.this.client = new Socket(GameClient.this.ip, PublicResource.iPort);
 
           GameClient.this.dOut = new DataOutputStream(GameClient.this.client.getOutputStream());
           GameClient.this.dIn = new DataInputStream(GameClient.this.client.getInputStream());
           System.out.println("connected to 192.168.1.102");
 
           PublicResource.bServer = false;
           PublicResource.bPlaying = false;
           PublicResource.iColor = 0;
           PublicResource.bConnected = true;
 
           PublicResource.panelMenu.setButtonEnabled();
 
           JOptionPane.showMessageDialog(
             PublicResource.panelBoard, 
             "连接成功！", 
             "信息", 
             1);
 
           PublicResource.panelEnd.LtoRbutton.setSelected(true);
           GameClient.this.getInputStr();
         }
         catch (UnknownHostException e1)
         {
           e1.printStackTrace();
         }
         catch (Exception e) {
           System.out.println(e.getMessage());
           if (e.getMessage().startsWith("Connection refused"))
             JOptionPane.showMessageDialog(PublicResource.panelBoard, 
               "连接不到对方", 
               "信息", 
               1);
           else if (e.getMessage().toLowerCase().startsWith("connection reset")) {
             if (PublicResource.bPlaying) {
               JOptionPane.showMessageDialog(PublicResource.panelBoard, 
                 "对方退出", 
                 "信息", 
                 1);
             }
           }
           else {
             JOptionPane.showMessageDialog(PublicResource.panelBoard, 
               e.getMessage(), 
               "信息", 
               1);
           }
           PublicResource.bServer = false;
           PublicResource.panelMenu.iswaiting = false;
           PublicResource.bConnected = false;
 
           PublicResource.panelBoard.matchend();
           try
           {
             if (GameClient.this.dOut != null)
               GameClient.this.dOut.close();
             if (GameClient.this.dIn != null)
               GameClient.this.dIn.close();
             if (GameClient.this.client != null)
               GameClient.this.client.close();
           }
           catch (IOException e1)
           {
             e1.printStackTrace();
           }
           GameClient.this.dOut = null;
           GameClient.this.dIn = null;
           GameClient.this.client = null;
         }
 
       }
 
     }
 
     .start();
   }
 
   public int[] getPara(String sPara, int num) {
     int[] iPara = new int[num];
     String para = sPara;
     int itemp = 0;
     for (int i = 0; i < num; i++) {
       System.out.println(para);
 
       int index = para.indexOf(',');
       if (index != -1)
         itemp = converttoint(para.substring(0, para.indexOf(',')));
       else
         itemp = converttoint(para);
       para = para.substring(para.indexOf(',') + 1);
       iPara[i] = itemp;
     }
     return iPara;
   }
 
   public int converttoint(String para) {
     int ipara = 0;
     for (int i = 0; i < para.length(); i++) {
       ipara = para.charAt(i) - '0' + ipara * 10;
     }
     return ipara;
   }
 
   public void exchange(int x, int y, int firstX, int firstY) {
     PublicResource.panelBoard.chessmen[firstX][firstY].iStatus = PublicResource.chessUnselected;
 
     PublicResource.panelBoard.chessmen[x][y].setVisible(false);
     PublicResource.panelBoard.chessmen[firstX][firstY].setVisible(false);
 
     if ((PublicResource.panelBoard.chessmen[x][y].chessNum >= 0) && (PublicResource.panelBoard.chessmen[x][y].chessNum < 32)) {
       PublicResource.panelBoard.iAllchess[PublicResource.panelBoard.chessmen[x][y].chessNum] = 0;
       PublicResource.panelDead.addDeadChess(PublicResource.panelBoard.chessmen[x][y].chessNum);
     }
     PublicResource.panelBoard.chessmen[x][y].chessNum = PublicResource.panelBoard.chessmen[firstX][firstY].chessNum;
     PublicResource.panelBoard.chessmen[x][y].dead = PublicResource.panelBoard.chessmen[firstX][firstY].dead;
 
     PublicResource.panelBoard.chessmen[firstX][firstY].dead = true;
     PublicResource.panelBoard.chessmen[firstX][firstY].chessNum = 32;
 
     PublicResource.panelBoard.chessmen[x][y].paintChessMan();
     PublicResource.panelBoard.chessmen[x][y].setVisible(true);
   }
 
   public void getInputStr()
     throws IOException
   {
     while (!PublicResource.bEnd) {
       String read = this.dIn.readUTF();
       System.out.println(read);
       if (read.startsWith("whofirst")) {
         if (read.substring(read.indexOf(',') + 1).equals("1")) {
           System.out.println("client: 先走");
           PublicResource.bOk = true;
           PublicResource.panelDead.showChessRun.setVisible(true);
         }
         else
         {
           System.out.println("client: 后走");
           PublicResource.bOk = false;
           PublicResource.panelDead.showChessRun.setVisible(false);
         }
 
       }
       else if (read.startsWith("newgameapply"))
       {
         PublicResource.panelMenu.newgame.setEnabled(false);
         int option = JOptionPane.showConfirmDialog(PublicResource.panelBoard, 
           "对方请求开始游戏，开始？", "信息", 0);
 
         if (option == 0) {
           PublicResource.panelBoard.rollback();
 
           String sInfo = "newgameok";
 
           if (!PublicResource.bServer) {
             PublicResource.panelMenu.gameclient.dOut.writeUTF(
               sInfo);
             PublicResource.panelMenu.gameclient.dOut.flush();
           } else {
             PublicResource.panelMenu.gameserver.out.writeUTF(
               sInfo);
             PublicResource.panelMenu.gameserver.out.flush();
           }
           PublicResource.bPlaying = true;
           PublicResource.panelEnd.LtoRbutton.setEnabled(false);
           PublicResource.panelEnd.RtoLbutton.setEnabled(false);
           PublicResource.panelMenu.setButtonEnabled();
         }
         else
         {
           String sInfo = "newgamefalse";
 
           PublicResource.panelMenu.newgame.setEnabled(true);
 
           if (!PublicResource.bServer) {
             PublicResource.panelMenu.gameclient.dOut.writeUTF(
               sInfo);
             PublicResource.panelMenu.gameclient.dOut.flush();
           } else {
             PublicResource.panelMenu.gameserver.out.writeUTF(
               sInfo);
             PublicResource.panelMenu.gameserver.out.flush();
           }
         }
       }
       else if (read.startsWith("color")) {
         System.out.println(read);
         if (read.substring(read.indexOf(',') + 1).equals("1")) {
           PublicResource.iColor = 1;
           PublicResource.panelDead.showChessColor.paintShowChess(true);
           PublicResource.panelDead.showChessColor.setVisible(true);
         }
         else if (read.substring(read.indexOf(',') + 1).equals("2")) {
           PublicResource.iColor = 2;
           PublicResource.panelDead.showChessColor.paintShowChess(false);
           PublicResource.panelDead.showChessColor.setVisible(true);
         }
 
       }
       else if (read.startsWith("newgameok")) {
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "游戏开始！", 
           "信息", 
           1);
 
         PublicResource.panelBoard.rollback();
 
         PublicResource.bPlaying = true;
         PublicResource.panelMenu.setButtonEnabled();
       }
       else if (read.startsWith("newgamefalse")) {
         PublicResource.panelMenu.newgame.setEnabled(true);
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "对方拒绝开始游戏！", 
           "信息", 
           1);
         PublicResource.panelEnd.LtoRbutton.setEnabled(true);
         PublicResource.panelEnd.RtoLbutton.setEnabled(true);
       }
       else if (read.startsWith("actionbutton"))
       {
         if (read.endsWith("1"))
           PublicResource.panelEnd.LtoRbutton.setSelected(true);
         else {
           PublicResource.panelEnd.RtoLbutton.setSelected(true);
         }
 
       }
       else if (read.startsWith("show")) {
         System.out.println(read);
         String strTemp = read.substring(read.indexOf(',') + 1);
         System.out.println(strTemp);
 
         int[] iPara = getPara(strTemp, 3);
         int x = iPara[0];
         int y = iPara[1];
         int chessNum = iPara[2];
         System.out.println(
           "receive: x:" + x + " y:" + y + " chessnum:" + chessNum);
 
         PublicResource.panelBoard.chessmen[x][y].setVisible(false);
         PublicResource.panelBoard.chessmen[x][y].chessNum = chessNum;
         PublicResource.panelBoard.iAllchess[chessNum] = 2;
         PublicResource.panelBoard.iChessleft -= 1;
         PublicResource.panelBoard.chessmen[x][y].iStatus = 
           PublicResource.chessUnselected;
         PublicResource.panelBoard.chessmen[x][y].paintChessMan();
         PublicResource.panelBoard.chessmen[x][y].setVisible(true);
 
         if ((PublicResource.iColor == 0) && (PublicResource.panelEnd.group.getSelection().getActionCommand() == "The first one")) {
           if (chessNum > 15)
             PublicResource.iColor = 1;
           else {
             PublicResource.iColor = 2;
           }
           PublicResource.panelDead.showChessColor.paintShowChess(PublicResource.iColor == 1);
           PublicResource.panelDead.showChessColor.setVisible(true);
         }
 
         PublicResource.bOk = true;
         PublicResource.panelDead.showChessRun.setVisible(true);
       }
       else if (read.startsWith("move")) {
         String strTemp = read.substring(read.indexOf(',') + 1);
         int[] iPara = getPara(strTemp, 2);
         int x = iPara[0];
         int y = iPara[1];
 
         exchange(
           x, 
           y, 
           PublicResource.panelBoard.firstX, 
           PublicResource.panelBoard.firstY);
 
         PublicResource.bOk = true;
         PublicResource.panelDead.showChessRun.setVisible(true);
       }
       else if (read.startsWith("peaceapply")) {
         int option = JOptionPane.showConfirmDialog(PublicResource.panelBoard, 
           "对方请求和局，同意否？", "信息", 0);
         String sInfo = null;
 
         if (option == 0) {
           sInfo = "peaceyes";
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
           catch (IOException e1)
           {
             e1.printStackTrace();
           }
 
           PublicResource.panelBoard.matchend();
         }
         else
         {
           sInfo = "peaceno";
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
           catch (IOException e1)
           {
             e1.printStackTrace();
           }
         }
 
       }
       else if (read.startsWith("peaceyes")) {
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "和局！", 
           "信息", 
           1);
 
         PublicResource.panelBoard.matchend();
       }
       else if (read.startsWith("peaceno"))
       {
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "对方拒绝和局！", 
           "信息", 
           1);
       }
       else if (read.startsWith("lose"))
       {
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "对方认输！", 
           "信息", 
           1);
 
         PublicResource.panelBoard.matchend();
       }
       else if (read.startsWith("selected")) {
         String strTemp = read.substring(read.indexOf(',') + 1);
 
         int[] iPara = getPara(strTemp, 2);
         int x = iPara[0];
         int y = iPara[1];
 
         PublicResource.panelBoard.chessmen[x][y].iStatus = 
           PublicResource.chessSelected;
         PublicResource.panelBoard.chessmen[x][y].setVisible(false);
         PublicResource.panelBoard.chessmen[x][y].setVisible(true);
         PublicResource.panelBoard.firstX = x;
         PublicResource.panelBoard.firstY = y;
       }
       else if (read.startsWith("unselected")) {
         String strTemp = read.substring(read.indexOf(',') + 1);
 
         int[] iPara = getPara(strTemp, 2);
         int x = iPara[0];
         int y = iPara[1];
 
         PublicResource.panelBoard.chessmen[x][y].iStatus = 
           PublicResource.chessUnselected;
         PublicResource.panelBoard.chessmen[x][y].setVisible(false);
         PublicResource.panelBoard.chessmen[x][y].setVisible(true);
       }
       else if (read.startsWith("end")) {
         String end = null;
         if (read.toLowerCase().endsWith("lose")) {
           end = "你输啦！";
         }
         else if (read.toLowerCase().endsWith("win")) {
           end = "恭喜恭喜，胜利！";
         }
 
         JOptionPane.showMessageDialog(PublicResource.panelBoard, end, "信息", 1);
 
         PublicResource.panelBoard.matchend();
       }
     }
   }
 }

