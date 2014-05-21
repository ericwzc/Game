 package com.pp;
 
 import java.awt.Image;
 import java.awt.Toolkit;
 import java.io.BufferedReader;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import javax.swing.ImageIcon;
 
 public class LoadImage
 {
   public static String getTextFromJar(String s, Class class1)
   {
     String s1 = "";
     InputStream inputstream = class1.getResourceAsStream(s);
     if (inputstream != null) {
       BufferedReader bufferedreader = 
         new BufferedReader(new InputStreamReader(inputstream));
       try
       {
         String s2;
         while ((s2 = bufferedreader.readLine()) != null)
         {
           s1 = s1 + s2 + "\n";
         }
       } catch (IOException ioexception) {
         ioexception.printStackTrace();
       }
     }
     return s1;
   }
 
   public static ImageIcon loadImage(String filename) throws IOException {
     InputStream is = ChessMenu.class.getResourceAsStream(filename);
     ByteArrayOutputStream os = new ByteArrayOutputStream();
     try {
       label65: 
       while (true) { int b = is.read();
         if (b < 0) break;
         os.write(b); break label65;
       }
 
       return new ImageIcon(os.toByteArray());
     } catch (IOException ex) {
       ex.printStackTrace();
       throw ex;
     } finally {
       is.close();
       os.close();
     }
   }
 
   public static Image getImageFromJar(String s, Class class1)
   {
     Image image = null;
     InputStream inputstream = class1.getResourceAsStream(s);
     if (inputstream != null) {
       ByteArrayOutputStream bytearrayoutputstream = 
         new ByteArrayOutputStream();
       try {
         byte[] abyte0 = new byte[1024];
         for (int i = 0; (i = inputstream.read(abyte0)) >= 0; ) {
           bytearrayoutputstream.write(abyte0, 0, i);
         }
         image = 
           Toolkit.getDefaultToolkit().createImage(
           bytearrayoutputstream.toByteArray());
       } catch (IOException ioexception) {
         System.out.println(ioexception.getMessage());
         ioexception.printStackTrace();
       }
     }
     else {
       System.out.println("inputstream: null" + s);
     }return image;
   }
 }

