 package com.pp;
 
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.PrintStream;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 
 public class FanActionListener
   implements ActionListener
 {
   protected Object objTarget;
   protected Method objMethod;
 
   public FanActionListener(Object objTarget, String sMethod)
     throws NoSuchMethodException, SecurityException
   {
     this.objTarget = objTarget;
     Class[] clsParams = { ActionEvent.class };
     this.objMethod = objTarget.getClass().getMethod(sMethod, clsParams);
   }
 
   public void actionPerformed(ActionEvent event)
   {
     Object[] objArgs = { event };
     try
     {
       this.objMethod.invoke(this.objTarget, objArgs);
     }
     catch (IllegalAccessException e)
     {
       System.out.println("FanfanqiActionListener: " + e);
     }
     catch (InvocationTargetException e) {
       System.out.println("FanfanqiActionListener: " + e);
     }
   }
 }

