 package com.pp;
 
 public class PublicResource
 {
   public static boolean bOk = false;
   public static boolean bEnd = false;
 
   public static boolean bServer = false;
   public static int iColor = 0;
   public static boolean bPlaying = false;
   public static boolean bConnected = false;
 
   public static int iFirst = 0;
 
   public static int iPort = 1111;
 
   public static int chessUnknow = 0;
   public static int chessUnselected = 1;
   public static int chessSelected = 2;
 
   public static String[][] sAllChessmen = { 
     { "兵", "red" }, 
     { "兵", "red" }, 
     { "兵", "red" }, 
     { "兵", "red" }, 
     { "兵", "red" }, 
     { "炮", "red" }, 
     { "炮", "red" }, 
     { "馬", "red" }, 
     { "馬", "red" }, 
     { "車", "red" }, 
     { "車", "red" }, 
     { "相", "red" }, 
     { "相", "red" }, 
     { "士", "red" }, 
     { "士", "red" }, 
     { "帥", "red" }, 
     { "卒", "black" }, 
     { "卒", "black" }, 
     { "卒", "black" }, 
     { "卒", "black" }, 
     { "卒", "black" }, 
     { "炮", "black" }, 
     { "炮", "black" }, 
     { "馬", "black" }, 
     { "馬", "black" }, 
     { "車", "black" }, 
     { "車", "black" }, 
     { "象", "black" }, 
     { "象", "black" }, 
     { "士", "black" }, 
     { "士", "black" }, 
     { "將", "black" } };
 
   public static ChessBoard panelBoard = new ChessBoard();
   public static DeadChessBoard panelDead = new DeadChessBoard();
   public static EndPanel panelEnd = new EndPanel();
   public static ChessMenu panelMenu = new ChessMenu();
   public static InitImage imageBox = new InitImage();
 }

