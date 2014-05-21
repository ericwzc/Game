 package com.pp;
 
 import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
 
 public class GameServer
 {
   public ServerSocket server = null;
   public Socket sIn = null;
   public DataInputStream in = null;
   public DataOutputStream out = null;
 
   public int[] iCheckcolor = new int[2];
 
   public void waitConnect()
   {
     new Thread() {
       public void run() {
         try {
           GameServer.this.server = new ServerSocket(PublicResource.iPort);
           System.out.println("Server is waiting");
           PublicResource.panelMenu.connectServer.setEnabled(false);
           PublicResource.panelBoard.panelWait.setVisible(true);
           Socket sIn = GameServer.this.server.accept();
           PublicResource.panelBoard.panelWait.setVisible(false);
           GameServer.this.in = new DataInputStream(sIn.getInputStream());
           GameServer.this.out = new DataOutputStream(sIn.getOutputStream());
 
           PublicResource.bServer = true;
           PublicResource.bPlaying = false;
           PublicResource.bConnected = true;
 
           PublicResource.panelMenu.setButtonEnabled();
 
           JOptionPane.showMessageDialog(
             PublicResource.panelBoard, 
             "连接成功！", 
             "信息", 
             1);
 
           PublicResource.panelEnd.LtoRbutton.setSelected(true);
           GameServer.this.getInputStr();
         }
         catch (Exception e)
         {
           System.out.println(e.getMessage());
           if (e.getMessage().startsWith("Connection refused")) {
             JOptionPane.showMessageDialog(
               PublicResource.panelBoard, 
               "连接不到对方", 
               "信息", 
               1);
           }
           else if (e.getMessage().toLowerCase().startsWith(
             "connection reset"))
           {
             if (PublicResource.bPlaying)
               JOptionPane.showMessageDialog(
                 PublicResource.panelBoard, 
                 "对方退出", 
                 "信息", 
                 1);
           }
           else {
             JOptionPane.showMessageDialog(
               PublicResource.panelBoard, 
               e.getMessage(), 
               "信息", 
               1);
           }
 
           PublicResource.bConnected = false;
           PublicResource.bServer = false;
           PublicResource.panelMenu.iswaiting = false;
 
           PublicResource.panelBoard.matchend();
           try
           {
             if (GameServer.this.out != null)
               GameServer.this.out.close();
             if (GameServer.this.in != null)
               GameServer.this.in.close();
             if (GameServer.this.sIn != null)
               GameServer.this.sIn.close();
             if (GameServer.this.server != null)
               GameServer.this.server.close();
           }
           catch (IOException e1) {
             e1.printStackTrace();
           }
           GameServer.this.out = null;
           GameServer.this.in = null;
           GameServer.this.sIn = null;
           GameServer.this.server = null;
         }
       }
     }
 
     .start();
   }
 
   public int[] getPara(String sPara, int num) {
     int[] iPara = new int[num];
     String para = sPara;
     int itemp = 0;
     for (int i = 0; i < num; i++)
     {
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
     PublicResource.panelBoard.chessmen[firstX][firstY].iStatus = 
       PublicResource.chessUnselected;
 
     PublicResource.panelBoard.chessmen[x][y].setVisible(false);
     PublicResource.panelBoard.chessmen[firstX][firstY].setVisible(false);
 
     if ((PublicResource.panelBoard.chessmen[x][y].chessNum >= 0) && 
       (PublicResource.panelBoard.chessmen[x][y].chessNum < 32)) {
       PublicResource.panelBoard.iAllchess[
         PublicResource.panelBoard.chessmen[
         x][y].chessNum] = 
         0;
 
       PublicResource.panelDead.addDeadChess(PublicResource.panelBoard.chessmen[x][y].chessNum);
     }
     PublicResource.panelBoard.chessmen[x][y].chessNum = 
       PublicResource.panelBoard.chessmen[firstX][firstY].chessNum;
     PublicResource.panelBoard.chessmen[x][y].dead = 
       PublicResource.panelBoard.chessmen[firstX][firstY].dead;
 
     PublicResource.panelBoard.chessmen[firstX][firstY].dead = true;
     PublicResource.panelBoard.chessmen[firstX][firstY].chessNum = 32;
 
     PublicResource.panelBoard.chessmen[x][y].paintChessMan();
     PublicResource.panelBoard.chessmen[x][y].setVisible(true);
   }
 
   public int checkEnd()
   {
     boolean red = false;
     boolean black = false;
     for (int i = 0; i < 16; i++) {
       if (PublicResource.panelBoard.iAllchess[i] != 0)
         red = true;
     }
     for (int i = 16; i < 32; i++) {
       if (PublicResource.panelBoard.iAllchess[i] != 0)
         black = true;
     }
     if (!red)
       return 1;
     if (!black)
       return 2;
     return 0;
   }
 
   public void getInputStr() throws IOException {
     while (!PublicResource.bEnd) {
       String read = this.in.readUTF();
 
       if (read.startsWith("newgameapply"))
       {
         PublicResource.panelMenu.newgame.setEnabled(false);
         int option = JOptionPane.showConfirmDialog(PublicResource.panelBoard, 
           "对方请求开始游戏，开始？", "信息", 0);
 
         if (option == 0)
         {
           PublicResource.panelBoard.rollback();
 
           PublicResource.bPlaying = true;
           PublicResource.panelEnd.LtoRbutton.setEnabled(false);
           PublicResource.panelEnd.RtoLbutton.setEnabled(false);
           PublicResource.panelMenu.setButtonEnabled();
 
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
           System.out.println(PublicResource.iFirst);
           if (PublicResource.iFirst == 0) {
             int whofirst = 
               (int)(Math.round(Math.random() * 100.0D) % 2L);
             String a = null;
             if (whofirst == 0) {
               a = "whofirst,1";
               this.out.writeUTF(a);
               this.out.flush();
               PublicResource.bOk = false;
               PublicResource.panelDead.showChessRun.setVisible(false);
               PublicResource.iFirst = 2;
             } else {
               a = "whofirst,0";
               this.out.writeUTF(a);
               this.out.flush();
               PublicResource.bOk = true;
               PublicResource.panelDead.showChessRun.setVisible(true);
               PublicResource.iFirst = 1;
             }
             System.out.println("server:" + a);
           }
           else if (PublicResource.iFirst < 0) {
             String a = null;
             if (PublicResource.iFirst == -1) {
               a = "whofirst,1";
               this.out.writeUTF(a);
               this.out.flush();
               PublicResource.bOk = false;
               PublicResource.panelDead.showChessRun.setVisible(false);
               PublicResource.iFirst = 2;
             } else if (PublicResource.iFirst == -2) {
               a = "whofirst,0";
               this.out.writeUTF(a);
               this.out.flush();
               PublicResource.bOk = true;
               PublicResource.panelDead.showChessRun.setVisible(true);
               PublicResource.iFirst = 1;
             }
             System.out.println("server:" + a);
           }
 
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
       else if (read.startsWith("newgameok")) {
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "游戏开始！", 
           "信息", 
           1);
 
         PublicResource.panelBoard.rollback();
         PublicResource.bPlaying = true;
         PublicResource.panelMenu.setButtonEnabled();
 
         if (PublicResource.iFirst == 0) {
           int whofirst = 
             (int)(Math.round(Math.random() * 100.0D) % 2L);
           String a = null;
           if (whofirst == 0) {
             a = "whofirst,1";
             this.out.writeUTF(a);
             this.out.flush();
             PublicResource.bOk = false;
             PublicResource.panelDead.showChessRun.setVisible(false);
             PublicResource.iFirst = 2;
           } else {
             a = "whofirst,0";
             this.out.writeUTF(a);
             this.out.flush();
             PublicResource.bOk = true;
             PublicResource.panelDead.showChessRun.setVisible(true);
             PublicResource.iFirst = 1;
           }
           System.out.println("server:" + a);
         }
         else if (PublicResource.iFirst < 0) {
           String a = null;
           if (PublicResource.iFirst == -1) {
             a = "whofirst,1";
             this.out.writeUTF(a);
             this.out.flush();
             PublicResource.bOk = false;
             PublicResource.panelDead.showChessRun.setVisible(false);
             PublicResource.iFirst = 2;
           } else if (PublicResource.iFirst == -2) {
             a = "whofirst,0";
             this.out.writeUTF(a);
             this.out.flush();
             PublicResource.bOk = true;
             PublicResource.panelDead.showChessRun.setVisible(true);
             PublicResource.iFirst = 1;
           }
           System.out.println("server:" + a);
         }
 
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
         if (read.endsWith("1")) {
           PublicResource.panelEnd.LtoRbutton.setSelected(true);
         }
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
 
         if ((PublicResource.iColor == 0) && (PublicResource.panelEnd.group.getSelection().getActionCommand() == "The first different"))
         {
           if ((PublicResource.iFirst == 1) && (PublicResource.iColor == 0)) {
             if (chessNum > 15)
               this.iCheckcolor[1] = 2;
             else {
               this.iCheckcolor[1] = 1;
             }
             if (this.iCheckcolor[0] != this.iCheckcolor[1]) {
               PublicResource.iColor = this.iCheckcolor[0];
               PublicResource.panelDead.showChessColor.paintShowChess(PublicResource.iColor == 1);
               PublicResource.panelDead.showChessColor.setVisible(true);
               PublicResource.iFirst = -1;
               String sInfo2 = "color," + this.iCheckcolor[1];
               System.out.println(sInfo2);
               try {
                 if (!PublicResource.bServer) {
                   PublicResource.panelMenu.gameclient.dOut
                     .writeUTF(
                     sInfo2);
                   PublicResource.panelMenu.gameclient.dOut
                     .flush();
                 } else {
                   PublicResource.panelMenu.gameserver.out
                     .writeUTF(
                     sInfo2);
                   PublicResource.panelMenu.gameserver.out.flush();
                 }
               }
               catch (IOException e)
               {
                 e.printStackTrace();
               }
             }
           } else if ((PublicResource.iFirst == 2) && (PublicResource.iColor == 0)) {
             if (chessNum > 15)
               this.iCheckcolor[0] = 2;
             else
               this.iCheckcolor[0] = 1;
           }
         }
         else if (PublicResource.iColor == 0) {
           if (chessNum > 15)
             PublicResource.iColor = 1;
           else {
             PublicResource.iColor = 2;
           }
           PublicResource.iFirst = 0 - PublicResource.iFirst;
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
 
         int end = checkEnd();
         String sInfo = "end,win";
         if (end == 1) {
           try {
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
           catch (IOException e)
           {
             e.printStackTrace();
           }
           JOptionPane.showMessageDialog(
             PublicResource.panelBoard, 
             "你输啦！", 
             "信息", 
             1);
 
           PublicResource.panelBoard.matchend();
         }
         else if (end == 2) {
           try {
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
           catch (IOException e)
           {
             e.printStackTrace();
           }
           JOptionPane.showMessageDialog(
             PublicResource.panelBoard, 
             "你输啦！", 
             "信息", 
             1);
 
           PublicResource.panelBoard.matchend();
         }
         else {
           PublicResource.bOk = true;
           PublicResource.panelDead.showChessRun.setVisible(true);
         }
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
         else {
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
       } else if (read.startsWith("lose"))
       {
         JOptionPane.showMessageDialog(
           PublicResource.panelBoard, 
           "对方认输！", 
           "信息", 
           1);
 
         PublicResource.panelBoard.matchend();
       } else if (read.startsWith("selected")) {
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
     }
   }
 }

